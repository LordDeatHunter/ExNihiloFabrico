package wraith.fabricaeexnihilo.recipe.barrel;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;

public sealed interface BarrelRecipeTrigger {
    MapCodec<BarrelRecipeTrigger> CODEC = Codec.STRING.dispatchMap(BarrelRecipeTrigger::getType, BarrelRecipeTrigger::forType);

    static MapCodec<? extends BarrelRecipeTrigger> forType(String type) {
        return switch (type) {
            case Tick.TYPE -> Tick.CODEC;
            case ItemInserted.TYPE -> ItemInserted.CODEC;
            default -> throw new IllegalArgumentException("Unknown trigger type: " + type);
        };
    }

    static BarrelRecipeTrigger fromPacket(RegistryByteBuf buf) {
        var type = buf.readByte();
        return switch (type) {
            case 0 -> new Tick(buf.readFloat());
            case 1 -> new ItemInserted(Ingredient.PACKET_CODEC.decode(buf));
            default -> throw new IllegalArgumentException("Unknown trigger type id: " + type);
        };
    }

    void toPacket(RegistryByteBuf buf);

    PacketCodec<RegistryByteBuf, BarrelRecipeTrigger> PACKET_CODEC = PacketCodec.of(BarrelRecipeTrigger::toPacket, BarrelRecipeTrigger::fromPacket);

    String getType();

    MapCodec<? extends BarrelRecipeTrigger> getCodec();

    record Tick(float chance) implements BarrelRecipeTrigger {
        public static final String TYPE = "tick";
        @Override
        public void toPacket(RegistryByteBuf buf) {
            buf.writeByte(0);
            buf.writeFloat(chance);
        }

        @Override
        public String getType() {
            return TYPE;
        }

        public static final MapCodec<Tick> CODEC = Codec.FLOAT.fieldOf("chance").xmap(Tick::new, Tick::chance);

        @Override
        public MapCodec<? extends BarrelRecipeTrigger> getCodec() {
            return CODEC;
        }
    }

    record ItemInserted(Ingredient predicate) implements BarrelRecipeTrigger {
        public static final String TYPE = "insert_item";
        @Override
        public void toPacket(RegistryByteBuf buf) {
            buf.writeByte(1);
            Ingredient.PACKET_CODEC.encode(buf, predicate);
        }

        @Override
        public String getType() {
            return TYPE;
        }
        public static final MapCodec<ItemInserted> CODEC = Ingredient.ALLOW_EMPTY_CODEC.fieldOf("item").xmap(ItemInserted::new, ItemInserted::predicate);

        @Override
        public MapCodec<? extends BarrelRecipeTrigger> getCodec() {
            return CODEC;
        }
    }
}
