package wraith.fabricaeexnihilo.recipe.util;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Predicate;

public sealed abstract class BlockIngredient implements Predicate<BlockState> {
    protected final Map<String, String> properties;

    protected BlockIngredient(Map<String, String> properties) {
        this.properties = Map.copyOf(properties);
    }

    private static BlockIngredient fromData(String id, Map<String, String> states) {
        if (id.startsWith("#")) {
            return new Tag(TagKey.of(RegistryKeys.BLOCK, Identifier.of(id.substring(1))), states);
        } else {
            return new Single(Registries.BLOCK.getEntry(Identifier.of(id)).orElseThrow(), states);
        }
    }

    private static BlockIngredient fromData(Pair<String, Map<String, String>> data) {
        return fromData(data.getFirst(), data.getSecond());
    }

    private static final MapCodec<Pair<String, Map<String, String>>> OBJ_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.STRING.fieldOf("id").forGetter(Pair::getFirst),
                    Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("states").forGetter(Pair::getSecond)
            ).apply(instance, Pair::of)
    );
    private static final Codec<Pair<String, Map<String, String>>> DATA_CODEC = Codec.withAlternative(
            OBJ_CODEC.codec(),
            Codec.STRING,
            str -> Pair.of(str, Map.of())
    );

    public static Codec<BlockIngredient> CODEC = DATA_CODEC.xmap(BlockIngredient::fromData, ingredient -> Pair.of(ingredient.id(), ingredient.properties));

    public static PacketCodec<RegistryByteBuf, BlockIngredient> PACKET_CODEC = PacketCodec.ofStatic(BlockIngredient::toPacket, BlockIngredient::fromPacket);

    public static BlockIngredient fromPacket(RegistryByteBuf buf) {
        var states = buf.readMap(PacketByteBuf::readString, PacketByteBuf::readString);
        var id = buf.readString();
        return fromData(id, states);
    }

    public static void toPacket(RegistryByteBuf buf, @NotNull BlockIngredient ingredient) {
        buf.writeMap(ingredient.properties, PacketByteBuf::writeString, PacketByteBuf::writeString);
        buf.writeString(ingredient.id());
    }

    protected boolean stateMatches(BlockState state) {
        for (var entry : properties.entrySet()) {
            var found = state.getProperties()
                    .stream()
                    .filter(property -> property.getName().equals(entry.getKey()))
                    .findFirst();
            if (found.isEmpty()) return false;
            var current = state.get(found.get());
            var parsed = found.get().parse(entry.getValue());
            // Parsing fails are silently ignored since multiple properties can have the same name
            if (parsed.isPresent() && !current.equals(parsed.get())) return false;
        }

        return true;
    }

    @SuppressWarnings("deprecation")
    public static BlockIngredient single(Block block) {
        return new Single(block.getRegistryEntry(), Map.of());
    }

    public static BlockIngredient single(RegistryEntry.Reference<Block> block) {
        return new Single(block, Map.of());
    }

    public static BlockIngredient tag(TagKey<Block> tag) {
        return new Tag(tag, Map.of());
    }

    public abstract String id();

    public abstract Either<Block, TagKey<Block>> getValue();

    private static final class Single extends BlockIngredient {
        private final Identifier blockId;
        private final Block block;

        @SuppressWarnings("deprecation")
        private Single(Block block, Map<String, String> properties) {
            super(properties);
            this.block = block;
            this.blockId = block.getRegistryEntry().registryKey().getValue();
        }

        private Single(RegistryEntry.Reference<Block> blockEntry, Map<String, String> properties) {
            super(properties);
            this.block = blockEntry.value();
            this.blockId = blockEntry.registryKey().getValue();
        }

        @Override
        public boolean test(BlockState state) {
            return state.isOf(block) && stateMatches(state);
        }

        @Override
        public String id() {
            return blockId.toString();
        }

        @Override
        public Either<Block, TagKey<Block>> getValue() {
            return Either.left(block);
        }
    }

    private static final class Tag extends BlockIngredient {
        private final TagKey<Block> tag;

        private Tag(TagKey<Block> tag, Map<String, String> properties) {
            super(properties);
            this.tag = tag;
        }

        @Override
        public boolean test(BlockState state) {
            return state.getRegistryEntry().isIn(tag) && stateMatches(state);
        }

        @Override
        public String id() {
            return "#" + tag.id().toString();
        }

        @Override
        public Either<Block, TagKey<Block>> getValue() {
            return Either.right(tag);
        }
    }
}
