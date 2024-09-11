package wraith.fabricaeexnihilo.datagen.provider.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModTags;
import wraith.fabricaeexnihilo.util.ItemUtils;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public BlockTagProvider(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    protected void generateTags() {
        // Generate dummies to avoid datagen errors (minecraft/fapi specify at runtime)
        getOrCreateTagBuilder(ConventionalBlockTags.GLASS_BLOCKS);
        getOrCreateTagBuilder(BlockTags.LEAVES);
        getOrCreateTagBuilder(BlockTags.WOOL);
        getOrCreateTagBuilder(BlockTags.CORAL_PLANTS);
        getOrCreateTagBuilder(BlockTags.CORAL_BLOCKS);

        getOrCreateTagBuilder(ModTags.Common.CONCRETE_POWDERS)
                .add(Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, 
                        Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER,
                        Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER,
                        Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER,
                        Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER);
        getOrCreateTagBuilder(ModTags.Common.CONCRETES)
                .add(Blocks.WHITE_CONCRETE, Blocks.ORANGE_CONCRETE, Blocks.MAGENTA_CONCRETE,
                        Blocks.LIGHT_BLUE_CONCRETE, Blocks.YELLOW_CONCRETE, Blocks.LIME_CONCRETE,
                        Blocks.PINK_CONCRETE, Blocks.GRAY_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE,
                        Blocks.CYAN_CONCRETE, Blocks.PURPLE_CONCRETE, Blocks.BLUE_CONCRETE,
                        Blocks.BROWN_CONCRETE, Blocks.GREEN_CONCRETE, Blocks.RED_CONCRETE, Blocks.BLACK_CONCRETE);
        getOrCreateTagBuilder(ModTags.Common.TORCHES)
                .add(Blocks.TORCH, Blocks.WALL_TORCH, Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH);
        
        // Optional to avoid issues with optinal modules
        ModBlocks.INFESTED_LEAVES.keySet().forEach(getOrCreateTagBuilder(ModTags.INFESTED_LEAVES)::addOptional);

        getOrCreateTagBuilder(ModTags.CROOKABLES)
                .addTag(BlockTags.LEAVES)
                .addTag(ModTags.INFESTED_LEAVES);
        getOrCreateTagBuilder(ModTags.HAMMERABLES)
                .addTag(ConventionalBlockTags.GLASS_BLOCKS)
                .addTag(ModTags.Common.CONCRETES)
                .addTag(ModTags.Common.CONCRETE_POWDERS)
                .addTag(BlockTags.WOOL)
                .addTag(BlockTags.CORAL_PLANTS)
                .addTag(BlockTags.CORAL_BLOCKS)
                .add(Blocks.STONE, Blocks.COBBLESTONE, Blocks.GRAVEL, Blocks.SAND, ItemUtils.getExNihiloBlock("silt"))
                .add(Blocks.ANDESITE, Blocks.GRANITE, Blocks.DIORITE, Blocks.CALCITE, Blocks.PRISMARINE)
                .add(Blocks.END_STONE, Blocks.END_STONE_BRICKS, Blocks.NETHER_BRICKS, Blocks.NETHERRACK)
                .add(Blocks.BLACKSTONE, Blocks.COBBLED_DEEPSLATE)
                .add(ItemUtils.getExNihiloBlock("crushed_granite"));
    }
}
