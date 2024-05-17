package wraith.fabricaeexnihilo.recipe.barrel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.modules.barrels.BarrelState;
import wraith.fabricaeexnihilo.recipe.util.BlockIngredient;
import wraith.fabricaeexnihilo.recipe.util.EntityStack;
import wraith.fabricaeexnihilo.recipe.util.FluidIngredient;
import java.util.List;
import java.util.Objects;

public sealed interface BarrelRecipeAction {
    default boolean canRun(BarrelRecipe recipe, BarrelBlockEntity barrel) {
        return true;
    }

    void apply(ServerWorld world, BarrelBlockEntity barrel);

    default void toPacket(RegistryByteBuf buf) {
        buf.writeByte(getId());
        writePacket(buf);
    }

    void writePacket(RegistryByteBuf buf);

    static PacketCodec<RegistryByteBuf, BlockState> BLOCKSTATE_PACKET_CODEC = PacketCodecs.unlimitedRegistryCodec(BlockState.CODEC);

    static BarrelRecipeAction fromPacket(RegistryByteBuf buf) {
        var type = buf.readByte();
        return switch (type) {
            case SpawnEntity.ID -> new SpawnEntity(EntityStack.PACKET_CODEC.decode(buf));
            case StoreItem.ID -> new StoreItem(ItemStack.PACKET_CODEC.decode(buf));
            case StoreFluid.ID -> new StoreFluid(FluidVariant.PACKET_CODEC.decode(buf), buf.readVarLong());
            case ConsumeFluid.ID -> new ConsumeFluid(FluidIngredient.fromPacket(buf), buf.readVarLong());
            case ConvertBlock.ID -> new ConvertBlock(BlockIngredient.fromPacket(buf), BLOCKSTATE_PACKET_CODEC.decode(buf));
            case DropItem.ID -> new DropItem(ItemStack.PACKET_CODEC.decode(buf));
            case FillCompost.ID -> new FillCompost(buf);
            default -> throw new JsonParseException("Unknown action type id: " + type);
        };
    }

    PacketCodec<RegistryByteBuf, BarrelRecipeAction> PACKET_CODEC = PacketCodec.of(BarrelRecipeAction::toPacket, BarrelRecipeAction::fromPacket);

    byte getId();

    String getName();
    Codec<BarrelRecipeAction> CODEC = Codec.STRING.dispatch(BarrelRecipeAction::getName, BarrelRecipeAction::forType);
    MapCodec<? extends BarrelRecipeAction> getCodec();

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

    record SpawnEntity(EntityStack entities) implements BarrelRecipeAction {
        private static final String NAME = "spawn_entity";
        private static final byte ID = 0;

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
        public static final MapCodec<SpawnEntity> CODEC = EntityStack.CODEC.fieldOf("entities").xmap(SpawnEntity::new, SpawnEntity::entities);
        @Override
        public MapCodec<? extends BarrelRecipeAction> getCodec() {
            return CODEC;
        }
    }

    record StoreItem(ItemStack stack) implements BarrelRecipeAction {
        private static final String NAME = "store_item";
        private static final byte ID = 1;

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

        public static final MapCodec<StoreItem> CODEC = ItemStack.CODEC.fieldOf("stack").xmap(StoreItem::new, StoreItem::stack);
        @Override
        public MapCodec<? extends BarrelRecipeAction> getCodec() {
            return CODEC;
        }
    }

    record StoreFluid(FluidVariant fluid, long amount) implements BarrelRecipeAction {
        private static final String NAME = "store_fluid";
        private static final byte ID = 2;

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

        public static final MapCodec<StoreFluid> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        FluidVariant.CODEC.fieldOf("fluid").forGetter(StoreFluid::fluid),
                        Codec.LONG.fieldOf("amount").forGetter(StoreFluid::amount)
                ).apply(instance, StoreFluid::new)
        );

        @Override
        public MapCodec<? extends BarrelRecipeAction> getCodec() {
            return CODEC;
        }
    }

    record ConsumeFluid(FluidIngredient fluid, long amount) implements BarrelRecipeAction {
        private static final String NAME = "consume_fluid";
        private static final byte ID = 3;

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

        public static final MapCodec<ConsumeFluid> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        FluidIngredient.CODEC.fieldOf("fluid").forGetter(ConsumeFluid::fluid),
                        Codec.LONG.fieldOf("amount").forGetter(ConsumeFluid::amount)
                ).apply(instance, ConsumeFluid::new)
        );

        @Override
        public MapCodec<? extends BarrelRecipeAction> getCodec() {
            return CODEC;
        }
    }

    record ConvertBlock(BlockIngredient filter, BlockState result) implements BarrelRecipeAction {
        private static final String NAME = "convert_block";
        private static final byte ID = 4;
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
        public static MapCodec<ConvertBlock> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        BlockIngredient.CODEC.fieldOf("filter").forGetter(ConvertBlock::filter),
                        BlockState.CODEC.fieldOf("result").forGetter(ConvertBlock::result)
                ).apply(instance, ConvertBlock::new)
        );
        @Override
        public MapCodec<? extends BarrelRecipeAction> getCodec() {
            return CODEC;
        }
    }

    record DropItem(ItemStack stack) implements BarrelRecipeAction {
        private static final String NAME = "drop_item";
        private static final byte ID = 5;

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

        public static final MapCodec<? extends BarrelRecipeAction> CODEC = ItemStack.CODEC.fieldOf("stack")
                .xmap(DropItem::new, DropItem::stack);

        @Override
        public MapCodec<? extends BarrelRecipeAction> getCodec() {
            return CODEC;
        }
    }

    record FillCompost(ItemStack result, float increment) implements BarrelRecipeAction {
        private static final String NAME = "fill_compost";
        private static final byte ID = 6;


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

        public static MapCodec<FillCompost> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        ItemStack.CODEC.fieldOf("result").forGetter(FillCompost::result),
                        Codec.FLOAT.fieldOf("increment").forGetter(FillCompost::increment)
                ).apply(instance, FillCompost::new)
        );

        @Override
        public MapCodec<? extends BarrelRecipeAction> getCodec() {
            return CODEC;
        }
    }
}
