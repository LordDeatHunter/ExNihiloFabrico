package wraith.fabricaeexnihilo.modules.base;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import java.util.Map;

public class EnchantmentContainer {

    private final Map<RegistryEntry.Reference<Enchantment>, Integer> enchantments = new Object2IntOpenHashMap<>();

    public static void addEnchantments(ItemStack itemStack, EnchantmentContainer container) {
        container.enchantments.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .forEach(entry -> {
                    var enchantment = entry.getKey();
                    if (enchantment != null) {
                        itemStack.addEnchantment(enchantment, entry.getValue());
                    }
                });
    }

    public Map<RegistryEntry.Reference<Enchantment>, Integer> getEnchantments() {
        return enchantments;
    }

    public NbtCompound writeNbt() {
        var nbt = new NbtCompound();
        enchantments.forEach((enchantment, level) -> nbt.putInt(enchantment.registryKey().getValue().toString(), level));
        return nbt;
    }

    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        enchantments.clear();
        var enchantmentRegistry = registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
        nbt.getKeys().forEach(key -> enchantments.put(
                enchantmentRegistry.getOrThrow(RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(key))),
                nbt.getInt(key)));
    }

    public int getEnchantmentLevel(RegistryKey<Enchantment> enchantmentKey) {
        var id = enchantmentKey.getValue();
        var enchantment = enchantments.entrySet().stream().filter(e -> e.getKey().registryKey().getValue().equals(id)).findAny();
        return enchantment.isEmpty() ? 0 : enchantments.getOrDefault(enchantment.get().getKey(), 0);
    }

    public void setEnchantmentLevel(RegistryEntry<Enchantment> enchantment, int level) {
        enchantments.put((RegistryEntry.Reference<Enchantment>) enchantment, level);
    }
}
