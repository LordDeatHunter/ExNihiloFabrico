package wraith.fabricaeexnihilo.compatibility.recipeviewer;

import com.google.common.collect.HashMultimap;
import net.minecraft.recipe.RecipeManager;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import java.util.HashMap;

public class SieveRecipeCombiner {
    public static void combineRecipes(RecipeManager recipeManager, int maxOutputs, RecipeConsumer consumer) {
        var sieveRecipes = recipeManager.listAllOfType(ModRecipes.SIEVE);
        var map = new HashMap<SieveRecipeKey, SieveRecipeOutputs>();
        for (var recipe : sieveRecipes) {
            for (var key : SieveRecipeKey.getKeys(recipe.value())) {
                var outputs = SieveRecipeOutputs.of(recipe.value(), key.meshKey());
                map.computeIfAbsent(key, __ -> new SieveRecipeOutputs(HashMultimap.create()))
                        .outputs().putAll(outputs.outputs());
            }
        }

        for (var entry : map.entrySet()) {
            for (var outputs : entry.getValue().split(maxOutputs)) {
                consumer.accept(entry.getKey(), outputs);
            }
        }
    }

    @FunctionalInterface
    public interface RecipeConsumer {
        void accept(SieveRecipeKey key, SieveRecipeOutputs outputs);
    }
}
