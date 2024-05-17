package wraith.fabricaeexnihilo.client;

import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.ItemStack;
import wraith.fabricaeexnihilo.modules.base.Colored;
import wraith.fabricaeexnihilo.util.Color;

public final class FENItemColorProvider implements ItemColorProvider {

    private FENItemColorProvider() {
    }

    public static final FENItemColorProvider INSTANCE = new FENItemColorProvider();

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        return stack.getItem() instanceof Colored colored ? colored.getColor(tintIndex) : Color.WHITE.toIntAlphaOne();
    }

}