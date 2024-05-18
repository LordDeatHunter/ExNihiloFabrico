package wraith.fabricaeexnihilo.compatibility.megane;

import lol.bai.megane.api.provider.FluidProvider;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.fluid.Fluid;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.BarrelState;

class BarrelFluidProvider extends FluidProvider<BarrelBlockEntity> {
    @Override
    public int getSlotCount() {
        var barrel = getObject();
        if (barrel.getState() != BarrelState.FLUID || barrel.isCrafting()) return 0;
        return 1;
    }

    @Override
    public @Nullable Fluid getFluid(int slot) {
        var barrel = getObject();
        if (barrel.getState() != BarrelState.FLUID || barrel.isCrafting()) return null;
        return barrel.getFluid().getFluid();
    }

    @Override
    public double getStored(int slot) {
        var barrel = getObject();
        return droplets(barrel.getFluidAmount());
    }

    @Override
    public double getMax(int slot) {
        return droplets(FluidConstants.BUCKET);
    }
}
