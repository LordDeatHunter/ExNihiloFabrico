package wraith.fabricaeexnihilo.modules.crucibles;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

@SuppressWarnings("UnstableApiUsage")
public class CrucibleBlockEntity extends BaseBlockEntity implements EnchantableBlockEntity {

    public static final Identifier BLOCK_ENTITY_ID = id("crucible");

    public static final BlockEntityType<CrucibleBlockEntity> TYPE = FabricBlockEntityTypeBuilder.create(
            CrucibleBlockEntity::new,
            ModBlocks.CRUCIBLES.values().toArray(new CrucibleBlock[0])
    ).build(null);

    static {
        ItemStorage.SIDED.registerForBlockEntity((crucible, direction) -> crucible.itemStorage, TYPE);
        FluidStorage.SIDED.registerForBlockEntity((crucible, direction) -> crucible.fluidStorage, TYPE);
    }

    /**
     * Enchantments
     */
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

    @SuppressWarnings("unused") // lambda stuff
    public static void ticker(World world, BlockPos blockPos, BlockState blockState, CrucibleBlockEntity crucibleEntity) {
        crucibleEntity.tick();
    }

    public ActionResult activate(@Nullable PlayerEntity player, @Nullable Hand hand) {
        if (world == null || player == null) {
            return ActionResult.PASS;
        }
        var held = player.getStackInHand(hand == null ? player.getActiveHand() : hand);
        if (held == null || held.isEmpty()) {
            return ActionResult.PASS;
        }

        var bucketFluidStorage = FluidStorage.ITEM.find(held, ContainerItemContext.ofPlayerHand(player, hand));
        if (bucketFluidStorage != null) {
            var amount = StorageUtil.move(fluidStorage, bucketFluidStorage, fluid -> true, Long.MAX_VALUE, null);
            if (amount > 0) {
                markDirty();
                markForUpdate();
                return ActionResult.SUCCESS;
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
                markForUpdate();
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
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
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt == null) {
            FabricaeExNihilo.LOGGER.warn("A crucible at " + pos + " is missing data.");
            return;
        }
        readNbtWithoutWorldInfo(nbt);
    }

    private void readNbtWithoutWorldInfo(NbtCompound nbt) {
        renderStack = ItemStack.fromNbt(nbt.getCompound("render"));
        fluid = CodecUtils.fromNbt(CodecUtils.FLUID_VARIANT, nbt.get("fluid"));
        contained = nbt.getLong("contained");
        queued = nbt.getLong("queued");
        heat = nbt.getInt("heat");
        requiresFireproof = !nbt.contains("requiresFireproof") || nbt.getBoolean("requiresFireproof");
        enchantments.readNbt(nbt.getCompound("enchantments"));
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
            markForUpdate();
            tickCounter = FabricaeExNihilo.CONFIG.get().crucibles().tickRate();
        }
    }

    public void updateHeat() {
        if (world == null) {
            return;
        }
        var oldHeat = heat;
        var state = world.getBlockState(pos.down());
        heat = CrucibleHeatRecipe.find(state, world).map(CrucibleHeatRecipe::getHeat).orElse(0);
        if (state.getBlock() instanceof FluidBlock) {
            heat *= state.getFluidState().getHeight();
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
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        writeNbtWithoutWorldInfo(nbt);
    }

    private void writeNbtWithoutWorldInfo(NbtCompound nbt) {
        nbt.put("render", renderStack.writeNbt(new NbtCompound()));
        nbt.put("fluid", CodecUtils.toNbt(CodecUtils.FLUID_VARIANT, fluid));
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
            var recipeOptional = CrucibleRecipe.find(resource.toStack(), isFireproof(), world);
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