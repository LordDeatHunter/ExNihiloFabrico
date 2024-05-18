package wraith.fabricaeexnihilo.modules;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterStatusEffect;
import wraith.fabricaeexnihilo.util.Color;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class ModEffects {
    public static final RegistryEntry.Reference<StatusEffect> MILKED = register("milked", new StatusEffect(StatusEffectCategory.NEUTRAL, Color.WHITE.toInt()){});
    /**
     * A status effect used to mark entities that have been processed by the witch water code so that they are no longer
     * processed.
     */
    public static final RegistryEntry.Reference<StatusEffect> WITCH_WATERED = register("witch_watered", new WitchWaterStatusEffect());

    private static RegistryEntry.Reference<StatusEffect> register(String path, StatusEffect effect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, id(path), effect);
    }

    public static void init() {}
}
