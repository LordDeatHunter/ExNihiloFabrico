package wraith.fabricaeexnihilo.modules.farming;

import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import wraith.fabricaeexnihilo.util.Lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TallPlantableItem extends Item {

    private final Lazy<TallPlantBlock[]> plants;

    public TallPlantableItem(Lazy<TallPlantBlock[]> plants,  Item.Settings settings) {
        super(settings);
        this.plants = plants;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var world = context.getWorld();
        var plantPos = context.getBlockPos().offset(context.getSide());
        var shuffledPlants = new ArrayList<>(List.of(plants.get()));
        Collections.shuffle(shuffledPlants);
        for (var plant : shuffledPlants) {
            var state = plant.getPlacementState(new ItemPlacementContext(context));
            if (state == null)
                return ActionResult.PASS;
            var lower = state.with(TallPlantBlock.HALF, DoubleBlockHalf.LOWER);
            var upper = state.with(TallPlantBlock.HALF, DoubleBlockHalf.UPPER);
            if (lower.canPlaceAt(world, plantPos) && world.isAir(plantPos.up())) {
                world.setBlockState(plantPos, lower);
                world.setBlockState(plantPos.up(), upper);
                var player = context.getPlayer();
                if (player != null && !player.isCreative()) {
                    context.getStack().decrement(1);
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

}
