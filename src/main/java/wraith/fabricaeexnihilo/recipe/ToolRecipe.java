package wraith.fabricaeexnihilo.recipe;

import com.google.gson.JsonObject;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.Loot;
import wraith.fabricaeexnihilo.util.CodecUtils;

import java.util.List;

public class ToolRecipe extends BaseRecipe<ToolRecipe.Context> {
    private final ToolType tool;
    private final BlockIngredient block;
    private final Loot result;

    public ToolRecipe( ToolType tool, BlockIngredient block, Loot result) {
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

    public enum ToolType {
        HAMMER(ModRecipes.HAMMER, ModRecipes.HAMMER_SERIALIZER),
        CROOK(ModRecipes.CROOK, ModRecipes.CROOK_SERIALIZER);

        public final RecipeType<ToolRecipe> type;
        public final RecipeSerializer<?> serializer;

        ToolType(RecipeType<ToolRecipe> type, RecipeSerializer<?> serializer) {
            this.type = type;
            this.serializer = serializer;
        }

        public static ToolType fromRecipeType(String type) {
            return switch (type) {
                case "fabricaeexnihilo:hammer" -> HAMMER;
                case "fabricaeexnihilo:crook" -> CROOK;
                default -> throw new IllegalStateException("Tried to find tool type for unknown recipe type: " + type);
            };
        }
        public static String toRecipeType(ToolType type) {
            return switch (type) {
                case HAMMER -> "fabricaeexnihilo:hammer";
                case CROOK -> "fabricaeexnihilo:crook";
            };
        }
    }

    public static class Serializer implements RecipeSerializer<ToolRecipe> {
        public static final MapCodec<ToolRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Codec.STRING.fieldOf("type").xmap(ToolType::fromRecipeType, ToolType::toRecipeType).forGetter(recipe -> recipe.tool),
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

        @Override
        public MapCodec<ToolRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ToolRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }

    public record Context(BlockState state) implements RecipeContext {
    }
}
