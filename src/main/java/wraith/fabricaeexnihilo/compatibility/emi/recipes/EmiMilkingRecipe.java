package wraith.fabricaeexnihilo.compatibility.emi.recipes;

import dev.emi.emi.api.FabricEmiStack;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.text.Text;
import wraith.fabricaeexnihilo.compatibility.emi.FENEmiPlugin;
import wraith.fabricaeexnihilo.compatibility.recipeviewer.EntityRenderer;
import wraith.fabricaeexnihilo.modules.ModTags;
import wraith.fabricaeexnihilo.recipe.barrel.MilkingRecipe;

@SuppressWarnings("UnstableApiUsage")
public class EmiMilkingRecipe extends BasicEmiRecipe {
    private static final int WIDTH = 8 * 18;
    private static final int HEIGHT = 3 * 18;
    private final EntityType<?> entity;
    private final EmiStack fluid;
    private final int cooldown;

    public EmiMilkingRecipe(RecipeEntry<MilkingRecipe> recipeEntry) {
        super(FENEmiPlugin.MILKING_CATEGORY, recipeEntry.id(), WIDTH, HEIGHT);
        var recipe = recipeEntry.value();
        entity = recipe.getEntity();
        fluid = FabricEmiStack.of(recipe.getFluid(), recipe.getAmount());
        outputs.add(fluid);
        cooldown = recipe.getCooldown();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        var entity = this.entity.create(MinecraftClient.getInstance().world);
        if (entity != null) {
            widgets.addDrawable(0, 0, 3 * 18, 3 * 18, (draw, mouseX, mouseY, delta) ->
                    EntityRenderer.drawEntity(draw, mouseX, mouseY, entity, 0, 0, 3 * 18, 3 * 18, 30));
        }

        widgets.addTexture(EmiTexture.PLUS, 3 * 18 + 2, 18 + 2);
        widgets.addSlot(EmiIngredient.of(ModTags.BARRELS), 4 * 18, 18).drawBack(false);
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 5 * 18, 18);
        widgets.addSlot(fluid, 6 * 18 + 9, 18);
        widgets.addText(Text.translatable("emi.category.fabricaeexnihilo.barrel.milking.cooldown", cooldown / 20f), 3 * 18, 2 * 18 + 4, 0xFF404040, false);
    }
}
