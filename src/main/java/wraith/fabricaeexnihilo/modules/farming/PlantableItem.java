package wraith.fabricaeexnihilo.modules.farming;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import wraith.fabricaeexnihilo.util.Lazy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlantableItem extends Item {
    private final Lazy<Block[]> plants;

    public PlantableItem(Lazy<Block[]> plants,  Item.Settings settings) {
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

            if (state.canPlaceAt(world, plantPos)) {
                world.setBlockState(plantPos, state);
                var player = context.getPlayer();
                if (player != null && !player.isCreative()) {
                    context.getStack().decrement(1);
                }
                return ActionResult.SUCCESS;
            }
        }
        return super.useOnBlock(context);
    }

}
