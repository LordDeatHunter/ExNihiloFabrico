package wraith.fabricaeexnihilo.modules.crucibles;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ExtractionOnlyStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.InsertionOnlyStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.base.BaseBlockEntity;
import wraith.fabricaeexnihilo.modules.base.EnchantableBlockEntity;
import wraith.fabricaeexnihilo.modules.base.EnchantmentContainer;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleHeatRecipe;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleRecipe;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.Iterator;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class CrucibleBlockEntity extends BaseBlockEntity implements EnchantableBlockEntity {

    public static final Identifier BLOCK_ENTITY_ID = id("crucible");

    public static final BlockEntityType<CrucibleBlockEntity> TYPE = BlockEntityType.Builder.create(
            CrucibleBlockEntity::new,
            ModBlocks.CRUCIBLES.values().toArray(new CrucibleBlock[0])
    ).build(null);

    static {
        ItemStorage.SIDED.registerForBlockEntity((crucible, direction) -> crucible.itemStorage, TYPE);
        FluidStorage.SIDED.registerForBlockEntity((crucible, direction) -> crucible.fluidStorage, TYPE);
    }

    private final EnchantmentContainer enchantments = new EnchantmentContainer();
    private final Storage<FluidVariant> fluidStorage = new CrucibleFluidStorage();
    private final Storage<ItemVariant> itemStorage = new CrucibleItemStorage();
    private long contained = 0;
    private FluidVariant fluid = FluidVariant.blank();
    private int heat = 0;
    private boolean requiresFireproof = false;
    private long queued = 0;
    private ItemStack renderStack = ItemStack.EMPTY;
    private int tickCounter;

    public CrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(TYPE, pos, state);
        tickCounter = world == null
                ? FabricaeExNihilo.CONFIG.get().crucibles().tickRate()
                : world.random.nextInt(FabricaeExNihilo.CONFIG.get().crucibles().tickRate());
    }

    public ItemActionResult activate(@Nullable PlayerEntity player, @Nullable Hand hand) {
        if (world == null || player == null) {
            return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        var held = player.getStackInHand(hand == null ? player.getActiveHand() : hand);
        if (held == null || held.isEmpty()) {
            return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }

        var bucketFluidStorage = FluidStorage.ITEM.find(held, ContainerItemContext.ofPlayerHand(player, hand));
        if (bucketFluidStorage != null) {
            var amount = StorageUtil.move(fluidStorage, bucketFluidStorage, fluid -> true, Long.MAX_VALUE, null);
            if (amount > 0) {
                markDirty();
                return ItemActionResult.SUCCESS;
            }
        }

        try (var t = Transaction.openOuter()) {
            var amount = itemStorage.insert(ItemVariant.of(held), 1, t);
            if (amount > 0) {
                t.commit();
                if (!player.isCreative()) {
                    held.decrement((int) amount);
                }
                markDirty();
                return ItemActionResult.SUCCESS;
            }
        }

        return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }

    public long getContained() {
        return contained;
    }

    private int getEfficiencyMultiplier() {
        return 1 + enchantments.getEnchantmentLevel(Enchantments.EFFICIENCY);
    }

    @Override
    public EnchantmentContainer getEnchantmentContainer() {
        return enchantments;
    }

    public EnchantmentContainer getEnchantments() {
        return enchantments;
    }

    private int getFireAspectAdder() {
        return enchantments.getEnchantmentLevel(Enchantments.FIRE_ASPECT);
    }

    public FluidVariant getFluid() {
        return fluid;
    }

    public long getMaxCapacity() {
        return FluidConstants.BUCKET * (isFireproof() ? FabricaeExNihilo.CONFIG.get().crucibles().stoneVolume() : FabricaeExNihilo.CONFIG.get().crucibles().woodVolume());
    }

    public int getProcessingSpeed() {
        return getEfficiencyMultiplier() * (isFireproof() ? (requiresFireproof ? heat : 1) * FabricaeExNihilo.CONFIG.get().crucibles().stoneProcessingRate() : FabricaeExNihilo.CONFIG.get().crucibles().woodProcessingRate());
    }

    public long getQueued() {
        return queued;
    }

    public ItemStack getRenderStack() {
        return renderStack;
    }

    public boolean isFireproof() {
        return getCachedState().getBlock() instanceof CrucibleBlock crucible && crucible.isFireproof();
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt == null) {
            FabricaeExNihilo.LOGGER.warn("A crucible at {} is missing data.", pos);
            return;
        }
        readNbtWithoutWorldInfo(nbt, registryLookup);
    }

    private void readNbtWithoutWorldInfo(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        renderStack = ItemStack.fromNbtOrEmpty(registryLookup, nbt.getCompound("render"));
        fluid = CodecUtils.fromNbt(FluidVariant.CODEC, nbt.getCompound("fluid"), registryLookup);
        contained = nbt.getLong("contained");
        queued = nbt.getLong("queued");
        heat = nbt.getInt("heat");
        requiresFireproof = !nbt.contains("requiresFireproof") || nbt.getBoolean("requiresFireproof");
        enchantments.readNbt(nbt.getCompound("enchantments"), registryLookup);
        updateHeat(); // Enchantments affect heat
    }

    public void tick() {
        if (queued == 0 || contained > getMaxCapacity() || (requiresFireproof && (heat <= 0 || !isFireproof()))) {
            return;
        }
        if (--tickCounter <= 0) {
            var amount = Math.min(queued, getProcessingSpeed());
            contained += amount;
            queued -= amount;
            markDirty();
            tickCounter = FabricaeExNihilo.CONFIG.get().crucibles().tickRate();
        }
    }

    public void updateHeat() {
        if (world == null) {
            return;
        }
        var oldHeat = heat;
        var state = world.getBlockState(pos.down());
        heat = CrucibleHeatRecipe.find(state, world).map(RecipeEntry::value).map(CrucibleHeatRecipe::getHeat).orElse(0);
        if (state.getBlock() instanceof FluidBlock) {
            heat = (int) (heat * state.getFluidState().getHeight());
        }
        heat += getFireAspectAdder();
        if (heat != oldHeat) {
            markDirty();
        }
    }

    /**
     * NBT Serialization section
     */

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        writeNbtWithoutWorldInfo(nbt, registryLookup);
    }

    private void writeNbtWithoutWorldInfo(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.put("render", renderStack.encodeAllowEmpty(registryLookup));
        nbt.put("fluid", FluidVariant.CODEC.encodeStart(registryLookup.getOps(NbtOps.INSTANCE), fluid).getOrThrow());
        nbt.putLong("contained", contained);
        nbt.putLong("queued", queued);
        nbt.putInt("heat", heat);
        nbt.putBoolean("requiresFireproof", requiresFireproof);
        nbt.put("enchantments", enchantments.writeNbt());
    }

    private record CrucibleSnapshot(long contained, long queued, FluidVariant fluid, boolean requiresFireproof,
                                    ItemStack renderStack) {
    }

    private class CrucibleFluidStorage extends SnapshotParticipant<CrucibleSnapshot> implements SingleSlotStorage<FluidVariant>, ExtractionOnlyStorage<FluidVariant> {

        @Override
        protected CrucibleSnapshot createSnapshot() {
            return new CrucibleSnapshot(contained, queued, fluid, requiresFireproof, renderStack);
        }

        @Override
        public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
            if (!resource.equals(fluid))
                return 0;

            var amount = Math.min(maxAmount, contained);
            updateSnapshots(transaction);
            contained -= amount;
            return amount;
        }

        @Override
        public long getAmount() {
            return contained;
        }

        @Override
        public long getCapacity() {
            return getMaxCapacity();
        }

        @Override
        public FluidVariant getResource() {
            return fluid;
        }

        @Override
        public boolean isResourceBlank() {
            return fluid.isBlank();
        }

        @Override
        protected void readSnapshot(CrucibleSnapshot snapshot) {
            contained = snapshot.contained;
            queued = snapshot.queued;
            fluid = snapshot.fluid;
            requiresFireproof = snapshot.requiresFireproof;
            renderStack = snapshot.renderStack.copy();
        }
    }

    private class CrucibleItemStorage extends SnapshotParticipant<CrucibleSnapshot> implements InsertionOnlyStorage<ItemVariant>, SingleSlotStorage<ItemVariant> {
        @Override
        protected CrucibleSnapshot createSnapshot() {
            return new CrucibleSnapshot(contained, queued, fluid, requiresFireproof, renderStack.copy());
        }

        @Override
        public long getAmount() {
            return 0;
        }

        @Override
        public long getCapacity() {
            return 1;
        }

        @Override
        public ItemVariant getResource() {
            return ItemVariant.blank();
        }

        @Override
        public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            var recipeOptional = CrucibleRecipe.find(resource.toStack(), isFireproof(), world).map(RecipeEntry::value);
            if (recipeOptional.isEmpty()) return 0;
            var recipe = recipeOptional.get();
            if (!recipe.getFluid().equals(fluid) && !fluid.isBlank()) return 0;

            if (recipe.getAmount() > getMaxCapacity() - contained - queued) return 0;
            updateSnapshots(transaction);
            fluid = recipe.getFluid();
            queued += recipe.getAmount();
            requiresFireproof = recipe.requiresFireproofCrucible();
            renderStack = resource.toStack();
            return 1;
        }

        // Compiler dumb
        @Override
        public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            return InsertionOnlyStorage.super.extract(resource, maxAmount, transaction);
        }

        @Override
        public boolean isResourceBlank() {
            return true;
        }

        @Override
        protected void readSnapshot(CrucibleSnapshot snapshot) {
            contained = snapshot.contained;
            queued = snapshot.queued;
            fluid = snapshot.fluid;
            requiresFireproof = snapshot.requiresFireproof;
            renderStack = snapshot.renderStack.copy();
        }

        // Parents have conflicting impls, pick one
        @Override
        @NotNull
        public Iterator<StorageView<ItemVariant>> iterator() {
            return SingleSlotStorage.super.iterator();
        }
    }
}