package wraith.fabricaeexnihilo.datagen.builder.recipe;

import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.compatibility.DefaultApiModule;
import wraith.fabricaeexnihilo.recipe.SieveRecipe;

import java.util.*;

public class SieveRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final ItemStack result;
    private final List<Segment> segments = new ArrayList<>();
    private Segment currentSegment;

    private SieveRecipeJsonBuilder(ItemStack result) {
        this.result = result;
    }

    public static SieveRecipeJsonBuilder of(ItemStack result) {
        return new SieveRecipeJsonBuilder(result);
    }

    public static SieveRecipeJsonBuilder of(ItemConvertible result) {
        return new SieveRecipeJsonBuilder(new ItemStack(result));
    }

    public SieveRecipeJsonBuilder from(Ingredient input, String name) {
        if (currentSegment != null) segments.add(currentSegment);
        currentSegment = new Segment(input, false, name, new HashMap<>());
        return this;
    }

    public SieveRecipeJsonBuilder from(ItemConvertible input) {
        return from(Ingredient.ofItems(input), Registries.ITEM.getId(input.asItem()).getPath());
    }

    public SieveRecipeJsonBuilder fromWaterlogged(Ingredient input, String name) {
        if (currentSegment != null) segments.add(currentSegment);
        currentSegment = new Segment(input, true, name, new HashMap<>());
        return this;
    }

    public SieveRecipeJsonBuilder fromWaterlogged(ItemConvertible input) {
        return fromWaterlogged(Ingredient.ofItems(input), Registries.ITEM.getId(input.asItem()).getPath());
    }

    public SieveRecipeJsonBuilder mesh(Item mesh, double... chances) {
        if (currentSegment == null) throw new IllegalStateException("No active segment");
        //noinspection deprecation
        currentSegment.rolls.put(mesh.getRegistryEntry().registryKey().getValue(), Arrays.stream(chances).boxed().toList());
        return this;
    }

    public SieveRecipeJsonBuilder meshes(Map<Item, double[]> meshes) {
        meshes.forEach(this::mesh);
        return this;
    }

    public SieveRecipeJsonBuilder stringMesh(double... chances) {
        return mesh(DefaultApiModule.INSTANCE.stringMesh, chances);
    }

    public SieveRecipeJsonBuilder flintMesh(double... chances) {
        return mesh(DefaultApiModule.INSTANCE.flintMesh, chances);
    }

    public SieveRecipeJsonBuilder ironMesh(double... chances) {
        return mesh(DefaultApiModule.INSTANCE.ironMesh, chances);
    }

    public SieveRecipeJsonBuilder diamondMesh(double... chances) {
        return mesh(DefaultApiModule.INSTANCE.diamondMesh, chances);
    }

    public SieveRecipeJsonBuilder netheriteMesh(double... chances) {
        return mesh(DefaultApiModule.INSTANCE.netheriteMesh, chances);
    }

    public SieveRecipeJsonBuilder copperMesh(double... chances) {
        return mesh(DefaultApiModule.INSTANCE.copperMesh, chances);
    }

    public SieveRecipeJsonBuilder goldMesh(double... chances) {
        return mesh(DefaultApiModule.INSTANCE.goldMesh, chances);
    }

    public SieveRecipeJsonBuilder emeraldMesh(double... chances) {
        return mesh(DefaultApiModule.INSTANCE.emeraldMesh, chances);
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
        if (currentSegment == null) throw new IllegalStateException("No segments");
        segments.add(currentSegment);
        if (segments.size() == 1) {
            exporter.accept(recipeId,
                    new SieveRecipe(result, currentSegment.input, currentSegment.waterlogged, currentSegment.rolls), null);
            return;
        }
        for (var segment : segments) {
            exporter.accept(recipeId.withSuffixedPath("_from_" + segment.name),
                    new SieveRecipe(result, segment.input, segment.waterlogged, segment.rolls), null);
        }
    }

    private record Segment(Ingredient input, boolean waterlogged, String name, Map<Identifier, List<Double>> rolls) {
    }
}
