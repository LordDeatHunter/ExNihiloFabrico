package wraith.fabricaeexnihilo.modules.infested;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class SilkWormItem extends Item {

    public SilkWormItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var result = InfestedHelper.tryToInfest(context.getWorld(), context.getBlockPos());
        if (result == ActionResult.SUCCESS && (context.getPlayer() == null || !context.getPlayer().isCreative())) {
            context.getStack().decrement(1);
        }
        return result;
    }

}
