package wraith.fabricaeexnihilo.compatibility.emi.recipes;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.text.Text;
import wraith.fabricaeexnihilo.compatibility.emi.EmiIngredientUtil;
import wraith.fabricaeexnihilo.compatibility.emi.FENEmiPlugin;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleHeatRecipe;

public class EmiCrucibleHeatRecipe extends BasicEmiRecipe {
    private static final int WIDTH = 150;
    private static final int HEIGHT = 18 * 2;
    private final EmiIngredient block;
    private final int heat;

    public EmiCrucibleHeatRecipe(RecipeEntry<CrucibleHeatRecipe> recipe) {
        super(FENEmiPlugin.CRUCIBLE_HEAT_CATEGORY, recipe.id(), WIDTH, HEIGHT);
        block = EmiIngredientUtil.ingredientOf(recipe.value().getBlock());
        catalysts.add(block);
        heat = recipe.value().getHeat();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(block, 0, 18).catalyst(true);
        widgets.addTexture(EmiTexture.FULL_FLAME, 2, 2);

        widgets.addText(Text.translatable("emi.category.fabricaeexnihilo.crucible_heat.speed", heat), 22, 24, 0xFF404040, false);
    }
}
