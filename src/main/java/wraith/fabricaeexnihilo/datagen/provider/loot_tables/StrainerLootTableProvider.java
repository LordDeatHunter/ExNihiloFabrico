package wraith.fabricaeexnihilo.datagen.provider.loot_tables;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.modules.ModLootContextTypes;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class StrainerLootTableProvider extends SimpleFabricLootTableProvider {
    public StrainerLootTableProvider(FabricDataOutput generator, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(generator, registryLookup, ModLootContextTypes.STRAINER);
    }

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> consumer) {
        consumer.accept(RegistryKey.of(RegistryKeys.LOOT_TABLE, id("gameplay/strainer")), LootTable.builder()
                .pool(LootPool.builder()
                        .with(LootTableEntry.builder(RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("gameplay/fishing/junk")))
                                .weight(10))
                        .with(LootTableEntry.builder(RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("gameplay/fishing/fish")))
                                .weight(85))
                )
        );
    }
}
