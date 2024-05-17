package wraith.fabricaeexnihilo.datagen.provider.recipe;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import wraith.fabricaeexnihilo.datagen.builder.recipe.WitchWaterEntityRecipeJsonBuilder;
import wraith.fabricaeexnihilo.datagen.builder.recipe.WitchWaterWorldRecipeJsonBuilder;
import wraith.fabricaeexnihilo.modules.ModTags;

import java.util.concurrent.CompletableFuture;

public class WitchWaterRecipeProvider extends FabricRecipeProvider {
    public WitchWaterRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.SPIDER, EntityType.CAVE_SPIDER).offerTo(exporter, "entity/cave_spider");
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.SQUID, EntityType.GHAST).offerTo(exporter, "entity/ghast");
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.PUFFERFISH, EntityType.GUARDIAN).offerTo(exporter, "entity/guardian");
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.SLIME, EntityType.MAGMA_CUBE).offerTo(exporter, "entity/magma_cube");
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.COW, EntityType.MOOSHROOM).offerTo(exporter, "entity/mooshroom");
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.BAT, EntityType.PHANTOM).offerTo(exporter, "entity/phantom_from_bat");
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.PARROT, EntityType.PHANTOM).offerTo(exporter, "entity/phantom_from_parrot");
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.PIG, EntityType.PIGLIN).offerTo(exporter, "entity/piglin");
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.PANDA, EntityType.RAVAGER).offerTo(exporter, "entity/ravager_from_panda");
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.POLAR_BEAR, EntityType.RAVAGER).offerTo(exporter, "entity/ravager_from_polar_bear");
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.TURTLE, EntityType.SHULKER).offerTo(exporter, "entity/shulker");
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.SILVERFISH, EntityType.COD).offerTo(exporter, "entity/silverfish_from_cod");
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.SILVERFISH, EntityType.SALMON).offerTo(exporter, "entity/silverfish_from_salmon");
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.SILVERFISH, EntityType.TROPICAL_FISH).offerTo(exporter, "entity/silverfish_from_tropical_fish");
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.HORSE, EntityType.SKELETON_HORSE).offerTo(exporter, "entity/skeleton_horse");
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.CHICKEN, EntityType.VEX).offerTo(exporter, "entity/vex");
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.IRON_GOLEM, EntityType.WARDEN).offerTo(exporter, "entity/warden");
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.SKELETON, EntityType.WITHER_SKELETON).offerTo(exporter, "entity/wither_skeleton");
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.DONKEY, EntityType.ZOMBIE_HORSE).offerTo(exporter, "entity/zombie_horse_from_donkey");
        WitchWaterEntityRecipeJsonBuilder.of(EntityType.MULE, EntityType.ZOMBIE_HORSE).offerTo(exporter, "entity/zombie_horse_from_mule");

        offerVillagerRecipe(VillagerProfession.ARMORER, EntityType.PILLAGER, exporter);
        offerVillagerRecipe(VillagerProfession.BUTCHER, EntityType.VINDICATOR, exporter);
        offerVillagerRecipe(VillagerProfession.CARTOGRAPHER, EntityType.PILLAGER, exporter);
        offerVillagerRecipe(VillagerProfession.CLERIC, EntityType.EVOKER, exporter);
        offerVillagerRecipe(VillagerProfession.FARMER, EntityType.HUSK, exporter);
        offerVillagerRecipe(VillagerProfession.FISHERMAN, EntityType.DROWNED, exporter);
        offerVillagerRecipe(VillagerProfession.FLETCHER, EntityType.STRAY, exporter);
        offerVillagerRecipe(VillagerProfession.LEATHERWORKER, EntityType.PILLAGER, exporter);
        offerVillagerRecipe(VillagerProfession.LIBRARIAN, EntityType.ILLUSIONER, exporter);
        offerVillagerRecipe(VillagerProfession.MASON, EntityType.PILLAGER, exporter);
        offerVillagerRecipe(VillagerProfession.NITWIT, EntityType.ZOMBIE_VILLAGER, exporter);
        offerVillagerRecipe(VillagerProfession.NONE, EntityType.ZOMBIE_VILLAGER, exporter);
        offerVillagerRecipe(VillagerProfession.SHEPHERD, EntityType.PILLAGER, exporter);
        offerVillagerRecipe(VillagerProfession.TOOLSMITH, EntityType.PILLAGER, exporter);
        offerVillagerRecipe(VillagerProfession.WEAPONSMITH, EntityType.PILLAGER, exporter);

        new WitchWaterWorldRecipeJsonBuilder(ModTags.TRUE_LAVA)
                .result(Blocks.COBBLESTONE, 3)
                .result(Blocks.ANDESITE, 1)
                .result(Blocks.DIORITE, 1)
                .result(Blocks.GRANITE, 1)
                .offerTo(exporter, "world/lava");
        new WitchWaterWorldRecipeJsonBuilder(ModTags.TRUE_WATER)
                .result(Blocks.DIRT, 51)
                .result(Blocks.GRASS_BLOCK, 12)
                .result(Blocks.COARSE_DIRT, 12)
                .result(Blocks.MYCELIUM, 12)
                .result(Blocks.PODZOL, 12)
                .offerTo(exporter, "world/water");
        new WitchWaterWorldRecipeJsonBuilder(ModTags.Common.BLOOD)
                .result(Blocks.NETHERRACK, 1)
                .result(Blocks.RED_SAND, 16)
                .result(Blocks.FIRE_CORAL_BLOCK, 4)
                .offerTo(exporter, "world/blood");
        new WitchWaterWorldRecipeJsonBuilder(ModTags.Common.BRINE)
                .result(Blocks.DEAD_BRAIN_CORAL_BLOCK, 16)
                .result(Blocks.DEAD_TUBE_CORAL_BLOCK, 16)
                .result(Blocks.DEAD_FIRE_CORAL_BLOCK, 16)
                .result(Blocks.DEAD_HORN_CORAL_BLOCK, 16)
                .result(Blocks.DEAD_BUBBLE_CORAL_BLOCK, 16)
                .result(Blocks.BRAIN_CORAL_BLOCK, 16)
                .result(Blocks.TUBE_CORAL_BLOCK, 16)
                .result(Blocks.FIRE_CORAL_BLOCK, 16)
                .result(Blocks.HORN_CORAL_BLOCK, 16)
                .result(Blocks.BUBBLE_CORAL_BLOCK, 16)
                .result(Blocks.PRISMARINE, 1)
                .offerTo(exporter, "world/brine");
    }

    private void offerVillagerRecipe(VillagerProfession profession, EntityType<?> result, RecipeExporter exporter) {
        WitchWaterEntityRecipeJsonBuilder.villager(profession, result).offerTo(exporter, "entity/villager_" + profession.id());
    }

    @Override
    public String getName() {
        return "Witchwater Recipes";
    }

    @Override
    protected Identifier getRecipeIdentifier(Identifier identifier) {
        return super.getRecipeIdentifier(identifier).withPrefixedPath("witch_water_");
    }
}
