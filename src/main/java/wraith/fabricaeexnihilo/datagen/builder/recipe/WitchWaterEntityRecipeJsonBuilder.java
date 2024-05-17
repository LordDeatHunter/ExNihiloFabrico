package wraith.fabricaeexnihilo.datagen.builder.recipe;

import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterEntityRecipe;

public class WitchWaterEntityRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final EntityType<?> target;
    private final String nbt;
    private final EntityType<?> result;

    public WitchWaterEntityRecipeJsonBuilder(EntityType<?> target, String nbt, EntityType<?> result) {
        this.target = target;
        this.nbt = nbt;
        this.result = result;
    }

    public static WitchWaterEntityRecipeJsonBuilder of(EntityType<?> from, EntityType<?> to) {
        return new WitchWaterEntityRecipeJsonBuilder(from, "", to);
    }

    public static WitchWaterEntityRecipeJsonBuilder villager(VillagerProfession from, EntityType<?> to) {
        return new WitchWaterEntityRecipeJsonBuilder(EntityType.VILLAGER, "VillagerData{profession:\"%s\"}".formatted(Registries.VILLAGER_PROFESSION.getId(from).toString()), to);
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
        exporter.accept(recipeId, new WitchWaterEntityRecipe(target, WitchWaterEntityRecipe.Serializer.parseNbtPath(nbt), result), null);
    }
}
