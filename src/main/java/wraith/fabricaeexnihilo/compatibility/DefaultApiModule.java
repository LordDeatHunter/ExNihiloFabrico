package wraith.fabricaeexnihilo.compatibility;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.util.Color;

public class DefaultApiModule implements FENApiModule {
    public static final DefaultApiModule INSTANCE = new DefaultApiModule();
    public Item ironPiece;
    public Item copperPiece;
    public Item goldPiece;
    public FENRegistries.WoodenBlockBundle oakBlocks;
    public FENRegistries.WoodenBlockBundle birchBlocks;
    public FENRegistries.WoodenBlockBundle spruceBlocks;
    public FENRegistries.WoodenBlockBundle acaciaBlocks;
    public FENRegistries.WoodenBlockBundle darkOakBlocks;
    public FENRegistries.WoodenBlockBundle jungleBlocks;
    public FENRegistries.WoodenBlockBundle warpedBlocks;
    public FENRegistries.WoodenBlockBundle crimsonBlocks;
    public FENRegistries.WoodenBlockBundle mangroveBlocks;
    public FENRegistries.WoodenBlockBundle cherryBlocks;
    public FENRegistries.WoodenBlockBundle bambooBlocks;
    public Block dust;
    public Block silt;
    public Block crushedCalcite;
    public Block crushedAndesite;
    public Block crushedDiorite;
    public Block crushedGranite;
    public Block crushedPrismarine;
    public Block crushedEndstone;
    public Block crushedNetherrack;
    public Block crushedDeepslate;
    public Block crushedBlackstone;
    public Block porcelainCrucible;
    public Block stoneBarrel;
    public Block infestedOakLeaves;
    public Block infestedBirchLeaves;
    public Block infestedSpruceLeaves;
    public Block infestedAcaciaLeaves;
    public Block infestedDarkOakLeaves;
    public Block infestedJungleLeaves;
    public Block infestedMangroveLeaves;
    public Block infestedCherryLeaves;
    public Item stringMesh;
    public Item flintMesh;
    public Item ironMesh;
    public Item diamondMesh;
    public Item netheriteMesh;
    public Item copperMesh;
    public Item goldMesh;
    public Item emeraldMesh;
    public Item myceliumSeeds;
    public Item warpedSeeds;
    public Item crimsonSeeds;
    public Item grassSeeds;
    public Item chorusSeeds;
    public Item seaPickleSeeds;
    public Item sugarcaneSeeds;
    public Item cactusSeeds;
    public Item kelpSeeds;
    public Item sunflowerSeeds;
    public Item lilacSeeds;
    public Item roseBushSeeds;
    public Item peonySeeds;

    @Override
    public void onInit(FENRegistries registries) {
        ironPiece = registries.registerOrePiece("iron", registries.defaultItemSettings());
        copperPiece = registries.registerOrePiece("copper", registries.defaultItemSettings());
        goldPiece = registries.registerOrePiece("gold", registries.defaultItemSettings());

        oakBlocks = registries.registerWood("oak", false, registries.woodenBlockSettings());
        birchBlocks = registries.registerWood("birch", false, registries.woodenBlockSettings());
        spruceBlocks = registries.registerWood("spruce", false, registries.woodenBlockSettings());
        acaciaBlocks = registries.registerWood("acacia", false, registries.woodenBlockSettings());
        darkOakBlocks = registries.registerWood("dark_oak", false, registries.woodenBlockSettings());
        jungleBlocks = registries.registerWood("jungle", false, registries.woodenBlockSettings());
        warpedBlocks = registries.registerWood("warped", true, registries.woodenBlockSettings());
        crimsonBlocks = registries.registerWood("crimson", true, registries.woodenBlockSettings());
        mangroveBlocks = registries.registerWood("mangrove", false, registries.woodenBlockSettings());
        cherryBlocks = registries.registerWood("cherry", false, registries.woodenBlockSettings());
        bambooBlocks = registries.registerWood("bamboo", false, registries.woodenBlockSettings());

        dust = registries.registerCrushedBlock("dust", registries.sandyBlockSettings());
        silt = registries.registerCrushedBlock("silt", registries.sandyBlockSettings());
        crushedCalcite = registries.registerCrushedBlock("crushed_calcite", registries.sandyBlockSettings());

        crushedAndesite = registries.registerCrushedBlock("crushed_andesite", registries.gravelyBlockSettings());
        crushedDiorite = registries.registerCrushedBlock("crushed_diorite", registries.gravelyBlockSettings());
        crushedGranite = registries.registerCrushedBlock("crushed_granite", registries.gravelyBlockSettings());
        crushedPrismarine = registries.registerCrushedBlock("crushed_prismarine", registries.gravelyBlockSettings());
        crushedEndstone = registries.registerCrushedBlock("crushed_endstone", registries.gravelyBlockSettings());
        crushedNetherrack = registries.registerCrushedBlock("crushed_netherrack", registries.gravelyBlockSettings());
        crushedDeepslate = registries.registerCrushedBlock("crushed_deepslate", registries.gravelyBlockSettings());
        crushedBlackstone = registries.registerCrushedBlock("crushed_blackstone", registries.gravelyBlockSettings());

        porcelainCrucible = registries.registerCrucible("porcelain", true, registries.stoneBlockSettings());
        stoneBarrel = registries.registerBarrel("stone", true, registries.stoneBlockSettings());

        infestedOakLeaves = registries.registerInfestedLeaves("oak", Identifier.of("minecraft:oak_leaves"), registries.infestedLeavesBlockSettings());
        infestedBirchLeaves = registries.registerInfestedLeaves("birch", Identifier.of("minecraft:birch_leaves"), registries.infestedLeavesBlockSettings());
        infestedSpruceLeaves = registries.registerInfestedLeaves("spruce", Identifier.of("minecraft:spruce_leaves"), registries.infestedLeavesBlockSettings());
        infestedAcaciaLeaves = registries.registerInfestedLeaves("acacia", Identifier.of("minecraft:acacia_leaves"), registries.infestedLeavesBlockSettings());
        infestedDarkOakLeaves = registries.registerInfestedLeaves("dark_oak", Identifier.of("minecraft:dark_oak_leaves"), registries.infestedLeavesBlockSettings());
        infestedJungleLeaves = registries.registerInfestedLeaves("jungle", Identifier.of("minecraft:jungle_leaves"), registries.infestedLeavesBlockSettings());
        infestedMangroveLeaves = registries.registerInfestedLeaves("mangrove", Identifier.of("minecraft:mangrove_leaves"), registries.infestedLeavesBlockSettings());
        infestedCherryLeaves = registries.registerInfestedLeaves("cherry", Identifier.of("minecraft:cherry_leaves"), registries.infestedLeavesBlockSettings());

        stringMesh = registries.registerMesh("string", Color.WHITE, 10, registries.defaultItemSettings());
        flintMesh = registries.registerMesh("flint", Color.GRAY, 12, registries.defaultItemSettings());
        ironMesh = registries.registerMesh("iron", new Color("777777"), 14, registries.defaultItemSettings());
        diamondMesh = registries.registerMesh("diamond", Color.DARK_AQUA, 10, registries.defaultItemSettings());
        netheriteMesh = registries.registerMesh("netherite", new Color("3B393B"), 15, registries.defaultItemSettings());
        copperMesh = registries.registerMesh("copper", Color.COPPER, 13, registries.defaultItemSettings());
        goldMesh = registries.registerMesh("gold", Color.GOLDEN, 22, registries.defaultItemSettings());
        emeraldMesh = registries.registerMesh("emerald", Color.DARK_GREEN, 24, registries.defaultItemSettings());

        var seedConfig = FabricaeExNihilo.CONFIG.get().seeds();
        if (seedConfig.enabled()) {
            if (seedConfig.mycelium()) {
                myceliumSeeds = registries.registerTransformingSeed("mycelium", Identifier.of("minecraft:dirt"), Identifier.of("minecraft:mycelium"));
            }
            if (seedConfig.netherSpores()) {
                warpedSeeds = registries.registerTransformingSeed("warped", Identifier.of("minecraft:netherrack"), Identifier.of("minecraft:warped_nylium"));
                crimsonSeeds = registries.registerTransformingSeed("crimson", Identifier.of("minecraft:netherrack"), Identifier.of("minecraft:crimson_nylium"));
            }
            if (seedConfig.grass()) {
                grassSeeds = registries.registerTransformingSeed("grass", Identifier.of("minecraft:dirt"), Identifier.of("minecraft:grass_block"));
            }
            //TODO: Replace seeds bellow with vanilla variants
            if (seedConfig.chorus()) {
                chorusSeeds = registries.registerSeed("chorus", Identifier.of("minecraft:chorus_flower"));
            }
            if (seedConfig.seaPickle()) {
                seaPickleSeeds = registries.registerSeed("sea_pickle", Identifier.of("minecraft:sea_pickle"));
            }
            if (seedConfig.sugarCane()) {
                sugarcaneSeeds = registries.registerSeed("sugarcane", Identifier.of("minecraft:sugar_cane"));
            }
            if (seedConfig.cactus()) {
                cactusSeeds = registries.registerSeed("cactus", Identifier.of("minecraft:cactus"));
            }
            if (seedConfig.kelp()) {
                kelpSeeds = registries.registerSeed("kelp", Identifier.of("minecraft:kelp"));
            }
            if (seedConfig.flowerSeeds()) {
                sunflowerSeeds = registries.registerTallPlantSeed("sunflower", Identifier.of("minecraft:sunflower"));
                lilacSeeds = registries.registerTallPlantSeed("lilac", Identifier.of("minecraft:lilac"));
                roseBushSeeds = registries.registerTallPlantSeed("rose_bush", Identifier.of("minecraft:rose_bush"));
                peonySeeds = registries.registerTallPlantSeed("peony", Identifier.of("minecraft:peony"));
            }
        }
    }
}
