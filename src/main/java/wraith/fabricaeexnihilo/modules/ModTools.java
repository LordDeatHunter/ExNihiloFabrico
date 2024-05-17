package wraith.fabricaeexnihilo.modules;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.modules.materials.ModToolMaterials;
import wraith.fabricaeexnihilo.modules.tools.CrookItem;
import wraith.fabricaeexnihilo.modules.tools.HammerItem;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

// TODO: merge with ModItems (why is this a thing?)
public class ModTools {

    public static final Map<Identifier, Item> CROOKS = new HashMap<>();
    public static final Map<Identifier, Item> HAMMERS = new HashMap<>();

    static {
        for (var toolMaterial : ModToolMaterials.values()) {
            var crookId = id(toolMaterial.name().toLowerCase(Locale.ROOT) + "_crook");
            var crook = new CrookItem(toolMaterial, new Item.Settings());
            CROOKS.put(crookId, crook);
        }
        HAMMERS.put(id("wooden_hammer"), new HammerItem(ToolMaterials.WOOD, new Item.Settings()));
        HAMMERS.put(id("stone_hammer"), new HammerItem(ToolMaterials.STONE, new Item.Settings()));
        HAMMERS.put(id("iron_hammer"), new HammerItem(ToolMaterials.IRON, new Item.Settings()));
        HAMMERS.put(id("golden_hammer"), new HammerItem(ToolMaterials.GOLD, new Item.Settings()));
        HAMMERS.put(id("diamond_hammer"), new HammerItem(ToolMaterials.DIAMOND, new Item.Settings()));
        HAMMERS.put(id("netherite_hammer"), new HammerItem(ToolMaterials.NETHERITE, new Item.Settings()));
    }

    public static void registerItems() {
        CROOKS.forEach((identifier, item) -> Registry.register(Registries.ITEM, identifier, item));
        HAMMERS.forEach((identifier, item) -> Registry.register(Registries.ITEM, identifier, item));
    }

}
