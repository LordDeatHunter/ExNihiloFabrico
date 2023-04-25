package wraith.fabricaeexnihilo.modules;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class ModTags {
    public static final TagKey<Item> CROOKS = TagKey.of(RegistryKeys.ITEM, id("crooks"));
    public static final TagKey<Item> HAMMERS = TagKey.of(RegistryKeys.ITEM, id("hammers"));
    public static final TagKey<Item> BARRELS = TagKey.of(RegistryKeys.ITEM, id("barrels"));
    public static final TagKey<Block> HAMMERABLES = TagKey.of(RegistryKeys.BLOCK, id("hammerables"));
    public static final TagKey<Block> CROOKABLES = TagKey.of(RegistryKeys.BLOCK, id("crookables"));
    public static final TagKey<Block> INFESTED_LEAVES = TagKey.of(RegistryKeys.BLOCK, id("infested_leaves"));
    public static final TagKey<Fluid> HOT = TagKey.of(RegistryKeys.FLUID, id("hot"));
    public static final TagKey<Fluid> WITCHWATER = TagKey.of(RegistryKeys.FLUID, id("witchwater"));
    public static final TagKey<Fluid> TRUE_LAVA = TagKey.of(RegistryKeys.FLUID, id("true_lava"));
    public static final TagKey<Fluid> TRUE_WATER = TagKey.of(RegistryKeys.FLUID, id("true_water"));

    private ModTags() {
    }

    public static final class Common {
        public static final TagKey<Block> CONCRETES = TagKey.of(RegistryKeys.BLOCK, new Identifier("c", "concretes"));
        public static final TagKey<Block> CONCRETE_POWDERS = TagKey.of(RegistryKeys.BLOCK, new Identifier("c", "concrete_powders"));
        public static final TagKey<Block> TORCHES = TagKey.of(RegistryKeys.BLOCK, new Identifier("c", "torches"));
        public static final TagKey<Item> VEGETABLES = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "vegetables"));
        public static final TagKey<Item> SEEDS = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "seeds"));
        public static final TagKey<Item> RAW_MEAT = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "raw_meat"));
        public static final TagKey<Item> COOKED_MEAT = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "cooked_meat"));
        public static final TagKey<Fluid> BRINE = TagKey.of(RegistryKeys.FLUID, new Identifier("c", "brine"));
        public static final TagKey<Fluid> BLOOD = TagKey.of(RegistryKeys.FLUID, new Identifier("c", "blood"));

        private Common() {
        }
    }
}
