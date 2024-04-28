package wraith.fabricaeexnihilo.compatibility.rei;

import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import wraith.fabricaeexnihilo.compatibility.recipeviewer.FireType;
import wraith.fabricaeexnihilo.mixins.FluidBlockAccess;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;

import java.util.function.Function;

public class ReiIngredientUtil {
    private ReiIngredientUtil() {}

    public static EntryIngredient of(FluidIngredient fluid) {
        return fluid.getValue().map(EntryIngredients::of, EntryIngredients::ofFluidTag);
    }

    public static EntryIngredient of(BlockIngredient ingredient) {
        Function<Block, EntryStack<?>> stackFunction = block -> {
            if (block == Blocks.FIRE) return EntryStack.of(FireEntryDefinition.TYPE, FireType.NORMAL);
            if (block == Blocks.SOUL_FIRE) return EntryStack.of(FireEntryDefinition.TYPE, FireType.SOUL);
            if (block instanceof FluidBlockAccess fluidBlock) return EntryStacks.of(fluidBlock.getFluid().getBucketItem());
            return EntryStacks.of(block);
        };
        return ingredient.getValue().map(
                block -> EntryIngredient.of(stackFunction.apply(block)),
                tag -> EntryIngredients.ofTag(tag, entry -> stackFunction.apply(entry.value())));
    }
}
