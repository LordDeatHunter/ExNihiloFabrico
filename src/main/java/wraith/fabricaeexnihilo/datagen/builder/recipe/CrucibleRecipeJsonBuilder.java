package wraith.fabricaeexnihilo.datagen.builder.recipe;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleRecipe;

public class CrucibleRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final Ingredient input;
    private final long amount;
    private final FluidVariant fluid;
    private boolean requiresFireproofCrucible = false;

    public CrucibleRecipeJsonBuilder(Ingredient input, FluidVariant fluid, long amount) {
        this.input = input;
        this.amount = amount;
        this.fluid = fluid;
    }

    public static CrucibleRecipeJsonBuilder lava(Ingredient input, long amount) {
        return new CrucibleRecipeJsonBuilder(input, FluidVariant.of(Fluids.LAVA), amount)
                .requiresFireproofCrucible();
    }

    public static CrucibleRecipeJsonBuilder lava(ItemConvertible input, long amount) {
        return lava(Ingredient.ofItems(input), amount);
    }

    public static CrucibleRecipeJsonBuilder water(Ingredient input, long amount) {
        return new CrucibleRecipeJsonBuilder(input, FluidVariant.of(Fluids.WATER), amount);
    }

    public static CrucibleRecipeJsonBuilder water(ItemConvertible input, long amount) {
        return water(Ingredient.ofItems(input), amount);
    }

    public static CrucibleRecipeJsonBuilder water(TagKey<Item> input, long amount) {
        return water(Ingredient.fromTag(input), amount);
    }

    public CrucibleRecipeJsonBuilder requiresFireproofCrucible() {
        this.requiresFireproofCrucible = true;
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
        return Items.AIR;
    }

    @Override
    public void offerTo(RecipeExporter exporter, Identifier recipeId) {
        exporter.accept(recipeId, new CrucibleRecipe(input, amount, fluid, requiresFireproofCrucible), null);
    }
}
