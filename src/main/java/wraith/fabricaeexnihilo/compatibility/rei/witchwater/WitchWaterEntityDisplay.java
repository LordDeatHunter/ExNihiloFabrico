package wraith.fabricaeexnihilo.compatibility.rei.witchwater;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterEntityRecipe;

import java.util.List;
import java.util.Optional;

public class WitchWaterEntityDisplay implements Display {
    public final NbtPathArgumentType.NbtPath nbt;
    public final EntityType<?> result;
    public final EntityType<?> target;
    private final Identifier id;

    public WitchWaterEntityDisplay(RecipeEntry<WitchWaterEntityRecipe> recipeEntry) {
        var recipe = recipeEntry.value();
        this.target = recipe.getTarget();
        this.result = recipe.getResult();
        this.nbt = recipe.getNbt();
        this.id = recipeEntry.id();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PluginEntry.WITCH_WATER_ENTITY;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of();
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of();
    }

    @Override
    public Optional<Identifier> getDisplayLocation() {
        return Optional.of(id);
    }
}
