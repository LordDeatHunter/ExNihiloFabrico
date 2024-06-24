package wraith.fabricaeexnihilo.modules;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public final class ModTags {
    public static final TagKey<Item> BARRELS = TagKey.of(RegistryKeys.ITEM, id("barrels"));
    public static final TagKey<Item> SIEVES = TagKey.of(RegistryKeys.ITEM, id("sieves"));
    public static final TagKey<Item> CRUCIBLES = TagKey.of(RegistryKeys.ITEM, id("crucibles"));
    public static final TagKey<Item> CROOKS = TagKey.of(RegistryKeys.ITEM, id("crooks"));
    public static final TagKey<Item> HAMMERS = TagKey.of(RegistryKeys.ITEM, id("hammers"));
    public static final TagKey<Block> CROOKABLES = TagKey.of(RegistryKeys.BLOCK, id("crookables"));
    public static final TagKey<Block> HAMMERABLES = TagKey.of(RegistryKeys.BLOCK, id("hammerables"));
    public static final TagKey<Block> INFESTED_LEAVES = TagKey.of(RegistryKeys.BLOCK, id("infested_leaves"));
    public static final TagKey<Fluid> HOT = TagKey.of(RegistryKeys.FLUID, id("hot"));
    public static final TagKey<Fluid> TRUE_LAVA = TagKey.of(RegistryKeys.FLUID, id("true_lava"));
    public static final TagKey<Fluid> TRUE_WATER = TagKey.of(RegistryKeys.FLUID, id("true_water"));
    public static final TagKey<Fluid> WITCHWATER = TagKey.of(RegistryKeys.FLUID, id("witchwater"));
    public static final TagKey<Fluid> WATER_LIKE = TagKey.of(RegistryKeys.FLUID, id("water_like"));

    private ModTags() {
    }

    public static final class Common {
        public static final TagKey<Fluid> BLOOD = TagKey.of(RegistryKeys.FLUID, Identifier.of("c", "blood"));
        public static final TagKey<Fluid> BRINE = TagKey.of(RegistryKeys.FLUID, Identifier.of("c", "brine"));
        public static final TagKey<Block> CONCRETES = TagKey.of(RegistryKeys.BLOCK, Identifier.of("c", "concretes"));
        public static final TagKey<Block> CONCRETE_POWDERS = TagKey.of(RegistryKeys.BLOCK, Identifier.of("c", "concrete_powders"));
        public static final TagKey<Block> TORCHES = TagKey.of(RegistryKeys.BLOCK, Identifier.of("c", "torches"));
        public static final TagKey<Item> COOKED_MEAT = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "cooked_meat"));
        public static final TagKey<Item> RAW_MEAT = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "raw_meat"));
        public static final TagKey<Item> SEEDS = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "seeds"));
        public static final TagKey<Item> VEGETABLES = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "vegetables"));
        public static final TagKey<Item> PORCELAIN = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "porcelain"));
        public static final TagKey<Item> SALT = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "salt"));

        private Common() {
        }
    }
}
