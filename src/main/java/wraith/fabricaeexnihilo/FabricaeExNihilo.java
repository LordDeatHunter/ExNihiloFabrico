package wraith.fabricaeexnihilo;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mattidragon.configloader.api.ConfigManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.fabric.impl.resource.conditions.DefaultResourceConditionTypes;
import net.fabricmc.fabric.impl.resource.conditions.ResourceConditionsImpl;
import net.fabricmc.fabric.impl.resource.conditions.conditions.RegistryContainsResourceCondition;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.config.FabricaeExNihiloConfig;
import wraith.fabricaeexnihilo.loot.CopyEnchantmentsLootFunction;
import wraith.fabricaeexnihilo.modules.*;
import wraith.fabricaeexnihilo.modules.fluids.BloodFluid;
import wraith.fabricaeexnihilo.modules.fluids.BrineFluid;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.util.BonusEnchantingManager;
import wraith.fabricaeexnihilo.util.EntrypointHelper;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.Arrays;
import java.util.List;

public class FabricaeExNihilo implements ModInitializer {
    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> ItemUtils.getExNihiloItemStack("wooden_crook"))
            .displayName(Text.translatable("itemGroup.fabricaeexnihilo.general"))
            .entries((context, entries) -> {
                ModTools.CROOKS.values().stream().map(ItemStack::new).forEach(entries::add);
                ModTools.HAMMERS.values().stream().map(ItemStack::new).forEach(entries::add);
                ModBlocks.BARRELS.values().stream().map(ItemStack::new).forEach(entries::add);
                ModBlocks.CRUCIBLES.values().stream().map(ItemStack::new).forEach(entries::add);
                ModBlocks.CRUSHED.values().stream().map(ItemStack::new).forEach(entries::add);
                ModBlocks.INFESTED_LEAVES.values().stream().map(ItemStack::new).forEach(entries::add);
                ModBlocks.SIEVES.values().stream().map(ItemStack::new).forEach(entries::add);
                ModBlocks.STRAINERS.values().stream().map(ItemStack::new).forEach(entries::add);
                ModItems.DOLLS.values().stream().map(ItemStack::new).forEach(entries::add);
                ModItems.MESHES.values().stream().map(ItemStack::new).forEach(entries::add);
                ModItems.ORE_PIECES.values().stream().map(ItemStack::new).forEach(entries::add);
                ModItems.PEBBLES.values().stream().map(ItemStack::new).forEach(entries::add);
                ModItems.SEEDS.values().stream().map(ItemStack::new).forEach(entries::add);

                entries.add(new ItemStack(ModItems.COOKED_SILKWORM));
                entries.add(new ItemStack(ModItems.RAW_SILKWORM));
                entries.add(new ItemStack(ModItems.PORCELAIN));
                entries.add(new ItemStack(ModItems.UNFIRED_PORCELAIN_CRUCIBLE));
                entries.add(new ItemStack(ModItems.SALT_BOTTLE));
                entries.add(new ItemStack(ModBlocks.END_CAKE));
                entries.add(new ItemStack(WitchWaterFluid.BUCKET));
                entries.add(new ItemStack(BrineFluid.BUCKET));
                entries.add(new ItemStack(BloodFluid.BUCKET));
            })
            .build();
    public static final Logger LOGGER = LogManager.getLogger("Fabricae Ex Nihilo");
    public static final ConfigManager<wraith.fabricaeexnihilo.config.FabricaeExNihiloConfig> CONFIG = ConfigManager.create(wraith.fabricaeexnihilo.config.FabricaeExNihiloConfig.CODEC, FabricaeExNihiloConfig.DEFAULT, "fabricaeexnihilo");

    public static Identifier id(String path) {
        return new Identifier("fabricaeexnihilo", path);
    }

    public record RegistryContainsItemsCondition(List<Identifier> entries) implements ResourceCondition {
        public static final MapCodec<RegistryContainsItemsCondition> CODEC = Identifier.CODEC.listOf().fieldOf("values").xmap(RegistryContainsItemsCondition::new, RegistryContainsItemsCondition::entries);

        public RegistryContainsItemsCondition(Identifier... entries) {
            this(List.of(entries));
        }

        @SafeVarargs
        public RegistryContainsItemsCondition(RegistryKey<Item>... entries) {
            this(Arrays.stream(entries).map(RegistryKey::getValue).toList());
        }

        public static final ResourceConditionType<RegistryContainsItemsCondition> TYPE = ResourceConditionType.create(id("all_items_present"), CODEC);

        @Override
        public ResourceConditionType<?> getType() {
            return TYPE;
        }

        @SuppressWarnings("UnstableApiUsage")
        @Override
        public boolean test(@Nullable RegistryWrapper.WrapperLookup registryLookup) {
            return ResourceConditionsImpl.registryContains(registryLookup, RegistryKeys.ITEM.getValue(), this.entries());
        }
    }

    public record RegistryContainsBlocksCondition(List<Identifier> entries) implements ResourceCondition {
        public static final MapCodec<RegistryContainsBlocksCondition> CODEC = Identifier.CODEC.listOf().fieldOf("values").xmap(RegistryContainsBlocksCondition::new, RegistryContainsBlocksCondition::entries);

        public RegistryContainsBlocksCondition(Identifier... entries) {
            this(List.of(entries));
        }

        @SafeVarargs
        public RegistryContainsBlocksCondition(RegistryKey<Block>... entries) {
            this(Arrays.stream(entries).map(RegistryKey::getValue).toList());
        }

        public static final ResourceConditionType<RegistryContainsBlocksCondition> TYPE = ResourceConditionType.create(id("all_blocks_present"), CODEC);

        @Override
        public ResourceConditionType<?> getType() {
            return TYPE;
        }

        @SuppressWarnings("UnstableApiUsage")
        @Override
        public boolean test(@Nullable RegistryWrapper.WrapperLookup registryLookup) {
            return ResourceConditionsImpl.registryContains(registryLookup, RegistryKeys.BLOCK.getValue(), this.entries());
        }
    }

    @Override
    public void onInitialize() {
        EntrypointHelper.callEntrypoints();

        ModLootContextTypes.register();
        ResourceConditions.register(RegistryContainsItemsCondition.TYPE);
        ResourceConditions.register(RegistryContainsBlocksCondition.TYPE);
        Registry.register(Registries.LOOT_FUNCTION_TYPE, id("copy_enchantments"), CopyEnchantmentsLootFunction.TYPE);
        Registry.register(Registries.ITEM_GROUP, id("general"), ITEM_GROUP);

        LOGGER.debug("Registering Status Effects");
        ModEffects.init();

        LOGGER.debug("Registering Fluids");
        ModFluids.registerFluids();

        LOGGER.debug("Registering Blocks");
        ModBlocks.registerBlocks();

        LOGGER.debug("Registering Items");
        ModBlocks.registerBlockItems();
        ModItems.registerItems();
        ModTools.registerItems();

        LOGGER.debug("Registering Block Entities");
        ModBlocks.registerBlockEntities();

        LOGGER.debug("Creating Tags");
        BonusEnchantingManager.generateDefaultTags();
        LOGGER.debug("Creating Recipes");
        ModRecipes.register();
    }

}
