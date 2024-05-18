package wraith.fabricaeexnihilo.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.Loot;

import java.util.List;

public class ToolRecipe extends BaseRecipe<ToolRecipe.Context> {
    private final ToolType tool;
    private final BlockIngredient block;
    private final Loot result;

    public static final MapCodec<ToolRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ToolType.CODEC.fieldOf("type").forGetter(recipe -> recipe.tool),
                    BlockIngredient.CODEC.fieldOf("block").forGetter(recipe -> recipe.block),
                    Loot.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
            ).apply(instance, ToolRecipe::new)
    );

    public static final PacketCodec<RegistryByteBuf, ToolRecipe> PACKET_CODEC = PacketCodec.tuple(
            PacketCodec.ofStatic(PacketByteBuf::writeEnumConstant, buf -> buf.readEnumConstant(ToolType.class)), recipe -> recipe.tool,
            BlockIngredient.PACKET_CODEC, recipe -> recipe.block,
            Loot.PACKET_CODEC, recipe -> recipe.result,
            ToolRecipe::new
    );

    public ToolRecipe(ToolType tool, BlockIngredient block, Loot result) {
        this.tool = tool;
        this.block = block;
        this.result = result;
    }

    public static List<RecipeEntry<ToolRecipe>> find(ToolType type, BlockState state, @Nullable World world) {
        if (world == null) {
            return List.of();
        }
        return world.getRecipeManager().getAllMatches(type.type, new Context(state), world);
    }

    @Override
    public boolean matches(Context context, World world) {
        return block.test(context.state);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return tool.serializer;
    }

    @Override
    public RecipeType<?> getType() {
        return tool.type;
    }

    @Override
    public ItemStack getDisplayStack() {
        return result.stack();
    }

    public ToolType getTool() {
        return tool;
    }

    public BlockIngredient getBlock() {
        return block;
    }

    public Loot getResult() {
        return result;
    }

    public enum ToolType implements StringIdentifiable {
        HAMMER("hammer", ModRecipes.HAMMER, ModRecipes.HAMMER_SERIALIZER),
        CROOK("crook", ModRecipes.CROOK, ModRecipes.CROOK_SERIALIZER);

        public static final Codec<ToolType> CODEC = StringIdentifiable.createCodec(ToolType::values);

        public final String id;
        public final RecipeType<ToolRecipe> type;
        public final RecipeSerializer<?> serializer;

        ToolType(String name, RecipeType<ToolRecipe> type, RecipeSerializer<?> serializer) {
            this.id = "fabricaeexnihilo:" + name;
            this.type = type;
            this.serializer = serializer;
        }

        @Override
        public String asString() {
            return id;
        }
    }

    public static class Serializer implements RecipeSerializer<ToolRecipe> {
        @Override
        public MapCodec<ToolRecipe> codec() {
            return ToolRecipe.CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ToolRecipe> packetCodec() {
            return ToolRecipe.PACKET_CODEC;
        }
    }

    public record Context(BlockState state) implements RecipeContext {
    }
}
