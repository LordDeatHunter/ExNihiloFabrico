package wraith.fabricaeexnihilo.recipe.barrel;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.BarrelState;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;

public sealed interface BarrelRecipeCondition {
    boolean check(World world, BarrelBlockEntity barrel);

    default void toPacket(RegistryByteBuf buf) {
        buf.writeByte(getId());
        writePacket(buf);
    }

    void writePacket(RegistryByteBuf buf);

    byte getId();

    String getName();

    static MapCodec<? extends BarrelRecipeCondition> fromType(String type) {
        return switch (type) {
            case FluidAbove.NAME -> FluidAbove.CODEC;
            case BlockAbove.NAME -> BlockAbove.CODEC;
            case BlockBelow.NAME -> BlockBelow.CODEC;
            case FluidIn.NAME -> FluidIn.CODEC;
            default -> throw new IllegalArgumentException("Unknown condition type: " + type);
        };
    }

    static BarrelRecipeCondition fromPacket(RegistryByteBuf buf) {
        var type = buf.readByte();
        return switch (type) {
            case FluidAbove.ID -> new FluidAbove(buf);
            case BlockAbove.ID -> new BlockAbove(buf);
            case BlockBelow.ID -> new BlockBelow(buf);
            case FluidIn.ID -> new FluidIn(buf);
            default -> throw new IllegalArgumentException("Unknown condition type id: " + type);
        };
    }

    MapCodec<? extends BarrelRecipeCondition> getCodec();

    Codec<BarrelRecipeCondition> CODEC = Codec.STRING.dispatch(BarrelRecipeCondition::getName, BarrelRecipeCondition::fromType);

    PacketCodec<RegistryByteBuf, BarrelRecipeCondition> PACKET_CODEC = PacketCodec.of(BarrelRecipeCondition::toPacket, BarrelRecipeCondition::fromPacket);

    record FluidAbove(FluidIngredient fluid) implements BarrelRecipeCondition {
        private static final String NAME = "fluid_above";
        private static final byte ID = 0;

        public FluidAbove(RegistryByteBuf buf) {
            this(FluidIngredient.fromPacket(buf));
        }

        @Override
        public boolean check(World world, BarrelBlockEntity barrel) {
            return fluid.test(world.getFluidState(barrel.getPos().up()).getFluid());
        }

        @Override
        public void writePacket(RegistryByteBuf buf) {
            fluid.toPacket(buf);
        }

        @Override
        public byte getId() {
            return ID;
        }

        @Override
        public String getName() {
            return NAME;
        }

        public static final MapCodec<FluidAbove> CODEC = FluidIngredient.CODEC.fieldOf("fluid").xmap(FluidAbove::new, FluidAbove::fluid);

        @Override
        public MapCodec<? extends BarrelRecipeCondition> getCodec() {
            return CODEC;
        }
    }

    record BlockAbove(BlockIngredient block) implements BarrelRecipeCondition {
        private static final String NAME = "block_above";
        private static final byte ID = 1;

        public BlockAbove(RegistryByteBuf buf) {
            this(BlockIngredient.fromPacket(buf));
        }

        @Override
        public boolean check(World world, BarrelBlockEntity barrel) {
            return block.test(world.getBlockState(barrel.getPos().up()));
        }

        @Override
        public void writePacket(RegistryByteBuf buf) {
            BlockIngredient.toPacket(buf, block);
        }

        @Override
        public byte getId() {
            return ID;
        }

        @Override
        public String getName() {
            return NAME;
        }

        public static final MapCodec<BlockAbove> CODEC = BlockIngredient.CODEC.fieldOf("block").xmap(BlockAbove::new, BlockAbove::block);

        @Override
        public MapCodec<? extends BarrelRecipeCondition> getCodec() {
            return CODEC;
        }
    }

    record BlockBelow(BlockIngredient block) implements BarrelRecipeCondition {
        private static final String NAME = "block_below";
        private static final byte ID = 2;

        public BlockBelow(RegistryByteBuf buf) {
            this(BlockIngredient.fromPacket(buf));
        }

        @Override
        public boolean check(World world, BarrelBlockEntity barrel) {
            return block.test(world.getBlockState(barrel.getPos().down()));
        }

        @Override
        public void writePacket(RegistryByteBuf buf) {
            BlockIngredient.toPacket(buf, block);
        }

        @Override
        public byte getId() {
            return ID;
        }

        @Override
        public String getName() {
            return NAME;
        }

        public static final MapCodec<BlockBelow> CODEC = BlockIngredient.CODEC.fieldOf("block").xmap(BlockBelow::new, BlockBelow::block);

        @Override
        public MapCodec<? extends BarrelRecipeCondition> getCodec() {
            return CODEC;
        }
    }

    record FluidIn(FluidIngredient fluid) implements BarrelRecipeCondition {
        private static final String NAME = "fluid_in";
        private static final byte ID = 3;

        public FluidIn(RegistryByteBuf buf) {
            this(FluidIngredient.fromPacket(buf));
        }

        @Override
        public boolean check(World world, BarrelBlockEntity barrel) {
            if (barrel.getState() != BarrelState.FLUID && barrel.getState() != BarrelState.EMPTY) return false;
            if (!fluid.test(barrel.getFluid().getFluid())) return false;
            return barrel.getFluidAmount() >= FluidConstants.BUCKET;
        }

        @Override
        public void writePacket(RegistryByteBuf buf) {
            fluid.toPacket(buf);
        }

        @Override
        public byte getId() {
            return ID;
        }

        @Override
        public String getName() {
            return NAME;
        }

        public static final MapCodec<FluidIn> CODEC = FluidIngredient.CODEC.fieldOf("fluid").xmap(FluidIn::new, FluidIn::fluid);

        @Override
        public MapCodec<? extends BarrelRecipeCondition> getCodec() {
            return CODEC;
        }
    }
}
