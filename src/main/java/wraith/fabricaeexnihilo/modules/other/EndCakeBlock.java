package wraith.fabricaeexnihilo.modules.other;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class EndCakeBlock extends CakeBlock {

    public EndCakeBlock(Settings settings) {
        super(settings);
    }

    protected static ItemActionResult tryEat(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.getRegistryKey() == World.END) {
            return ItemActionResult.FAIL;
        }
        if (!(world instanceof ServerWorld serverWorld) || player.hasVehicle() || player.hasPassengers() || !player.canUsePortals(false)) {
            return ItemActionResult.FAIL;
        }
        int i = state.get(BITES);
        if (i < 6) {
            world.setBlockState(pos, state.with(BITES, i + 1), Block.NOTIFY_ALL);
        } else {
            world.removeBlock(pos, false);
            world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
        }
        TeleportTarget destination = ((EndPortalBlock) Blocks.END_PORTAL).createTeleportTarget(serverWorld, player, pos);
        if (destination == null) {
            return ItemActionResult.FAIL;
        }
        player.teleportTo(destination);
        return ItemActionResult.SUCCESS;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            if (tryEat(world, pos, state, player).isAccepted()) {
                return ActionResult.SUCCESS;
            }
            // empty stack assumed -- onUse is only called for empty hand interactions
            return ActionResult.CONSUME;
        }
        return tryEat(world, pos, state, player).toActionResult();
    }

    @Override
    public ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        if (world.isClient) {
            if (tryEat(world, pos, state, player).isAccepted()) {
                return ItemActionResult.SUCCESS;
            }
            if (stack.isEmpty()) {
                return ItemActionResult.CONSUME;
            }
        }
        return tryEat(world, pos, state, player);
    }

}
