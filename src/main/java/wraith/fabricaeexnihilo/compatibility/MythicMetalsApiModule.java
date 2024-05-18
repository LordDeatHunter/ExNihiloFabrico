package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.util.Color;

public class MythicMetalsApiModule implements FENApiModule {
    public static final MythicMetalsApiModule INSTANCE = new MythicMetalsApiModule();
    public Item adamantitePiece;
    public Item aquariumPiece;
    public Item banglumPiece;
    public Item carmotPiece;
    public Item kyberPiece;
    public Item manganesePiece;
    public Item midasGoldPiece;
    public Item mythrilPiece;
    public Item orichalcumPiece;
    public Item osmiumPiece;
    public Item palladiumPiece;
    public Item platinumPiece;
    public Item prometheumPiece;
    public Item quadrillumPiece;
    public Item runitePiece;
    public Item silverPiece;
    public Item stormyxPiece;
    public Item tinPiece;
    public Block crushedBlackstone;
    public Block crushedDeepslate;
    public Item adamantiteMesh;


    @Override
    public void onInit(FENRegistries registries) {
        adamantitePiece = registries.registerOrePiece("adamantite", registries.defaultItemSettings());
        aquariumPiece = registries.registerOrePiece("aquarium", registries.defaultItemSettings());
        banglumPiece = registries.registerOrePiece("banglum", registries.defaultItemSettings());
        carmotPiece = registries.registerOrePiece("carmot", registries.defaultItemSettings());
        kyberPiece = registries.registerOrePiece("kyber", registries.defaultItemSettings());
        manganesePiece = registries.registerOrePiece("manganese", registries.defaultItemSettings());
        midasGoldPiece = registries.registerOrePiece("midas_gold", registries.defaultItemSettings());
        mythrilPiece = registries.registerOrePiece("mythril", registries.defaultItemSettings());
        orichalcumPiece = registries.registerOrePiece("orichalcum", registries.defaultItemSettings());
        osmiumPiece = registries.registerOrePiece("osmium", registries.defaultItemSettings());
        palladiumPiece = registries.registerOrePiece("palladium", registries.defaultItemSettings());
        platinumPiece = registries.registerOrePiece("platinum", registries.defaultItemSettings());
        prometheumPiece = registries.registerOrePiece("prometheum", registries.defaultItemSettings());
        quadrillumPiece = registries.registerOrePiece("quadrillum", registries.defaultItemSettings());
        runitePiece = registries.registerOrePiece("runite", registries.defaultItemSettings());
        silverPiece = registries.registerOrePiece("silver", registries.defaultItemSettings());
        stormyxPiece = registries.registerOrePiece("stormyx", registries.defaultItemSettings());
        tinPiece = registries.registerOrePiece("tin", registries.defaultItemSettings());

        crushedBlackstone = registries.registerCrushedBlock("crushed_blackstone", registries.gravelyBlockSettings());
        crushedDeepslate = registries.registerCrushedBlock("crushed_deepslate", registries.gravelyBlockSettings());

        adamantiteMesh = registries.registerMesh("adamantite", Color.DARK_RED, 20, registries.defaultItemSettings());
    }

    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("mythicmetals");
    }

    @Override
    @Nullable
    public ResourceCondition getResourceCondition() {
        return ResourceConditions.allModsLoaded("mythicmetals");
    }
}
