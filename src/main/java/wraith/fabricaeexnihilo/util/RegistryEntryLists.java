package wraith.fabricaeexnihilo.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntryList;

public class RegistryEntryLists {
    private static final DynamicRegistryManager STATIC_DRM = DynamicRegistryManager.of(Registries.REGISTRIES);

    public static <T> RegistryEntryList<T> fromJson(RegistryKey<? extends Registry<T>> registry, JsonElement json) {
        return CodecUtils.deserialize(RegistryCodecs.entryList(registry), getRegistryOps(JsonOps.INSTANCE), json);
    }

    public static <T> JsonElement toJson(RegistryKey<? extends Registry<T>> registry, RegistryEntryList<T> list) {
        return CodecUtils.serialize(RegistryCodecs.entryList(registry), getRegistryOps(JsonOps.INSTANCE), list);
    }

    public static <T> RegistryEntryList<T> fromPacket(RegistryKey<? extends Registry<T>> registry, PacketByteBuf buf) {
        var wrapper = buf.readNbt();
        if (wrapper == null)
            throw new NullPointerException("wraith.fabricaeexnihilo.util.RegistryEntryLists.fromPacket()");
        var nbt = wrapper.get("value");
        return CodecUtils.deserialize(RegistryCodecs.entryList(registry), getRegistryOps(NbtOps.INSTANCE), nbt);
    }

    public static <T> void toPacket(RegistryKey<? extends Registry<T>> registry, RegistryEntryList<T> list, PacketByteBuf buf) {
        var nbt = CodecUtils.serialize(RegistryCodecs.entryList(registry), getRegistryOps(NbtOps.INSTANCE), list);
        var wrapper = new NbtCompound();
        wrapper.put("value", nbt);
        buf.writeNbt(wrapper);
    }

    private static <O> RegistryOps<O> getRegistryOps(DynamicOps<O> ops) {
        return RegistryOps.of(ops, STATIC_DRM);
    }
}
