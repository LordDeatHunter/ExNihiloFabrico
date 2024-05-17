package wraith.fabricaeexnihilo.compatibility.megane;

import lol.bai.megane.api.provider.FluidProvider;
import net.minecraft.fluid.Fluid;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.crucibles.CrucibleBlockEntity;

public class CrucibleFluidProvider extends FluidProvider<CrucibleBlockEntity> {
    @Override
    public int getSlotCount() {
        return 1;
    }

    @Override
    public @Nullable Fluid getFluid(int slot) {
        return getObject().getFluid().getFluid();
    }

    @Override
    public double getStored(int slot) {
        return droplets(getObject().getContained());
    }

    @Override
    public double getMax(int slot) {
        return droplets(getObject().getMaxCapacity());
    }
}
