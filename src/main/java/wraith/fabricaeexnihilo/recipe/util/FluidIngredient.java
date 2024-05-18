package wraith.fabricaeexnihilo.recipe.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

@SuppressWarnings("deprecation")
public sealed abstract class FluidIngredient implements Predicate<Fluid> {
    public static final Codec<FluidIngredient> CODEC = Codec.STRING.xmap(FluidIngredient::fromId, FluidIngredient::toId);
    public static final PacketCodec<RegistryByteBuf, FluidIngredient> PACKET_CODEC = PacketCodecs.STRING.<RegistryByteBuf>cast().xmap(FluidIngredient::fromId, FluidIngredient::toId);

    public static FluidIngredient fromId(String id) {
        if (id.startsWith("#")) {
            return new Tag(TagKey.of(RegistryKeys.FLUID, new Identifier(id.substring(1))));
        } else {
            return new Single(Registries.FLUID.get(new Identifier(id)));
        }
    }

    public static FluidIngredient fromPacket(RegistryByteBuf buf) {
        return PACKET_CODEC.decode(buf);
    }

    public void toPacket(RegistryByteBuf buf) {
        FluidIngredient.PACKET_CODEC.encode(buf, this);
    }

    public static FluidIngredient single(Fluid fluid) {
        return new Single(fluid);
    }

    public static FluidIngredient tag(TagKey<Fluid> tag) {
        return new Tag(tag);
    }

    public abstract Either<Fluid, TagKey<Fluid>> getValue();

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
        public String toId() {
            return "#" + tag.id().toString();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }
}
