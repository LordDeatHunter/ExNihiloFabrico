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

        infestedOakLeaves = registries.registerInfestedLeaves("oak", new Identifier("minecraft:oak_leaves"), registries.infestedLeavesBlockSettings());
        infestedBirchLeaves = registries.registerInfestedLeaves("birch", new Identifier("minecraft:birch_leaves"), registries.infestedLeavesBlockSettings());
        infestedSpruceLeaves = registries.registerInfestedLeaves("spruce", new Identifier("minecraft:spruce_leaves"), registries.infestedLeavesBlockSettings());
        infestedAcaciaLeaves = registries.registerInfestedLeaves("acacia", new Identifier("minecraft:acacia_leaves"), registries.infestedLeavesBlockSettings());
        infestedDarkOakLeaves = registries.registerInfestedLeaves("dark_oak", new Identifier("minecraft:dark_oak_leaves"), registries.infestedLeavesBlockSettings());
        infestedJungleLeaves = registries.registerInfestedLeaves("jungle", new Identifier("minecraft:jungle_leaves"), registries.infestedLeavesBlockSettings());
        infestedMangroveLeaves = registries.registerInfestedLeaves("mangrove", new Identifier("minecraft:mangrove_leaves"), registries.infestedLeavesBlockSettings());
        infestedCherryLeaves = registries.registerInfestedLeaves("cherry", new Identifier("minecraft:cherry_leaves"), registries.infestedLeavesBlockSettings());

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
                myceliumSeeds = registries.registerTransformingSeed("mycelium", new Identifier("minecraft:dirt"), new Identifier("minecraft:mycelium"));
            }
            if (seedConfig.netherSpores()) {
                warpedSeeds = registries.registerTransformingSeed("warped", new Identifier("minecraft:netherrack"), new Identifier("minecraft:warped_nylium"));
                crimsonSeeds = registries.registerTransformingSeed("crimson", new Identifier("minecraft:netherrack"), new Identifier("minecraft:crimson_nylium"));
            }
            if (seedConfig.grass()) {
                grassSeeds = registries.registerTransformingSeed("grass", new Identifier("minecraft:dirt"), new Identifier("minecraft:grass_block"));
            }
            //TODO: Replace seeds bellow with vanilla variants
            if (seedConfig.chorus()) {
                chorusSeeds = registries.registerSeed("chorus", new Identifier("minecraft:chorus_flower"));
            }
            if (seedConfig.seaPickle()) {
                seaPickleSeeds = registries.registerSeed("sea_pickle", new Identifier("minecraft:sea_pickle"));
            }
            if (seedConfig.sugarCane()) {
                sugarcaneSeeds = registries.registerSeed("sugarcane", new Identifier("minecraft:sugar_cane"));
            }
            if (seedConfig.cactus()) {
                cactusSeeds = registries.registerSeed("cactus", new Identifier("minecraft:cactus"));
            }
            if (seedConfig.kelp()) {
                kelpSeeds = registries.registerSeed("kelp", new Identifier("minecraft:kelp"));
            }
            if (seedConfig.flowerSeeds()) {
                sunflowerSeeds = registries.registerTallPlantSeed("sunflower", new Identifier("minecraft:sunflower"));
                lilacSeeds = registries.registerTallPlantSeed("lilac", new Identifier("minecraft:lilac"));
                roseBushSeeds = registries.registerTallPlantSeed("rose_bush", new Identifier("minecraft:rose_bush"));
                peonySeeds = registries.registerTallPlantSeed("peony", new Identifier("minecraft:peony"));
            }
        }
    }
}
