package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.RecipeContext;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public final class BarrelRecipe extends BaseRecipe<BarrelRecipe.Context> {
    private final BarrelRecipeTrigger trigger;
    private final List<BarrelRecipeCondition> conditions;
    private final List<BarrelRecipeAction> actions;
    private final int duration;

    private final Item icon;

    private BarrelRecipe(Identifier id, BarrelRecipeTrigger trigger, int duration, List<BarrelRecipeAction> actions, List<BarrelRecipeCondition> conditions, Item icon) {
        super(id);
        this.trigger = trigger;
        this.duration = duration;
        this.actions = actions;
        this.conditions = conditions;
        this.icon = icon;
    }

    public static Optional<BarrelRecipe> findTick(BarrelBlockEntity barrel) {
        var world = barrel.getWorld();
        if (world == null) return Optional.empty();
        var recipes = world.getRecipeManager().getAllMatches(ModRecipes.BARREL, new Context(barrel, Optional.empty()), world);
        return !recipes.isEmpty() ? Optional.of(recipes.get(world.random.nextInt(recipes.size()))) : Optional.empty();
    }

    public static Optional<BarrelRecipe> findInsert(BarrelBlockEntity barrel, ItemVariant inserted) {
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
        @Override
        public BarrelRecipe read(Identifier id, JsonObject json) {
            var trigger = BarrelRecipeTrigger.fromJson(JsonHelper.getObject(json, "trigger"));
            var duration = JsonHelper.getInt(json, "duration", 0);
            var actions = JsonHelper.getArray(json, "actions")
                    .asList()
                    .stream()
                    .map(element -> JsonHelper.asObject(element, "action"))
                    .map(BarrelRecipeAction::fromJson)
                    .toList();
            var conditions = JsonHelper.getArray(json, "conditions", new JsonArray())
                    .asList()
                    .stream()
                    .map(element -> JsonHelper.asObject(element, "condition"))
                    .map(BarrelRecipeCondition::fromJson)
                    .toList();
            var icon = Registries.ITEM.get(new Identifier(JsonHelper.getString(json, "icon")));

            return new BarrelRecipe(id, trigger, duration, actions, conditions, icon);
        }

        @Override
        public BarrelRecipe read(Identifier id, PacketByteBuf buf) {
            var trigger = BarrelRecipeTrigger.fromPacket(buf);
            var duration = buf.readVarInt();
            var actions = buf.readList(BarrelRecipeAction::fromPacket);
            var conditions = buf.readList(BarrelRecipeCondition::fromPacket);
            var icon = buf.readRegistryValue(Registries.ITEM);

            return new BarrelRecipe(id, trigger, duration, actions, conditions, icon);
        }

        @Override
        public void write(PacketByteBuf buf, BarrelRecipe recipe) {
            recipe.trigger.toPacket(buf);
            buf.writeVarInt(recipe.duration);
            buf.writeCollection(recipe.actions, (buf1, action) -> action.toPacket(buf1));
            buf.writeCollection(recipe.conditions, (buf1, condition) -> condition.toPacket(buf1));
            buf.writeRegistryValue(Registries.ITEM, recipe.icon);
        }
    }

}
