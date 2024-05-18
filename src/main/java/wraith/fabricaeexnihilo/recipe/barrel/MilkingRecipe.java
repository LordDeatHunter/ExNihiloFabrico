package wraith.fabricaeexnihilo.recipe.barrel;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
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

public class MilkingRecipe extends BaseRecipe<MilkingRecipe.Context> {
    private final EntityType<?> entity;
    private final FluidVariant fluid;
    private final long amount;
    private final int cooldown;

    public MilkingRecipe(EntityType<?> entity, FluidVariant fluid, long amount, int cooldown) {
        this.entity = entity;
        this.fluid = fluid;
        this.amount = amount;
        this.cooldown = cooldown;
    }

    public static Optional<RecipeEntry<MilkingRecipe>> find(EntityType<?> entity, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.MILKING, new Context(entity), world);
    }

    @Override
    public boolean matches(Context context, World world) {
        return this.entity == context.entity;
    }

    @Override
    public ItemStack getDisplayStack() {
        return fluid.getFluid().getBucketItem().getDefaultStack();
    }

    public FluidVariant getFluid() {
        return fluid;
    }

    public long getAmount() {
        return amount;
    }

    public int getCooldown() {
        return cooldown;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.MILKING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.MILKING;
    }

    public EntityType<?> getEntity() {
        return entity;
    }

    public static class Serializer implements RecipeSerializer<MilkingRecipe> {
        public static final MapCodec<MilkingRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Registries.ENTITY_TYPE.getCodec().fieldOf("entity").forGetter(recipe -> recipe.entity),
                        FluidVariant.CODEC.fieldOf("fluid").forGetter(recipe -> recipe.fluid),
                        Codec.LONG.fieldOf("amount").forGetter(recipe -> recipe.amount),
                        Codec.INT.fieldOf("cooldown").forGetter(recipe -> recipe.cooldown)
                ).apply(instance, MilkingRecipe::new)
        );

        public static final PacketCodec<RegistryByteBuf, MilkingRecipe> PACKET_CODEC = PacketCodec.tuple(
                PacketCodecs.registryValue(RegistryKeys.ENTITY_TYPE), MilkingRecipe::getEntity,
                FluidVariant.PACKET_CODEC, MilkingRecipe::getFluid,
                PacketCodecs.VAR_LONG, MilkingRecipe::getAmount,
                PacketCodecs.VAR_INT, MilkingRecipe::getCooldown,
                MilkingRecipe::new
        );

        @Override
        public MapCodec<MilkingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, MilkingRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }

    public record Context(EntityType<?> entity) implements RecipeContext {
    }
}
