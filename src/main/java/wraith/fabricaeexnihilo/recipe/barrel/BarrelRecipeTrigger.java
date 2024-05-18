package wraith.fabricaeexnihilo.recipe.barrel;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;

public sealed interface BarrelRecipeTrigger {
    MapCodec<BarrelRecipeTrigger> CODEC = Codec.STRING.dispatchMap(BarrelRecipeTrigger::getType, BarrelRecipeTrigger::forType);
    PacketCodec<RegistryByteBuf, BarrelRecipeTrigger> PACKET_CODEC = PacketCodecs.BYTE.<RegistryByteBuf>cast().dispatch(BarrelRecipeTrigger::getId, BarrelRecipeTrigger::forId);

    static MapCodec<? extends BarrelRecipeTrigger> forType(String type) {
        return switch (type) {
            case Tick.TYPE -> Tick.CODEC;
            case ItemInserted.TYPE -> ItemInserted.CODEC;
            default -> throw new IllegalArgumentException("Unknown trigger type: " + type);
        };
    }

    static PacketCodec<RegistryByteBuf, ? extends BarrelRecipeTrigger> forId(byte id) {
        return switch (id) {
            case Tick.ID -> Tick.PACKET_CODEC;
            case ItemInserted.ID -> ItemInserted.PACKET_CODEC;
            default -> throw new IllegalArgumentException("Unknown trigger type id: " + id);
        };
    }

    void writePacket(RegistryByteBuf buf);

    String getType();

    byte getId();

    MapCodec<? extends BarrelRecipeTrigger> getCodec();

    PacketCodec<RegistryByteBuf, ? extends BarrelRecipeTrigger> getPacketCodec();

    record Tick(float chance) implements BarrelRecipeTrigger {
        public static final String TYPE = "tick";
        public static final byte ID = 0;

        public static final MapCodec<Tick> CODEC = Codec.FLOAT.fieldOf("chance").xmap(Tick::new, Tick::chance);
        public static final PacketCodec<RegistryByteBuf, Tick> PACKET_CODEC = PacketCodec.of(Tick::writePacket, Tick::new);

        public Tick(RegistryByteBuf buf) {
            this(buf.readFloat());
        }

        @Override
        public void writePacket(RegistryByteBuf buf) {
            buf.writeFloat(chance);
        }

        @Override
        public String getType() {
            return TYPE;
        }

        @Override
        public byte getId() {
            return ID;
        }


        @Override
        public MapCodec<? extends BarrelRecipeTrigger> getCodec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ? extends BarrelRecipeTrigger> getPacketCodec() {
            return PACKET_CODEC;
        }
    }

    record ItemInserted(Ingredient predicate) implements BarrelRecipeTrigger {
        public static final String TYPE = "insert_item";
        public static final byte ID = 1;

        public static final MapCodec<ItemInserted> CODEC = Ingredient.ALLOW_EMPTY_CODEC.fieldOf("item").xmap(ItemInserted::new, ItemInserted::predicate);
        public static final PacketCodec<RegistryByteBuf, ItemInserted> PACKET_CODEC = PacketCodec.of(ItemInserted::writePacket, ItemInserted::new);

        public ItemInserted(RegistryByteBuf buf) {
            this(Ingredient.PACKET_CODEC.decode(buf));
        }

        @Override
        public void writePacket(RegistryByteBuf buf) {
            Ingredient.PACKET_CODEC.encode(buf, predicate);
        }

        @Override
        public String getType() {
            return TYPE;
        }

        @Override
        public byte getId() {
            return ID;
        }

        @Override
        public MapCodec<? extends BarrelRecipeTrigger> getCodec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ? extends BarrelRecipeTrigger> getPacketCodec() {
            return PACKET_CODEC;
        }
    }
}
