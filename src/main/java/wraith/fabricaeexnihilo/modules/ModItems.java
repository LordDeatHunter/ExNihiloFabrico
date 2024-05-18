package wraith.fabricaeexnihilo.modules;

import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.modules.infested.SilkWormItem;
import wraith.fabricaeexnihilo.modules.sieves.MeshItem;

import java.util.LinkedHashMap;
import java.util.Map;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class ModItems {

    public static final Item.Settings BASE_SETTINGS = new Item.Settings();
    public static final Item COOKED_SILKWORM;
    public static final Map<Identifier, Item> DOLLS = new LinkedHashMap<>();
    public static final Map<Identifier, MeshItem> MESHES = new LinkedHashMap<>();
    public static final Map<Identifier, Item> ORE_PIECES = new LinkedHashMap<>();
    public static final Map<Identifier, Item> PEBBLES = new LinkedHashMap<>();
    public static final Item PORCELAIN;
    public static final Item RAW_SILKWORM;
    public static final Item SALT_BOTTLE;
    public static final Map<Identifier, Item> SEEDS = new LinkedHashMap<>();
    public static final Item UNFIRED_PORCELAIN_CRUCIBLE;

    static {
        // TODO: replace RESOURCES with PEBBLES and single block fields
        PEBBLES.put(id("andesite_pebble"), new Item(BASE_SETTINGS));
        PEBBLES.put(id("basalt_pebble"), new Item(BASE_SETTINGS));
        PEBBLES.put(id("blackstone_pebble"), new Item(BASE_SETTINGS));
        PEBBLES.put(id("calcite_pebble"), new Item(BASE_SETTINGS));
        PEBBLES.put(id("dripstone_pebble"), new Item(BASE_SETTINGS));
        PEBBLES.put(id("deepslate_pebble"), new Item(BASE_SETTINGS));
        PEBBLES.put(id("diorite_pebble"), new Item(BASE_SETTINGS));
        PEBBLES.put(id("granite_pebble"), new Item(BASE_SETTINGS));
        PEBBLES.put(id("stone_pebble"), new Item(BASE_SETTINGS));
        PEBBLES.put(id("tuff_pebble"), new Item(BASE_SETTINGS));

        PORCELAIN = new Item(BASE_SETTINGS);
        UNFIRED_PORCELAIN_CRUCIBLE = new Item(BASE_SETTINGS);
        SALT_BOTTLE = new Item(BASE_SETTINGS);
        RAW_SILKWORM = new SilkWormItem(new Item.Settings().maxCount(64).food(FoodComponents.COD));
        COOKED_SILKWORM = new Item(new Item.Settings().maxCount(64).food(FoodComponents.COOKED_COD));

        DOLLS.put(id("doll"), new Item(BASE_SETTINGS));
        DOLLS.put(id("doll_blaze"), new Item(BASE_SETTINGS));
        DOLLS.put(id("doll_enderman"), new Item(BASE_SETTINGS));
        DOLLS.put(id("doll_guardian"), new Item(BASE_SETTINGS));
        DOLLS.put(id("doll_shulker"), new Item(BASE_SETTINGS));
    }

    public static void registerItems() {
        // Register stuff
        Registry.register(Registries.ITEM, id("porcelain"), PORCELAIN);
        Registry.register(Registries.ITEM, id("unfired_porcelain_crucible"), UNFIRED_PORCELAIN_CRUCIBLE);
        Registry.register(Registries.ITEM, id("salt_bottle"), SALT_BOTTLE);
        Registry.register(Registries.ITEM, id("raw_silkworm"), RAW_SILKWORM);
        Registry.register(Registries.ITEM, id("cooked_silkworm"), COOKED_SILKWORM);

        SEEDS.forEach((identifier, item) -> Registry.register(Registries.ITEM, identifier, item));
        MESHES.forEach((identifier, item) -> Registry.register(Registries.ITEM, identifier, item));
        PEBBLES.forEach((identifier, item) -> Registry.register(Registries.ITEM, identifier, item));
        DOLLS.forEach((identifier, item) -> Registry.register(Registries.ITEM, identifier, item));
        ORE_PIECES.forEach((identifier, item) -> Registry.register(Registries.ITEM, identifier, item));
        Registry.register(Registries.ITEM, id("end_cake"), new BlockItem(ModBlocks.END_CAKE, new Item.Settings().maxCount(1)));
        ModFluids.registerBuckets();
    }

}
