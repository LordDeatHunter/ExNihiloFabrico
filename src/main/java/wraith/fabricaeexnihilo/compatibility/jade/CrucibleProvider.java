package wraith.fabricaeexnihilo.compatibility.jade;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlockEntity;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

@SuppressWarnings("UnstableApiUsage")
public class CrucibleProvider implements IBlockComponentProvider {
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!(blockAccessor.getBlockEntity() instanceof CrucibleBlockEntity crucible)) return;

        if (crucible.getQueued() > 0) {
            tooltip.add(Text.translatable("fabricaeexnihilo.hud.crucible.queued", crucible.getQueued() / 81));
        }
        if (crucible.getContained() > 0) {
            var fluid = crucible.getFluid();
            tooltip.add(Text.translatable("fabricaeexnihilo.hud.fluid_content", FluidVariantAttributes.getName(fluid), crucible.getContained() / 81, crucible.getMaxCapacity() / 81));
        }
    }

    @Override
    public Identifier getUid() {
        return id("crucible");
    }
}
