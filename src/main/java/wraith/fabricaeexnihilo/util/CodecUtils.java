package wraith.fabricaeexnihilo.util;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtSizeTracker;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import wraith.fabricaeexnihilo.FabricaeExNihilo;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;


/**
 * Contains {@link Codec}s that provide a more friendly format for end users. They allow specifying only the necessary
 * details in a simple format. Also has utility methods for quickly using codecs.
 */
@SuppressWarnings("UnstableApiUsage")
public class CodecUtils {
    public static final Codec<FluidVariant> FLUID_VARIANT = FluidVariant.CODEC;

    public static final Codec<ItemVariant> ITEM_VARIANT = ItemVariant.CODEC;

    public static final Codec<ItemStack> ITEM_STACK = ItemStack.CODEC;

    public static <T> T fromPacket(Codec<T> codec, RegistryByteBuf buf) {
        NbtElement nbt = buf.readNbt(NbtSizeTracker.ofUnlimitedBytes());
        return fromNbt(codec, nbt, buf.getRegistryManager());
    }

    public static <T> void toPacket(Codec<T> codec, T data, RegistryByteBuf buf) {
        var nbtData = toNbt(codec, data, buf.getRegistryManager());
        buf.writeNbt(nbtData);
    }

    public static <T> T fromNbt(Codec<T> codec, NbtElement data, RegistryWrapper.WrapperLookup registryLookup) {
        return deserialize(codec, registryLookup.getOps(NbtOps.INSTANCE), data);
    }

    public static <T> NbtElement toNbt(Codec<T> codec, T data, RegistryWrapper.WrapperLookup registryLookup) {
        return serialize(codec, registryLookup.getOps(NbtOps.INSTANCE), data);
    }

    public static <T> T fromJson(Codec<T> codec, JsonElement data, RegistryWrapper.WrapperLookup registryLookup) {
        return deserialize(codec, registryLookup.getOps(JsonOps.INSTANCE), data);
    }

    public static <T> JsonElement toJson(Codec<T> codec, T data) {
        return serialize(codec, JsonOps.INSTANCE, data);
    }

    public static <T, O> T deserialize(Codec<T> codec, DynamicOps<O> ops, O data) {
        return codec.decode(ops, data).getOrThrow().getFirst();
    }

    public static <T, O> O serialize(Codec<T> codec, DynamicOps<O> ops, T data) {
        return codec.encodeStart(ops, data).getOrThrow();
    }
}
