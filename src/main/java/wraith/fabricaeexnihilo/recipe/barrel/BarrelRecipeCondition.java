package wraith.fabricaeexnihilo.recipe.barrel;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.BarrelState;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;

public sealed interface BarrelRecipeCondition {
    Codec<BarrelRecipeCondition> CODEC = Codec.STRING.dispatch(BarrelRecipeCondition::getName, BarrelRecipeCondition::fromType);
    PacketCodec<RegistryByteBuf, BarrelRecipeCondition> PACKET_CODEC = PacketCodecs.BYTE.<RegistryByteBuf>cast().dispatch(BarrelRecipeCondition::getId, BarrelRecipeCondition::fromId);

    boolean check(World world, BarrelBlockEntity barrel);

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

    static PacketCodec<RegistryByteBuf, ? extends BarrelRecipeCondition> fromId(byte id) {
        return switch (id) {
            case FluidAbove.ID -> FluidAbove.PACKET_CODEC;
            case BlockAbove.ID -> BlockAbove.PACKET_CODEC;
            case BlockBelow.ID -> BlockBelow.PACKET_CODEC;
            case FluidIn.ID -> FluidIn.PACKET_CODEC;
            default -> throw new IllegalArgumentException("Unknown condition type id: " + id);
        };
    }

    MapCodec<? extends BarrelRecipeCondition> getCodec();

    PacketCodec<RegistryByteBuf, ? extends BarrelRecipeCondition> getPacketCodec();

    record FluidAbove(FluidIngredient fluid) implements BarrelRecipeCondition {
        private static final String NAME = "fluid_above";
        private static final byte ID = 0;

        public static final MapCodec<FluidAbove> CODEC = FluidIngredient.CODEC.fieldOf("fluid").xmap(FluidAbove::new, FluidAbove::fluid);
        public static final PacketCodec<RegistryByteBuf, FluidAbove> PACKET_CODEC = PacketCodec.of(FluidAbove::writePacket, FluidAbove::new);

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

        @Override
        public MapCodec<? extends BarrelRecipeCondition> getCodec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ? extends BarrelRecipeCondition> getPacketCodec() {
            return PACKET_CODEC;
        }
    }

    record BlockAbove(BlockIngredient block) implements BarrelRecipeCondition {
        private static final String NAME = "block_above";
        private static final byte ID = 1;

        public static final MapCodec<BlockAbove> CODEC = BlockIngredient.CODEC.fieldOf("block").xmap(BlockAbove::new, BlockAbove::block);
        public static final PacketCodec<RegistryByteBuf, BlockAbove> PACKET_CODEC = PacketCodec.of(BlockAbove::writePacket, BlockAbove::new);

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

        @Override
        public MapCodec<? extends BarrelRecipeCondition> getCodec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ? extends BarrelRecipeCondition> getPacketCodec() {
            return PACKET_CODEC;
        }
    }

    record BlockBelow(BlockIngredient block) implements BarrelRecipeCondition {
        private static final String NAME = "block_below";
        private static final byte ID = 2;

        public static final MapCodec<BlockBelow> CODEC = BlockIngredient.CODEC.fieldOf("block").xmap(BlockBelow::new, BlockBelow::block);
        public static final PacketCodec<RegistryByteBuf, BlockBelow> PACKET_CODEC = PacketCodec.of(BlockBelow::writePacket, BlockBelow::new);

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

        @Override
        public MapCodec<? extends BarrelRecipeCondition> getCodec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ? extends BarrelRecipeCondition> getPacketCodec() {
            return PACKET_CODEC;
        }
    }

    record FluidIn(FluidIngredient fluid) implements BarrelRecipeCondition {
        private static final String NAME = "fluid_in";
        private static final byte ID = 3;

        public static final MapCodec<FluidIn> CODEC = FluidIngredient.CODEC.fieldOf("fluid").xmap(FluidIn::new, FluidIn::fluid);
        public static final PacketCodec<RegistryByteBuf, FluidIn> PACKET_CODEC = PacketCodec.of(FluidIn::writePacket, FluidIn::new);

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

        @Override
        public MapCodec<? extends BarrelRecipeCondition> getCodec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ? extends BarrelRecipeCondition> getPacketCodec() {
            return PACKET_CODEC;
        }
    }
}
