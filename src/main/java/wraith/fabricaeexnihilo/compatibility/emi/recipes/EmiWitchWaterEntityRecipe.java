package wraith.fabricaeexnihilo.compatibility.emi.recipes;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import wraith.fabricaeexnihilo.compatibility.emi.FENEmiPlugin;
import wraith.fabricaeexnihilo.compatibility.recipeviewer.EntityRenderer;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterEntityRecipe;

import java.util.ArrayList;
import java.util.List;

public class EmiWitchWaterEntityRecipe extends BasicEmiRecipe {
    public static final int WIDTH = 8 * 18;
    public static final int HEIGHT = 3 * 18;
    private final EntityType<?> target;
    private final EntityType<?> result;
    private final NbtPathArgumentType.NbtPath nbt;

    public EmiWitchWaterEntityRecipe(RecipeEntry<WitchWaterEntityRecipe> recipe) {
        super(FENEmiPlugin.WITCH_WATER_ENTITY_CATEGORY, recipe.id(), WIDTH, HEIGHT);
        target = recipe.value().getTarget();
        result = recipe.value().getResult();
        nbt = recipe.value().getNbt();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        var target = this.target.create(MinecraftClient.getInstance().world);
        var result = this.result.create(MinecraftClient.getInstance().world);

        if (target == null || result == null) {
            throw new RuntimeException("Unable to create EMI display entities");
        }

        var targetTooltip = new ArrayList<>(List.of(target.getDisplayName()));
        var nbt = this.nbt.toString();
        if (!nbt.equals("{}")) {
            targetTooltip.add(Text.translatable("emi.category.fabricaeexnihilo.witch_water_entity.nbt_required").formatted(Formatting.GRAY));
            targetTooltip.add(Text.literal(nbt).formatted(Formatting.YELLOW));
        }

        widgets.addDrawable(0, 0, 3 * 18, 3 * 18, (draw, mouseX, mouseY, delta) -> EntityRenderer.drawEntity(draw, mouseX, mouseY, target, 0, 0, 54, 54, 32))
                .tooltipText(targetTooltip);
        widgets.addDrawable(5 * 18, 0, 3 * 18, 3 * 18, (draw, mouseX, mouseY, delta) -> EntityRenderer.drawEntity(draw, mouseX - 5 * 18, mouseY, result, 0, 0, 54, 54, 32))
                .tooltipText(List.of(result.getDisplayName()));

        widgets.addTexture(EmiTexture.EMPTY_ARROW, WIDTH / 2 - 12, 9);
        widgets.addSlot(EmiStack.of(WitchWaterFluid.STILL), WIDTH / 2 - 9, 30).drawBack(false);
    }
}
