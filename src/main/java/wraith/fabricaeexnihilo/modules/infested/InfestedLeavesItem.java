package wraith.fabricaeexnihilo.modules.infested;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import wraith.fabricaeexnihilo.modules.base.Colored;
import wraith.fabricaeexnihilo.util.Color;

public class InfestedLeavesItem extends BlockItem implements Colored {

    public InfestedLeavesItem(InfestedLeavesBlock block, Item.Settings settings) {
        super(block, settings);
    }

    @Override
    public int getColor(int index) {
        return Color.WHITE.toInt();
    }
}
