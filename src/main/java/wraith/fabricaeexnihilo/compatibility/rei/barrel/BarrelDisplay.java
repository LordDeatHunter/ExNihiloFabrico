package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.fabric.FluidStackHooksFabric;
import me.shedaniel.rei.api.client.util.ClientEntryStacks;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.compatibility.rei.ReiIngredientUtil;
import wraith.fabricaeexnihilo.recipe.barrel.BarrelRecipe;
import wraith.fabricaeexnihilo.recipe.barrel.BarrelRecipeAction;
import wraith.fabricaeexnihilo.recipe.barrel.BarrelRecipeCondition;
import wraith.fabricaeexnihilo.recipe.barrel.BarrelRecipeTrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class BarrelDisplay implements Display {
    private final Identifier id;
    final int duration;
    @Nullable
    final EntryIngredient triggerItem;
    @Nullable
    EntryIngredient above;
    @Nullable
    EntryIngredient below;
    @Nullable
    EntryIngredient inputFluid;
    @Nullable
    EntryIngredient nearby;
    @Nullable
    EntryIngredient conversionOutput;
    @Nullable
    EntryIngredient compostResult;
    float compostAmount;
    final List<EntryIngredient> outputs = new ArrayList<>();

    public BarrelDisplay(RecipeEntry<BarrelRecipe> recipeEntry) {
        var recipe = recipeEntry.value();
        id = recipeEntry.id();
        duration = recipe.getDuration() * FabricaeExNihilo.CONFIG.get().barrels().tickRate() / 20;
        triggerItem = getTriggerItem(recipe);

        recipe.getConditions().forEach(this::processCondition);
        recipe.getActions().forEach(this::processAction);
    }

    private void processCondition(BarrelRecipeCondition condition) {
        if (condition instanceof BarrelRecipeCondition.BlockAbove blockAbove) {
            above = ReiIngredientUtil.of(blockAbove.block());
        } else if (condition instanceof BarrelRecipeCondition.FluidAbove fluidAbove) {
            above = ReiIngredientUtil.of(fluidAbove.fluid());
        } else if (condition instanceof BarrelRecipeCondition.BlockBelow blockBelow) {
            below = ReiIngredientUtil.of(blockBelow.block());
        } else if (condition instanceof BarrelRecipeCondition.FluidIn fluidIn) {
            inputFluid = ReiIngredientUtil.of(fluidIn.fluid());
        } else {
            FabricaeExNihilo.LOGGER.warn("Unsupported barrel recipe condition in REI code: {}", condition);
        }
    }

    private void processAction(BarrelRecipeAction action) {
        if (action instanceof BarrelRecipeAction.SpawnEntity spawnEntity) {
            var egg = SpawnEggItem.forEntity(spawnEntity.entities().getType());
            if (egg == null) return;
            var stack = new ItemStack(egg, spawnEntity.entities().getSize());
            outputs.add(EntryIngredients.of(stack));
        } else if (action instanceof BarrelRecipeAction.StoreItem storeItem) {
            outputs.add(EntryIngredients.of(storeItem.stack()));
        } else if (action instanceof BarrelRecipeAction.StoreFluid storeFluid) {
            if (storeFluid.fluid().isBlank()) return;
            outputs.add(EntryIngredients.of(FluidStackHooksFabric.fromFabric(storeFluid.fluid(), storeFluid.amount())));
        } else if (action instanceof BarrelRecipeAction.ConsumeFluid consumeFluid) {
            if (consumeFluid.fluid().isEmpty()) return;
            inputFluid = ReiIngredientUtil.of(consumeFluid.fluid()).map(entryStack -> ClientEntryStacks.setFluidRenderRatio(EntryStacks.of(entryStack.<FluidStack>cast().getValue().copyWithAmount(consumeFluid.amount())), (float) consumeFluid.amount() / FluidConstants.BUCKET));
        } else if (action instanceof BarrelRecipeAction.ConvertBlock convertBlock) {
            nearby = ReiIngredientUtil.of(convertBlock.filter());
            conversionOutput = EntryIngredients.of(convertBlock.result().getBlock());
        } else if (action instanceof BarrelRecipeAction.DropItem dropItem) {
            outputs.add(EntryIngredients.of(dropItem.stack()));
        } else if (action instanceof BarrelRecipeAction.FillCompost fillCompost) {
            compostResult = EntryIngredients.of(fillCompost.result());
            compostAmount = fillCompost.increment();
        } else {
            FabricaeExNihilo.LOGGER.warn("Unsupported barrel recipe action in REI code: " + action);
        }
    }

    @Nullable
    private EntryIngredient getTriggerItem(BarrelRecipe recipe) {
        var trigger = recipe.getTrigger();
        if (trigger instanceof BarrelRecipeTrigger.ItemInserted itemInsertedTrigger) {
            return EntryIngredients.ofIngredient(itemInsertedTrigger.predicate());
        } else {
            return null;
        }
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        var inputs = new ArrayList<EntryIngredient>();
        if (triggerItem != null) inputs.add(triggerItem);
        if (above != null) inputs.add(above);
        if (below != null) inputs.add(below);
        if (nearby != null) inputs.add(nearby);
        if (inputFluid != null) inputs.add(inputFluid);
        return inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        var outputs = new ArrayList<>(this.outputs);
        if (conversionOutput != null) outputs.add(conversionOutput);
        if (compostResult != null) outputs.add(compostResult);
        return outputs;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.BARREL;
    }

    @Override
    public Optional<Identifier> getDisplayLocation() {
        return Optional.of(id);
    }
}
