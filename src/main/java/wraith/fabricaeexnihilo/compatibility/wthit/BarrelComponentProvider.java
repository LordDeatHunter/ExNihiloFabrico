package wraith.fabricaeexnihilo.compatibility.wthit;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import net.minecraft.text.Text;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.BarrelState;

public class BarrelComponentProvider implements IBlockComponentProvider {
    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        BarrelBlockEntity barrel = accessor.getBlockEntity();
        if (barrel == null) return;

        if (barrel.getState() == BarrelState.COMPOST) {
            if (barrel.getCompostLevel() < 1) {
                tooltip.addLine(Text.translatable("fabricaeexnihilo.hud.barrel.compost.filling", (int) (barrel.getCompostLevel() * 100)));
            } else {
                tooltip.addLine(Text.translatable("fabricaeexnihilo.hud.barrel.compost.composting", (int) (barrel.getRecipeProgress() * 100)));
            }
        } else if (barrel.isCrafting()) {
            tooltip.addLine(Text.translatable("fabricaeexnihilo.hud.barrel.alchemy.processing", (int) (100.0 * barrel.getRecipeProgress())));
        }
    }
}
