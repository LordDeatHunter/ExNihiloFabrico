package wraith.fabricaeexnihilo.modules.fluids;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import wraith.fabricaeexnihilo.modules.ModFluids;
import wraith.fabricaeexnihilo.modules.base.AbstractFluid;
import wraith.fabricaeexnihilo.modules.base.FluidSettings;

public class MilkFluid extends AbstractFluid {
    private static final FluidSettings FLUID_SETTINGS = new FluidSettings("milk", 0xFFFFFF, false, false);
    public static final Item BUCKET = Items.MILK_BUCKET;
    public static final MilkFluid FLOWING = new MilkFluid(false);
    public static final MilkFluid STILL = new MilkFluid(true);
    public static final FluidBlock BLOCK = new FluidBlock(STILL, ModFluids.getBlockSettings());

    static {
        // Milk buckets
//        FluidStorage.ITEM.registerForItems((stack, context) -> new FullItemFluidStorage(context, Items.BUCKET, FluidVariant.of(MilkFluid.STILL), FluidConstants.BUCKET), Items.MILK_BUCKET);
        FluidStorage.combinedItemApiProvider(BUCKET).register((context) -> new FullItemFluidStorage(context, Items.BUCKET, FluidVariant.of(MilkFluid.STILL), FluidConstants.BUCKET));
    }

    public MilkFluid(boolean isStill) {
        super(isStill, FLUID_SETTINGS,
                () -> BLOCK,
                () -> Items.MILK_BUCKET,
                () -> FLOWING,
                () -> STILL
        );
    }

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == STILL || fluid == FLOWING;
    }

}
