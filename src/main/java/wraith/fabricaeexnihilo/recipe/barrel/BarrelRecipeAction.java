package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.BarrelState;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.EntityStack;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;
import java.util.Objects;

public sealed interface BarrelRecipeAction {
    Codec<BarrelRecipeAction> CODEC = Codec.STRING.dispatch(BarrelRecipeAction::getName, BarrelRecipeAction::forType);
    PacketCodec<RegistryByteBuf, BarrelRecipeAction> PACKET_CODEC = PacketCodecs.BYTE.<RegistryByteBuf>cast().dispatch(BarrelRecipeAction::getId, BarrelRecipeAction::forId);

    static MapCodec<? extends BarrelRecipeAction> forType(String name) {
        return switch (name) {
            case SpawnEntity.NAME -> SpawnEntity.CODEC;
            case StoreItem.NAME -> StoreItem.CODEC;
            case StoreFluid.NAME -> StoreFluid.CODEC;
            case ConsumeFluid.NAME -> ConsumeFluid.CODEC;
            case ConvertBlock.NAME -> ConvertBlock.CODEC;
            case DropItem.NAME -> DropItem.CODEC;
            case FillCompost.NAME -> FillCompost.CODEC;
            default -> throw new JsonParseException("Unknown action name: " + name);
        };
    }

    static PacketCodec<RegistryByteBuf, ? extends BarrelRecipeAction> forId(byte id) {
        return switch (id) {
            case SpawnEntity.ID -> SpawnEntity.PACKET_CODEC;
            case StoreItem.ID -> StoreItem.PACKET_CODEC;
            case StoreFluid.ID -> StoreFluid.PACKET_CODEC;
            case ConsumeFluid.ID -> ConsumeFluid.PACKET_CODEC;
            case ConvertBlock.ID -> ConvertBlock.PACKET_CODEC;
            case DropItem.ID -> DropItem.PACKET_CODEC;
            case FillCompost.ID -> FillCompost.PACKET_CODEC;
            default -> throw new IllegalArgumentException("Unknown action id: " + id);
        };
    }

    default boolean canRun(BarrelRecipe recipe, BarrelBlockEntity barrel) {
        return true;
    }

    void apply(ServerWorld world, BarrelBlockEntity barrel);

    void writePacket(RegistryByteBuf buf);

    byte getId();

    String getName();

    MapCodec<? extends BarrelRecipeAction> getCodec();

    PacketCodec<RegistryByteBuf, ? extends BarrelRecipeAction> getPacketCodec();

    record SpawnEntity(EntityStack entities) implements BarrelRecipeAction {
        private static final String NAME = "spawn_entity";
        private static final byte ID = 0;

        public static final MapCodec<SpawnEntity> CODEC = EntityStack.CODEC.fieldOf("entities").xmap(SpawnEntity::new, SpawnEntity::entities);
        public static final PacketCodec<RegistryByteBuf, SpawnEntity> PACKET_CODEC = PacketCodec.of(SpawnEntity::writePacket, SpawnEntity::new);

        public SpawnEntity(RegistryByteBuf buf) {
            this(EntityStack.PACKET_CODEC.decode(buf));
        }

        @Override
        public void apply(ServerWorld world, BarrelBlockEntity barrel) {
            var pos = barrel.getPos().up();
            for (int i = 0; i < entities.getSize(); i++) {
                var entity = entities.getEntity(world, pos);
                if (entity == null) continue;
                world.spawnEntity(entity);
            }
        }

        @Override
        public void writePacket(RegistryByteBuf buf) {
            EntityStack.PACKET_CODEC.encode(buf, entities);
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
        public MapCodec<? extends BarrelRecipeAction> getCodec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ? extends BarrelRecipeAction> getPacketCodec() {
            return PACKET_CODEC;
        }
    }

    record StoreItem(ItemStack stack) implements BarrelRecipeAction {
        private static final String NAME = "store_item";
        private static final byte ID = 1;

        public static final MapCodec<StoreItem> CODEC = ItemStack.CODEC.fieldOf("stack").xmap(StoreItem::new, StoreItem::stack);
        public static final PacketCodec<RegistryByteBuf, StoreItem> PACKET_CODEC = PacketCodec.of(StoreItem::writePacket, StoreItem::new);

        public StoreItem(RegistryByteBuf buf) {
            this(ItemStack.PACKET_CODEC.decode(buf));
        }

        @Override
        public void apply(ServerWorld world, BarrelBlockEntity barrel) {
            barrel.setItem(stack);
        }

        @Override
        public void writePacket(RegistryByteBuf buf) {
            ItemStack.PACKET_CODEC.encode(buf, stack);
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
        public MapCodec<? extends BarrelRecipeAction> getCodec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ? extends BarrelRecipeAction> getPacketCodec() {
            return PACKET_CODEC;
        }
    }

    record StoreFluid(FluidVariant fluid, long amount) implements BarrelRecipeAction {
        private static final String NAME = "store_fluid";
        private static final byte ID = 2;

        public static final MapCodec<StoreFluid> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        FluidVariant.CODEC.fieldOf("fluid").forGetter(StoreFluid::fluid),
                        Codec.LONG.fieldOf("amount").forGetter(StoreFluid::amount)
                ).apply(instance, StoreFluid::new)
        );
        public static final PacketCodec<RegistryByteBuf, StoreFluid> PACKET_CODEC = PacketCodec.of(StoreFluid::writePacket, StoreFluid::new);

        public StoreFluid(RegistryByteBuf buf) {
            this(FluidVariant.PACKET_CODEC.decode(buf), buf.readVarLong());
        }

        @Override
        public void apply(ServerWorld world, BarrelBlockEntity barrel) {
            barrel.setFluid(fluid, amount);
        }

        @Override
        public void writePacket(RegistryByteBuf buf) {
            FluidVariant.PACKET_CODEC.encode(buf, fluid);
            buf.writeVarLong(amount);
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
        public MapCodec<? extends BarrelRecipeAction> getCodec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ? extends BarrelRecipeAction> getPacketCodec() {
            return PACKET_CODEC;
        }
    }

    record ConsumeFluid(FluidIngredient fluid, long amount) implements BarrelRecipeAction {
        private static final String NAME = "consume_fluid";
        private static final byte ID = 3;

        public static final MapCodec<ConsumeFluid> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        FluidIngredient.CODEC.fieldOf("fluid").forGetter(ConsumeFluid::fluid),
                        Codec.LONG.fieldOf("amount").forGetter(ConsumeFluid::amount)
                ).apply(instance, ConsumeFluid::new)
        );
        public static final PacketCodec<RegistryByteBuf, ConsumeFluid> PACKET_CODEC = PacketCodec.of(ConsumeFluid::writePacket, ConsumeFluid::new);

        public ConsumeFluid(RegistryByteBuf buf) {
            this(FluidIngredient.fromPacket(buf), buf.readVarLong());
        }

        @Override
        public boolean canRun(BarrelRecipe recipe, BarrelBlockEntity barrel) {
            return fluid.test(barrel.getFluid().getFluid()) && barrel.getFluidAmount() >= amount;
        }

        @Override
        public void apply(ServerWorld world, BarrelBlockEntity barrel) {
            barrel.setFluid(barrel.getFluid(), barrel.getFluidAmount() - amount);
        }

        @Override
        public void writePacket(RegistryByteBuf buf) {
            fluid.toPacket(buf);
            buf.writeVarLong(amount);
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
        public MapCodec<? extends BarrelRecipeAction> getCodec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ? extends BarrelRecipeAction> getPacketCodec() {
            return PACKET_CODEC;
        }
    }

    record ConvertBlock(BlockIngredient filter, BlockState result) implements BarrelRecipeAction {
        private static final String NAME = "convert_block";
        private static final byte ID = 4;
        private static final PacketCodec<RegistryByteBuf, BlockState> BLOCKSTATE_PACKET_CODEC = PacketCodecs.unlimitedRegistryCodec(BlockState.CODEC);

        public static MapCodec<ConvertBlock> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        BlockIngredient.CODEC.fieldOf("filter").forGetter(ConvertBlock::filter),
                        BlockState.CODEC.fieldOf("result").forGetter(ConvertBlock::result)
                ).apply(instance, ConvertBlock::new)
        );
        public static final PacketCodec<RegistryByteBuf, ConvertBlock> PACKET_CODEC = PacketCodec.of(ConvertBlock::writePacket, ConvertBlock::new);

        public ConvertBlock(RegistryByteBuf buf) {
            this(BlockIngredient.fromPacket(buf), BLOCKSTATE_PACKET_CODEC.decode(buf));
        }

        @Override
        public boolean canRun(BarrelRecipe recipe, BarrelBlockEntity barrel) {
            var world = Objects.requireNonNull(barrel.getWorld(), "world is null");
            var pos = barrel.getPos();
            var radius = FabricaeExNihilo.CONFIG.get().barrels().leakRadius();

            return BlockPos.stream(pos.add(-radius, 0, -radius), pos.add(radius, -2, radius))
                    .map(world::getBlockState)
                    .anyMatch(filter);
        }

        @Override
        public void apply(ServerWorld world, BarrelBlockEntity barrel) {
            var pos = barrel.getPos();
            var radius = FabricaeExNihilo.CONFIG.get().barrels().leakRadius();

            var positions = BlockPos.stream(pos.add(-radius, 0, -radius), pos.add(radius, -2, radius))
                    .map(BlockPos::toImmutable)
                    .filter(candidate -> filter.test(world.getBlockState(candidate)))
                    .toList();
            var chosen = positions.get(world.random.nextInt(positions.size()));
            world.setBlockState(chosen, result, Block.NOTIFY_LISTENERS);
        }

        @Override
        public void writePacket(RegistryByteBuf buf) {
            BlockIngredient.toPacket(buf, filter);
            BLOCKSTATE_PACKET_CODEC.encode(buf, result);
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
        public MapCodec<? extends BarrelRecipeAction> getCodec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ? extends BarrelRecipeAction> getPacketCodec() {
            return PACKET_CODEC;
        }
    }

    record DropItem(ItemStack stack) implements BarrelRecipeAction {
        private static final String NAME = "drop_item";
        private static final byte ID = 5;

        public static final MapCodec<? extends BarrelRecipeAction> CODEC = ItemStack.CODEC.fieldOf("stack").xmap(DropItem::new, DropItem::stack);
        public static final PacketCodec<RegistryByteBuf, DropItem> PACKET_CODEC = PacketCodec.of(DropItem::writePacket, DropItem::new);

        public DropItem(RegistryByteBuf buf) {
            this(ItemStack.PACKET_CODEC.decode(buf));
        }

        @Override
        public void apply(ServerWorld world, BarrelBlockEntity barrel) {
            var pos = Vec3d.ofBottomCenter(barrel.getPos().up());
            ItemScatterer.spawn(world, pos.x, pos.y, pos.z, stack.copy());
        }

        @Override
        public void writePacket(RegistryByteBuf buf) {
            ItemStack.PACKET_CODEC.encode(buf, stack);
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
        public MapCodec<? extends BarrelRecipeAction> getCodec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ? extends BarrelRecipeAction> getPacketCodec() {
            return PACKET_CODEC;
        }
    }

    record FillCompost(ItemStack result, float increment) implements BarrelRecipeAction {
        private static final String NAME = "fill_compost";
        private static final byte ID = 6;

        public static MapCodec<FillCompost> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        ItemStack.CODEC.fieldOf("result").forGetter(FillCompost::result),
                        Codec.FLOAT.fieldOf("increment").forGetter(FillCompost::increment)
                ).apply(instance, FillCompost::new)
        );
        public static final PacketCodec<RegistryByteBuf, FillCompost> PACKET_CODEC = PacketCodec.of(FillCompost::writePacket, FillCompost::new);

        public FillCompost(RegistryByteBuf buf) {
            this(ItemStack.PACKET_CODEC.decode(buf), buf.readFloat());
        }

        @Override
        public boolean canRun(BarrelRecipe recipe, BarrelBlockEntity barrel) {
            return barrel.getState() == BarrelState.EMPTY || (barrel.getState() == BarrelState.COMPOST && ItemStack.areEqual(barrel.getItem(), result));
        }

        @Override
        public void apply(ServerWorld world, BarrelBlockEntity barrel) {
            barrel.fillCompost(result, increment);
        }

        @Override
        public void writePacket(RegistryByteBuf buf) {
            ItemStack.PACKET_CODEC.encode(buf, result);
            buf.writeFloat(increment);
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
        public MapCodec<? extends BarrelRecipeAction> getCodec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ? extends BarrelRecipeAction> getPacketCodec() {
            return PACKET_CODEC;
        }
    }
}
