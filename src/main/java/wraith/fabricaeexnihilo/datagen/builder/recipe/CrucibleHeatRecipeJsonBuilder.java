package wraith.fabricaeexnihilo.datagen.builder.recipe;

import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleHeatRecipe;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;

public class CrucibleHeatRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final BlockIngredient block;
    private final int heat;

    public CrucibleHeatRecipeJsonBuilder(BlockIngredient block, int heat) {
        this.block = block;
        this.heat = heat;
    }

    public CrucibleHeatRecipeJsonBuilder(Block block, int heat) {
        this(BlockIngredient.single(block), heat);
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
        offerTo(exporter, new Identifier(recipePath));
    }

    @Override
    public void offerTo(RecipeExporter exporter, Identifier recipeId) {
        exporter.accept(recipeId, new CrucibleHeatRecipe(block, heat), null);
    }
}
