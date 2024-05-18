package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.barrel.MilkingRecipe;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public final class MilkingDisplay implements Display {

    public final long amount;
    public final EntryIngredient entity;
    public final EntryIngredient result;
    private final Identifier id;

    public MilkingDisplay(RecipeEntry<MilkingRecipe> recipeEntry) {
        var recipe = recipeEntry.value();
        this.entity = EntryIngredients.ofItems(Stream.<ItemConvertible>ofNullable(SpawnEggItem.forEntity(recipe.getEntity())).toList());
        this.amount = recipe.getAmount();
        this.result = EntryIngredients.of(recipe.getFluid().getFluid(), amount);
        this.id = recipeEntry.id();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.MILKING;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(entity);
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