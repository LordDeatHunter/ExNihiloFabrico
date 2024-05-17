package wraith.fabricaeexnihilo.compatibility.emi.recipes;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.recipe.RecipeEntry;
import wraith.fabricaeexnihilo.compatibility.emi.EmiIngredientUtil;
import wraith.fabricaeexnihilo.compatibility.emi.FENEmiPlugin;
import wraith.fabricaeexnihilo.compatibility.emi.FENEmiTextures;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterWorldRecipe;

public class EmiWitchWaterWorldRecipe extends BasicEmiRecipe {
    private static final int WIDTH = 11 * 18;
    private static final int HEIGHT = 3 * 18;
    private final EmiIngredient target;

    public EmiWitchWaterWorldRecipe(RecipeEntry<WitchWaterWorldRecipe> recipe) {
        super(FENEmiPlugin.WITCH_WATER_WORLD_CATEGORY, recipe.id(), WIDTH, HEIGHT);
        catalysts.add(EmiIngredient.of(WitchWaterFluid.TAG));
        target = EmiIngredientUtil.ingredientOf(recipe.value().getTarget());
        catalysts.add(target);
        outputs.addAll(EmiIngredientUtil.stacksOf(recipe.value().getResult()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(EmiStack.of(WitchWaterFluid.STILL), 0, 0).catalyst(true);
        widgets.addTexture(FENEmiTextures.PLUS, 1, 19);
        widgets.addSlot(target, 0, 36).catalyst(true);

        widgets.addTexture(FENEmiTextures.ARROW_RIGHT, 18, 18);

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 3; y++) {
                var slotIndex = y * 9 + x;
                if (slotIndex < outputs.size()) {
                    widgets.addSlot(outputs.get(slotIndex), 36 + x * 18, y * 18).recipeContext(this);
                } else {
                    widgets.addSlot(36 + x * 18, y * 18);
                }
            }
        }
    }
}
