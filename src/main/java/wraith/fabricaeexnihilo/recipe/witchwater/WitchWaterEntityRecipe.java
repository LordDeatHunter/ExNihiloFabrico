package wraith.fabricaeexnihilo.recipe.witchwater;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.command.argument.NbtPathArgumentType.NbtPath;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.RecipeContext;

import java.util.Optional;

public class WitchWaterEntityRecipe extends BaseRecipe<WitchWaterEntityRecipe.Context> {
    private final EntityType<?> target;
    private final NbtPathArgumentType.NbtPath nbt;
    private final EntityType<?> result;

    public WitchWaterEntityRecipe(EntityType<?> target, NbtPathArgumentType.NbtPath nbt, EntityType<?> result) {
        this.target = target;
        this.nbt = nbt;
        this.result = result;
    }

    public static Optional<RecipeEntry<WitchWaterEntityRecipe>> find(Entity entity, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.WITCH_WATER_ENTITY, new Context(entity), world);
    }

    @Override
    public boolean matches(Context context, World world) {
        if (target != context.entity.getType())
            return false;
        return nbt.count(context.entity.writeNbt(new NbtCompound())) > 0;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.WITCH_WATER_ENTITY_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.WITCH_WATER_ENTITY;
    }

    @Override
    public ItemStack getDisplayStack() {
        var egg = SpawnEggItem.forEntity(result);
        return egg == null ? ItemStack.EMPTY : egg.getDefaultStack();
    }

    public EntityType<?> getTarget() {
        return target;
    }

    public NbtPathArgumentType.NbtPath getNbt() {
        return nbt;
    }

    public EntityType<?> getResult() {
        return result;
    }

    public record Context(Entity entity) implements RecipeContext {
    }

    public static class Serializer implements RecipeSerializer<WitchWaterEntityRecipe> {
        public static final MapCodec<WitchWaterEntityRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Registries.ENTITY_TYPE.getCodec().fieldOf("target").forGetter(recipe -> recipe.target),
                        Codec.STRING.xmap(Serializer::parseNbtPath, NbtPathArgumentType.NbtPath::toString)
                                .fieldOf("nbt").forGetter(recipe -> recipe.nbt),
                        Registries.ENTITY_TYPE.getCodec().fieldOf("result").forGetter(recipe -> recipe.result)
                ).apply(instance, WitchWaterEntityRecipe::new)
        );

        private static final PacketCodec<RegistryByteBuf, WitchWaterEntityRecipe> PACKET_CODEC = PacketCodec.tuple(
                PacketCodecs.registryValue(RegistryKeys.ENTITY_TYPE), WitchWaterEntityRecipe::getTarget,
                PacketCodecs.STRING.xmap(Serializer::parseNbtPath, NbtPath::toString), WitchWaterEntityRecipe::getNbt,
                PacketCodecs.registryValue(RegistryKeys.ENTITY_TYPE), WitchWaterEntityRecipe::getResult,
                WitchWaterEntityRecipe::new
        );

        public static NbtPathArgumentType.NbtPath parseNbtPath(String string) {
            NbtPathArgumentType.NbtPath nbt;
            try {
                var reader = new StringReader(string);
                nbt = NbtPathArgumentType.nbtPath().parse(reader);
                var remaining = reader.getRemaining();
                if (!remaining.isEmpty()) {
                    throw new IllegalArgumentException("Found trailing data after nbt path: " + remaining);
                }
            } catch (CommandSyntaxException e) {
                throw new IllegalStateException("Invalid nbt filter", e);
            }
            return nbt;
        }

        @Override
        public MapCodec<WitchWaterEntityRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, WitchWaterEntityRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}
