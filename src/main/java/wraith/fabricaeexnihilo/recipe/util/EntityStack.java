package wraith.fabricaeexnihilo.recipe.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class EntityStack {
    public static final Codec<EntityStack> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Registries.ENTITY_TYPE.getCodec()
                    .fieldOf("type")
                    .forGetter(EntityStack::getType),
            Codec.INT
                    .optionalFieldOf("size")
                    .forGetter(entityStack -> Optional.of(entityStack.getSize())),
            NbtCompound.CODEC
                    .optionalFieldOf("data")
                    .forGetter(entityStack -> Optional.of(entityStack.getData()))
    ).apply(instance, (type, size, data) -> new EntityStack(type, size.orElse(1), data.orElse(new NbtCompound()))));
    public static final PacketCodec<RegistryByteBuf, EntityStack> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.registryValue(RegistryKeys.ENTITY_TYPE), EntityStack::getType,
            PacketCodecs.INTEGER, EntityStack::getSize,
            PacketCodecs.UNLIMITED_NBT_COMPOUND, EntityStack::getData,
            EntityStack::new
    );
    public static final EntityStack EMPTY = new EntityStack(EntityType.PIG, 0);
    private EntityType<?> type;
    private int size;
    private NbtCompound data;

    public EntityStack(Identifier identifier, int size, NbtCompound data) {
        this(Registries.ENTITY_TYPE.get(identifier), size, data);
    }

    public EntityStack(EntityType<?> entityType, int size, NbtCompound data) {
        this.type = entityType;
        this.size = size;
        this.data = data;
    }

    public EntityStack(EntityType<?> entityType, int size) {
        this(entityType, size, new NbtCompound());
    }

    public boolean isEmpty() {
        return this == EMPTY || size == 0;
    }

    public EntityType<?> getType() {
        return type;
    }

    public void setType(EntityType<?> type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public NbtCompound getData() {
        return data;
    }

    public void setData(NbtCompound data) {
        this.data = data;
    }

    public Entity getEntity(ServerWorld world, BlockPos pos, SpawnReason spawnType) {
        var entity = type.create(world, null, pos, spawnType, true, true);
        NbtComponent.of(data).applyToEntity(entity);
        return entity;
    }

    public Entity getEntity(ServerWorld world, BlockPos pos) {
        return getEntity(world, pos, SpawnReason.SPAWNER);
    }

    public EntityStack copy() {
        return new EntityStack(this.type, this.size, this.data.copy());
    }

}