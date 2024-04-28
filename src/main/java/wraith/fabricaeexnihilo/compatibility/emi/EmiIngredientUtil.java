package wraith.fabricaeexnihilo.compatibility.emi;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import wraith.fabricaeexnihilo.compatibility.recipeviewer.FireType;
import wraith.fabricaeexnihilo.mixins.FluidBlockAccess;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;
import wraith.fabricaeexnihilo.recipe.util.Loot;
import wraith.fabricaeexnihilo.recipe.util.WeightedList;

import java.util.List;
import java.util.function.Function;

public class EmiIngredientUtil {
    private EmiIngredientUtil() {}

    public static EmiIngredient ingredientOf(FluidIngredient fluid) {
        return fluid.getValue().map(EmiStack::of, tag -> EmiIngredient.of(Registries.FLUID.getEntryList(tag)
                .stream()
                .flatMap(RegistryEntryList::stream)
                .map(RegistryEntry::value)
                .map(EmiStack::of)
                .toList()));
    }

    public static EmiIngredient ingredientOf(BlockIngredient ingredient) {
        Function<Block, EmiStack> blockFunction = block -> {
            if (block == Blocks.FIRE) return new FireEmiStack(FireType.NORMAL);
            if (block == Blocks.SOUL_FIRE) return new FireEmiStack(FireType.SOUL);
            if (block instanceof FluidBlockAccess fluidBlock) return EmiStack.of(fluidBlock.getFluid().getBucketItem());
            return EmiStack.of(block);
        };

        return ingredient.getValue().map(blockFunction, tag -> EmiIngredient.of(Registries.BLOCK.getEntryList(tag)
                .stream()
                .flatMap(RegistryEntryList::stream)
                .map(RegistryEntry::value)
                .map(blockFunction)
                .toList()));
    }

    public static List<EmiStack> stacksOf(Loot loot) {
        return loot.chances().stream()
                .map(chance -> EmiStack.of(loot.stack()).setChance((float)(double)chance))
                .toList();
    }

    public static List<EmiStack> stacksOf(WeightedList list) {
        return list.getValues()
                .entrySet()
                .stream()
                .map(entry -> EmiStack.of(entry.getKey()).setChance(entry.getValue() / (float) list.getTotalWeight()))
                .toList();
    }
}
