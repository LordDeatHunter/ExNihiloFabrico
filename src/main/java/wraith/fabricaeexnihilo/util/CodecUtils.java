package wraith.fabricaeexnihilo.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtSizeTracker;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.RegistryWrapper;


/**
 * Contains {@link Codec}s that provide a more friendly format for end users. They allow specifying only the necessary
 * details in a simple format. Also has utility methods for quickly using codecs.
 */
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
