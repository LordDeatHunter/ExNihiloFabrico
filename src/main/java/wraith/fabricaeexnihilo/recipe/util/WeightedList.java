package wraith.fabricaeexnihilo.recipe.util;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Pair;
import net.minecraft.util.math.random.Random;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class WeightedList {
    public static final Codec<WeightedList> CODEC = Codec.unboundedMap(Registries.BLOCK.getCodec(), Codec.INT)
            .xmap(WeightedList::new, WeightedList::getValues);

    @SuppressWarnings("deprecation")
    public static final PacketCodec<RegistryByteBuf, WeightedList> PACKET_CODEC = PacketCodecs.map(
            i -> (Map<Block, Integer>) new Object2ObjectOpenHashMap<Block, Integer>(i),
            PacketCodecs.registryValue(RegistryKeys.BLOCK),
            PacketCodecs.INTEGER
    ).xmap(WeightedList::new, WeightedList::getValues);

    private final Map<Block, Integer> values;
    private int totalWeight;

    public WeightedList(Map<Block, Integer> values) {
        this.values = values;
        setTotalWeight();
    }

    public WeightedList(List<Pair<Block, Integer>> values) {
        this.values = new HashMap<>();
        for (var value : values) {
            this.values.put(value.getLeft(), value.getRight());
        }
        setTotalWeight();
    }

    public Map<Block, Integer> getValues() {
        return values;
    }

    private void setTotalWeight() {
        this.totalWeight = this.values.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public Block choose(Random random) {
        var rem = random.nextInt(totalWeight + 1);
        Block block = null;
        for (var entry : values.entrySet()) {
            rem -= entry.getValue();
            block = entry.getKey();
            if (rem <= 0) {
                break;
            }
        }
        return block;
    }

    public List<ItemStack> asListOfStacks() {
        return values.entrySet().stream().map(item -> ItemUtils.asStack(item.getKey(), item.getValue())).toList();
    }

    public <U> List<U> flatten(Function<Block, U> func) {
        return values.keySet().stream().map(func).toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeightedList that = (WeightedList) o;

        return values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }
}