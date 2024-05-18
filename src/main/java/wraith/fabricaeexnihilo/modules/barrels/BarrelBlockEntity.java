package wraith.fabricaeexnihilo.modules.barrels;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.base.BaseBlockEntity;
import wraith.fabricaeexnihilo.modules.base.EnchantableBlockEntity;
import wraith.fabricaeexnihilo.modules.base.EnchantmentContainer;
import wraith.fabricaeexnihilo.recipe.barrel.BarrelRecipe;
import wraith.fabricaeexnihilo.util.CodecUtils;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class BarrelBlockEntity extends BaseBlockEntity implements EnchantableBlockEntity {
    public static final Identifier BLOCK_ENTITY_ID = id("barrel");

    public static final BlockEntityType<BarrelBlockEntity> TYPE = BlockEntityType.Builder.create(
            BarrelBlockEntity::new,
            ModBlocks.BARRELS.values().toArray(new BarrelBlock[0])
    ).build(null);

    static {
        ItemStorage.SIDED.registerForBlockEntity((barrel, direction) -> barrel.itemStorage, TYPE);
        FluidStorage.SIDED.registerForBlockEntity((barrel, direction) -> barrel.fluidStorage, TYPE);
    }

    public final BarrelFluidStorage fluidStorage;
    public final BarrelItemStorage itemStorage;
    private final EnchantmentContainer enchantments = new EnchantmentContainer();
    private int tickCounter;
    private Identifier lazeRecipeId;

    private BarrelState state = BarrelState.EMPTY;
    private FluidVariant fluid = FluidVariant.blank();
    private long fluidAmount = 0;
    private ItemStack stack = ItemStack.EMPTY; // Should only be non-empty in the item or compost state
    private float compostLevel = 0;
    private RecipeEntry<BarrelRecipe> recipe = null;
    private float recipeProgress = 0;

    public BarrelBlockEntity(BlockPos pos, BlockState state) {
        super(TYPE, pos, state);
        fluidStorage = new BarrelFluidStorage(this);
        itemStorage = new BarrelItemStorage(this);
        tickCounter = world == null
                ? FabricaeExNihilo.CONFIG.get().barrels().tickRate()
                : world.random.nextInt(FabricaeExNihilo.CONFIG.get().barrels().tickRate());
    }

    public ItemActionResult activate(@Nullable PlayerEntity player, @Nullable Hand hand) {
        if (world == null || player == null || hand == null || isCrafting()) {
            return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        if (state == BarrelState.ITEM) {
            dropInventoryAtPlayer(player);
            return ItemActionResult.SUCCESS;
        }
        return insertFromHand(player, hand);
    }

    public void dropInventoryAtPlayer(PlayerEntity player) {
        if (world == null || state != BarrelState.ITEM) {
            return;
        }
        var entity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.0625, pos.getZ() + 0.5, stack);
        entity.setVelocity(player.getPos().subtract(entity.getPos()).normalize().multiply(0.5));
        if (world != null) {
            world.spawnEntity(entity);
        }
        setItem(ItemStack.EMPTY);
    }

    public int getEfficiencyMultiplier() {
        return 1 + enchantments.getEnchantmentLevel(Enchantments.EFFICIENCY);
    }

    public EnchantmentContainer getEnchantmentContainer() {
        return enchantments;
    }

    public ItemActionResult insertFromHand(PlayerEntity player, Hand hand) {
        if (world == null)
            return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;

        var held = player.getStackInHand(hand);
        if (held.isEmpty()) return ItemActionResult.SUCCESS;

        try (Transaction t = Transaction.openOuter()) {
            var inserted = (int) itemStorage.insert(ItemVariant.of(held), held.getCount(), t);
            if (inserted != 0) {
                if (!player.isCreative()) {
                    held.decrement(inserted);
                }
                t.commit();
                return ItemActionResult.SUCCESS;
            }
        }

        var bucketFluidStorage = FluidStorage.ITEM.find(held, ContainerItemContext.ofPlayerHand(player, hand));
        if (bucketFluidStorage == null) return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;

        try (Transaction t = Transaction.openOuter()) {
            var amount = StorageUtil.findExtractableContent(bucketFluidStorage, t);

            long moved;
            if (amount == null) {
                // Barrel to bucket
                var fluid = fluidStorage.getResource();
                moved = StorageUtil.move(fluidStorage, bucketFluidStorage, fluidVariant -> true, FluidConstants.BUCKET, t);
                if (moved != 0)
                    world.playSound(null, pos, FluidVariantAttributes.getFillSound(fluid), SoundCategory.BLOCKS, 1.0F, 1.0F);
            } else {
                // Bucket to barrel
                if (player.isCreative()) {
                    moved = fluidStorage.insert(amount.resource(), amount.amount(), t);
                } else {
                    moved = StorageUtil.move(bucketFluidStorage, fluidStorage, fluidVariant -> true, FluidConstants.BUCKET, t);
                }

                if (moved != 0)
                    world.playSound(null, pos, FluidVariantAttributes.getEmptySound(fluidStorage.getResource()), SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            if (moved != 0) {
                t.commit();
                return ItemActionResult.SUCCESS;
            }
        }
        return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }

    public boolean isFireproof() {
        return getCachedState().getBlock() instanceof BarrelBlock barrel && barrel.isFireproof();
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.put("enchantments", enchantments.writeNbt());
        nbt.putString("state", state.getId());
        nbt.put("fluid", CodecUtils.toNbt(FluidVariant.CODEC, fluid, registryLookup));
        nbt.putLong("fluidAmount", fluidAmount);
        nbt.put("stack", stack.encodeAllowEmpty(registryLookup));
        nbt.putFloat("compostLevel", compostLevel);
        if (recipe != null)
            nbt.putString("recipe", recipe.id().toString());
        nbt.putFloat("recipeProgress", recipeProgress);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        enchantments.readNbt(nbt.getCompound("enchantments"));
        state = BarrelState.byId(nbt.getString("state"));
        fluid = CodecUtils.fromNbt(FluidVariant.CODEC, nbt.getCompound("fluid"), registryLookup);
        fluidAmount = nbt.getLong("fluidAmount");
        stack = ItemStack.fromNbtOrEmpty(registryLookup, nbt.getCompound("stack"));
        compostLevel = nbt.getFloat("compostLevel");
        lazeRecipeId = nbt.contains("recipe") ? Identifier.tryParse(nbt.getString("recipe")) : null;
        recipeProgress = nbt.getFloat("recipeProgress");
    }

    public void tick() {
        if (isCrafting() && world != null && world.isClient) {
            var random = world.random;
            world.addParticle(ParticleTypes.EFFECT,
                    pos.getX() + 0.5 + random.nextGaussian() * 0.2,
                    pos.getY() + 1,
                    pos.getZ() + 0.5 + random.nextGaussian() * 0.2,
                    0,
                    0,
                    0);
        }
        if (tickCounter <= 0) {
            tickCounter = FabricaeExNihilo.CONFIG.get().barrels().tickRate();
            markDirty();
            tickRecipe();
        } else {
            --tickCounter;
            markDirty();
        }
    }

    private void tickRecipe() {
        if (world == null/* || world.isClient*/) return;

        if (getRecipe() != null) {
            BarrelRecipe currentRecipe = getRecipe().value();
            if (!currentRecipe.canContinue(world, this)) {
                recipe = null;
                recipeProgress = 0;
                return;
            }

            var duration = currentRecipe.getDuration();
            recipeProgress += 1f / duration * getEfficiencyMultiplier();
            if (recipeProgress >= 1) {
                finishRecipe();
            }
            markDirty();
//            markForUpdate();
            return;
        }

        switch (state) {
            case EMPTY, FLUID -> BarrelRecipe.findTick(this).ifPresent(recipe -> {
                this.recipe = recipe;
                if (recipe.value().getDuration() == 0) {
                    finishRecipe(); // instant recipe
                }
                markDirty();
//                    markForUpdate();
            });
            case ITEM -> {}
            case COMPOST -> {
                if (compostLevel < 1) break;
                recipeProgress += (float) (FabricaeExNihilo.CONFIG.get().barrels().compostRate() * getEfficiencyMultiplier());
                if (recipeProgress >= 1) finishCompost();
                markDirty();
//                markForUpdate();
            }
        }

    }

    public void setFluid(FluidVariant fluid, long amount) {
        if (amount == 0 || fluid.isBlank()) {
            this.fluid = FluidVariant.blank();
            this.fluidAmount = 0;
            this.state = BarrelState.EMPTY;
        } else {
            this.fluid = fluid;
            this.fluidAmount = amount;
            this.state = BarrelState.FLUID;
        }
        markDirty();
        markForUpdate();
    }

    public void setItem(ItemStack stack) {
        setFluid(FluidVariant.blank(), 0);
        if (stack.isEmpty()) {
            this.state = BarrelState.EMPTY;
            this.stack = ItemStack.EMPTY;
        } else {
            this.state = BarrelState.ITEM;
            this.stack = stack.copy();
        }
        markDirty();
        markForUpdate();
    }

    public void fillCompost(ItemStack result, float increment) {
        switch (state) {
            case EMPTY:
                state = BarrelState.COMPOST;
                stack = result;
            case COMPOST:
                if (!ItemStack.areEqual(stack, result)) throw new IllegalStateException("Tired to compost incompatible item");
                compostLevel += increment;
                break;
            default:
                throw new IllegalStateException("Can't compost in state: " + state);
        }
        markDirty();
        markForUpdate();
    }

    public FluidVariant getFluid() {
        return fluid;
    }

    public long getFluidAmount() {
        return fluidAmount;
    }

    public ItemStack getItem() {
        return stack;
    }

    public BarrelState getState() {
        return state;
    }

    public boolean isCrafting() {
        return recipeProgress > 0;
    }

    void beginRecipe(RecipeEntry<BarrelRecipe> recipe) {
        this.recipe = recipe;
    }

    private void finishRecipe() {
        if (world instanceof ServerWorld serverWorld) {
            getRecipe().value().apply(serverWorld, this);
        }
        recipe = null;
        recipeProgress = 0;
        markForUpdate();
    }

    private void finishCompost() {
        state = BarrelState.ITEM;
        compostLevel = 0;
        recipeProgress = 0;
        markForUpdate();
    }

    @SuppressWarnings("unchecked")
    private RecipeEntry<BarrelRecipe> getRecipe() {
        if (lazeRecipeId != null && world != null) {
            var recipeEntry = world.getRecipeManager().get(lazeRecipeId);
            recipe = (RecipeEntry<BarrelRecipe>) recipeEntry.filter(r -> r.value() instanceof BarrelRecipe).orElse(null);
            lazeRecipeId = null;
        }
        return recipe;
    }

    public float getRecipeProgress() {
        return recipeProgress;
    }

    public float getCompostLevel() {
        return compostLevel;
    }

    public class Snapshot {
        private final BarrelState state;
        private final FluidVariant fluid;
        private final long fluidAmount;
        private final ItemStack stack;
        private final float compostLevel;
        private final RecipeEntry<BarrelRecipe> recipe;
        private final float recipeProgress;

        public Snapshot() {
            this.state = BarrelBlockEntity.this.state;
            this.fluid = BarrelBlockEntity.this.fluid;
            this.fluidAmount = BarrelBlockEntity.this.fluidAmount;
            this.stack = BarrelBlockEntity.this.stack.copy();
            this.compostLevel = BarrelBlockEntity.this.compostLevel;
            this.recipeProgress = BarrelBlockEntity.this.recipeProgress;
            this.recipe = BarrelBlockEntity.this.getRecipe();
        }

        public void apply() {
            BarrelBlockEntity.this.state = this.state;
            BarrelBlockEntity.this.fluid = this.fluid;
            BarrelBlockEntity.this.fluidAmount = this.fluidAmount;
            BarrelBlockEntity.this.stack = this.stack;
            BarrelBlockEntity.this.compostLevel = this.compostLevel;
            BarrelBlockEntity.this.recipeProgress = this.recipeProgress;
            BarrelBlockEntity.this.recipe = this.recipe;
        }
    }
}