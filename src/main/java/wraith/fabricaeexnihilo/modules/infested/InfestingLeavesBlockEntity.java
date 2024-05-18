package wraith.fabricaeexnihilo.modules.infested;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.base.BaseBlockEntity;
import wraith.fabricaeexnihilo.modules.base.Colored;
import wraith.fabricaeexnihilo.util.Color;

import java.util.Optional;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class InfestingLeavesBlockEntity extends BaseBlockEntity implements Colored {
    public static final Identifier BLOCK_ENTITY_ID = id("infesting");
    private double progress = 0.0;
    private InfestedLeavesBlock target = ModBlocks.INFESTED_LEAVES.values().stream().findFirst().orElseThrow();
    private int tickCounter = 0;
    public static final BlockEntityType<InfestingLeavesBlockEntity> TYPE = BlockEntityType.Builder.create(
            InfestingLeavesBlockEntity::new,
            ModBlocks.INFESTING_LEAVES
    ).build(null);

    public InfestingLeavesBlockEntity(BlockPos pos, BlockState state) {
        super(TYPE, pos, state);
    }

    public static void ticker(World world, BlockPos pos, BlockState state, InfestingLeavesBlockEntity blockEntity) {
        // Don't update every single tick
        if (++blockEntity.tickCounter % FabricaeExNihilo.CONFIG.get().infested().updateFrequency() != 0) {
            return;
        }
        // Advance
        blockEntity.progress += FabricaeExNihilo.CONFIG.get().infested().progressPerUpdate();
        blockEntity.markDirty();

        if (blockEntity.progress >= 1f) { // Done Transforming
            var newState = blockEntity.target.getDefaultState()
                    .with(LeavesBlock.DISTANCE, state.get(LeavesBlock.DISTANCE))
                    .with(LeavesBlock.PERSISTENT, state.get(LeavesBlock.PERSISTENT));
            world.setBlockState(pos, newState);
        } else if (!world.isClient && blockEntity.progress > FabricaeExNihilo.CONFIG.get().infested().minimumSpreadProgress()) {
            InfestedHelper.tryToSpreadFrom(world, pos, FabricaeExNihilo.CONFIG.get().infested().infestingSpreadAttempts());
        }
    }

    @Override
    public int getColor(int index) {
        var originalColor = MinecraftClient.getInstance().getBlockColors().getColor(Registries.BLOCK.get(target.getOld()).getDefaultState(), world, pos, 0);
        return Color.average(Color.WHITE, new Color(originalColor), progress).toInt();
    }

    public double getProgress() {
        return progress;
    }

    public InfestedLeavesBlock getTarget() {
        return target;
    }

    public void setTarget(InfestedLeavesBlock target) {
        this.target = target;
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        readNbtWithoutWorldInfo(nbt, registryLookup);
    }

    public void readNbtWithoutWorldInfo(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        progress = nbt.getDouble("progress");
        target = Registries.BLOCK.getOrEmpty(new Identifier(nbt.getString("target")))
                .flatMap(block -> block instanceof InfestedLeavesBlock infested ? Optional.of(infested) : Optional.empty())
                .orElse(ModBlocks.INFESTED_LEAVES.values().stream().findFirst().orElseThrow());
    }

    @SuppressWarnings("deprecation")
    public void toNBTWithoutWorldInfo(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putDouble("progress", progress);
        nbt.putString("target", target.getRegistryEntry().getKey().get().getValue().toString());
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        toNBTWithoutWorldInfo(nbt, registryLookup);
    }
}