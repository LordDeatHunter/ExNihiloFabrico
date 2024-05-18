package wraith.fabricaeexnihilo.modules;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import wraith.fabricaeexnihilo.modules.base.AbstractFluid;
import wraith.fabricaeexnihilo.modules.fluids.BloodFluid;
import wraith.fabricaeexnihilo.modules.fluids.BrineFluid;
import wraith.fabricaeexnihilo.modules.fluids.MilkFluid;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;

import java.util.List;

public final class ModFluids {
    public static final List<AbstractFluid> FLUIDS = List.of(
            WitchWaterFluid.STILL,
            MilkFluid.STILL,
            BrineFluid.STILL,
            BloodFluid.STILL
    );

    public static AbstractBlock.Settings getBlockSettings() {
        return AbstractBlock.Settings.copy(Blocks.WATER).liquid().replaceable().noCollision();
    }

    public static Item.Settings getBucketItemSettings() {
        return new Item.Settings().maxCount(1).recipeRemainder(Items.BUCKET);
    }

    public static void registerBuckets() {
        FLUIDS.stream().filter(fluid -> fluid != MilkFluid.STILL).forEach(AbstractFluid::registerBucket);
    }

    public static void registerFluidBlocks() {
        FLUIDS.forEach(AbstractFluid::registerFluidBlock);
    }

    public static void registerFluids() {
        FLUIDS.forEach(AbstractFluid::registerFluids);
    }
}
