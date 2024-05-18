package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;
import wraith.fabricaeexnihilo.util.Color;

public class TechRebornApiModule implements FENApiModule {
    public static final TechRebornApiModule INSTANCE = new TechRebornApiModule();
    public Item tinPiece;
    public Item silverPiece;
    public Item leadPiece;
    public Item iridiumPiece;
    public Item tungstenPiece;
    public Item carbonMesh;
    public FENRegistries.WoodenBlockBundle rubberBlocks;
    public Block infestedRubberLeaves;


    @Override
    public void onInit(FENRegistries registries) {
        tinPiece = registries.registerOrePiece("tin", registries.defaultItemSettings());
        silverPiece = registries.registerOrePiece("silver", registries.defaultItemSettings());
        leadPiece = registries.registerOrePiece("lead", registries.defaultItemSettings());
        iridiumPiece = registries.registerOrePiece("iridium", registries.defaultItemSettings());
        tungstenPiece = registries.registerOrePiece("tungsten", registries.defaultItemSettings());

        carbonMesh = registries.registerMesh("carbon", Color.BLACK, 14, registries.defaultItemSettings());
        rubberBlocks = registries.registerWood("rubber", false, registries.woodenBlockSettings());
        infestedRubberLeaves = registries.registerInfestedLeaves("rubber", new Identifier("techreborn:rubber_leaves"), registries.infestedLeavesBlockSettings());
    }

    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("techreborn");
    }

    @Override
    @Nullable
    public ResourceCondition getResourceCondition() {
        return ResourceConditions.allModsLoaded("techreborn");
    }
}
