package wraith.fabricaeexnihilo.recipe.crucible;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.RecipeContext;

import java.util.Optional;

public class CrucibleRecipe extends BaseRecipe<CrucibleRecipe.Context> {
    private final Ingredient input;
    private final long amount;
    private final FluidVariant fluid;
    private final boolean requiresFireproofCrucible;

    public CrucibleRecipe(Ingredient input, long amount, FluidVariant fluid, boolean requiresFireproofCrucible) {
        this.input = input;
        this.amount = amount;
        this.fluid = fluid;
        this.requiresFireproofCrucible = requiresFireproofCrucible;
    }

    public static Optional<RecipeEntry<CrucibleRecipe>> find(ItemStack input, boolean isFireproof, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.CRUCIBLE, new Context(input, isFireproof), world);
    }

    @Override
    public boolean matches(Context context, World world) {
        return input.test(context.input) && (!requiresFireproofCrucible || context.isFireproof);
    }

    @Override
    public ItemStack getDisplayStack() {
        return fluid.getFluid().getBucketItem().getDefaultStack();
    }

    public static boolean requiresFireproofCrucible(RecipeEntry<CrucibleRecipe> entry) {
        return entry.value().requiresFireproofCrucible;
    }

    public boolean requiresFireproofCrucible() {
        return requiresFireproofCrucible;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CRUCIBLE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CRUCIBLE;
    }

    public Ingredient getInput() {
        return input;
    }

    public long getAmount() {
        return amount;
    }

    public FluidVariant getFluid() {
        return fluid;
    }

    public static class Serializer implements RecipeSerializer<CrucibleRecipe> {
        public static final MapCodec<CrucibleRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Ingredient.ALLOW_EMPTY_CODEC.fieldOf("input").forGetter(recipe -> recipe.input),
                        Codec.LONG.fieldOf("amount").forGetter(recipe -> recipe.amount),
                        FluidVariant.CODEC.fieldOf("fluid").forGetter(recipe -> recipe.fluid),
                        Codec.BOOL.fieldOf("requiresFireproofCrucible").forGetter(recipe -> recipe.requiresFireproofCrucible)
                ).apply(instance, CrucibleRecipe::new)
        );
        public static final PacketCodec<RegistryByteBuf, CrucibleRecipe> PACKET_CODEC = PacketCodec.tuple(
                Ingredient.PACKET_CODEC, recipe -> recipe.input,
                PacketCodec.ofStatic(PacketByteBuf::writeLong, PacketByteBuf::readLong), recipe -> recipe.amount,
                FluidVariant.PACKET_CODEC, recipe-> recipe.fluid,
                PacketCodecs.BOOL, recipe -> recipe.requiresFireproofCrucible,
                CrucibleRecipe::new
        );

        @Override
        public MapCodec<CrucibleRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, CrucibleRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }

    public record Context(ItemStack input, boolean isFireproof) implements RecipeContext {
    }
}
