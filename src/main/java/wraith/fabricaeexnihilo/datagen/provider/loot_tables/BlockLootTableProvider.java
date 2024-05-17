package wraith.fabricaeexnihilo.datagen.provider.loot_tables;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryWrapper;
import wraith.fabricaeexnihilo.loot.CopyEnchantmentsLootFunction;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.util.EntrypointHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BlockLootTableProvider extends FabricBlockLootTableProvider {

    public BlockLootTableProvider(FabricDataOutput generator, CompletableFuture<RegistryWrapper.WrapperLookup> lookup) {
        super(generator, lookup);
    }

    @Override
    public void generate() {
        ModBlocks.STRAINERS.values().forEach(block -> addDrop(block, addConditions(drops(block), block)));
        ModBlocks.CRUSHED.values().forEach(block -> addDrop(block, addConditions(drops(block), block)));
        ModBlocks.CRUCIBLES.values().forEach(block -> addDrop(block, this::enchantableBlockEntityDrops));
        ModBlocks.SIEVES.values().forEach(block -> addDrop(block, this::enchantableBlockEntityDrops));
        ModBlocks.BARRELS.values().forEach(block -> addDrop(block, this::enchantableBlockEntityDrops));
        ModBlocks.INFESTED_LEAVES.values().forEach(block -> addDrop(block, this::infestedLeavesDrops));
        addDrop(ModBlocks.END_CAKE, dropsNothing());
    }

    // Currently not possible without internal api. Waiting on api update
    @SuppressWarnings("UnstableApiUsage")
    private LootTable.Builder addConditions(LootTable.Builder builder, List<ResourceCondition> conditions) {
        if (!conditions.isEmpty())
            FabricDataGenHelper.addConditions(builder, conditions.toArray(ResourceCondition[]::new));
        return builder;
    }

    private LootTable.Builder addConditions(LootTable.Builder builder, ItemConvertible owner) {
        return addConditions(builder, EntrypointHelper.CONDITIONS.computeIfAbsent(owner, __ -> new ArrayList<>()));
    }

    private LootTable.Builder infestedLeavesDrops(Block block) {
        return addConditions(LootTable.builder().pool(addSurvivesExplosionCondition(block, LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(ItemEntry.builder(Items.STRING).conditionally(RandomChanceLootCondition.builder(0.1f))))), block);
    }

    public LootTable.Builder enchantableBlockEntityDrops(Block block) {
        return addConditions(LootTable.builder().pool(addSurvivesExplosionCondition(block, LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(ItemEntry.builder(block).apply(CopyEnchantmentsLootFunction.builder())))), block);
    }
}
