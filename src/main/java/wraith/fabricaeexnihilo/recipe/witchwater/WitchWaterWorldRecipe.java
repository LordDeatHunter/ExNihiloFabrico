package wraith.fabricaeexnihilo.recipe.witchwater;

import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.RecipeContext;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;
import wraith.fabricaeexnihilo.recipe.util.WeightedList;

import java.util.Optional;

public class WitchWaterWorldRecipe extends BaseRecipe<WitchWaterWorldRecipe.Context> {
    private final FluidIngredient target;
    private final WeightedList result;

    public WitchWaterWorldRecipe(FluidIngredient target, WeightedList result) {
        this.target = target;
        this.result = result;
    }

    public static Optional<RecipeEntry<WitchWaterWorldRecipe>> find(Fluid fluid, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.WITCH_WATER_WORLD, new Context(fluid), world);
    }

    @Override
    public boolean matches(Context context, World world) {
        return target.test(context.fluid);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.WITCH_WATER_WORLD_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.WITCH_WATER_WORLD;
    }

    @Override
    public ItemStack getDisplayStack() {
        return result.asListOfStacks().get(0);
    }

    public FluidIngredient getTarget() {
        return target;
    }

    public WeightedList getResult() {
        return result;
    }

    protected record Context(Fluid fluid) implements RecipeContext {
    }

    public static class Serializer implements RecipeSerializer<WitchWaterWorldRecipe> {
        public static final MapCodec<WitchWaterWorldRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        FluidIngredient.CODEC.fieldOf("target").forGetter(recipe -> recipe.target),
                        WeightedList.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
                ).apply(instance, WitchWaterWorldRecipe::new)
        );

        public static final PacketCodec<RegistryByteBuf, WitchWaterWorldRecipe> PACKET_CODEC = PacketCodec.tuple(
                FluidIngredient.PACKET_CODEC, WitchWaterWorldRecipe::getTarget,
                WeightedList.PACKET_CODEC, WitchWaterWorldRecipe::getResult,
                WitchWaterWorldRecipe::new
        );

        @Override
        public MapCodec<WitchWaterWorldRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, WitchWaterWorldRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}
