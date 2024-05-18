package wraith.fabricaeexnihilo.datagen;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * This class provides dummy items that are used instead of modded items in datagen when the mod is not loaded.
 * This allows us to datagen content for mods that aren't enabled or installed during generation.
 * If the mod is loaded during datagen we use their items.
 */
public class DatagenItems {
    private static final boolean MYTHIC_METALS_INSTALLED = FabricLoader.getInstance().isModLoaded("mythicmetals");
    private static final boolean TECH_REBORN_INSTALLED = FabricLoader.getInstance().isModLoaded("techreborn");
    private static final boolean INDREV_INSTALLED = FabricLoader.getInstance().isModLoaded("indrev");
    private static final boolean MODERN_INDUSTRIALIZATION_INSTALLED = FabricLoader.getInstance().isModLoaded("modern_industrialization");

    public static final Identifier MM_ADAMANTITE_INGOT_ID = new Identifier("mythicmetals", "adamantite_ingot");
    public static final Identifier MM_RAW_ADAMANTITE_ID = new Identifier("mythicmetals", "raw_adamantite");
    public static final Identifier MM_RAW_AQUARIUM_ID = new Identifier("mythicmetals", "raw_aquarium");
    public static final Identifier MM_RAW_BANGLUM_ID = new Identifier("mythicmetals", "raw_banglum");
    public static final Identifier MM_RAW_CARMOT_ID = new Identifier("mythicmetals", "raw_carmot");
    public static final Identifier MM_RAW_KYBER_ID = new Identifier("mythicmetals", "raw_kyber");
    public static final Identifier MM_RAW_MANGANESE_ID = new Identifier("mythicmetals", "raw_manganese");
    public static final Identifier MM_RAW_MIDAS_GOLD_ID = new Identifier("mythicmetals", "raw_midas_gold");
    public static final Identifier MM_RAW_MYTHRIL_ID = new Identifier("mythicmetals", "raw_mythril");
    public static final Identifier MM_RAW_ORICHALCUM_ID = new Identifier("mythicmetals", "raw_orichalcum");
    public static final Identifier MM_RAW_OSMIUM_ID = new Identifier("mythicmetals", "raw_osmium");
    public static final Identifier MM_RAW_PALLADIUM_ID = new Identifier("mythicmetals", "raw_palladium");
    public static final Identifier MM_RAW_PLATINUM_ID = new Identifier("mythicmetals", "raw_platinum");
    public static final Identifier MM_RAW_PROMETHEUM_ID = new Identifier("mythicmetals", "raw_prometheum");
    public static final Identifier MM_RAW_QUADRILLUM_ID = new Identifier("mythicmetals", "raw_quadrillum");
    public static final Identifier MM_RAW_RUNITE_ID = new Identifier("mythicmetals", "raw_runite");
    public static final Identifier MM_RAW_SILVER_ID = new Identifier("mythicmetals", "raw_silver");
    public static final Identifier MM_RAW_STORMYX_ID = new Identifier("mythicmetals", "raw_stormyx");
    public static final Identifier MM_RAW_TIN_ID = new Identifier("mythicmetals", "raw_tin");

    public static final Identifier TR_RAW_IRIDIUM_ID = new Identifier("techreborn", "raw_iridium");
    public static final Identifier TR_RAW_LEAD_ID = new Identifier("techreborn", "raw_lead");
    public static final Identifier TR_RAW_SILVER_ID = new Identifier("techreborn", "raw_silver");
    public static final Identifier TR_RAW_TIN_ID = new Identifier("techreborn", "raw_tin");
    public static final Identifier TR_RAW_TUNGSTEN_ID = new Identifier("techreborn", "raw_tungsten");
    public static final Identifier TR_RUBBER_LOG_ID = new Identifier("techreborn", "rubber_log");
    public static final Identifier TR_RUBBER_PLANKS_ID = new Identifier("techreborn", "rubber_planks");
    public static final Identifier TR_RUBBER_SLAB_ID = new Identifier("techreborn", "rubber_slab");

    public static final Identifier MI_RAW_ANTIMONY_ID = new Identifier("modern_industrialization", "raw_antimony");
    public static final Identifier MI_RAW_LEAD_ID = new Identifier("modern_industrialization", "raw_lead");
    public static final Identifier MI_RAW_NICKEL_ID = new Identifier("modern_industrialization", "raw_nickel");
    public static final Identifier MI_RAW_TIN_ID = new Identifier("modern_industrialization", "raw_tin");

    public static final Identifier IR_RAW_LEAD_ID = new Identifier("indrev", "raw_lead");
    public static final Identifier IR_RAW_SILVER_ID = new Identifier("indrev", "raw_silver");
    public static final Identifier IR_RAW_TIN_ID = new Identifier("indrev", "raw_tin");
    public static final Identifier IR_RAW_TUNGSTEN_ID = new Identifier("indrev", "raw_tungsten");

    static {
        if (System.getProperty("fabric-api.datagen") == null) throw new IllegalStateException("Datagen dummy items loaded outside of datagen environment");
    }

    public static Item getDummyItem(Identifier id) {
        return Registries.ITEM.get(id);
    }

    // Called from EntrypointHelper
    public static void register() {
        if (!MYTHIC_METALS_INSTALLED) {
            registerDummies(MM_ADAMANTITE_INGOT_ID, MM_RAW_ADAMANTITE_ID, MM_RAW_AQUARIUM_ID, MM_RAW_BANGLUM_ID,
                    MM_RAW_CARMOT_ID, MM_RAW_KYBER_ID, MM_RAW_MANGANESE_ID, MM_RAW_MIDAS_GOLD_ID, MM_RAW_MYTHRIL_ID,
                    MM_RAW_ORICHALCUM_ID, MM_RAW_OSMIUM_ID, MM_RAW_PALLADIUM_ID, MM_RAW_PLATINUM_ID,
                    MM_RAW_PROMETHEUM_ID, MM_RAW_QUADRILLUM_ID, MM_RAW_RUNITE_ID, MM_RAW_SILVER_ID, MM_RAW_STORMYX_ID,
                    MM_RAW_TIN_ID);
        }

        if (!TECH_REBORN_INSTALLED) {
            registerDummies(TR_RAW_IRIDIUM_ID, TR_RAW_LEAD_ID, TR_RAW_SILVER_ID, TR_RAW_TIN_ID, TR_RAW_TUNGSTEN_ID,
                    TR_RUBBER_LOG_ID, TR_RUBBER_PLANKS_ID, TR_RUBBER_SLAB_ID);
        }

        if (!INDREV_INSTALLED) {
            registerDummies(IR_RAW_LEAD_ID, IR_RAW_SILVER_ID, IR_RAW_TIN_ID, IR_RAW_TUNGSTEN_ID);
        }

        if (!MODERN_INDUSTRIALIZATION_INSTALLED) {
            registerDummies(MI_RAW_ANTIMONY_ID, MI_RAW_LEAD_ID, MI_RAW_NICKEL_ID, MI_RAW_TIN_ID);
        }
    }

    private static void registerDummies(Identifier... ids) {
        for (var id : ids) {
            Registry.register(Registries.ITEM, id, new Item(new Item.Settings()));
        }
    }
}
