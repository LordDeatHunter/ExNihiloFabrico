package wraith.fabricaeexnihilo.datagen.provider.recipe;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.DefaultApiModule;
import wraith.fabricaeexnihilo.datagen.builder.recipe.CrucibleHeatRecipeJsonBuilder;
import wraith.fabricaeexnihilo.datagen.builder.recipe.CrucibleRecipeJsonBuilder;
import wraith.fabricaeexnihilo.modules.ModTags;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;

import java.util.function.Consumer;

public class CrucibleRecipeProvider extends FabricRecipeProvider {
    public CrucibleRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        new CrucibleHeatRecipeJsonBuilder(BlockIngredient.tag(ModTags.Common.TORCHES), 1).offerTo(exporter, "heat_from_torch");
        new CrucibleHeatRecipeJsonBuilder(Blocks.GLOWSTONE, 2).offerTo(exporter, "heat_from_glowstone");
        new CrucibleHeatRecipeJsonBuilder(Blocks.SHROOMLIGHT, 2).offerTo(exporter, "heat_from_shroomlight");
        new CrucibleHeatRecipeJsonBuilder(Blocks.MAGMA_BLOCK, 3).offerTo(exporter, "heat_from_magma_block");
        new CrucibleHeatRecipeJsonBuilder(Blocks.LAVA, 4).offerTo(exporter, "heat_from_lava");
        new CrucibleHeatRecipeJsonBuilder(Blocks.CAMPFIRE, 4).offerTo(exporter, "heat_from_campfire");
        new CrucibleHeatRecipeJsonBuilder(Blocks.SOUL_CAMPFIRE, 4).offerTo(exporter, "heat_from_soul_campfire");
        new CrucibleHeatRecipeJsonBuilder(Blocks.FIRE, 5).offerTo(exporter, "heat_from_fire");
        new CrucibleHeatRecipeJsonBuilder(Blocks.SOUL_FIRE, 5).offerTo(exporter, "heat_from_soul_fire");

        CrucibleRecipeJsonBuilder.lava(Items.OBSIDIAN, 81000).offerTo(exporter, "lava_from_obsidian");

        CrucibleRecipeJsonBuilder.lava(Items.NETHERRACK, 40500).offerTo(exporter, "lava_from_netherrack");
        CrucibleRecipeJsonBuilder.lava(Items.END_STONE, 20250).offerTo(exporter, "lava_from_end_stone");
        CrucibleRecipeJsonBuilder.lava(Items.ANDESITE, 20250).offerTo(exporter, "lava_from_andesite");
        CrucibleRecipeJsonBuilder.lava(Items.DIORITE, 20250).offerTo(exporter, "lava_from_diorite");
        CrucibleRecipeJsonBuilder.lava(Items.GRANITE, 20250).offerTo(exporter, "lava_from_granite");
        CrucibleRecipeJsonBuilder.lava(DefaultApiModule.INSTANCE.crushedNetherrack, 10125).offerTo(exporter, "lava_from_crushed_netherrack");
        CrucibleRecipeJsonBuilder.lava(DefaultApiModule.INSTANCE.crushedEndstone, 10125).offerTo(exporter, "lava_from_crushed_endstone");
        CrucibleRecipeJsonBuilder.lava(DefaultApiModule.INSTANCE.crushedAndesite, 10125).offerTo(exporter, "lava_from_crushed_andesite");
        CrucibleRecipeJsonBuilder.lava(DefaultApiModule.INSTANCE.crushedDiorite, 10125).offerTo(exporter, "lava_from_crushed_diorite");
        CrucibleRecipeJsonBuilder.lava(DefaultApiModule.INSTANCE.crushedGranite, 10125).offerTo(exporter, "lava_from_crushed_granite");

        CrucibleRecipeJsonBuilder.lava(Items.STONE, 20250).offerTo(exporter, "lava_from_stone");
        CrucibleRecipeJsonBuilder.lava(Items.COBBLESTONE, 20250).offerTo(exporter, "lava_from_cobblestone");
        CrucibleRecipeJsonBuilder.lava(Items.GRAVEL, 10125).offerTo(exporter, "lava_from_gravel");
        CrucibleRecipeJsonBuilder.lava(Items.SAND, 6750).offerTo(exporter, "lava_from_sand");
        CrucibleRecipeJsonBuilder.lava(DefaultApiModule.INSTANCE.dust, 3375).offerTo(exporter, "lava_from_dust");

        CrucibleRecipeJsonBuilder.water(ItemTags.SMALL_FLOWERS, 8100).offerTo(exporter, "water_from_flowers");
        CrucibleRecipeJsonBuilder.water(ItemTags.LEAVES, 20250).offerTo(exporter, "water_from_leaves");
        CrucibleRecipeJsonBuilder.water(ItemTags.SAPLINGS, 8100).offerTo(exporter, "water_from_saplings");
        CrucibleRecipeJsonBuilder.water(ModTags.Common.SEEDS, 8100).offerTo(exporter, "water_from_seeds");
        CrucibleRecipeJsonBuilder.water(ModTags.Common.VEGETABLES, 8100).offerTo(exporter, "water_from_vegetables");
    }

    @Override
    public String getName() {
        return "Crucible Recipes";
    }

    @Override
    protected Identifier getRecipeIdentifier(Identifier identifier) {
        return super.getRecipeIdentifier(identifier).withPrefixedPath("crucible/");
    }
}
