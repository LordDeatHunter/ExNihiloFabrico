package wraith.fabricaeexnihilo.recipe.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

@SuppressWarnings("deprecation")
public sealed abstract class FluidIngredient implements Predicate<Fluid> {
    public static final Codec<FluidIngredient> CODEC = Codec.of(
            Codec.STRING.comap(FluidIngredient::toId),
            Codec.PASSTHROUGH.flatMap(
                    dyn -> Codec.STRING.parse(dyn).map(s -> FluidIngredient.fromId(s, null))
            )
    );

    public static final PacketCodec<RegistryByteBuf, FluidIngredient> PACKET_CODEC = PacketCodec.of(
            (value, buf) -> buf.writeString(value.toId()),
            buf -> FluidIngredient.fromId(buf.readString(),
                    buf.getRegistryManager().getOptionalWrapper(RegistryKeys.FLUID).orElse(null))
    );

    public static FluidIngredient fromId(String id, @Nullable RegistryEntryLookup<Fluid> lookup) {
        if (id.startsWith("#"))
            return new Tag(TagKey.of(RegistryKeys.FLUID, new Identifier(id.substring(1))));
        else if (lookup != null) {
            return new Single(lookup.getOrThrow(RegistryKey.of(RegistryKeys.FLUID, new Identifier(id))).value());
        } else {
            return new Single(Registries.FLUID.get(new Identifier(id)));
        }
    }

    public static FluidIngredient fromPacket(RegistryByteBuf buf) {
        var id = buf.readByte();
        return switch (id) {
            case 0 -> new Single(Registries.FLUID.get(buf.readIdentifier()));
            case 1 -> new Tag(TagKey.of(RegistryKeys.FLUID, buf.readIdentifier()));
            default -> throw new IllegalStateException("Unexpected fluid ingredient type: " + id);
        };
    }

    public static FluidIngredient single(Fluid fluid) {
        return new Single(fluid);
    }

    public static FluidIngredient tag(TagKey<Fluid> tag) {
        return new Tag(tag);
    }

    public abstract Either<Fluid, TagKey<Fluid>> getValue();

    public abstract void toPacket(PacketByteBuf buf);

    public abstract String toId();

    public abstract boolean isEmpty();

    private static final class Single extends FluidIngredient {
        private final Fluid fluid;

        private Single(Fluid fluid) {
            this.fluid = fluid;
        }

        @Override
        public boolean test(Fluid fluid) {
            return fluid.matchesType(this.fluid);
        }

        @Override
        public Either<Fluid, TagKey<Fluid>> getValue() {
            return Either.left(fluid);
        }

        @Override
        public void toPacket(PacketByteBuf buf) {
            buf.writeByte(0);
            buf.writeIdentifier(Registries.FLUID.getId(fluid));
        }

        @Override
        public String toId() {
            return fluid.getRegistryEntry().registryKey().getValue().toString();
        }

        @Override
        public boolean isEmpty() {
            return fluid == Fluids.EMPTY;
        }
    }

    private static final class Tag extends FluidIngredient {
        private final TagKey<Fluid> tag;

        private Tag(TagKey<Fluid> tag) {
            this.tag = tag;
        }

        @Override
        public boolean test(Fluid fluid) {
            return fluid.isIn(tag);
        }

        @Override
        public Either<Fluid, TagKey<Fluid>> getValue() {
            return Either.right(tag);
        }

        @Override
        public void toPacket(PacketByteBuf buf) {
            buf.writeByte(1);
            buf.writeIdentifier(tag.id());
        }

        @Override
        public String toId() {
            return "#" + tag.id().toString();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }
}
