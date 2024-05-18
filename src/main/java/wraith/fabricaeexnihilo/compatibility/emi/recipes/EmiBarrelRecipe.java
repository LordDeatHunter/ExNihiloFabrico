package wraith.fabricaeexnihilo.compatibility.emi.recipes;

import dev.emi.emi.api.FabricEmiStack;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.compatibility.emi.EmiIngredientUtil;
import wraith.fabricaeexnihilo.compatibility.emi.FENEmiPlugin;
import wraith.fabricaeexnihilo.compatibility.emi.FENEmiTextures;
import wraith.fabricaeexnihilo.modules.ModTags;
import wraith.fabricaeexnihilo.recipe.barrel.BarrelRecipe;
import wraith.fabricaeexnihilo.recipe.barrel.BarrelRecipeAction;
import wraith.fabricaeexnihilo.recipe.barrel.BarrelRecipeCondition;
import wraith.fabricaeexnihilo.recipe.barrel.BarrelRecipeTrigger;

import java.util.ArrayList;
import java.util.List;

public class EmiBarrelRecipe implements EmiRecipe {
    private static final int WIDTH = 8 * 18;
    private static final int HEIGHT = 3 * 18;
    private final Identifier id;
    private final int duration;
    @Nullable
    private final EmiIngredient triggerItem;
    @Nullable
    private EmiIngredient above;
    @Nullable
    private EmiIngredient below;
    @Nullable
    private EmiIngredient inputFluid;
    @Nullable
    private EmiIngredient nearby;
    @Nullable
    private EmiStack conversionOutput;
    @Nullable
    private EmiStack compostResult;
    private float compostAmount;
    private final List<EmiStack> outputs = new ArrayList<>();

    public EmiBarrelRecipe(RecipeEntry<BarrelRecipe> recipeEntry) {
        id = recipeEntry.id();
        var recipe = recipeEntry.value();
        duration = recipe.getDuration() * FabricaeExNihilo.CONFIG.get().barrels().tickRate() / 20;
        triggerItem = getTriggerItem(recipe);

        recipe.getConditions().forEach(this::processCondition);
        recipe.getActions().forEach(this::processAction);
    }

    private void processCondition(BarrelRecipeCondition condition) {
        if (condition instanceof BarrelRecipeCondition.BlockAbove blockAbove) {
            above = EmiIngredientUtil.ingredientOf(blockAbove.block());
        } else if (condition instanceof BarrelRecipeCondition.FluidAbove fluidAbove) {
            above = EmiIngredientUtil.ingredientOf(fluidAbove.fluid());
        } else if (condition instanceof BarrelRecipeCondition.BlockBelow blockBelow) {
            below = EmiIngredientUtil.ingredientOf(blockBelow.block());
        } else if (condition instanceof BarrelRecipeCondition.FluidIn fluidIn) {
            inputFluid = EmiIngredientUtil.ingredientOf(fluidIn.fluid());
        } else {
            FabricaeExNihilo.LOGGER.warn("Unsupported barrel recipe condition in REI code: " + condition);
        }
    }

    private void processAction(BarrelRecipeAction action) {
        if (action instanceof BarrelRecipeAction.SpawnEntity spawnEntity) {
            var egg = SpawnEggItem.forEntity(spawnEntity.entities().getType());
            if (egg == null) return;
            var stack = new ItemStack(egg, spawnEntity.entities().getSize());
            outputs.add(EmiStack.of(stack));
        } else if (action instanceof BarrelRecipeAction.StoreItem storeItem) {
            outputs.add(EmiStack.of(storeItem.stack()));
        } else if (action instanceof BarrelRecipeAction.StoreFluid storeFluid) {
            if (storeFluid.fluid().isBlank()) return;
            outputs.add(FabricEmiStack.of(storeFluid.fluid(), storeFluid.amount()));
        } else if (action instanceof BarrelRecipeAction.ConsumeFluid consumeFluid) {
            if (consumeFluid.fluid().isEmpty()) return;
            inputFluid = EmiIngredientUtil.ingredientOf(consumeFluid.fluid()).setAmount(consumeFluid.amount());
        } else if (action instanceof BarrelRecipeAction.ConvertBlock convertBlock) {
            nearby = EmiIngredientUtil.ingredientOf(convertBlock.filter());
            conversionOutput = EmiStack.of(convertBlock.result().getBlock());
        } else if (action instanceof BarrelRecipeAction.DropItem dropItem) {
            outputs.add(EmiStack.of(dropItem.stack()));
        } else if (action instanceof BarrelRecipeAction.FillCompost fillCompost) {
            compostResult = EmiStack.of(fillCompost.result()).setChance(compostAmount);
            compostAmount = fillCompost.increment();
        } else {
            FabricaeExNihilo.LOGGER.warn("Unsupported barrel recipe action in REI code: " + action);
        }
    }

    @Nullable
    private EmiIngredient getTriggerItem(BarrelRecipe recipe) {
        var trigger = recipe.getTrigger();
        if (trigger instanceof BarrelRecipeTrigger.ItemInserted itemInsertedTrigger) {
            return EmiIngredient.of(itemInsertedTrigger.predicate());
        } else {
            return null;
        }
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return FENEmiPlugin.BARREL_CATEGORY;
    }

    @Override
    public @Nullable Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        var inputs = new ArrayList<EmiIngredient>();
        if (triggerItem != null) inputs.add(triggerItem);
        if (above != null) inputs.add(above);
        if (below != null) inputs.add(below);
        if (nearby != null) inputs.add(nearby);
        if (inputFluid != null) inputs.add(inputFluid);
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        var outputs = new ArrayList<>(this.outputs);
        if (conversionOutput != null) outputs.add(conversionOutput);
        if (compostResult != null) outputs.add(compostResult);
        return outputs;
    }

    @Override
    public int getDisplayWidth() {
        return WIDTH;
    }

    @Override
    public int getDisplayHeight() {
        return HEIGHT;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        var rowCount = (outputs.size() - 1) / 2;
        for (int i = 0; i < outputs.size(); i++) {
            var col = i % 2;
            var row = i / 2;
            widgets.addSlot(outputs.get(i), (col + 6) * 18, (int) ((row + 1 - rowCount / 2.0) * 18)).recipeContext(this);
        }
        if (outputs.size() > 0) {
            widgets.addTexture(EmiTexture.EMPTY_ARROW, 4 * 18 - 6, 18 - 1);
        }

        if (above != null) widgets.addSlot(above, 2 * 18, 0);
        if (below != null) widgets.addSlot(below, 2 * 18, 2 * 18);
        if (triggerItem != null) widgets.addSlot(triggerItem, 0, inputFluid == null ? 18 : 9);
        if (inputFluid != null) widgets.addSlot(inputFluid, 0, triggerItem == null ? 18 : 18 + 9);
        widgets.addTexture(FENEmiTextures.ARROW_RIGHT, 19, 18);
        widgets.addSlot(EmiIngredient.of(ModTags.BARRELS), 2 * 18, 18).drawBack(false);

        if (nearby != null && conversionOutput != null) {
            widgets.addText(Text.translatable("emi.category.fabricaeexnihilo.barrel.nearby"), 3 * 18 + 4, 18 + 6, 0xffffffff, true);
            widgets.addSlot(nearby, 3 * 18 + 4, 2 * 18);
            widgets.addTexture(FENEmiTextures.ARROW_RIGHT, 18 * 4 - 1, 18 * 2);
            widgets.addSlot(conversionOutput, 4 * 18 + 13, 2 * 18).recipeContext(this);
        }

        if (compostResult != null) {
            widgets.addSlot(compostResult, 3 * 18 + 4, 0).recipeContext(this);
            widgets.addText(Text.translatable("emi.category.fabricaeexnihilo.barrel.compost", compostAmount * 100), 4 * 18 + 4, 4, 0xffffffff, true);
        }

        if (duration != 0) {
            widgets.addText(Text.translatable("emi.category.fabricaeexnihilo.barrel.duration", duration), 0, 0, 0xffffffff, true);
        }
    }
}
