package wraith.fabricaeexnihilo.modules.infested;

import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.util.Lazy;
import java.util.HashMap;

public final class InfestedHelper {

    private InfestedHelper() {
    }

    private static final Lazy<HashMap<Block, InfestedLeavesBlock>> LEAF_TO_INFESTED = new Lazy<>(() -> {
        HashMap<Block, InfestedLeavesBlock> map = new HashMap<>();
        for (var entry : ModBlocks.INFESTED_LEAVES.values()) {
            map.put(entry.getLeafBlock(), entry);
        }
        return map;
    });

    public static ActionResult tryToInfest(World world, BlockPos pos) {
        var originalState = world.getBlockState(pos);
        var infestedBlock = LEAF_TO_INFESTED.get().get(originalState.getBlock());
        if (infestedBlock == null) {
            return ActionResult.PASS;
        }

        var newState = ModBlocks.INFESTING_LEAVES
                .getDefaultState()
                .with(LeavesBlock.DISTANCE, originalState.get(LeavesBlock.DISTANCE))
                .with(LeavesBlock.PERSISTENT, originalState.get(LeavesBlock.PERSISTENT));

        world.setBlockState(pos, newState);

        if (!(world.getBlockEntity(pos) instanceof InfestingLeavesBlockEntity blockEntity)) {
            FabricaeExNihilo.LOGGER.warn("Placed block doesn't have a block entity! (InfestedHelper)");
            return ActionResult.PASS;
        }

        blockEntity.setTarget(infestedBlock);

        return ActionResult.SUCCESS;
    }

    public static void tryToSpreadFrom(World world, BlockPos pos, int tries) {
        for (var attempt = 0; attempt < tries; ++attempt) {
            if ((world.random.nextFloat()) < FabricaeExNihilo.CONFIG.get().infested().spreadChance()) {
                var i = world.random.nextInt(3) - 1;
                var j = world.random.nextInt(3) - 1;
                var k = world.random.nextInt(3) - 1;
                var newPos = pos.add(new Vec3i(i, j, k));
                if (World.isValid(newPos)) {
                    tryToInfest(world, newPos);
                }
            }
        }
    }
}
