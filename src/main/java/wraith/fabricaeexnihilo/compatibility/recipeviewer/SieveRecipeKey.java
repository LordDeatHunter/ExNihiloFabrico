package wraith.fabricaeexnihilo.compatibility.recipeviewer;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.recipe.SieveRecipe;
import wraith.fabricaeexnihilo.util.ItemUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record SieveRecipeKey(Ingredient input, boolean waterlogged, Item mesh, Identifier meshKey) {
    public static List<SieveRecipeKey> getKeys(SieveRecipe recipe) {
        var keys = new ArrayList<SieveRecipeKey>();

        for (var entry : recipe.getRolls().entrySet()) {
            keys.add(new SieveRecipeKey(recipe.getInput(),
                    recipe.isWaterlogged(),
                    ItemUtils.getItem(entry.getKey()),
                    entry.getKey()));
        }

        return keys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SieveRecipeKey that = (SieveRecipeKey) o;
        return waterlogged == that.waterlogged && inputsMatch(that) && Objects.equals(mesh, that.mesh);
    }

    private boolean inputsMatch(SieveRecipeKey that) {
        var first = input.getMatchingStacks();
        var second = that.input.getMatchingStacks();
        if (second.length != first.length)
            return false;

        for (int i = 0; i < first.length; i++) {
            if (!ItemStack.areEqual(first[i], second[i]))
                return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        //noinspection UnstableApiUsage
        return Objects.hash(Arrays.hashCode(Arrays.stream(input.getMatchingStacks()).map(ItemVariant::of).toArray()), waterlogged, mesh);
    }
}
