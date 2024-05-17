package wraith.fabricaeexnihilo.modules.farming;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.util.Lazy;

public class TransformingItem extends Item {
    private final Lazy<Block> from;
    private final Lazy<Block> to;

    public TransformingItem(Lazy<Block> from, Lazy<Block> to,  Item.Settings settings) {
        super(settings);
        this.from = from;
        this.to = to;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var world = context.getWorld();
        var blockPos = context.getBlockPos();
        var target = world.getBlockState(blockPos).getBlock();
        //TODO: make this work if someone overrides a block
        if (target == from.get()) {
            world.setBlockState(blockPos, to.get().getDefaultState());
            if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
                context.getStack().decrement(1);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

}