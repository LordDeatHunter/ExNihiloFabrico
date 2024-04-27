package wraith.fabricaeexnihilo.modules.barrels;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import wraith.fabricaeexnihilo.recipe.barrel.BarrelRecipe;

@SuppressWarnings("UnstableApiUsage")
public class BarrelItemStorage extends SnapshotParticipant<BarrelBlockEntity.Snapshot> implements SingleSlotStorage<ItemVariant> {
    private final BarrelBlockEntity barrel;

    public BarrelItemStorage(BarrelBlockEntity barrel) {
        this.barrel = barrel;
    }

    @Override
    public long insert(ItemVariant item, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(item, maxAmount);
        if (barrel.isCrafting()) return 0;
        if (barrel.getState() == BarrelState.ITEM) return 0;

        var recipe = BarrelRecipe.findInsert(barrel, item);

        if (recipe.isPresent()) {
            updateSnapshots(transaction);
            barrel.beginRecipe(recipe.get());
            return 1;
        }

        return 0;
    }

    @Override
    public long extract(ItemVariant item, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(item, maxAmount);
        if (barrel.isCrafting()) return 0;
        if (barrel.getState() != BarrelState.ITEM) return 0;
        if (!item.equals(getResource())) return 0;

        var extracted = Math.min(getAmount(), maxAmount);
        if (extracted == 0) return 0;

        updateSnapshots(transaction);
        var stack = barrel.getItem();
        stack.decrement((int) extracted);
        barrel.setItem(stack); // this handles resetting the state with empty stack

        return extracted;
    }

    @Override
    public boolean isResourceBlank() {
        return getResource().isBlank();
    }

    @Override
    public ItemVariant getResource() {
        return ItemVariant.of(barrel.getItem());
    }

    @Override
    public long getAmount() {
        return barrel.getItem().getCount();
    }

    @Override
    public long getCapacity() {
        return barrel.getItem().getMaxCount();
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
