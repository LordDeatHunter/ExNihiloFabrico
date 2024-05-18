package wraith.fabricaeexnihilo.compatibility.emi.recipes;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.recipe.RecipeEntry;
import wraith.fabricaeexnihilo.compatibility.emi.EmiIngredientUtil;
import wraith.fabricaeexnihilo.compatibility.emi.FENEmiPlugin;
import wraith.fabricaeexnihilo.compatibility.emi.FENEmiTextures;
import wraith.fabricaeexnihilo.recipe.ToolRecipe;

public class EmiToolRecipe extends BasicEmiRecipe {
    private static final int HEIGHT = 18;

    private final EmiIngredient block;
    private final EmiTexture glyph;

    public EmiToolRecipe(RecipeEntry<ToolRecipe> recipeEntry) {
        super(recipeEntry.value().getTool() == ToolRecipe.ToolType.HAMMER ? FENEmiPlugin.HAMMERING_CATEGORY : FENEmiPlugin.CROOKING_CATEGORY,
                recipeEntry.id(), calcWidth(recipeEntry.value()), HEIGHT);
        var recipe = recipeEntry.value();
        block = EmiIngredientUtil.ingredientOf(recipe.getBlock());
        inputs.add(block);
        outputs.addAll(EmiIngredientUtil.stacksOf(recipe.getResult()));
        glyph = (recipe.getTool() == ToolRecipe.ToolType.HAMMER) ? FENEmiTextures.HAMMER : FENEmiTextures.CROOK;
    }

    private static int calcWidth(ToolRecipe recipe) {
        return 18 * 2 + recipe.getResult().chances().size() * 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(block, 0, 0);
        widgets.addTexture(glyph, 18 + 1, 1);
        for (int i = 0; i < outputs.size(); i++) {
            widgets.addSlot(outputs.get(i), 18 * (2 + i), 0).recipeContext(this);
        }
    }
}
