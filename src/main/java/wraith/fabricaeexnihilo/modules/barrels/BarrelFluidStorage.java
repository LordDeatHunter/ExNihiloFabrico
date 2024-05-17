package wraith.fabricaeexnihilo.modules.barrels;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import wraith.fabricaeexnihilo.modules.ModTags;

@SuppressWarnings("deprecation")
public class BarrelFluidStorage extends SnapshotParticipant<BarrelBlockEntity.Snapshot> implements SingleSlotStorage<FluidVariant> {
    private final BarrelBlockEntity barrel;

    public BarrelFluidStorage(BarrelBlockEntity barrel) {
        this.barrel = barrel;
    }

    @Override
    public long insert(FluidVariant fluid, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(fluid, maxAmount);
        if (barrel.isCrafting()) return 0;
        if (barrel.getState() == BarrelState.FLUID && !fluid.equals(barrel.getFluid())) return 0;
        if (barrel.getState() != BarrelState.FLUID && barrel.getState() != BarrelState.EMPTY) return 0;
        if (fluid.getFluid().isIn(ModTags.HOT) && !barrel.isFireproof()) return 0;

        var inserted = Math.min(getCapacity() - getAmount(), maxAmount);
        if (inserted == 0) return 0;

        updateSnapshots(transaction);
        barrel.setFluid(fluid, getAmount() + inserted);

        return inserted;
    }

    @Override
    public long extract(FluidVariant fluid, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(fluid, maxAmount);
        if (barrel.isCrafting()) return 0;
        if (barrel.getState() != BarrelState.FLUID) return 0;
        if (!fluid.equals(barrel.getFluid())) return 0;

        var extracted = Math.min(getAmount(), maxAmount);
        if (extracted == 0) return 0;

        updateSnapshots(transaction);
        barrel.setFluid(barrel.getFluid(), getAmount() - extracted);

        return extracted;
    }

    @Override
    public boolean isResourceBlank() {
        return getResource().isBlank();
    }

    @Override
    public FluidVariant getResource() {
        return barrel.getFluid();
    }

    @Override
    public long getAmount() {
        return barrel.getFluidAmount();
    }

    @Override
    public long getCapacity() {
        return FluidConstants.BUCKET;
    }

    @Override
    protected BarrelBlockEntity.Snapshot createSnapshot() {
        return barrel.new Snapshot();
    }

    @Override
    protected void readSnapshot(BarrelBlockEntity.Snapshot snapshot) {
        snapshot.apply();
    }
}
