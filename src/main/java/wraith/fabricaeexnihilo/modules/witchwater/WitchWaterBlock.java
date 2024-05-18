package wraith.fabricaeexnihilo.modules.witchwater;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModEffects;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterEntityRecipe;
import wraith.fabricaeexnihilo.recipe.witchwater.WitchWaterWorldRecipe;

public class WitchWaterBlock extends FluidBlock {

    public WitchWaterBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
    }

    public static void applyStatusEffect(LivingEntity entity, StatusEffectInstance statusEffect) {
        // Grab the potion effect on the entity (null if not active) compare its duration (defaulting to 0) to the new duration
        boolean hasEffect = entity.getActiveStatusEffects().containsKey(statusEffect.getEffectType());
        var entityEffectDuration = hasEffect ? entity.getActiveStatusEffects().get(statusEffect.getEffectType()).getDuration() : Integer.MIN_VALUE;
        if (entityEffectDuration <= statusEffect.getDuration() - 20) {
            entity.addStatusEffect(statusEffect);
        }
    }

    public static boolean fluidInteraction(World world, BlockPos witchPos, BlockPos otherPos) {
        var fluidState = world.getFluidState(otherPos);
        if (fluidState.isEmpty() || fluidState.isIn(WitchWaterFluid.TAG)) {
            return false;
        }
        var recipe = WitchWaterWorldRecipe.find(fluidState.getFluid(), world);
        if (recipe.isEmpty()) {
            return false;
        }
        var changePos = witchPos.offset(Direction.DOWN) == otherPos ? otherPos : witchPos;
        world.setBlockState(changePos, recipe.get().value().getResult().choose(world.random).getDefaultState());
        world.playSound(null, changePos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.7f, 0.8f + world.random.nextFloat() * 0.2f);
        return true;
    }

    // A status effect is used to mark entities that have been processed so that they are no longer processed.
    public static boolean isMarked(LivingEntity entity) {
        return entity.hasStatusEffect(ModEffects.WITCH_WATERED);
    }

    public static void markEntity(LivingEntity entity) {
        applyStatusEffect(entity, WitchWaterStatusEffect.getInstance());
    }

    public static boolean receiveNeighborFluids(World world, BlockPos pos) {
        for (var direction : Direction.values()) {
            var fluidState = world.getFluidState(pos.offset(direction));
            if (fluidState.isEmpty()) {
                continue;
            }
            if (fluidInteraction(world, pos, pos.offset(direction)) && direction != Direction.DOWN) {
                return false;
            }
        }
        return true;
    }

    public static void replaceMob(World world, Entity toKill, @Nullable Entity toSpawn) {
        toKill.remove(Entity.RemovalReason.DISCARDED);
        if (toSpawn != null) {
            if (toSpawn instanceof LivingEntity livingEntity) {
                markEntity(livingEntity);
            }
            world.spawnEntity(toSpawn);
        }
    }

    public static void replaceMob(World world, LivingEntity toKill, EntityType<?> spawnType) {
        var toSpawn = spawnType.create(world);
        if (toSpawn instanceof LivingEntity livingEntity) {
            // Set position and angles
            livingEntity.refreshPositionAndAngles(toKill.getBlockPos(), toKill.getYaw(), toKill.getPitch());
            livingEntity.setVelocity(toKill.getVelocity());
            livingEntity.headYaw = toKill.headYaw;

            // Slime -> Magma Slime
            /*if (toKill instanceof SlimeEntity slimeEntity && livingEntity instanceof MagmaCubeEntity magmaCubeEntity) {
                //TODO: mixin for setting slime size
            }*/

            // Set Health
            livingEntity.setHealth(livingEntity.getMaxHealth() * toKill.getHealth() / toKill.getMaxHealth());

            if (livingEntity instanceof MobEntity mob && world instanceof ServerWorld serverWorld) {
                mob.initialize(serverWorld, world.getLocalDifficulty(mob.getBlockPos()), SpawnReason.CONVERSION, null);
            }
        }
        replaceMob(world, toKill, toSpawn);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world == null || entity == null || !entity.isAlive() || entity.isRemoved()) {
            return;
        }
        if (entity instanceof LivingEntity livingEntity && !isMarked(livingEntity)) {
            switch (livingEntity) {
                case CreeperEntity creeper -> {
                    markEntity(livingEntity);
                    if (!creeper.isIgnited()) {
                        var lightning = EntityType.LIGHTNING_BOLT.create(world);
                        if (world instanceof ServerWorld serverWorld && lightning != null) {
                            lightning.setPos(creeper.getPos().x, creeper.getPos().y, creeper.getPos().z);
                            creeper.onStruckByLightning(serverWorld, lightning);
                        }
                    }
                    creeper.setHealth(creeper.getMaxHealth());
                    return;
                }
                case RabbitEntity rabbit -> {
                    markEntity(rabbit);
                    // Killer Rabbit.
                    if (rabbit.getVariant() != RabbitEntity.RabbitType.EVIL) {
                        rabbit.setVariant(RabbitEntity.RabbitType.EVIL);
                    }
                    return;
                }
                case PlayerEntity player when !player.isCreative() -> {
                    FabricaeExNihilo.CONFIG.get().witchwater().effects().forEach((effect) -> {
                        var type = Registries.STATUS_EFFECT.getEntry(effect.type()).orElseThrow();
                        applyStatusEffect(player, new StatusEffectInstance(type, effect.duration(), effect.amplifier()));
                    });
                    return;
                }
                default -> {
                }
            }
            var recipe = WitchWaterEntityRecipe.find(entity, world);
            if (recipe.isPresent()) {
                replaceMob(world, livingEntity, recipe.get().value().getResult());
                return;
            }
            markEntity(livingEntity);
            return;
        }
        if (entity instanceof ArrowEntity arrow) {
            // Replace arrows with shulker bullets
            var bullet = EntityType.SHULKER_BULLET.create(world);
            if (bullet != null) {
                bullet.setVelocity(arrow.getVelocity());
                bullet.refreshPositionAndAngles(arrow.getBlockPos(), arrow.getYaw(), arrow.getPitch());
            }
            replaceMob(world, arrow, bullet);
        }
        //TODO: item changes
    }

}
