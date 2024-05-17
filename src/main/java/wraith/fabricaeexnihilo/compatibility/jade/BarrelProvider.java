package wraith.fabricaeexnihilo.compatibility.jade;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.impl.ui.ItemStackElement;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class BarrelProvider implements IBlockComponentProvider {
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!(blockAccessor.getBlockEntity() instanceof BarrelBlockEntity barrel)) return;


        if (barrel.isCrafting()) {
            tooltip.add(Text.translatable("fabricaeexnihilo.hud.barrel.alchemy.processing", (int) (100.0 * barrel.getRecipeProgress())));
            return;
        }

        switch (barrel.getState()) {
            case EMPTY -> {
            }
            case FLUID -> {
                var fluid = barrel.getFluid();
                var name = FluidVariantAttributes.getName(fluid);
                tooltip.add(Text.translatable("fabricaeexnihilo.hud.fluid_content", name, barrel.getFluidAmount() / 81, 1000));
            }
            case ITEM -> {
                tooltip.add(ItemStackElement.of(barrel.getItem()));
            }
            case COMPOST -> {
                if (barrel.getCompostLevel() < 1) {
                    tooltip.add(Text.translatable("fabricaeexnihilo.hud.barrel.compost.filling", (int) (barrel.getCompostLevel() * 100)));
                } else {
                    tooltip.add(Text.translatable("fabricaeexnihilo.hud.barrel.compost.composting", (int) (barrel.getRecipeProgress() * 100)));
                }
            }
        }
    }

    @Override
    public Identifier getUid() {
        return id("barrel");
    }
}
