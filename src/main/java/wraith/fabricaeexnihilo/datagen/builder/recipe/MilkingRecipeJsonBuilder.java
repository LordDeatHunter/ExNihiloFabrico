package wraith.fabricaeexnihilo.datagen.builder.recipe;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.barrel.MilkingRecipe;

public class MilkingRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final EntityType<?> entity;
    private final FluidVariant fluid;
    private long amount = 810;
    private int cooldown = 20;

    public MilkingRecipeJsonBuilder(EntityType<?> entity, FluidVariant fluid) {
        this.entity = entity;
        this.fluid = fluid;
    }

    public MilkingRecipeJsonBuilder amount(long amount) {
        this.amount = amount;
        return this;
    }

    public MilkingRecipeJsonBuilder cooldown(int cooldown) {
        this.cooldown = cooldown;
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
        exporter.accept(recipeId, new MilkingRecipe(entity, fluid, amount, cooldown), null);
    }
}
