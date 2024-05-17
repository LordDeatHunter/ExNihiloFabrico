package wraith.fabricaeexnihilo.compatibility.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.DefaultApiModule;
import wraith.fabricaeexnihilo.compatibility.emi.recipes.*;
import wraith.fabricaeexnihilo.compatibility.recipeviewer.SieveRecipeCombiner;
import wraith.fabricaeexnihilo.modules.ModTags;
import wraith.fabricaeexnihilo.modules.ModTools;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;
import wraith.fabricaeexnihilo.recipe.ModRecipes;

import java.util.function.Function;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class FENEmiPlugin implements EmiPlugin {
    private static final Identifier TREE_ICONS = id("textures/gui/emi/tree_icons.png");

    public static final EmiRecipeCategory CROOKING_CATEGORY = new EmiRecipeCategory(id("crooking"), EmiStack.of(ModTools.CROOKS.get(id("wooden_crook"))), new EmiTexture(TREE_ICONS, 144, 0, 16, 16));
    public static final EmiRecipeCategory HAMMERING_CATEGORY = new EmiRecipeCategory(id("hammering"), EmiStack.of(ModTools.HAMMERS.get(id("iron_hammer"))), new EmiTexture(TREE_ICONS, 128, 0, 16, 16));
    public static final EmiRecipeCategory SIEVE_CATEGORY = new EmiRecipeCategory(id("sieve"), EmiStack.of(DefaultApiModule.INSTANCE.oakBlocks.sieve()), new EmiTexture(TREE_ICONS, 112, 0, 16, 16));
    public static final EmiRecipeCategory WITCH_WATER_WORLD_CATEGORY = new EmiRecipeCategory(id("witch_water_world"), EmiStack.of(WitchWaterFluid.STILL), new EmiTexture(TREE_ICONS, 96, 0, 16, 16));
    public static final EmiRecipeCategory WITCH_WATER_ENTITY_CATEGORY = new EmiRecipeCategory(id("witch_water_entity"), EmiStack.of(WitchWaterFluid.STILL), new EmiTexture(TREE_ICONS, 80, 0, 16, 16));
    public static final EmiRecipeCategory CRUCIBLE_HEAT_CATEGORY = new EmiRecipeCategory(id("crucible_heat"), EmiStack.of(DefaultApiModule.INSTANCE.porcelainCrucible), new EmiTexture(TREE_ICONS, 64, 0, 16, 16));
    public static final EmiRecipeCategory FIREPROOF_CRUCIBLE = new EmiRecipeCategory(id("fireproof_crucible"), EmiStack.of(DefaultApiModule.INSTANCE.porcelainCrucible), new EmiTexture(TREE_ICONS, 48, 0, 16, 16));
    public static final EmiRecipeCategory WOODEN_CRUCIBLE_CATEGORY = new EmiRecipeCategory(id("wooden_crucible"), EmiStack.of(DefaultApiModule.INSTANCE.oakBlocks.crucible()), new EmiTexture(TREE_ICONS, 32, 0, 16, 16));
    public static final EmiRecipeCategory MILKING_CATEGORY = new EmiRecipeCategory(id("barrel/milking"), EmiStack.of(DefaultApiModule.INSTANCE.oakBlocks.barrel()), new EmiTexture(TREE_ICONS, 16, 0, 16, 16));
    public static final EmiRecipeCategory BARREL_CATEGORY = new EmiRecipeCategory(id("barrel"), EmiStack.of(DefaultApiModule.INSTANCE.oakBlocks.barrel()), new EmiTexture(TREE_ICONS, 0, 0, 16, 16));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(CROOKING_CATEGORY);
        registry.addWorkstation(CROOKING_CATEGORY, EmiIngredient.of(ModTags.CROOKS));
        registry.addCategory(HAMMERING_CATEGORY);
        registry.addWorkstation(HAMMERING_CATEGORY, EmiIngredient.of(ModTags.HAMMERS));
        registry.addCategory(SIEVE_CATEGORY);
        registry.addWorkstation(SIEVE_CATEGORY, EmiIngredient.of(ModTags.SIEVES));
        registry.addCategory(WITCH_WATER_WORLD_CATEGORY);
        registry.addWorkstation(WITCH_WATER_WORLD_CATEGORY, EmiIngredient.of(WitchWaterFluid.TAG));
        registry.addCategory(WITCH_WATER_ENTITY_CATEGORY);
        registry.addWorkstation(WITCH_WATER_ENTITY_CATEGORY, EmiIngredient.of(WitchWaterFluid.TAG));
        registry.addCategory(FIREPROOF_CRUCIBLE);
        registry.addWorkstation(FIREPROOF_CRUCIBLE, EmiStack.of(DefaultApiModule.INSTANCE.porcelainCrucible));
        registry.addCategory(CRUCIBLE_HEAT_CATEGORY);
        registry.addWorkstation(CRUCIBLE_HEAT_CATEGORY, EmiIngredient.of(ModTags.CRUCIBLES));
        registry.addCategory(WOODEN_CRUCIBLE_CATEGORY);
        registry.addWorkstation(WOODEN_CRUCIBLE_CATEGORY, EmiIngredient.of(ModTags.CRUCIBLES));
        registry.addCategory(MILKING_CATEGORY);
        registry.addWorkstation(MILKING_CATEGORY, EmiIngredient.of(ModTags.BARRELS));
        registry.addCategory(BARREL_CATEGORY);
        registry.addWorkstation(BARREL_CATEGORY, EmiIngredient.of(ModTags.BARRELS));

        addRecipes(registry, ModRecipes.CROOK, EmiToolRecipe::new);
        addRecipes(registry, ModRecipes.HAMMER, EmiToolRecipe::new);
        addRecipes(registry, ModRecipes.WITCH_WATER_WORLD, EmiWitchWaterWorldRecipe::new);
        addRecipes(registry, ModRecipes.WITCH_WATER_ENTITY, EmiWitchWaterEntityRecipe::new);
        addRecipes(registry, ModRecipes.CRUCIBLE, EmiCrucibleRecipe::new);
        addRecipes(registry, ModRecipes.CRUCIBLE_HEAT, EmiCrucibleHeatRecipe::new);
        addRecipes(registry, ModRecipes.MILKING, EmiMilkingRecipe::new);
        addRecipes(registry, ModRecipes.BARREL, EmiBarrelRecipe::new);
        SieveRecipeCombiner.combineRecipes(registry.getRecipeManager(), 3 * 9, (key, outputs) -> registry.addRecipe(new EmiSieveRecipe(key, outputs)));
    }

    private <C extends Inventory, T extends Recipe<C>> void addRecipes(EmiRegistry registry, RecipeType<T> recipe, Function<RecipeEntry<T>, ? extends EmiRecipe> function) {
        registry.getRecipeManager()
                .listAllOfType(recipe)
                .stream()
                .map(function)
                .forEach(registry::addRecipe);
    }
}
