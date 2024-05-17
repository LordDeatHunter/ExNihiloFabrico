package wraith.fabricaeexnihilo.datagen.provider.recipe;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalFluidTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.DefaultApiModule;
import wraith.fabricaeexnihilo.datagen.builder.recipe.BarrelRecipeJsonBuilder;
import wraith.fabricaeexnihilo.datagen.builder.recipe.MilkingRecipeJsonBuilder;
import wraith.fabricaeexnihilo.modules.ModItems;
import wraith.fabricaeexnihilo.modules.ModTags;
import wraith.fabricaeexnihilo.modules.fluids.BrineFluid;
import wraith.fabricaeexnihilo.modules.fluids.MilkFluid;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;

import java.util.concurrent.CompletableFuture;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class BarrelRecipeProvider extends FabricRecipeProvider {
    public BarrelRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        new MilkingRecipeJsonBuilder(EntityType.COW, FluidVariant.of(MilkFluid.STILL)).offerTo(exporter, "milking/cow");
        new MilkingRecipeJsonBuilder(EntityType.WITCH, FluidVariant.of(WitchWaterFluid.STILL)).offerTo(exporter, "milking/witch");

        offerCompostingRecipes(exporter);
        offerLeakingRecipes(exporter);
        offerFluidTransformationRecipes(exporter);
        offerFluidCombinationRecipes(exporter);
        offerMiscAlchemyRecipes(exporter);
        offerRedSandstoneRecipes(exporter);
        offerFluidRecipes(exporter);
        offerCoralRecipes(exporter);
        offerSummoningRecipes(exporter);
    }

    @Override
    public String getName() {
        return "Barrel Recipes";
    }

    private void offerCompostingRecipes(RecipeExporter exporter) {
        BarrelRecipeJsonBuilder.itemTriggered(Items.CACTUS) // 0x00aa00
                .instant()
                .fillCompost(Items.DIRT, 0.0625f)
                .offerTo(exporter, "compost/cactus");
        BarrelRecipeJsonBuilder.itemTriggered(ModTags.Common.COOKED_MEAT) // 0xff5555
                .instant()
                .fillCompost(Items.DIRT, 0.25f)
                .offerTo(exporter, "compost/cooked_meat");
        BarrelRecipeJsonBuilder.itemTriggered(ConventionalItemTags.DYES) // 0xff5555
                .instant()
                .fillCompost(Items.DIRT, 0.125f)
                .offerTo(exporter, "compost/dyes");
        BarrelRecipeJsonBuilder.itemTriggered(ItemTags.LEAVES) // 0x00aa00
                .instant()
                .fillCompost(Items.DIRT, 0.125f)
                .offerTo(exporter, "compost/leaves");
        BarrelRecipeJsonBuilder.itemTriggered(Items.POPPED_CHORUS_FRUIT) // 0xff55ff
                .instant()
                .fillCompost(Items.END_STONE, 0.25f)
                .offerTo(exporter, "compost/popped_chorus_fruit");
        BarrelRecipeJsonBuilder.itemTriggered(ModTags.Common.RAW_MEAT) // 0xff5555
                .instant()
                .fillCompost(Items.DIRT, 0.125f)
                .offerTo(exporter, "compost/raw_meat");
        BarrelRecipeJsonBuilder.itemTriggered(Items.CHORUS_FLOWER) // 0xaa00aa
                .instant()
                .fillCompost(Items.END_STONE, 0.25f)
                .offerTo(exporter, "compost/chorus_flower");
        BarrelRecipeJsonBuilder.itemTriggered(Items.CHORUS_FRUIT) // 0xff55ff
                .instant()
                .fillCompost(Items.END_STONE, 0.0625f)
                .offerTo(exporter, "compost/chorus_fruit");
        BarrelRecipeJsonBuilder.itemTriggered(Items.COBWEB) // 0xffffff
                .instant()
                .fillCompost(Items.WHITE_WOOL, 0.5f)
                .offerTo(exporter, "compost/cobweb");
        BarrelRecipeJsonBuilder.itemTriggered(ItemTags.SAPLINGS) // 0x00ff00
                .instant()
                .fillCompost(Items.DIRT, 0.0625f)
                .offerTo(exporter, "compost/saplings");
        BarrelRecipeJsonBuilder.itemTriggered(ModTags.Common.SEEDS) // 0x55ff55z
                .instant()
                .fillCompost(Items.DIRT, 0.0625f)
                .offerTo(exporter, "compost/seeds");
        BarrelRecipeJsonBuilder.itemTriggered(ItemTags.SMALL_FLOWERS) // 0xff5555
                .instant()
                .fillCompost(Items.DIRT, 0.0625f)
                .offerTo(exporter, "compost/small_flowers");
        BarrelRecipeJsonBuilder.itemTriggered(ModTags.Common.VEGETABLES) // 0xffff55
                .instant()
                .fillCompost(Items.DIRT, 0.0625f)
                .offerTo(exporter, "compost/vegetables");
    }

    private void offerLeakingRecipes(RecipeExporter exporter) {
        float chance = 0.1f;
        BarrelRecipeJsonBuilder.tickTriggered(chance)
                .instant()
                .consumeFluid(WitchWaterFluid.TAG, FluidConstants.BUCKET / 10)
                .convertBlock(Blocks.GRAVEL, DefaultApiModule.INSTANCE.crushedNetherrack)
                .offerTo(exporter, "leaking/crushed_netherrack");
        BarrelRecipeJsonBuilder.tickTriggered(chance)
                .instant()
                .consumeFluid(ModTags.Common.BLOOD, FluidConstants.BUCKET / 20)
                .convertBlock(Blocks.GRAVEL, DefaultApiModule.INSTANCE.crushedNetherrack)
                .offerTo(exporter, "leaking/crushed_netherrack_with_blood");
        BarrelRecipeJsonBuilder.tickTriggered(chance)
                .instant()
                .consumeFluid(Fluids.WATER, FluidConstants.BUCKET / 10)
                .convertBlock(Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE)
                .offerTo(exporter, "leaking/mossy_cobblestone");
        BarrelRecipeJsonBuilder.tickTriggered(chance)
                .instant()
                .consumeFluid(Fluids.WATER, FluidConstants.BUCKET / 10)
                .convertBlock(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS)
                .offerTo(exporter, "leaking/mossy_stone_bricks");
        BarrelRecipeJsonBuilder.tickTriggered(chance)
                .instant()
                .consumeFluid(WitchWaterFluid.TAG, FluidConstants.BUCKET / 2)
                .convertBlock(BlockIngredient.tag(BlockTags.SMALL_FLOWERS), Blocks.BROWN_MUSHROOM.getDefaultState())
                .offerTo(exporter, "leaking/mushroom");
        BarrelRecipeJsonBuilder.tickTriggered(chance)
                .instant()
                .consumeFluid(WitchWaterFluid.TAG, FluidConstants.BUCKET / 2)
                .convertBlock(Blocks.PODZOL, Blocks.MYCELIUM)
                .offerTo(exporter, "leaking/mycelium");
        BarrelRecipeJsonBuilder.tickTriggered(chance)
                .instant()
                .consumeFluid(ModTags.Common.BLOOD, FluidConstants.BUCKET / 10)
                .convertBlock(Blocks.COBBLESTONE, Blocks.NETHERRACK)
                .offerTo(exporter, "leaking/netherrack");
        BarrelRecipeJsonBuilder.tickTriggered(chance)
                .instant()
                .consumeFluid(WitchWaterFluid.TAG, FluidConstants.BUCKET / 2)
                .convertBlock(Blocks.SAND, Blocks.SOUL_SAND)
                .offerTo(exporter, "leaking/soul_sand");
        BarrelRecipeJsonBuilder.tickTriggered(chance)
                .instant()
                .consumeFluid(WitchWaterFluid.TAG, FluidConstants.BUCKET / 2)
                .convertBlock(Blocks.COARSE_DIRT, Blocks.SOUL_SOIL)
                .offerTo(exporter, "leaking/soul_soil");
    }

    private void offerFluidTransformationRecipes(RecipeExporter exporter) {
        BarrelRecipeJsonBuilder.tickTriggered()
                .fluid(ModTags.TRUE_WATER)
                .below(Blocks.MYCELIUM)
                .duration(600)
                .storeFluid(WitchWaterFluid.STILL)
                .icon(WitchWaterFluid.BUCKET)
                .offerTo(exporter, "witchwater_from_transformation");
        BarrelRecipeJsonBuilder.tickTriggered()
                .fluid(ConventionalFluidTags.MILK)
                .below(Blocks.MYCELIUM)
                .duration(600)
                .storeItem(Items.SLIME_BLOCK)
                .offerTo(exporter, "slime_block_from_transformation");
    }

    private void offerFluidCombinationRecipes(RecipeExporter exporter) {
        BarrelRecipeJsonBuilder.tickTriggered()
                .fluid(Fluids.WATER)
                .above(ModTags.Common.BRINE)
                .instant()
                .storeItem(Items.ICE)
                .offerTo(exporter, "ice");
        BarrelRecipeJsonBuilder.tickTriggered()
                .fluid(Fluids.WATER)
                .above(Fluids.LAVA)
                .instant()
                .storeItem(Items.STONE)
                .offerTo(exporter, "stone");
        BarrelRecipeJsonBuilder.tickTriggered()
                .fluid(Fluids.LAVA)
                .above(Fluids.WATER)
                .instant()
                .storeItem(Items.OBSIDIAN)
                .offerTo(exporter, "obsidian");
    }

    private void offerMiscAlchemyRecipes(RecipeExporter exporter) {
        BarrelRecipeJsonBuilder.itemTriggered(DefaultApiModule.INSTANCE.silt, DefaultApiModule.INSTANCE.dust)
                .fluid(ModTags.TRUE_WATER)
                .instant()
                .storeItem(Items.CLAY)
                .offerTo(exporter, "clay");
        BarrelRecipeJsonBuilder.itemTriggered(Items.REDSTONE)
                .fluid(ModTags.TRUE_LAVA)
                .instant()
                .storeItem(Items.NETHERRACK)
                .offerTo(exporter, "netherrack");
        BarrelRecipeJsonBuilder.itemTriggered(Items.GLOWSTONE_DUST)
                .fluid(ModTags.TRUE_LAVA)
                .instant()
                .storeItem(Items.END_STONE)
                .offerTo(exporter, "end_stone");
        BarrelRecipeJsonBuilder.itemTriggered(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM)
                .fluid(ConventionalFluidTags.MILK)
                .instant()
                .storeItem(Items.SLIME_BLOCK)
                .offerTo(exporter, "slime_block");
        BarrelRecipeJsonBuilder.itemTriggered(ItemTags.WOOL)
                .fluid(ModTags.Common.BRINE)
                .instant()
                .storeItem(Items.WET_SPONGE)
                .offerTo(exporter, "sponge");
    }

    private void offerRedSandstoneRecipes(RecipeExporter exporter) {
        BarrelRecipeJsonBuilder.itemTriggered(Items.SAND)
                .fluid(ModTags.Common.BLOOD)
                .instant()
                .storeItem(Items.RED_SAND)
                .offerTo(exporter, "red_sand");
        BarrelRecipeJsonBuilder.itemTriggered(Items.SANDSTONE)
                .fluid(ModTags.Common.BLOOD)
                .instant()
                .storeItem(Items.RED_SANDSTONE)
                .offerTo(exporter, "red_sandstone");
        BarrelRecipeJsonBuilder.itemTriggered(Items.CUT_SANDSTONE)
                .fluid(ModTags.Common.BLOOD)
                .instant()
                .storeItem(Items.CUT_RED_SANDSTONE)
                .offerTo(exporter, "cut_red_sandstone");
        BarrelRecipeJsonBuilder.itemTriggered(Items.CHISELED_SANDSTONE)
                .fluid(ModTags.Common.BLOOD)
                .instant()
                .storeItem(Items.CHISELED_RED_SANDSTONE)
                .offerTo(exporter, "chiseled_red_sandstone");
        BarrelRecipeJsonBuilder.itemTriggered(Items.SMOOTH_SANDSTONE)
                .fluid(ModTags.Common.BLOOD)
                .instant()
                .storeItem(Items.SMOOTH_RED_SANDSTONE)
                .offerTo(exporter, "smooth_red_sandstone");
    }

    private void offerFluidRecipes(RecipeExporter exporter) {
        //noinspection deprecation
        BarrelRecipeJsonBuilder.itemTriggered(DefaultApiModule.INSTANCE.myceliumSeeds)
                .fluid(ModTags.TRUE_WATER)
                .instant()
                .storeFluid(WitchWaterFluid.STILL)
                .icon(WitchWaterFluid.BUCKET)
                .offerTo(withConditions(exporter, ResourceConditions.registryContains(DefaultApiModule.INSTANCE.myceliumSeeds.getRegistryEntry().registryKey())), "witchwater_from_seed");
        BarrelRecipeJsonBuilder.itemTriggered(ModTags.Common.SALT)
                .fluid(ModTags.TRUE_WATER)
                .instant()
                .storeFluid(BrineFluid.STILL)
                .icon(BrineFluid.BUCKET)
                .offerTo(withConditions(exporter, ResourceConditions.tagsPopulated(ModTags.Common.SALT)), "brine");
        BarrelRecipeJsonBuilder.itemTriggered(ModItems.SALT_BOTTLE)
                .fluid(ModTags.TRUE_WATER)
                .instant()
                .storeFluid(BrineFluid.STILL)
                .dropItem(Items.GLASS_BOTTLE)
                .icon(BrineFluid.BUCKET)
                .offerTo(exporter, "brine_from_salt");
    }

    private void offerCoralRecipes(RecipeExporter exporter) {
        offerCoralRecipe(exporter, Items.PINK_DYE, Items.BRAIN_CORAL_BLOCK, "brain");
        offerCoralRecipe(exporter, Items.MAGENTA_DYE, Items.BUBBLE_CORAL_BLOCK, "bubble");
        offerCoralRecipe(exporter, Items.RED_DYE, Items.FIRE_CORAL_BLOCK, "fire");
        offerCoralRecipe(exporter, Items.YELLOW_DYE, Items.HORN_CORAL_BLOCK, "horn");
        offerCoralRecipe(exporter, Items.BLUE_DYE, Items.TUBE_CORAL_BLOCK, "tube");
    }

    private void offerCoralRecipe(RecipeExporter exporter, Item dye, Item coralBlock, String name) {
        BarrelRecipeJsonBuilder.itemTriggered(dye)
                .fluid(ModTags.Common.BRINE)
                .instant()
                .storeItem(coralBlock)
                .offerTo(exporter, "coral/" + name);
    }

    private void offerSummoningRecipes(RecipeExporter  exporter) {
        BarrelRecipeJsonBuilder.itemTriggered(ModItems.DOLLS.get(id("doll_blaze")))
                .fluid(ModTags.TRUE_LAVA)
                .duration(20)
                .spawnEntity(EntityType.BLAZE)
                .storeFluid(Fluids.EMPTY)
                .icon(Items.BLAZE_SPAWN_EGG)
                .offerTo(exporter, "summon_blaze");
        BarrelRecipeJsonBuilder.itemTriggered(ModItems.DOLLS.get(id("doll_guardian")))
                .fluid(ModTags.Common.BRINE)
                .duration(20)
                .spawnEntity(EntityType.GUARDIAN)
                .storeFluid(Fluids.EMPTY)
                .icon(Items.GUARDIAN_SPAWN_EGG)
                .offerTo(exporter, "summon_guardian");
        BarrelRecipeJsonBuilder.itemTriggered(ModItems.DOLLS.get(id("doll_enderman")))
                .fluid(ModTags.WITCHWATER)
                .duration(20)
                .spawnEntity(EntityType.ENDERMAN)
                .storeFluid(Fluids.EMPTY)
                .icon(Items.ENDERMAN_SPAWN_EGG)
                .offerTo(exporter, "summon_enderman");
        BarrelRecipeJsonBuilder.itemTriggered(ModItems.DOLLS.get(id("doll_shulker")))
                .fluid(ModTags.WITCHWATER)
                .duration(20)
                .spawnEntity(EntityType.SHULKER)
                .storeFluid(Fluids.EMPTY)
                .icon(Items.SHULKER_SPAWN_EGG)
                .offerTo(exporter, "summon_shulker");
    }

    @Override
    protected Identifier getRecipeIdentifier(Identifier identifier) {
        return super.getRecipeIdentifier(identifier).withPrefixedPath("barrel_crafting/");
    }
}
