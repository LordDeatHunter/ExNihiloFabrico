package wraith.fabricaeexnihilo.compatibility.rei.witchwater;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.compatibility.rei.ReiIngredientUtil;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterWorldRecipe;

import java.util.List;
import java.util.Optional;

public class WitchWaterWorldDisplay implements Display {
    public final EntryIngredient input;
    public final List<EntryIngredient> outputs;
    private final Identifier id;

    public WitchWaterWorldDisplay(RecipeEntry<WitchWaterWorldRecipe> recipeEntry) {
        var recipe = recipeEntry.value();
        FluidIngredient fluidIngredient = recipe.getTarget();
        this.input = ReiIngredientUtil.of(fluidIngredient);
        this.outputs = recipe.getResult().flatten(EntryIngredients::of);
        this.id = recipeEntry.id();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.WITCH_WATER_WORLD;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(input);
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return outputs;
    }

    @Override
    public Optional<Identifier> getDisplayLocation() {
        return Optional.of(id);
    }
}
