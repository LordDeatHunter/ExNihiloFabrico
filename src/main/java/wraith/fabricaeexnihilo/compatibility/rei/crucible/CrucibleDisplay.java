package wraith.fabricaeexnihilo.compatibility.rei.crucible;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleRecipe;

import java.util.List;
import java.util.Optional;

public class CrucibleDisplay implements Display {
    private final CategoryIdentifier<?> category;
    public final long amount;
    public final EntryIngredient input;
    public final EntryIngredient result;
    private final Identifier id;

    public CrucibleDisplay(RecipeEntry<CrucibleRecipe> recipeEntry, CategoryIdentifier<?> category) {
        var recipe = recipeEntry.value();
        this.input = EntryIngredients.ofIngredient(recipe.getInput());
        this.category = category;
        this.result = EntryIngredients.of(recipe.getFluid().getFluid());
        this.amount = recipe.getAmount();
        this.id = recipeEntry.id();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return category;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(input);
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of(result);
    }

    @Override
    public Optional<Identifier> getDisplayLocation() {
        return Optional.of(id);
    }
}