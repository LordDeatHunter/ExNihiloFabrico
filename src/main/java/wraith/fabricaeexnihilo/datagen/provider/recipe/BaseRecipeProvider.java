package wraith.fabricaeexnihilo.datagen.provider.recipe;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import wraith.fabricaeexnihilo.compatibility.*;
import wraith.fabricaeexnihilo.modules.ModTags;

import java.util.concurrent.CompletableFuture;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;
import static wraith.fabricaeexnihilo.datagen.DatagenItems.*;
import static wraith.fabricaeexnihilo.modules.ModBlocks.*;
import static wraith.fabricaeexnihilo.modules.ModItems.*;
import static wraith.fabricaeexnihilo.modules.ModTools.CROOKS;
import static wraith.fabricaeexnihilo.modules.ModTools.HAMMERS;

/**
 * Provider for all recipes using vanilla recipe types
 */
public class BaseRecipeProvider extends FabricRecipeProvider {
    public BaseRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        var mythicMetalsExporter = withConditions(exporter, ResourceConditions.allModsLoaded("mythicmetals"));
        var indrevExporter = withConditions(exporter, ResourceConditions.allModsLoaded("indrev"));
        var modernIndustrializationExporter = withConditions(exporter, ResourceConditions.allModsLoaded("modern_industrialization"));
        var techRebornExporter = withConditions(exporter, ResourceConditions.allModsLoaded("techreborn"));

        offerDollRecipes(exporter);
        offerWoodRecipes(exporter, techRebornExporter);
        offerHammerRecipes(exporter);
        offerCrookRecipes(exporter);
        offerMeshRecipes(exporter, mythicMetalsExporter);
        offerPebbleToRockRecipes(exporter);
        offerOrePieceRecipes(exporter, mythicMetalsExporter, indrevExporter, modernIndustrializationExporter, techRebornExporter);
        offerMiscRecipes(exporter);
    }

    @Override
    public String getName() {
        return "Crafting Recipes";
    }

    private static void offerDollRecipes(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, DOLLS.get(id("doll")))
                .pattern("xyx")
                .pattern(" x ")
                .pattern("x x")
                .input('x', ModTags.Common.PORCELAIN)
                .input('y', Items.EMERALD)
                .criterion("has_porcelain", conditionsFromTag(ModTags.Common.PORCELAIN))
                .offerTo(exporter, id("doll/base"));
        createDollRecipe(Items.REDSTONE, Items.NETHER_WART, Items.BLACK_DYE, DOLLS.get(id("doll_enderman"))).offerTo(exporter, id("doll/enderman"));
        createDollRecipe(Items.NETHER_WART, Items.REDSTONE, Items.BLAZE_POWDER, DOLLS.get(id("doll_blaze"))).offerTo(exporter, id("doll/blaze"));
        createDollRecipe(Items.REDSTONE, Items.COD, Items.PRISMARINE_CRYSTALS, DOLLS.get(id("doll_guardian"))).offerTo(exporter, id("doll/guardian"));
        createDollRecipe(Items.END_STONE, Items.ENDER_PEARL, Items.PURPLE_DYE, DOLLS.get(id("doll_shulker"))).offerTo(exporter, id("doll/shulker"));
    }

    private static ShapedRecipeJsonBuilder createDollRecipe(Item top, Item bottom, Item corner, Item result) {
        return ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, result)
                .pattern("ctc")
                .pattern("sds")
                .pattern("cbc")
                .input('c', corner)
                .input('t', top)
                .input('b', bottom)
                .input('s', Items.GLOWSTONE_DUST)
                .input('d', DOLLS.get(id("doll")))
                .criterion("has_doll", conditionsFromItem(DOLLS.get(id("doll"))));
    }

    private static void offerWoodRecipes(RecipeExporter exporter, RecipeExporter techRebornExporter) {
        offerWoodSetRecipes(Items.ACACIA_LOG, Items.ACACIA_PLANKS, Items.ACACIA_SLAB, "acacia", exporter);
        offerWoodSetRecipes(Items.BIRCH_LOG, Items.BIRCH_PLANKS, Items.BIRCH_SLAB, "birch", exporter);
        offerWoodSetRecipes(Items.CRIMSON_STEM, Items.CRIMSON_PLANKS, Items.CRIMSON_SLAB, "crimson", exporter);
        offerWoodSetRecipes(Items.DARK_OAK_LOG, Items.DARK_OAK_PLANKS, Items.DARK_OAK_SLAB, "dark_oak", exporter);
        offerWoodSetRecipes(Items.JUNGLE_LOG, Items.JUNGLE_PLANKS, Items.JUNGLE_SLAB, "jungle", exporter);
        offerWoodSetRecipes(Items.OAK_LOG, Items.OAK_PLANKS, Items.OAK_SLAB, "oak", exporter);
        offerWoodSetRecipes(Items.SPRUCE_LOG, Items.SPRUCE_PLANKS, Items.SPRUCE_SLAB, "spruce", exporter);
        offerWoodSetRecipes(Items.WARPED_STEM, Items.WARPED_PLANKS, Items.WARPED_SLAB, "warped", exporter);
        offerWoodSetRecipes(Items.MANGROVE_LOG, Items.MANGROVE_PLANKS, Items.MANGROVE_SLAB, "mangrove", exporter);
        offerWoodSetRecipes(Items.CHERRY_LOG, Items.CHERRY_PLANKS, Items.CHERRY_SLAB, "cherry", exporter);
        offerWoodSetRecipes(Items.BAMBOO_BLOCK, Items.BAMBOO_PLANKS, Items.BAMBOO_SLAB, "bamboo", exporter);
        offerWoodSetRecipes(getDummyItem(TR_RUBBER_LOG_ID), getDummyItem(TR_RUBBER_PLANKS_ID), getDummyItem(TR_RUBBER_SLAB_ID), "rubber", techRebornExporter);
    }

    private static void offerWoodSetRecipes(Item log, Item planks, Item slab, String name, RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, BARRELS.get(id(name + "_barrel")))
                .group("fabricaeexnihilo:barrel")
                .pattern("# #")
                .pattern("# #")
                .pattern("#S#")
                .input('#', planks)
                .input('S', slab)
                .criterion("has_planks", conditionsFromItem(planks))
                .offerTo(exporter, id("barrel/" + name));

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, CRUCIBLES.get(id(name + "_crucible")))
                .group("fabricaeexnihilo:crucible")
                .pattern("# #")
                .pattern("#S#")
                .pattern("I I")
                .input('#', log)
                .input('S', slab)
                .input('I', Items.STICK)
                .criterion("has_log", conditionsFromItem(log))
                .offerTo(exporter, id("crucible/" + name));

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, SIEVES.get(id(name + "_sieve")))
                .group("fabricaeexnihilo:sieve")
                .pattern("# #")
                .pattern("#S#")
                .pattern("I I")
                .input('#', planks)
                .input('S', slab)
                .input('I', Items.STICK)
                .criterion("has_planks", conditionsFromItem(planks))
                .offerTo(exporter, id("sieve/" + name));

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, STRAINERS.get(id(name + "_strainer")))
                .group("fabricaeexnihilo:sieve")
                .pattern("#S#")
                .pattern("SSS")
                .pattern("#S#")
                .input('#', log)
                .input('S', Items.STRING)
                .criterion("has_log", conditionsFromItem(log))
                .offerTo(exporter, id("strainer/" + name));

    }

    private static void offerCrookRecipes(RecipeExporter exporter) {
        createCrookRecipe(PEBBLES.get(id("andesite_pebble")), CROOKS.get(id("andesite_crook"))).offerTo(exporter, id("crook/andesite"));
        createCrookRecipe(PEBBLES.get(id("basalt_pebble")), CROOKS.get(id("basalt_crook"))).offerTo(exporter, id("crook/basalt"));
        createCrookRecipe(PEBBLES.get(id("blackstone_pebble")), CROOKS.get(id("blackstone_crook"))).offerTo(exporter, id("crook/blackstone"));
        createCrookRecipe(Items.BLAZE_ROD, CROOKS.get(id("blaze_crook"))).offerTo(exporter, id("crook/blaze"));
        createCrookRecipe(Items.BONE, CROOKS.get(id("bone_crook"))).offerTo(exporter, id("crook/bone"));
        createCrookRecipe(Items.CLAY_BALL, CROOKS.get(id("clay_crook"))).offerTo(exporter, id("crook/clay"));
        createCrookRecipe(PEBBLES.get(id("deepslate_pebble")), CROOKS.get(id("deepslate_crook"))).offerTo(exporter, id("crook/deepslate"));
        createCrookRecipe(PEBBLES.get(id("diorite_pebble")), CROOKS.get(id("diorite_crook"))).offerTo(exporter, id("crook/diorite"));
        createCrookRecipe(PEBBLES.get(id("granite_pebble")), CROOKS.get(id("granite_crook"))).offerTo(exporter, id("crook/granite"));
        createCrookRecipe(Items.PRISMARINE_SHARD, CROOKS.get(id("prismarine_crook"))).offerTo(exporter, id("crook/prismarine"));
        createCrookRecipe(Items.POPPED_CHORUS_FRUIT, CROOKS.get(id("purpur_crook"))).offerTo(exporter, id("crook/purpur"));
        createCrookRecipe(PEBBLES.get(id("stone_pebble")), CROOKS.get(id("stone_crook"))).offerTo(exporter, id("crook/stone"));
        createCrookRecipe(Items.STICK, CROOKS.get(id("wooden_crook"))).offerTo(exporter, id("crook/wood"));
        CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItems(CROOKS.get(id("clay_crook"))), RecipeCategory.TOOLS, CROOKS.get(id("terracotta_crook")), 0.15f, 200)
                .criterion("has_material", conditionsFromItem(CROOKS.get(id("clay_crook"))))
                .offerTo(exporter, id("crook/terracotta"));
    }

    private static ShapedRecipeJsonBuilder createCrookRecipe(Item material, Item result) {
        return ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, result)
                .group("fabricaeexnihilo:crooks")
                .input('#', material)
                .pattern("##")
                .pattern(" #")
                .pattern(" #")
                .criterion("has_material", conditionsFromItem(material));
    }

    private static void offerHammerRecipes(RecipeExporter exporter) {
        createHammerRecipe(ItemTags.PLANKS, HAMMERS.get(id("wooden_hammer"))).offerTo(exporter, id("hammer/wood"));
        createHammerRecipe(Items.COBBLESTONE, HAMMERS.get(id("stone_hammer"))).offerTo(exporter, id("hammer/stone"));
        createHammerRecipe(Items.IRON_INGOT, HAMMERS.get(id("iron_hammer"))).offerTo(exporter, id("hammer/iron"));
        createHammerRecipe(Items.GOLD_INGOT, HAMMERS.get(id("golden_hammer"))).offerTo(exporter, id("hammer/gold"));
        createHammerRecipe(Items.DIAMOND, HAMMERS.get(id("diamond_hammer"))).offerTo(exporter, id("hammer/diamond"));
        createNetheriteRecipe(HAMMERS.get(id("diamond_hammer")), HAMMERS.get(id("netherite_hammer")), RecipeCategory.TOOLS)
                .criterion("has_diamond_hammer", conditionsFromItem(HAMMERS.get(id("diamond_hammer"))))
                .offerTo(exporter, id("hammer/netherite"));
    }

    private static ShapedRecipeJsonBuilder createHammerRecipe(Item material, Item result) {
        return ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, result)
                .input('#', material)
                .input('/', Items.STICK)
                .pattern(" # ")
                .pattern(" /#")
                .pattern("/  ")
                .criterion("has_material", conditionsFromItem(material));
    }

    private static ShapedRecipeJsonBuilder createHammerRecipe(TagKey<Item> material, Item result) {
        return ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, result)
                .input('#', material)
                .input('/', Items.STICK)
                .pattern(" # ")
                .pattern(" /#")
                .pattern("/  ")
                .criterion("has_material", conditionsFromTag(material));
    }

    private static void offerMeshRecipes(RecipeExporter exporter, RecipeExporter mythicMetalsExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, DefaultApiModule.INSTANCE.stringMesh)
                .input('#', Items.STRING)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .criterion("has_string", conditionsFromItem(Items.STRING))
                .offerTo(exporter, id("mesh/string"));

        createMeshRecipe(DefaultApiModule.INSTANCE.stringMesh, Items.FLINT, DefaultApiModule.INSTANCE.flintMesh).offerTo(exporter, id("mesh/flint"));
        createMeshRecipe(DefaultApiModule.INSTANCE.flintMesh, Items.IRON_INGOT, DefaultApiModule.INSTANCE.ironMesh).offerTo(exporter, id("mesh/iron"));
        createMeshRecipe(DefaultApiModule.INSTANCE.ironMesh, Items.DIAMOND, DefaultApiModule.INSTANCE.diamondMesh).offerTo(exporter, id("mesh/diamond"));
        createMeshRecipe(DefaultApiModule.INSTANCE.diamondMesh, getDummyItem(MM_ADAMANTITE_INGOT_ID), MythicMetalsApiModule.INSTANCE.adamantiteMesh).offerTo(mythicMetalsExporter, id("mesh/adamantite"));
        createMeshRecipe(DefaultApiModule.INSTANCE.flintMesh, Items.COPPER_INGOT, DefaultApiModule.INSTANCE.copperMesh).offerTo(exporter, id("mesh/copper"));
        createMeshRecipe(DefaultApiModule.INSTANCE.copperMesh, Items.GOLD_INGOT, DefaultApiModule.INSTANCE.goldMesh).offerTo(exporter, id("mesh/gold"));
        createMeshRecipe(DefaultApiModule.INSTANCE.goldMesh, Items.EMERALD, DefaultApiModule.INSTANCE.emeraldMesh).offerTo(exporter, id("mesh/emerald"));

        createNetheriteRecipe(DefaultApiModule.INSTANCE.diamondMesh, DefaultApiModule.INSTANCE.netheriteMesh, RecipeCategory.MISC)
                .criterion("has_previous", conditionsFromItem(DefaultApiModule.INSTANCE.diamondMesh))
                .offerTo(exporter, id("mesh/netherite"));
    }

    private static ShapedRecipeJsonBuilder createMeshRecipe(Item previous, Item ingredient, Item result) {
        return ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, result)
                .input('#', ingredient)
                .input('O', previous)
                .pattern("# #")
                .pattern("#O#")
                .pattern("# #")
                .criterion("has_previous", conditionsFromItem(previous));
    }

    private static void offerPebbleToRockRecipes(RecipeExporter exporter) {
        createPebbleToRockRecipe(PEBBLES.get(id("andesite_pebble")), Items.ANDESITE).offerTo(exporter, id("pebble_to_rock/andesite"));
        createPebbleToRockRecipe(PEBBLES.get(id("basalt_pebble")), Items.BASALT).offerTo(exporter, id("pebble_to_rock/basalt"));
        createPebbleToRockRecipe(PEBBLES.get(id("blackstone_pebble")), Items.BLACKSTONE).offerTo(exporter, id("pebble_to_rock/blackstone"));
        createPebbleToRockRecipe(PEBBLES.get(id("calcite_pebble")), Items.CALCITE).offerTo(exporter, id("pebble_to_rock/calcite"));
        createPebbleToRockRecipe(PEBBLES.get(id("deepslate_pebble")), Items.DEEPSLATE).offerTo(exporter, id("pebble_to_rock/deepslate"));
        createPebbleToRockRecipe(PEBBLES.get(id("diorite_pebble")), Items.DIORITE).offerTo(exporter, id("pebble_to_rock/diorite"));
        createPebbleToRockRecipe(PEBBLES.get(id("dripstone_pebble")), Items.POINTED_DRIPSTONE).offerTo(exporter, id("pebble_to_rock/dripstone"));
        createPebbleToRockRecipe(PEBBLES.get(id("granite_pebble")), Items.GRANITE).offerTo(exporter, id("pebble_to_rock/granite"));
        createPebbleToRockRecipe(PEBBLES.get(id("stone_pebble")), Items.STONE).offerTo(exporter, id("pebble_to_rock/stone"));
        createPebbleToRockRecipe(PEBBLES.get(id("tuff_pebble")), Items.TUFF).offerTo(exporter, id("pebble_to_rock/tuff"));
    }

    private static ShapedRecipeJsonBuilder createPebbleToRockRecipe(Item pebble, Item rock) {
        return ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, rock)
                .input('#', pebble)
                .pattern("##")
                .pattern("##")
                .criterion("has_pebble", conditionsFromItem(pebble));
    }

    private static void offerOrePieceRecipes(RecipeExporter exporter,
                                             RecipeExporter mythicMetalsExporter,
                                             RecipeExporter indrevExporter,
                                             RecipeExporter modernIndustrializationExporter,
                                             RecipeExporter techRebornExporter) {
        createOrePieceRecipe(DefaultApiModule.INSTANCE.copperPiece, Items.RAW_COPPER).offerTo(exporter, id("ore_piece/copper"));
        createOrePieceRecipe(DefaultApiModule.INSTANCE.goldPiece, Items.RAW_GOLD).offerTo(exporter, id("ore_piece/gold"));
        createOrePieceRecipe(DefaultApiModule.INSTANCE.ironPiece, Items.RAW_IRON).offerTo(exporter, id("ore_piece/iron"));

        createOrePieceRecipe(MythicMetalsApiModule.INSTANCE.adamantitePiece, getDummyItem(MM_RAW_ADAMANTITE_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/adamantite"));
        createOrePieceRecipe(MythicMetalsApiModule.INSTANCE.aquariumPiece, getDummyItem(MM_RAW_AQUARIUM_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/aquarium"));
        createOrePieceRecipe(MythicMetalsApiModule.INSTANCE.banglumPiece, getDummyItem(MM_RAW_BANGLUM_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/banglum"));
        createOrePieceRecipe(MythicMetalsApiModule.INSTANCE.carmotPiece, getDummyItem(MM_RAW_CARMOT_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/carmot"));
        createOrePieceRecipe(MythicMetalsApiModule.INSTANCE.kyberPiece, getDummyItem(MM_RAW_KYBER_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/kyber"));
        createOrePieceRecipe(MythicMetalsApiModule.INSTANCE.manganesePiece, getDummyItem(MM_RAW_MANGANESE_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/manganese"));
        createOrePieceRecipe(MythicMetalsApiModule.INSTANCE.midasGoldPiece, getDummyItem(MM_RAW_MIDAS_GOLD_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/midas_gold"));
        createOrePieceRecipe(MythicMetalsApiModule.INSTANCE.mythrilPiece, getDummyItem(MM_RAW_MYTHRIL_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/mythril"));
        createOrePieceRecipe(MythicMetalsApiModule.INSTANCE.orichalcumPiece, getDummyItem(MM_RAW_ORICHALCUM_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/orichalcum"));
        createOrePieceRecipe(MythicMetalsApiModule.INSTANCE.osmiumPiece, getDummyItem(MM_RAW_OSMIUM_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/osmium"));
        createOrePieceRecipe(MythicMetalsApiModule.INSTANCE.palladiumPiece, getDummyItem(MM_RAW_PALLADIUM_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/palladium"));
        createOrePieceRecipe(MythicMetalsApiModule.INSTANCE.platinumPiece, getDummyItem(MM_RAW_PLATINUM_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/platinum"));
        createOrePieceRecipe(MythicMetalsApiModule.INSTANCE.prometheumPiece, getDummyItem(MM_RAW_PROMETHEUM_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/prometheum"));
        createOrePieceRecipe(MythicMetalsApiModule.INSTANCE.quadrillumPiece, getDummyItem(MM_RAW_QUADRILLUM_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/quadrillum"));
        createOrePieceRecipe(MythicMetalsApiModule.INSTANCE.runitePiece, getDummyItem(MM_RAW_RUNITE_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/runite"));
        createOrePieceRecipe(MythicMetalsApiModule.INSTANCE.silverPiece, getDummyItem(MM_RAW_SILVER_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/silver"));
        createOrePieceRecipe(MythicMetalsApiModule.INSTANCE.stormyxPiece, getDummyItem(MM_RAW_STORMYX_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/stormyx"));
        createOrePieceRecipe(MythicMetalsApiModule.INSTANCE.tinPiece, getDummyItem(MM_RAW_TIN_ID)).offerTo(mythicMetalsExporter, id("ore_piece/mythicmetals/tin"));

        createOrePieceRecipe(TechRebornApiModule.INSTANCE.iridiumPiece, getDummyItem(TR_RAW_IRIDIUM_ID)).offerTo(techRebornExporter, id("ore_piece/techreborn/iridium"));
        createOrePieceRecipe(TechRebornApiModule.INSTANCE.leadPiece, getDummyItem(TR_RAW_LEAD_ID)).offerTo(techRebornExporter, id("ore_piece/techreborn/lead"));
        createOrePieceRecipe(TechRebornApiModule.INSTANCE.silverPiece, getDummyItem(TR_RAW_SILVER_ID)).offerTo(techRebornExporter, id("ore_piece/techreborn/silver"));
        createOrePieceRecipe(TechRebornApiModule.INSTANCE.tinPiece, getDummyItem(TR_RAW_TIN_ID)).offerTo(techRebornExporter, id("ore_piece/techreborn/tin"));
        createOrePieceRecipe(TechRebornApiModule.INSTANCE.tungstenPiece, getDummyItem(TR_RAW_TUNGSTEN_ID)).offerTo(techRebornExporter, id("ore_piece/techreborn/tungsten"));

        createOrePieceRecipe(ModernIndustrializationApiModule.INSTANCE.antimonyPiece, getDummyItem(MI_RAW_ANTIMONY_ID)).offerTo(modernIndustrializationExporter, id("ore_piece/modern_industrialization/antimony"));
        createOrePieceRecipe(ModernIndustrializationApiModule.INSTANCE.leadPiece, getDummyItem(MI_RAW_LEAD_ID)).offerTo(modernIndustrializationExporter, id("ore_piece/modern_industrialization/lead"));
        createOrePieceRecipe(ModernIndustrializationApiModule.INSTANCE.nickelPiece, getDummyItem(MI_RAW_NICKEL_ID)).offerTo(modernIndustrializationExporter, id("ore_piece/modern_industrialization/nickel"));
        createOrePieceRecipe(ModernIndustrializationApiModule.INSTANCE.tinPiece, getDummyItem(MI_RAW_TIN_ID)).offerTo(modernIndustrializationExporter, id("ore_piece/modern_industrialization/tin"));

        createOrePieceRecipe(IndustrialRevolutionApiModule.INSTANCE.leadPiece, getDummyItem(IR_RAW_LEAD_ID)).offerTo(indrevExporter, id("ore_piece/indrev/lead"));
        createOrePieceRecipe(IndustrialRevolutionApiModule.INSTANCE.silverPiece, getDummyItem(IR_RAW_SILVER_ID)).offerTo(indrevExporter, id("ore_piece/indrev/silver"));
        createOrePieceRecipe(IndustrialRevolutionApiModule.INSTANCE.tinPiece, getDummyItem(IR_RAW_TIN_ID)).offerTo(indrevExporter, id("ore_piece/indrev/tin"));
        createOrePieceRecipe(IndustrialRevolutionApiModule.INSTANCE.tungstenPiece, getDummyItem(IR_RAW_TUNGSTEN_ID)).offerTo(indrevExporter, id("ore_piece/indrev/tungsten"));
    }

    private static ShapelessRecipeJsonBuilder createOrePieceRecipe(Item piece, Item rawOre) {
        return ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, rawOre)
                .input(piece)
                .input(piece)
                .input(piece)
                .input(piece)
                .criterion("has_piece", conditionsFromItem(piece));
    }

    private static void offerMiscRecipes(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, END_CAKE)
                .pattern("###")
                .pattern("#C#")
                .pattern("###")
                .input('#', Items.ENDER_EYE)
                .input('C', Items.CAKE)
                .criterion("has_ender_eye", conditionsFromItem(Items.ENDER_EYE))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, PORCELAIN)
                .input(Items.CLAY_BALL)
                .input(Items.BONE_MEAL)
                .criterion("has_clay", conditionsFromItem(Items.CLAY_BALL))
                .offerTo(exporter);

        CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItems(RAW_SILKWORM), RecipeCategory.FOOD, COOKED_SILKWORM, 0.35f, 200)
                .criterion("has_silkworm", conditionsFromItem(RAW_SILKWORM))
                .offerTo(exporter, id("silkworm_cooked"));
        CookingRecipeJsonBuilder.createSmoking(Ingredient.ofItems(RAW_SILKWORM), RecipeCategory.FOOD, COOKED_SILKWORM, 0.35f, 100)
                .criterion("has_silkworm", conditionsFromItem(RAW_SILKWORM))
                .offerTo(exporter, id("silkworm_cooked_from_smoking"));
        CookingRecipeJsonBuilder.createCampfireCooking(Ingredient.ofItems(RAW_SILKWORM), RecipeCategory.FOOD, COOKED_SILKWORM, 0.35f, 600)
                .criterion("has_silkworm", conditionsFromItem(RAW_SILKWORM))
                .offerTo(exporter, id("silkworm_cooked_from_campfire_cooking"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, DefaultApiModule.INSTANCE.stoneBarrel)
                .group("fabricaeexnihilo:barrel")
                .pattern("# #")
                .pattern("# #")
                .pattern("#S#")
                .input('#', Items.STONE)
                .input('S', Items.STONE_SLAB)
                .criterion("has_stone", conditionsFromItem(Items.STONE))
                .offerTo(exporter, id("barrel/stone"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, UNFIRED_PORCELAIN_CRUCIBLE)
                .group("fabricaeexnihilo:crucible")
                .pattern("# #")
                .pattern("# #")
                .pattern("###")
                .input('#', PORCELAIN)
                .criterion("has_porcelain", conditionsFromItem(PORCELAIN))
                .offerTo(exporter, id("crucible/unfired_porcelain"));

        CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItems(UNFIRED_PORCELAIN_CRUCIBLE), RecipeCategory.DECORATIONS, CRUCIBLES.get(id("porcelain_crucible")), 0.15f, 200)
                .group("fabricaeexnihilo:crucible")
                .criterion("has_porcelain", conditionsFromItem(PORCELAIN))
                .offerTo(exporter, id("crucible/uporcelain"));
    }

    private static SmithingTransformRecipeJsonBuilder createNetheriteRecipe(Item input, Item result, RecipeCategory recipeCategory) {
        return SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.ofItems(input),
                Ingredient.ofItems(Items.NETHERITE_INGOT),
                recipeCategory,
                result);
    }
}
