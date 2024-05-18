package wraith.fabricaeexnihilo.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntryList;

public class RegistryEntryLists {
    private static final DynamicRegistryManager STATIC_DRM = DynamicRegistryManager.of(Registries.REGISTRIES);

    // Can be used as a registry lookup. Is also able to promote PacketByteBufs to RegistryByteBufs.
    public static DynamicRegistryManager getStaticLookup() {
        return STATIC_DRM;
    }

    public static <T> Codec<RegistryEntryList<T>> getCodec(RegistryKey<? extends Registry<T>> registry) {
        return RegistryCodecs.entryList(registry);
    }

    public static <T> PacketCodec<RegistryByteBuf, RegistryEntryList<T>> getPacketCodec(RegistryKey<? extends Registry<T>> registry) {
        return PacketCodecs.registryEntryList(registry);
    }

    // Use Codecs and PacketCodecs instead.
    @Deprecated
    public static <T> RegistryEntryList<T> fromJson(RegistryKey<? extends Registry<T>> registry, JsonElement json) {
        return CodecUtils.fromJson(RegistryCodecs.entryList(registry), json, STATIC_DRM);
    }

    @Deprecated
    public static <T> JsonElement toJson(RegistryKey<? extends Registry<T>> registry, RegistryEntryList<T> list) {
        return CodecUtils.toJson(RegistryCodecs.entryList(registry), list, STATIC_DRM);
    }

    @Deprecated
    public static <T> RegistryEntryList<T> fromPacket(RegistryKey<? extends Registry<T>> registry, PacketByteBuf buf) {
        var wrapper = buf.readNbt();
        if (wrapper == null)
            throw new NullPointerException("wraith.fabricaeexnihilo.util.RegistryEntryLists.fromPacket()");
        var nbt = wrapper.get("value");
        return CodecUtils.fromNbt(RegistryCodecs.entryList(registry), nbt, STATIC_DRM);
    }

    @Deprecated
    public static <T> void toPacket(RegistryKey<? extends Registry<T>> registry, RegistryEntryList<T> list, PacketByteBuf buf) {
        var nbt = CodecUtils.toNbt(RegistryCodecs.entryList(registry), list, STATIC_DRM);
        var wrapper = new NbtCompound();
        wrapper.put("value", nbt);
        buf.writeNbt(wrapper);
    }
}
