package wraith.fabricaeexnihilo;

import io.github.mattidragon.configloader.api.ConfigManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    @Override
    public void onInitialize() {
        EntrypointHelper.callEntrypoints();

        ModLootContextTypes.register();
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
