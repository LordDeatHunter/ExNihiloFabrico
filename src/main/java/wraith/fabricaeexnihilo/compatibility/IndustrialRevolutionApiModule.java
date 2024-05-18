package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;

public class IndustrialRevolutionApiModule implements FENApiModule {
    public static final IndustrialRevolutionApiModule INSTANCE = new IndustrialRevolutionApiModule();
    public Item tinPiece;
    public Item silverPiece;
    public Item leadPiece;
    public Item tungstenPiece;

    @Override
    public void onInit(FENRegistries registries) {
        tinPiece = registries.registerOrePiece("tin", registries.defaultItemSettings());
        silverPiece = registries.registerOrePiece("silver", registries.defaultItemSettings());
        leadPiece = registries.registerOrePiece("lead", registries.defaultItemSettings());
        tungstenPiece = registries.registerOrePiece("tungsten", registries.defaultItemSettings());
    }

    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("indrev");
    }

    @Override
    public @Nullable ResourceCondition getResourceCondition() {
        return ResourceConditions.allModsLoaded("indrev");
    }
}
