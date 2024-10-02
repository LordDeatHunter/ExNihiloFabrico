package wraith.fabricaeexnihilo.datagen.builder.recipe;

import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;
import wraith.fabricaeexnihilo.recipe.util.WeightedList;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterWorldRecipe;

import java.util.HashMap;
import java.util.Map;

public class WitchWaterWorldRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final FluidIngredient target;
    private final Map<Block, Integer> result = new HashMap<>();

    public WitchWaterWorldRecipeJsonBuilder(FluidIngredient target) {
        this.target = target;
    }

    public WitchWaterWorldRecipeJsonBuilder(Fluid target) {
        this.target = FluidIngredient.single(target);
    }

    public WitchWaterWorldRecipeJsonBuilder(TagKey<Fluid> target) {
        this.target = FluidIngredient.tag(target);
    }

    public WitchWaterWorldRecipeJsonBuilder result(Block block, int weight) {
        var prev = result.put(block, weight);
        if (prev != null) throw new IllegalStateException("Two entries for one block");
        return this;
    }

    @Override
    public CraftingRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CraftingRecipeJsonBuilder group(@Nullable String group) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item getOutputItem() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public void offerTo(RecipeExporter exporter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void offerTo(RecipeExporter exporter, String recipePath) {
        offerTo(exporter, Identifier.of(recipePath));
    }

    @Override
    public void offerTo(RecipeExporter exporter, Identifier recipeId) {
        exporter.accept(recipeId, new WitchWaterWorldRecipe(target, new WeightedList(result)), null);
    }
}
