package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.RecipeContext;

import java.util.List;
import java.util.Optional;

public final class BarrelRecipe extends BaseRecipe<BarrelRecipe.Context> {
    private final BarrelRecipeTrigger trigger;
    private final List<BarrelRecipeCondition> conditions;
    private final List<BarrelRecipeAction> actions;
    private final int duration;

    private final Item icon;

    public BarrelRecipe(BarrelRecipeTrigger trigger, int duration, List<BarrelRecipeAction> actions, List<BarrelRecipeCondition> conditions, Item icon) {
        this.trigger = trigger;
        this.duration = duration;
        this.actions = actions;
        this.conditions = conditions;
        this.icon = icon;
    }

    public static Optional<RecipeEntry<BarrelRecipe>> findTick(BarrelBlockEntity barrel) {
        var world = barrel.getWorld();
        if (world == null) return Optional.empty();
        var recipes = world.getRecipeManager().getAllMatches(ModRecipes.BARREL, new Context(barrel, Optional.empty()), world);
        return !recipes.isEmpty() ? Optional.of(recipes.get(world.random.nextInt(recipes.size()))) : Optional.empty();
    }

    public static Optional<RecipeEntry<BarrelRecipe>> findInsert(BarrelBlockEntity barrel, ItemVariant inserted) {
        var world = barrel.getWorld();
        if (world == null) return Optional.empty();
        var recipes = world.getRecipeManager().getAllMatches(ModRecipes.BARREL, new Context(barrel, Optional.of(inserted)), world);
        return !recipes.isEmpty() ? Optional.of(recipes.get(world.random.nextInt(recipes.size()))) : Optional.empty();
    }

    @Override
    public boolean matches(Context context, World world) {
        if (context.trigger.isPresent()) {
            if (this.trigger instanceof BarrelRecipeTrigger.ItemInserted itemInserted) {
                if (!itemInserted.predicate().test(context.trigger.get().toStack())) return false;
            } else {
                return false;
            }
        } else {
            if (this.trigger instanceof BarrelRecipeTrigger.Tick tick) {
                if (world.random.nextFloat() > tick.chance()) return false;
            } else {
                return false;
            }
        }

        if (!conditions.stream().allMatch(condition -> condition.check(world, context.barrel))) return false;
        return actions.stream().allMatch(action -> action.canRun(this, context.barrel));
    }

    public boolean canContinue(World world, BarrelBlockEntity barrel) {
        if (!conditions.stream().allMatch(condition -> condition.check(world, barrel))) return false;
        return actions.stream().allMatch(action -> action.canRun(this, barrel));
    }

    public void apply(ServerWorld world, BarrelBlockEntity barrel) {
        for (BarrelRecipeAction action : actions) {
            action.apply(world, barrel);
        }
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.BARREL_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.BARREL;
    }

    @Override
    public ItemStack getDisplayStack() {
        return icon.getDefaultStack();
    }

    public int getDuration() {
        return duration;
    }

    public BarrelRecipeTrigger getTrigger() {
        return trigger;
    }

    public List<BarrelRecipeCondition> getConditions() {
        return conditions;
    }

    public List<BarrelRecipeAction> getActions() {
        return actions;
    }

    public record Context(BarrelBlockEntity barrel, Optional<ItemVariant> trigger) implements RecipeContext {
    }

    public static final class Serializer implements RecipeSerializer<BarrelRecipe> {
        public static final MapCodec<BarrelRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        BarrelRecipeTrigger.CODEC.fieldOf("trigger").forGetter(recipe -> recipe.trigger),
                        Codec.INT.fieldOf("duration").forGetter(recipe -> recipe.duration),
                        BarrelRecipeAction.CODEC.listOf().fieldOf("actions").forGetter(recipe -> recipe.actions),
                        BarrelRecipeCondition.CODEC.listOf().fieldOf("conditions").forGetter(recipe -> recipe.conditions),
                        Registries.ITEM.getCodec().fieldOf("icon").forGetter(recipe -> recipe.icon)
                ).apply(instance, BarrelRecipe::new)
        );

        public static final PacketCodec<RegistryByteBuf, BarrelRecipe> PACKET_CODEC = PacketCodec.tuple(
                BarrelRecipeTrigger.PACKET_CODEC, recipe -> recipe.trigger,
                PacketCodecs.INTEGER, recipe -> recipe.duration,
                BarrelRecipeAction.PACKET_CODEC.collect(PacketCodecs.toList()), recipe -> recipe.actions,
                BarrelRecipeCondition.PACKET_CODEC.collect(PacketCodecs.toList()), recipe -> recipe.conditions,
                PacketCodecs.registryValue(RegistryKeys.ITEM), recipe -> recipe.icon,
                BarrelRecipe::new
        );

        @Override
        public MapCodec<BarrelRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, BarrelRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}
