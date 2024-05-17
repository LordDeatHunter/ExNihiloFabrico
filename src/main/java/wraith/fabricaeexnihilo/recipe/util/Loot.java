package wraith.fabricaeexnihilo.recipe.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

public record Loot(ItemStack stack, List<Double> chances) {
    public static final Loot EMPTY = new Loot(ItemStack.EMPTY);
    public static final Codec<Loot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC
                    .fieldOf("stack")
                    .forGetter(Loot::stack),
            Codec.DOUBLE
                    .listOf()
                    .fieldOf("chances")
                    .forGetter(Loot::chances)
    ).apply(instance, Loot::new));
    public static final PacketCodec<RegistryByteBuf, Loot> PACKET_CODEC = PacketCodec.tuple(
            ItemStack.PACKET_CODEC, Loot::stack,
            PacketCodecs.DOUBLE.collect(PacketCodecs.toList()), Loot::chances,
            Loot::new
    );

    public Loot(ItemStack stack, double... chances) {
        this(stack, new ArrayList<>());
        for (var chance : chances) {
            this.chances.add(chance);
        }
    }

    public ItemStack createStack(Random random) {
        var stack = this.stack.copy();
        stack.setCount((int) chances.stream()
                .filter(chance -> chance > random.nextDouble())
                .count());
        return stack;
    }
}
