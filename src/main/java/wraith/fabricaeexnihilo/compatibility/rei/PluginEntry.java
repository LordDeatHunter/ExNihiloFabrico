package wraith.fabricaeexnihilo.compatibility.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.type.EntryTypeRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.compatibility.recipeviewer.SieveRecipeCombiner;
import wraith.fabricaeexnihilo.compatibility.rei.barrel.BarrelCategory;
import wraith.fabricaeexnihilo.compatibility.rei.barrel.BarrelDisplay;
import wraith.fabricaeexnihilo.compatibility.rei.barrel.MilkingCategory;
import wraith.fabricaeexnihilo.compatibility.rei.barrel.MilkingDisplay;
import wraith.fabricaeexnihilo.compatibility.rei.crucible.CrucibleCategory;
import wraith.fabricaeexnihilo.compatibility.rei.crucible.CrucibleDisplay;
import wraith.fabricaeexnihilo.compatibility.rei.crucible.CrucibleHeatCategory;
import wraith.fabricaeexnihilo.compatibility.rei.crucible.CrucibleHeatDisplay;
import wraith.fabricaeexnihilo.compatibility.rei.sieve.SieveCategory;
import wraith.fabricaeexnihilo.compatibility.rei.sieve.SieveDisplay;
import wraith.fabricaeexnihilo.compatibility.rei.tools.ToolCategory;
import wraith.fabricaeexnihilo.compatibility.rei.tools.ToolDisplay;
import wraith.fabricaeexnihilo.compatibility.rei.witchwater.WitchWaterEntityCategory;
import wraith.fabricaeexnihilo.compatibility.rei.witchwater.WitchWaterEntityDisplay;
import wraith.fabricaeexnihilo.compatibility.rei.witchwater.WitchWaterWorldCategory;
import wraith.fabricaeexnihilo.compatibility.rei.witchwater.WitchWaterWorldDisplay;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModTools;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.ToolRecipe;
import wraith.fabricaeexnihilo.recipe.barrel.BarrelRecipe;
import wraith.fabricaeexnihilo.recipe.barrel.MilkingRecipe;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleHeatRecipe;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleRecipe;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterEntityRecipe;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterWorldRecipe;
import wraith.fabricaeexnihilo.util.ItemUtils;
import wraith.fabricaeexnihilo.util.Lazy;

import java.util.Objects;
import java.util.function.Predicate;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class PluginEntry implements REIClientPlugin {
    public static final Lazy<EntryIngredient> BARRELS = new Lazy<>(() -> EntryIngredient.of(ModBlocks.BARRELS.values().stream().map(EntryStacks::of).toList()));

    public static final CategoryIdentifier<BarrelDisplay> BARREL = CategoryIdentifier.of(id("rei/barrel/crafting"));
    public static final CategoryIdentifier<ToolDisplay> CROOK = CategoryIdentifier.of(id("rei/tools/crook"));
    public static final CategoryIdentifier<ToolDisplay> CRUSHING = CategoryIdentifier.of(id("rei/tools/hammer"));
    public static final CategoryIdentifier<CrucibleDisplay> FIREPROOF_CRUCIBLE = CategoryIdentifier.of(id("rei/crucible/stone"));
    public static final CategoryIdentifier<CrucibleHeatDisplay> HEATING = CategoryIdentifier.of(id("rei/crucible/heat"));
    public static final CategoryIdentifier<MilkingDisplay> MILKING = CategoryIdentifier.of(id("rei/barrel/milking"));
    public static final CategoryIdentifier<SieveDisplay> SIFTING = CategoryIdentifier.of(id("rei/sieve"));
    public static final CategoryIdentifier<WitchWaterEntityDisplay> WITCH_WATER_ENTITY = CategoryIdentifier.of(id("rei/witchwater/entity"));
    public static final CategoryIdentifier<WitchWaterWorldDisplay> WITCH_WATER_WORLD = CategoryIdentifier.of(id("rei/witchwater/world"));
    public static final CategoryIdentifier<CrucibleDisplay> WOOD_CRUCIBLE = CategoryIdentifier.of(id("rei/crucible/wood"));

    @Override
    public void registerCategories(CategoryRegistry registry) {
        FabricaeExNihilo.LOGGER.info("Registering REI Categories");
        registry.add(new BarrelCategory());
        registry.add(new ToolCategory(CRUSHING, ItemUtils.getExNihiloItemStack("iron_hammer"), "fabricaeexnihilo.rei.category.hammer"));
        registry.add(new ToolCategory(CROOK, ItemUtils.getExNihiloItemStack("wooden_crook"), "fabricaeexnihilo.rei.category.crook"));
        registry.add(new CrucibleCategory(WOOD_CRUCIBLE, ItemUtils.getExNihiloItemStack("oak_crucible"), "fabricaeexnihilo.rei.category.wood_crucible"));
        registry.add(new CrucibleCategory(FIREPROOF_CRUCIBLE, ItemUtils.getExNihiloItemStack("porcelain_crucible"), "fabricaeexnihilo.rei.category.porcelain_crucible"));
        registry.add(new CrucibleHeatCategory(ItemUtils.getExNihiloItemStack("porcelain_crucible"), "fabricaeexnihilo.rei.category.crucible_heat"));
        registry.add(new SieveCategory(ItemUtils.getExNihiloItemStack("oak_sieve"), "fabricaeexnihilo.rei.category.sieve"));
        registry.add(new MilkingCategory(ItemUtils.getExNihiloItemStack("oak_barrel"), "fabricaeexnihilo.rei.category.barrel.milking"));
        registry.add(new WitchWaterEntityCategory());
        registry.add(new WitchWaterWorldCategory());
        ModTools.HAMMERS.values().forEach(hammer -> registry.addWorkstations(CRUSHING, EntryStacks.of(hammer)));
        ModTools.CROOKS.values().forEach(crook -> registry.addWorkstations(CROOK, EntryStacks.of(crook)));
        ModBlocks.CRUCIBLES.values().forEach(crucible -> {
            registry.addWorkstations(HEATING, EntryStacks.of(crucible));
            if (crucible.isFireproof()) {
                registry.addWorkstations(FIREPROOF_CRUCIBLE, EntryStacks.of(crucible));
            }
            registry.addWorkstations(WOOD_CRUCIBLE, EntryStacks.of(crucible));
        });
        ModBlocks.SIEVES.values().forEach(sieve -> registry.addWorkstations(SIFTING, EntryStacks.of(sieve)));
        ModBlocks.BARRELS.values().forEach(barrel -> {
            registry.addWorkstations(BARREL, EntryStacks.of(barrel));
            registry.addWorkstations(MILKING, EntryStacks.of(barrel));
        });
        registry.addWorkstations(WITCH_WATER_ENTITY, EntryStacks.of(WitchWaterFluid.BUCKET));
        registry.addWorkstations(WITCH_WATER_WORLD, EntryStacks.of(WitchWaterFluid.BUCKET));

//TODO: See if this is a viable solution
//        registry.removePlusButton(SIEVE);
//        registry.removePlusButton(CROOK);
//        registry.removePlusButton(HAMMER);
//        registry.removePlusButton(CRUCIBLE_HEAT);
//        registry.removePlusButton(WOOD_CRUCIBLE);
//        registry.removePlusButton(PORCELAIN_CRUCIBLE);
//        registry.removePlusButton(COMPOSTING);
//        registry.removePlusButton(LEAKING);
//        registry.removePlusButton(ON_TOP);
//        registry.removePlusButton(MILKING);
//        registry.removePlusButton(TRANSFORMING);
//        registry.removePlusButton(WITCH_WATER_ENTITY);
//        registry.removePlusButton(WITCH_WATER_WORLD);
//        registry.removePlusButton(ALCHEMY);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        FabricaeExNihilo.LOGGER.debug("Registering REI Displays");
        registry.registerRecipeFiller(ToolRecipe.class, ModRecipes.HAMMER, recipe -> new ToolDisplay(recipe, CRUSHING));
        registry.registerRecipeFiller(ToolRecipe.class, ModRecipes.CROOK, recipe -> new ToolDisplay(recipe, CROOK));
        registry.registerRecipeFiller(
                CrucibleRecipe.class,
                type -> Objects.equals(ModRecipes.CRUCIBLE, type),
                CrucibleRecipe::requiresFireproofCrucible,
                recipe -> new CrucibleDisplay(recipe, FIREPROOF_CRUCIBLE)
        );
        registry.registerRecipeFiller(CrucibleRecipe.class,
                type -> Objects.equals(ModRecipes.CRUCIBLE, type),
                Predicate.not(CrucibleRecipe::requiresFireproofCrucible),
                recipe -> new CrucibleDisplay(recipe, WOOD_CRUCIBLE));
        registry.registerRecipeFiller(CrucibleHeatRecipe.class, ModRecipes.CRUCIBLE_HEAT, CrucibleHeatDisplay::new);
        SieveRecipeCombiner.combineRecipes(registry.getRecipeManager(), SieveCategory.MAX_OUTPUTS, (key, outputs) -> registry.add(new SieveDisplay(key, outputs)));
        registry.registerRecipeFiller(BarrelRecipe.class, ModRecipes.BARREL, BarrelDisplay::new);
        registry.registerRecipeFiller(MilkingRecipe.class, ModRecipes.MILKING, MilkingDisplay::new);
        registry.registerRecipeFiller(WitchWaterEntityRecipe.class, ModRecipes.WITCH_WATER_ENTITY, WitchWaterEntityDisplay::new);
        //TODO: Merge recipes with same input, check sieve recipes
        registry.registerRecipeFiller(WitchWaterWorldRecipe.class, ModRecipes.WITCH_WATER_WORLD, WitchWaterWorldDisplay::new);
    }

    @Override
    public void registerEntryTypes(EntryTypeRegistry registry) {
        registry.register(FireEntryDefinition.TYPE, new FireEntryDefinition());
    }
}
