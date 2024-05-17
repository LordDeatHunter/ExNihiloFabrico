package wraith.fabricaeexnihilo.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.util.Loot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SieveRecipe extends BaseRecipe<SieveRecipe.Context> {
    private final ItemStack result;
    private final Ingredient input;
    private final boolean waterlogged;
    private final Map<Identifier, List<Double>> rolls;

    public SieveRecipe(ItemStack result, Ingredient input, boolean waterlogged, Map<Identifier, List<Double>> rolls) {
        this.result = result;
        this.input = input;
        this.waterlogged = waterlogged;
        this.rolls = rolls;
    }

    public static List<Loot> find(Item item, boolean waterlogged, Identifier mesh, @Nullable World world) {
        if (world == null) {
            return List.of();
        }
        return world.getRecipeManager().getAllMatches(ModRecipes.SIEVE, new Context(item, waterlogged), world)
                .stream()
                .map(RecipeEntry::value)
                .map(recipe -> new Loot(recipe.result, recipe.rolls.get(mesh)))
                .filter(loot -> loot.chances() != null)
                .toList();
    }

    @Override
    public boolean matches(SieveRecipe.Context context, World world) {
        return input.test(context.input.getDefaultStack()) && waterlogged == context.waterlogged;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SIEVE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.SIEVE;
    }

    @Override
    public ItemStack getDisplayStack() {
        return result;
    }

    public ItemStack getResult() {
        return result;
    }

    public Map<Identifier, ? extends List<Double>> getRolls() {
        return rolls;
    }

    public Ingredient getInput() {
        return input;
    }

    public boolean isWaterlogged() {
        return waterlogged;
    }

    public record Context(Item input, boolean waterlogged) implements RecipeContext {
    }

    public static class Serializer implements RecipeSerializer<SieveRecipe> {
        public static final MapCodec<SieveRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Ingredient.ALLOW_EMPTY_CODEC.fieldOf("input").forGetter(recipe -> recipe.input),
                        Codec.BOOL.fieldOf("waterlogged").orElse(false).forGetter(recipe -> recipe.waterlogged),
                        Codec.unboundedMap(Identifier.CODEC, Codec.DOUBLE.listOf()).fieldOf("rolls").forGetter(recipe -> recipe.rolls)
                ).apply(instance, SieveRecipe::new)
        );

        public static final PacketCodec<RegistryByteBuf, SieveRecipe> PACKET_CODEC = PacketCodec.tuple(
                ItemStack.PACKET_CODEC, recipe -> recipe.result,
                Ingredient.PACKET_CODEC, recipe -> recipe.input,
                PacketCodecs.BOOL, recipe -> recipe.waterlogged,
                PacketCodecs.map(HashMap::new, Identifier.PACKET_CODEC, PacketCodecs.DOUBLE.collect(PacketCodecs.toList())), recipe -> recipe.rolls,
                SieveRecipe::new
        );

        @Override
        public MapCodec<SieveRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, SieveRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}
