package wraith.fabricaeexnihilo.recipe.crucible;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.RecipeContext;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;

import java.util.Optional;
import java.util.function.Predicate;

public class CrucibleHeatRecipe extends BaseRecipe<CrucibleHeatRecipe.Context> {
    private final BlockIngredient block;
    private final int heat;

    public CrucibleHeatRecipe(BlockIngredient block, int heat) {
        this.block = block;
        this.heat = heat;
    }

    public static Optional<RecipeEntry<CrucibleHeatRecipe>> find(BlockState state, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.CRUCIBLE_HEAT, new Context(state), world);
    }

    @Override
    public boolean matches(Context context, World world) {
        return block.test(context.state);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CRUCIBLE_HEAT_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CRUCIBLE_HEAT;
    }

    @Override
    public ItemStack getDisplayStack() {
        return block.getValue().map(ItemStack::new, tag -> Registries.BLOCK
                .getEntryList(tag)
                .stream()
                .flatMap(RegistryEntryList::stream)
                .map(RegistryEntry::value)
                .map(ItemStack::new)
                .filter(Predicate.not(ItemStack::isEmpty))
                .findFirst()
                .orElse(ItemStack.EMPTY));
    }

    public BlockIngredient getBlock() {
        return block;
    }

    public int getHeat() {
        return heat;
    }

    public static class Serializer implements RecipeSerializer<CrucibleHeatRecipe> {
        public static final MapCodec<CrucibleHeatRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        BlockIngredient.CODEC.fieldOf("block").forGetter(recipe -> recipe.block),
                        Codec.INT.fieldOf("heat").forGetter(recipe -> recipe.heat)
                ).apply(instance, CrucibleHeatRecipe::new)
        );
        public static final PacketCodec<RegistryByteBuf, CrucibleHeatRecipe> PACKET_CODEC = PacketCodec.tuple(
                BlockIngredient.PACKET_CODEC, recipe -> recipe.block,
                PacketCodecs.INTEGER, recipe -> recipe.heat,
                CrucibleHeatRecipe::new
        );

        @Override
        public MapCodec<CrucibleHeatRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, CrucibleHeatRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }

    public record Context(BlockState state) implements RecipeContext {
    }
}
