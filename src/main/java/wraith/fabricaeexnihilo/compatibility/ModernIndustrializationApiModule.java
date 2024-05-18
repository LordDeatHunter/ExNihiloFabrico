package wraith.fabricaeexnihilo.compatibility;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.api.FENApiModule;
import wraith.fabricaeexnihilo.api.FENRegistries;

public class ModernIndustrializationApiModule implements FENApiModule {
    public static final ModernIndustrializationApiModule INSTANCE = new ModernIndustrializationApiModule();
    public Item antimonyPiece;
    public Item tinPiece;
    public Item nickelPiece;
    public Item leadPiece;

    @Override
    public void onInit(FENRegistries registries) {
        antimonyPiece = registries.registerOrePiece("antimony", registries.defaultItemSettings());
        tinPiece = registries.registerOrePiece("tin", registries.defaultItemSettings());
        nickelPiece = registries.registerOrePiece("nickel", registries.defaultItemSettings());
        leadPiece = registries.registerOrePiece("lead", registries.defaultItemSettings());
    }

    @Override
    public boolean shouldLoad() {
        return FabricLoader.getInstance().isModLoaded("modern_industrialization");
    }

    @Override
    @Nullable
    public ResourceCondition getResourceCondition() {
        return ResourceConditions.allModsLoaded("modern_industrialization");
    }
}
