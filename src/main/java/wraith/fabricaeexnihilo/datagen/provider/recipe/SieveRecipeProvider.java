package wraith.fabricaeexnihilo.datagen.provider.recipe;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.DefaultApiModule;
import wraith.fabricaeexnihilo.datagen.builder.recipe.SieveRecipeJsonBuilder;
import wraith.fabricaeexnihilo.modules.ModItems;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class SieveRecipeProvider extends FabricRecipeProvider {
    public SieveRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        offerMiscRecipes(exporter);
        offerStoneRecipes(exporter);
        offerPlantRecipes(exporter);
        offerSaplingRecipe(exporter);
        offerOreRecipes(exporter);
    }

    private void offerMiscRecipes(RecipeExporter exporter) {
        SieveRecipeJsonBuilder.of(Items.BLAZE_POWDER)
                .from(DefaultApiModule.INSTANCE.dust)
                .flintMesh(0.1).copperMesh(0.2).goldMesh(0.3, 0.05).emeraldMesh(0.4, 0.1)
                .offerTo(exporter, "blaze_powder");
        SieveRecipeJsonBuilder.of(Items.BONE_MEAL)
                .from(DefaultApiModule.INSTANCE.dust)
                .stringMesh(0.2).flintMesh(0.3).copperMesh(0.4).goldMesh(0.4, 0.1).emeraldMesh(0.5, 0.25)
                .offerTo(exporter, "bone_meal");
        SieveRecipeJsonBuilder.of(Items.GLOWSTONE_DUST)
                .from(DefaultApiModule.INSTANCE.dust)
                .flintMesh(0.04).copperMesh(0.06).goldMesh(0.05, 0.025, 0.025, 0.01).emeraldMesh(0.8, 0.03)
                .offerTo(exporter, "glowstone_dust");
        SieveRecipeJsonBuilder.of(Items.GUNPOWDER)
                .from(DefaultApiModule.INSTANCE.dust)
                .stringMesh(0.1).flintMesh(0.2).copperMesh(0.3).goldMesh(0.3, 0.1).emeraldMesh(0.4, 0.2, 0.05)
                .offerTo(exporter, "gunpowder");
        SieveRecipeJsonBuilder.of(Items.ENDER_PEARL)
                .from(DefaultApiModule.INSTANCE.crushedEndstone)
                .copperMesh(0.005, 0.005).goldMesh(0.01, 0.005).emeraldMesh(0.015, 0.025)
                .offerTo(exporter, "ender_pearl");
        SieveRecipeJsonBuilder.of(Items.GHAST_TEAR)
                .from(Blocks.SOUL_SAND)
                .diamondMesh(0.1).netheriteMesh(0.2)
                .offerTo(exporter, "ghast_tear");
    }

    private void offerStoneRecipes(RecipeExporter exporter) {
        SieveRecipeJsonBuilder.of(ModItems.PEBBLES.get(id("andesite_pebble")))
                .from(Items.DIRT)
                .stringMesh(0.75, 0.6, 0.33, 0.1).flintMesh(0.5, 0.5, 0.5, 0.5)
                .offerTo(exporter, "stone/andesite_pebble");
        SieveRecipeJsonBuilder.of(ModItems.PEBBLES.get(id("diorite_pebble")))
                .from(Items.DIRT)
                .stringMesh(0.75, 0.6, 0.33, 0.1).flintMesh(0.5, 0.5, 0.5, 0.5)
                .offerTo(exporter, "stone/diorite_pebble");
        SieveRecipeJsonBuilder.of(ModItems.PEBBLES.get(id("granite_pebble")))
                .from(Items.DIRT)
                .stringMesh(0.75, 0.6, 0.33, 0.1).flintMesh(0.5, 0.5, 0.5, 0.5)
                .offerTo(exporter, "stone/granite_pebble");
        SieveRecipeJsonBuilder.of(ModItems.PEBBLES.get(id("stone_pebble")))
                .from(Items.DIRT)
                .stringMesh(0.75, 0.6, 0.33, 0.2, 0.1, 0.1).flintMesh(0.8, 0.66, 0.5, 0.5, 0.33, 0.2, 0.1)
                .offerTo(exporter, "stone/stone_pebble");

        SieveRecipeJsonBuilder.of(Items.PRISMARINE_CRYSTALS)
                .from(DefaultApiModule.INSTANCE.crushedPrismarine)
                .goldMesh(0.01).emeraldMesh(0.025)
                .fromWaterlogged(Ingredient.ofItems(DefaultApiModule.INSTANCE.crushedPrismarine), "crushed_prismarine_in_water")
                .copperMesh(0.5, 0.01).goldMesh(0.66, 0.05).emeraldMesh(0.75, 0.1)
                .offerTo(exporter, "stone/prismarine_crystals");
        SieveRecipeJsonBuilder.of(Items.PRISMARINE_SHARD)
                .fromWaterlogged(Items.GRAVEL)
                .flintMesh(0.01).copperMesh(0.02).goldMesh(0.3).emeraldMesh(0.4)
                .offerTo(exporter, "stone/prismarine_shard");

        SieveRecipeJsonBuilder.of(ModItems.PEBBLES.get(id("basalt_pebble")))
                .from(DefaultApiModule.INSTANCE.crushedNetherrack)
                .stringMesh(0.5, 0.25, 0.1).flintMesh(0.33, 0.33, 0.33, 0.33)
                .offerTo(exporter, "stone/basalt_pebble");
        SieveRecipeJsonBuilder.of(ModItems.PEBBLES.get(id("blackstone_pebble")))
                .from(Items.SOUL_SAND)
                .stringMesh(0.5, 0.25, 0.1).flintMesh(0.33, 0.33, 0.33, 0.33)
                .offerTo(exporter, "stone/blackstone_pebble");
        SieveRecipeJsonBuilder.of(ModItems.PEBBLES.get(id("calcite_pebble")))
                .from(Items.GRAVEL)
                .stringMesh(0.5, 0.5, 0.25).flintMesh(0.66, 0.66, 0.33, 0.33)
                .offerTo(exporter, "stone/calcite_pebble");
        SieveRecipeJsonBuilder.of(ModItems.PEBBLES.get(id("deepslate_pebble")))
                .from(Items.GRAVEL)
                .stringMesh(0.5, 0.5, 0.25).flintMesh(0.66, 0.66, 0.33, 0.33)
                .offerTo(exporter, "stone/deepslate_pebble");
        SieveRecipeJsonBuilder.of(ModItems.PEBBLES.get(id("tuff_pebble")))
                .from(Items.GRAVEL)
                .stringMesh(0.5, 0.5, 0.25).flintMesh(0.66, 0.66, 0.33, 0.33)
                .offerTo(exporter, "stone/tuff_pebble");
        SieveRecipeJsonBuilder.of(ModItems.PEBBLES.get(id("dripstone_pebble")))
                .from(Items.GRAVEL)
                .stringMesh(0.5, 0.25).flintMesh(0.66, 0.33, 0.11)
                .offerTo(exporter, "stone/dripstone_pebble");
    }

    private void offerPlantRecipes(RecipeExporter exporter) {
        var plantRatesEmeraldPlus = Map.of(DefaultApiModule.INSTANCE.emeraldMesh, new double[]{0.3, 0.3});
        var plantRatesGoldPlus = ImmutableMap.<Item, double[]>builder().putAll(plantRatesEmeraldPlus).put(DefaultApiModule.INSTANCE.goldMesh, new double[]{0.2}).build();
        var plantRatesCopperPlus = ImmutableMap.<Item, double[]>builder().putAll(plantRatesGoldPlus).put(DefaultApiModule.INSTANCE.copperMesh, new double[]{0.12}).build();
        var plantRatesFlintPlus = ImmutableMap.<Item, double[]>builder().putAll(plantRatesCopperPlus).put(DefaultApiModule.INSTANCE.flintMesh, new double[]{0.08}).build();
        var plantRatesStringPlus = ImmutableMap.<Item, double[]>builder().putAll(plantRatesFlintPlus).put(DefaultApiModule.INSTANCE.stringMesh, new double[]{0.05}).build();

        // Misc Items
        SieveRecipeJsonBuilder.of(Items.APPLE)
                .from(Ingredient.fromTag(ItemTags.LEAVES), "leaves")
                .meshes(plantRatesStringPlus)
                .offerTo(exporter, "plant/apple");
        SieveRecipeJsonBuilder.of(Items.BAMBOO)
                .from(Items.JUNGLE_LEAVES)
                .meshes(plantRatesCopperPlus)
                .offerTo(exporter, "plant/bamboo");
        SieveRecipeJsonBuilder.of(Items.BROWN_MUSHROOM)
                .from(Items.DIRT)
                .copperMesh(0.05).goldMesh(0.1).emeraldMesh(0.2)
                .offerTo(exporter, "plant/brown_mushroom");
        SieveRecipeJsonBuilder.of(Items.LILY_PAD)
                .fromWaterlogged(DefaultApiModule.INSTANCE.silt)
                .meshes(plantRatesFlintPlus)
                .offerTo(exporter, "plant/lily_pad");
        SieveRecipeJsonBuilder.of(Items.RED_MUSHROOM)
                .from(Items.DIRT)
                .copperMesh(0.05).goldMesh(0.1).emeraldMesh(0.2)
                .offerTo(exporter, "plant/red_mushroom");
        SieveRecipeJsonBuilder.of(Items.PINK_PETALS)
                .from(Items.CHERRY_LEAVES)
                .meshes(plantRatesStringPlus)
                .offerTo(exporter, "plant/pink_petals");
        SieveRecipeJsonBuilder.of(ModItems.RAW_SILKWORM)
                .from(Ingredient.fromTag(ItemTags.LEAVES), "leaves")
                .meshes(plantRatesStringPlus)
                .offerTo(exporter, "plant/silkworm");

        // Grass like seeds
        SieveRecipeJsonBuilder.of(DefaultApiModule.INSTANCE.crimsonSeeds)
                .from(Items.DIRT)
                .meshes(plantRatesGoldPlus)
                .offerTo(requireItem(exporter, DefaultApiModule.INSTANCE.crimsonSeeds), "plant/crimson_seeds");
        SieveRecipeJsonBuilder.of(DefaultApiModule.INSTANCE.warpedSeeds)
                .from(Items.DIRT)
                .meshes(plantRatesGoldPlus)
                .offerTo(requireItem(exporter, DefaultApiModule.INSTANCE.warpedSeeds), "plant/warped_seeds");
        SieveRecipeJsonBuilder.of(DefaultApiModule.INSTANCE.grassSeeds)
                .from(Items.DIRT)
                .meshes(plantRatesStringPlus)
                .offerTo(requireItem(exporter, DefaultApiModule.INSTANCE.grassSeeds), "plant/grass_seeds");
        SieveRecipeJsonBuilder.of(DefaultApiModule.INSTANCE.myceliumSeeds)
                .from(Items.DIRT)
                .flintMesh(0.04).copperMesh(0.07).goldMesh(0.11).emeraldMesh(0.14)
                .offerTo(requireItem(exporter, DefaultApiModule.INSTANCE.myceliumSeeds), "plant/mycelium_seeds");

        // Misc FEN seeds
        SieveRecipeJsonBuilder.of(DefaultApiModule.INSTANCE.cactusSeeds)
                .from(Items.DIRT)
                .meshes(plantRatesFlintPlus)
                .offerTo(requireItem(exporter, DefaultApiModule.INSTANCE.cactusSeeds), "plant/cactus_seeds");
        SieveRecipeJsonBuilder.of(DefaultApiModule.INSTANCE.chorusSeeds)
                .from(DefaultApiModule.INSTANCE.crushedEndstone)
                .copperMesh(0.05).goldMesh(0.1).emeraldMesh(0.2)
                .offerTo(requireItem(exporter, DefaultApiModule.INSTANCE.chorusSeeds), "plant/chorus_seeds");
        SieveRecipeJsonBuilder.of(DefaultApiModule.INSTANCE.kelpSeeds)
                .fromWaterlogged(Items.SAND)
                .meshes(plantRatesCopperPlus)
                .offerTo(requireItem(exporter, DefaultApiModule.INSTANCE.kelpSeeds), "plant/kelp_seeds");
        SieveRecipeJsonBuilder.of(DefaultApiModule.INSTANCE.seaPickleSeeds)
                .fromWaterlogged(Items.SAND)
                .meshes(plantRatesCopperPlus)
                .offerTo(requireItem(exporter, DefaultApiModule.INSTANCE.seaPickleSeeds), "plant/sea_pickle_seeds");
        SieveRecipeJsonBuilder.of(DefaultApiModule.INSTANCE.sugarcaneSeeds)
                .fromWaterlogged(Items.SAND)
                .meshes(plantRatesCopperPlus)
                .offerTo(requireItem(exporter, DefaultApiModule.INSTANCE.sugarcaneSeeds), "plant/sugarcane_seeds");

        // Vanilla seeds and similar
        SieveRecipeJsonBuilder.of(Items.GLOW_BERRIES)
                .from(Items.COARSE_DIRT)
                .meshes(plantRatesStringPlus)
                .offerTo(exporter, "plant/glow_berries");
        SieveRecipeJsonBuilder.of(Items.COCOA_BEANS)
                .from(Items.SAND)
                .stringMesh(0.05, 0.025).flintMesh(0.08, 0.4).copperMesh(0.12, 0.08, 0.3).goldMesh(0.2, 0.1, 0.1, 0.05).emeraldMesh(0.3, 0.3, 0.2, 0.1, 0.05)
                .offerTo(exporter, "plant/cocoa_beans");
        SieveRecipeJsonBuilder.of(Items.NETHER_WART)
                .from(Items.SOUL_SAND)
                .stringMesh(0.1).flintMesh(0.15).copperMesh(0.2).goldMesh(0.3, 0.05).emeraldMesh(0.4, 0.15)
                .offerTo(exporter, "plant/nether_wart");
        SieveRecipeJsonBuilder.of(Items.POTATO)
                .from(Items.DIRT)
                .meshes(plantRatesFlintPlus)
                .offerTo(exporter, "plant/potato");
        SieveRecipeJsonBuilder.of(Items.CARROT)
                .from(Items.DIRT)
                .meshes(plantRatesFlintPlus)
                .offerTo(exporter, "plant/carrot");
        SieveRecipeJsonBuilder.of(Items.SWEET_BERRIES)
                .from(Items.DIRT)
                .stringMesh(0.02).flintMesh(0.06).copperMesh(0.12).goldMesh(0.2).emeraldMesh(0.3, 0.3)
                .offerTo(exporter, "plant/sweet_berries");
        SieveRecipeJsonBuilder.of(Items.BEETROOT_SEEDS)
                .from(Items.DIRT)
                .stringMesh(0.35).flintMesh(0.4).copperMesh(0.5).goldMesh(0.5, 0.35).emeraldMesh(0.5, 0.5)
                .offerTo(exporter, "plant/beetroot_seeds");
        SieveRecipeJsonBuilder.of(Items.WHEAT_SEEDS)
                .from(Items.DIRT)
                .flintMesh(0.3).copperMesh(0.5, 0.2).goldMesh(0.6, 0.6).emeraldMesh(0.75, 0.6, 0.5)
                .offerTo(exporter, "plant/wheat_seeds");
        SieveRecipeJsonBuilder.of(Items.MELON_SEEDS)
                .from(Items.DIRT)
                .flintMesh(0.2).copperMesh(0.4, 0.1).goldMesh(0.5, 0.5).emeraldMesh(0.7, 0.5, 0.4)
                .offerTo(exporter, "plant/melon_seeds");
        SieveRecipeJsonBuilder.of(Items.PUMPKIN_SEEDS)
                .from(Items.DIRT)
                .flintMesh(0.2).copperMesh(0.4, 0.1).goldMesh(0.5, 0.5).emeraldMesh(0.7, 0.5, 0.4)
                .offerTo(exporter, "plant/pumpkin_seeds");

        // Two tall flower seeds
        SieveRecipeJsonBuilder.of(DefaultApiModule.INSTANCE.lilacSeeds)
                .from(Items.DIRT)
                .meshes(plantRatesFlintPlus)
                .offerTo(requireItem(exporter, DefaultApiModule.INSTANCE.lilacSeeds), "plant/lilac_seeds");
        SieveRecipeJsonBuilder.of(DefaultApiModule.INSTANCE.peonySeeds)
                .from(Items.DIRT)
                .meshes(plantRatesFlintPlus)
                .offerTo(requireItem(exporter, DefaultApiModule.INSTANCE.peonySeeds), "plant/peony_seeds");
        SieveRecipeJsonBuilder.of(DefaultApiModule.INSTANCE.roseBushSeeds)
                .from(Items.DIRT)
                .meshes(plantRatesFlintPlus)
                .offerTo(requireItem(exporter, DefaultApiModule.INSTANCE.roseBushSeeds), "plant/rose_bush_seeds");
        SieveRecipeJsonBuilder.of(DefaultApiModule.INSTANCE.sunflowerSeeds)
                .from(Items.DIRT)
                .meshes(plantRatesFlintPlus)
                .offerTo(requireItem(exporter, DefaultApiModule.INSTANCE.sunflowerSeeds), "plant/sunflower_seeds");

    }

    private void offerSaplingRecipe(RecipeExporter exporter) {
        SieveRecipeJsonBuilder.of(Items.ACACIA_SAPLING)
                .from(Items.ACACIA_LEAVES)
                .flintMesh(0.1, 0.08).copperMesh(0.2, 0.1).goldMesh(0.05, 0.05, 0.05, 0.05).emeraldMesh(0.3, 0.3, 0.1)
                .from(Items.RED_SAND)
                .flintMesh(0.08).copperMesh(0.12).goldMesh(0.2).emeraldMesh(0.3, 0.3)
                .offerTo(exporter, "plant/sapling/acacia");
        SieveRecipeJsonBuilder.of(Items.BIRCH_SAPLING)
                .from(Items.BIRCH_LEAVES)
                .flintMesh(0.1, 0.08).copperMesh(0.2, 0.1).goldMesh(0.05, 0.05, 0.05, 0.05).emeraldMesh(0.3, 0.3, 0.1)
                .from(Items.DIRT)
                .flintMesh(0.08).copperMesh(0.12).goldMesh(0.2).emeraldMesh(0.3, 0.3)
                .offerTo(exporter, "plant/sapling/birch");
        SieveRecipeJsonBuilder.of(Items.DARK_OAK_SAPLING)
                .from(Items.DARK_OAK_LEAVES)
                .flintMesh(0.1, 0.08).copperMesh(0.2, 0.1).goldMesh(0.05, 0.05, 0.05, 0.05).emeraldMesh(0.3, 0.3, 0.1)
                .from(Items.DIRT)
                .flintMesh(0.08).copperMesh(0.12).goldMesh(0.2).emeraldMesh(0.3, 0.3)
                .offerTo(exporter, "plant/sapling/dark_oak");
        SieveRecipeJsonBuilder.of(Items.OAK_SAPLING)
                .from(Items.OAK_LEAVES)
                .flintMesh(0.1, 0.08).copperMesh(0.2, 0.1).goldMesh(0.05, 0.05, 0.05, 0.05).emeraldMesh(0.3, 0.3, 0.1)
                .from(Items.DIRT)
                .flintMesh(0.08).copperMesh(0.12).goldMesh(0.2).emeraldMesh(0.3, 0.3)
                .offerTo(exporter, "plant/sapling/oak");
        SieveRecipeJsonBuilder.of(Items.JUNGLE_SAPLING)
                .from(Items.JUNGLE_LEAVES)
                .flintMesh(0.1, 0.08).copperMesh(0.2, 0.1).goldMesh(0.05, 0.05, 0.05, 0.05).emeraldMesh(0.3, 0.3, 0.1)
                .from(Items.DIRT)
                .flintMesh(0.08).copperMesh(0.12).goldMesh(0.2).emeraldMesh(0.3, 0.3)
                .offerTo(exporter, "plant/sapling/jungle");
        SieveRecipeJsonBuilder.of(Items.SPRUCE_SAPLING)
                .from(Items.SPRUCE_LEAVES)
                .flintMesh(0.1, 0.08).copperMesh(0.2, 0.1).goldMesh(0.05, 0.05, 0.05, 0.05).emeraldMesh(0.3, 0.3, 0.1)
                .from(Items.DIRT)
                .flintMesh(0.08).copperMesh(0.12).goldMesh(0.2).emeraldMesh(0.3, 0.3)
                .offerTo(exporter, "plant/sapling/spruce");
        SieveRecipeJsonBuilder.of(Items.MANGROVE_PROPAGULE)
                .from(Items.MANGROVE_LEAVES)
                .flintMesh(0.1, 0.08).copperMesh(0.2, 0.1).goldMesh(0.05, 0.05, 0.05, 0.05).emeraldMesh(0.3, 0.3, 0.1)
                .from(Items.MUD)
                .flintMesh(0.08).copperMesh(0.12).goldMesh(0.2).emeraldMesh(0.3, 0.3)
                .offerTo(exporter, "plant/sapling/mangrove");
        SieveRecipeJsonBuilder.of(Items.CHERRY_SAPLING)
                .from(Items.CHERRY_LEAVES)
                .flintMesh(0.1, 0.08).copperMesh(0.2, 0.1).goldMesh(0.05, 0.05, 0.05, 0.05).emeraldMesh(0.3, 0.3, 0.1)
                .from(Items.DIRT)
                .flintMesh(0.08).copperMesh(0.12).goldMesh(0.2).emeraldMesh(0.3, 0.3)
                .offerTo(exporter, "plant/sapling/cherry");
    }

    private void offerOreRecipes(RecipeExporter exporter) {
        SieveRecipeJsonBuilder.of(Items.AMETHYST_SHARD)
                .from(DefaultApiModule.INSTANCE.crushedCalcite)
                .flintMesh(0.1).ironMesh(0.25).diamondMesh(0.3, 0.1).netheriteMesh(0.4, 0.2)
                .offerTo(exporter, "ore/amethyst");
        SieveRecipeJsonBuilder.of(Items.COAL)
                .from(Items.GRAVEL)
                .flintMesh(0.05).ironMesh(0.1).diamondMesh(0.2).netheriteMesh(0.4)
                .from(Items.SAND)
                .ironMesh(0.05).diamondMesh(0.1).netheriteMesh(0.2)
                .offerTo(exporter, "ore/coal");
        SieveRecipeJsonBuilder.of(DefaultApiModule.INSTANCE.copperPiece)
                .from(Items.GRAVEL)
                .flintMesh(0.1).ironMesh(0.15).diamondMesh(0.25).netheriteMesh(0.32)
                .from(Items.SAND)
                .diamondMesh(0.5).netheriteMesh(0.75)
                .offerTo(exporter, "ore/copper");
        SieveRecipeJsonBuilder.of(Items.DIAMOND)
                .from(Items.GRAVEL)
                .ironMesh(0.008).diamondMesh(0.016).netheriteMesh(0.032)
                .offerTo(exporter, "ore/diamond");
        SieveRecipeJsonBuilder.of(Items.EMERALD)
                .from(Items.GRAVEL)
                .ironMesh(0.008).diamondMesh(0.016).netheriteMesh(0.032)
                .offerTo(exporter, "ore/emerald");
        SieveRecipeJsonBuilder.of(Items.FLINT)
                .from(Items.GRAVEL)
                .stringMesh(0.25).flintMesh(0.3)
                .offerTo(exporter, "ore/flint");
        SieveRecipeJsonBuilder.of(DefaultApiModule.INSTANCE.goldPiece)
                .from(DefaultApiModule.INSTANCE.crushedNetherrack)
                .ironMesh(0.1, 0.1).diamondMesh(0.2, 0.2).netheriteMesh(0.3, 0.3)
                .from(Items.GRAVEL)
                .ironMesh(0.05).diamondMesh(0.08).netheriteMesh(0.12)
                .offerTo(exporter, "ore/gold");
        SieveRecipeJsonBuilder.of(DefaultApiModule.INSTANCE.ironPiece)
                .from(DefaultApiModule.INSTANCE.crushedGranite)
                .flintMesh(0.1, 0.05).ironMesh(0.3, 0.15, 0.15).diamondMesh(0.5, 0.4, 0.3).netheriteMesh(1, 0.75)
                .from(Items.GRAVEL)
                .flintMesh(0.1, 0.05).ironMesh(0.25, 0.15, 0.1).diamondMesh(0.4, 0.4, 0.4).netheriteMesh(0.75, 0.66, 0.5)
                .from(Items.RED_SAND)
                .flintMesh(0.05).ironMesh(0.1).diamondMesh(0.5, 0.1).netheriteMesh(0.75, 0.4)
                .from(Items.SAND)
                .diamondMesh(0.5).netheriteMesh(0.75)
                .offerTo(exporter, "ore/iron");
        SieveRecipeJsonBuilder.of(Items.LAPIS_LAZULI)
                .from(Items.GRAVEL)
                .flintMesh(0.05)
                .from(DefaultApiModule.INSTANCE.silt)
                .flintMesh(0.05).ironMesh(0.075).diamondMesh(0.5, 0.5).netheriteMesh(0.5, 0.5, 0.5)
                .offerTo(exporter, "ore/lapis_lazuli");
        SieveRecipeJsonBuilder.of(Items.NETHERITE_SCRAP)
                .from(DefaultApiModule.INSTANCE.crushedNetherrack)
                .ironMesh(0.008).diamondMesh(0.0012).netheriteMesh(0.016)
                .offerTo(exporter, "ore/netherite");
        SieveRecipeJsonBuilder.of(Items.QUARTZ)
                .from(Items.SOUL_SAND)
                .flintMesh(1, 0.33).ironMesh(0.4, 0.4, 0.2, 0.1, 0.1).diamondMesh(1, 1, 0.8, 0.3).netheriteMesh(1, 1, 1, 0.5, 0.1)
                .offerTo(exporter, "ore/quartz");
        SieveRecipeJsonBuilder.of(Items.REDSTONE)
                .from(DefaultApiModule.INSTANCE.dust)
                .flintMesh(0.15).ironMesh(0.15, 0.125).diamondMesh(0.25, 0.15, 0.1).netheriteMesh(0.4, 0.2, 0.1)
                .offerTo(exporter, "ore/redstone");
    }

    private RecipeExporter requireItem(RecipeExporter exporter, Item required) {
        return withConditions(exporter, ResourceConditions.registryContains(required.getRegistryEntry().registryKey()));
    }

    @Override
    public String getName() {
        return "Sieve Recipes";
    }

    @Override
    protected Identifier getRecipeIdentifier(Identifier identifier) {
        return super.getRecipeIdentifier(identifier).withPrefixedPath("sieve/");
    }
}
