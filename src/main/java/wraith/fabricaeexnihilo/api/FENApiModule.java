package wraith.fabricaeexnihilo.api;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * An entrypoint that allows mods to add their own resources to Fabricae Ex Nihilo.
 */
@FunctionalInterface
@ApiStatus.OverrideOnly
public interface FENApiModule {
    /**
     * The main entrypoint for FEN plugins.
     *
     * @param registries An object with methods that allows registering to internal registries.
     */
    void onInit(FENRegistries registries);

    /**
     * You can override this if you need to do additional checks before the module is used. Mostly used by integrated compatibility modules to check if the target mod is loaded.
     *
     * @return Whether the module should be loaded.
     */
    default boolean shouldLoad() {
        return true;
    }

    /**
     * Override this if to provide a resource condition for assets generated for this module. Mostly used by included modules to disable assets for other mods.
     *
     * @return A {@link ResourceCondition} that corresponds with when this plugin is loaded, or {@code null} if there isn't a condition, or it isn't required for assets to follow it.
     */
    @Nullable
    default ResourceCondition getResourceCondition() {
        return null;
    }
}
