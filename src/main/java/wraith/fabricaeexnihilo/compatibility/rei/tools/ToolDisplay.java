package wraith.fabricaeexnihilo.compatibility.rei.tools;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.rei.ReiIngredientUtil;
import wraith.fabricaeexnihilo.recipe.ToolRecipe;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;

import java.util.List;
import java.util.Optional;

//TODO: Make this like sieve, big box of outputs
public class ToolDisplay implements Display {
    public final CategoryIdentifier<ToolDisplay> category;
    public final EntryIngredient block;
    public final EntryIngredient result;
    private final Identifier id;

    public ToolDisplay(RecipeEntry<ToolRecipe> recipeEntry, CategoryIdentifier<ToolDisplay> category) {
        var recipe = recipeEntry.value();
        this.category = category;
        BlockIngredient blockIngredient = recipe.getBlock();
        this.block = ReiIngredientUtil.of(blockIngredient);
        this.result = EntryIngredients.of(recipe.getResult().stack());
        this.id = recipeEntry.id();
    }

    @Override
    public CategoryIdentifier<ToolDisplay> getCategoryIdentifier() {
        return category;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(block);
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
