package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.BarrelState;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;

@SuppressWarnings("UnstableApiUsage")
public sealed interface BarrelRecipeCondition {
    boolean check(World world, BarrelBlockEntity barrel);

    default void toPacket(PacketByteBuf buf) {
        buf.writeByte(getId());
        writePacket(buf);
    }

    default JsonObject toJson() {
        var json = new JsonObject();
        json.addProperty("type", getName());
        writeJson(json);
        return json;
    }

    void writePacket(PacketByteBuf buf);

    void writeJson(JsonObject json);

    byte getId();

    String getName();

    static BarrelRecipeCondition fromJson(JsonObject json) {
        var type = JsonHelper.getString(json, "type");
        return switch (type) {
            case FluidAbove.NAME -> new FluidAbove(json);
            case BlockAbove.NAME -> new BlockAbove(json);
            case BlockBelow.NAME -> new BlockBelow(json);
            case FluidIn.NAME -> new FluidIn(json);
            default -> throw new JsonParseException("Unknown condition type: " + type);
        };
    }

    static BarrelRecipeCondition fromPacket(PacketByteBuf buf) {
        var type = buf.readByte();
        return switch (type) {
            case FluidAbove.ID -> new FluidAbove(buf);
            case BlockAbove.ID -> new BlockAbove(buf);
            case BlockBelow.ID -> new BlockBelow(buf);
            case FluidIn.ID -> new FluidIn(buf);
            default -> throw new JsonParseException("Unknown condition type id: " + type);
        };
    }

    record FluidAbove(FluidIngredient fluid) implements BarrelRecipeCondition {
        private static final String NAME = "fluid_above";
        private static final byte ID = 0;

        public FluidAbove(JsonObject json) {
            this(FluidIngredient.fromJson(JsonHelper.getElement(json, "fluid")));
        }

        public FluidAbove(PacketByteBuf buf) {
            this(FluidIngredient.fromPacket(buf));
        }

        @Override
        public boolean check(World world, BarrelBlockEntity barrel) {
            return fluid.test(world.getFluidState(barrel.getPos().up()).getFluid());
        }

        @Override
        public void writePacket(PacketByteBuf buf) {
            fluid.toPacket(buf);
        }

        @Override
        public void writeJson(JsonObject json) {
            json.add("fluid", fluid.toJson());
        }

        @Override
        public byte getId() {
            return ID;
        }

        @Override
        public String getName() {
            return NAME;
        }
    }

    record BlockAbove(BlockIngredient block) implements BarrelRecipeCondition {
        private static final String NAME = "block_above";
        private static final byte ID = 1;

        public BlockAbove(JsonObject json) {
            this(BlockIngredient.fromJson(JsonHelper.getElement(json, "block")));
        }

        public BlockAbove(PacketByteBuf buf) {
            this(BlockIngredient.fromPacket(buf));
        }

        @Override
        public boolean check(World world, BarrelBlockEntity barrel) {
            return block.test(world.getBlockState(barrel.getPos().up()));
        }

        @Override
        public void writePacket(PacketByteBuf buf) {
            block.toPacket(buf);
        }

        @Override
        public void writeJson(JsonObject json) {
            json.add("block", block.toJson());
        }

        @Override
        public byte getId() {
            return ID;
        }

        @Override
        public String getName() {
            return NAME;
        }
    }

    record BlockBelow(BlockIngredient block) implements BarrelRecipeCondition {
        private static final String NAME = "block_below";
        private static final byte ID = 2;

        public BlockBelow(JsonObject json) {
            this(BlockIngredient.fromJson(JsonHelper.getElement(json, "block")));
        }

        public BlockBelow(PacketByteBuf buf) {
            this(BlockIngredient.fromPacket(buf));
        }

        @Override
        public boolean check(World world, BarrelBlockEntity barrel) {
            return block.test(world.getBlockState(barrel.getPos().down()));
        }

        @Override
        public void writePacket(PacketByteBuf buf) {
            block.toPacket(buf);
        }

        @Override
        public void writeJson(JsonObject json) {
            json.add("block", block.toJson());
        }

        @Override
        public byte getId() {
            return ID;
        }

        @Override
        public String getName() {
            return NAME;
        }
    }

    record FluidIn(FluidIngredient fluid) implements BarrelRecipeCondition {
        private static final String NAME = "fluid_in";
        private static final byte ID = 3;

        public FluidIn(JsonObject json) {
            this(FluidIngredient.fromJson(JsonHelper.getElement(json, "fluid")));
        }

        public FluidIn(PacketByteBuf buf) {
            this(FluidIngredient.fromPacket(buf));
        }

        @Override
        public boolean check(World world, BarrelBlockEntity barrel) {
            if (barrel.getState() != BarrelState.FLUID && barrel.getState() != BarrelState.EMPTY) return false;
            if (!fluid.test(barrel.getFluid().getFluid())) return false;
            if (barrel.getFluidAmount() < FluidConstants.BUCKET) return false;
            return true;
        }

        @Override
        public void writePacket(PacketByteBuf buf) {
            fluid.toPacket(buf);
        }

        @Override
        public void writeJson(JsonObject json) {
            json.add("fluid", fluid.toJson());
        }

        @Override
        public byte getId() {
            return ID;
        }

        @Override
        public String getName() {
            return NAME;
        }
    }
}
