package wraith.fabricaeexnihilo.modules.strainer;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleStackStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModLootContextTypes;
import wraith.fabricaeexnihilo.modules.base.BaseBlockEntity;

import java.util.stream.IntStream;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

@SuppressWarnings("UnstableApiUsage")
public class StrainerBlockEntity extends BaseBlockEntity {

    public static final BlockEntityType<StrainerBlockEntity> TYPE = FabricBlockEntityTypeBuilder.create(
            StrainerBlockEntity::new,
            ModBlocks.STRAINERS.values().toArray(StrainerBlock[]::new)
    ).build(null);

    public static final Identifier BLOCK_ENTITY_ID = id("strainer");

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(8, ItemStack.EMPTY);
    private final StrainerItemStorage storage = new StrainerItemStorage();
    private int timeUntilCatch = FabricaeExNihilo.CONFIG.get().strainers().maxWaitTime();

    static {
        ItemStorage.SIDED.registerForBlockEntity((entity, dir) -> entity.storage, TYPE);
    }

    public StrainerBlockEntity(BlockPos pos, BlockState state) {
        super(TYPE, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        inventory.clear();
        Inventories.readNbt(nbt, inventory);
        timeUntilCatch = nbt.getInt("timeUntilCatch");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("timeUntilCatch", timeUntilCatch);
    }

    public DefaultedList<ItemStack> getInventory() {
        return inventory;
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, StrainerBlockEntity strainer) {
        if (world.isClient || !state.get(StrainerBlock.WATERLOGGED))
            return;
        if (strainer.timeUntilCatch-- <= 0) {
            var params = new LootContextParameterSet.Builder((ServerWorld) world)
                    .add(LootContextParameters.BLOCK_ENTITY, strainer)
                    .add(LootContextParameters.BLOCK_STATE, state)
                    .add(LootContextParameters.ORIGIN, Vec3d.of(blockPos))
                    .build(ModLootContextTypes.STRAINER);

            var loot = world.getServer().getLootManager()
                    .getLootTable(id("gameplay/strainer"))
                    .generateLoot(params);
            for (int i = 0; i < strainer.inventory.size(); i++) {
                if (loot.isEmpty())
                    break;
                if (strainer.inventory.get(i).isEmpty()) {
                    strainer.inventory.set(i, loot.remove(0));
                }
            }
            var config = FabricaeExNihilo.CONFIG.get().strainers();
            strainer.timeUntilCatch = world.random.nextBetween(config.minWaitTime(), config.maxWaitTime());
            strainer.markForUpdate();
        }
        // dirty fix for https://github.com/LordDeatHunter/FabricaeExNihilo/issues/55
        if (--dirtyTimeout<=0) {
            // why do we even want to call this in tick()?
            strainer.markDirty();
            dirtyTimeout = 100;
        }
    }
    private static int dirtyTimeout = 100;

    private class StrainerItemStorage extends CombinedStorage<ItemVariant, StackStorage> {
        public StrainerItemStorage() {
            super(IntStream.range(0, 8).mapToObj(StackStorage::new).toList());
        }
    }

    private class StackStorage extends SingleStackStorage {
        private final int index;

        private StackStorage(int index) {
            this.index = index;
        }

        @Override
        protected ItemStack getStack() {
            return inventory.get(index);
        }

        @Override
        protected void setStack(ItemStack stack) {
            inventory.set(index, stack);
        }

        @Override
        protected int getCapacity(ItemVariant itemVariant) {
            return 1;
        }

        @Override
        protected boolean canInsert(ItemVariant itemVariant) {
            return false;
        }

        @Override
        public boolean supportsInsertion() {
            return false;
        }

        @Override
        protected void onFinalCommit() {
            markDirty();
            markForUpdate();
        }
    }

}
