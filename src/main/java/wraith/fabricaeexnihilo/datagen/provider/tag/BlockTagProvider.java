package wraith.fabricaeexnihilo.datagen.provider.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import wraith.fabricaeexnihilo.compatibility.DefaultApiModule;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.ModTags;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public BlockTagProvider(FabricDataOutput generator, CompletableFuture<RegistryWrapper.WrapperLookup> blockTags) {
        super(generator, blockTags);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
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

        // Optional to avoid issues with optional modules
        ModBlocks.INFESTED_LEAVES.keySet().forEach(getOrCreateTagBuilder(ModTags.INFESTED_LEAVES)::addOptional);

        getOrCreateTagBuilder(ModTags.CROOKABLES)
                .forceAddTag(BlockTags.LEAVES)
                .addTag(ModTags.INFESTED_LEAVES);
        getOrCreateTagBuilder(ModTags.HAMMERABLES)
                .forceAddTag(ConventionalBlockTags.GLASS_BLOCKS)
                .addTag(ModTags.Common.CONCRETES)
                .addTag(ModTags.Common.CONCRETE_POWDERS)
                .forceAddTag(BlockTags.WOOL)
                .forceAddTag(BlockTags.CORAL_PLANTS)
                .forceAddTag(BlockTags.CORAL_BLOCKS)
                .add(Blocks.STONE, Blocks.COBBLESTONE, Blocks.GRAVEL, Blocks.SAND, ItemUtils.getExNihiloBlock("silt"))
                .add(Blocks.ANDESITE, Blocks.GRANITE, Blocks.DIORITE, Blocks.CALCITE, Blocks.PRISMARINE)
                .add(Blocks.END_STONE, Blocks.END_STONE_BRICKS, Blocks.NETHER_BRICKS, Blocks.NETHERRACK)
                .add(ItemUtils.getExNihiloBlock("crushed_granite"));

        var pickaxeMineableTag = getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE);
        var axeMineableTag = getOrCreateTagBuilder(BlockTags.AXE_MINEABLE);
        var shovelMineableTag = getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE);
        var hoeMineableTag = getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE);
        ModBlocks.BARRELS.forEach((id, barrel) -> {
            if (barrel == DefaultApiModule.INSTANCE.stoneBarrel) {
                pickaxeMineableTag.add(barrel);
            } else {
                axeMineableTag.addOptional(id);
            }
        });
        ModBlocks.CRUCIBLES.forEach((id, crucible) -> {
            if (crucible == DefaultApiModule.INSTANCE.porcelainCrucible) {
                pickaxeMineableTag.add(crucible);
            } else {
                axeMineableTag.addOptional(id);
            }
        });
        ModBlocks.SIEVES.forEach((id, crucible) -> axeMineableTag.addOptional(id));
        ModBlocks.STRAINERS.forEach((id, crucible) -> axeMineableTag.addOptional(id));
        ModBlocks.CRUSHED.forEach((id, crucible) -> shovelMineableTag.addOptional(id));
        hoeMineableTag.addTag(ModTags.INFESTED_LEAVES);
    }
}
