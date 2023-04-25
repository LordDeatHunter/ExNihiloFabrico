package wraith.fabricaeexnihilo.modules.barrels;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.modules.ModEffects;
import wraith.fabricaeexnihilo.modules.barrels.modes.ItemMode;
import wraith.fabricaeexnihilo.modules.fluids.BloodFluid;
import wraith.fabricaeexnihilo.recipe.barrel.MilkingRecipe;

@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class BarrelBlock extends BlockWithEntity {
    private static final VoxelShape SHAPE = createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    private final boolean isFireproof;

    public BarrelBlock(Settings settings, boolean isFireproof) {
        super(settings);
        this.isFireproof = isFireproof;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BarrelBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void precipitationTick(BlockState state, World world, BlockPos pos, Biome.Precipitation precipitation) {
        var blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof BarrelBlockEntity barrel) {
            barrel.precipitationTick(precipitation);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, BarrelBlockEntity.TYPE, BarrelBlockEntity::ticker);
    }

    public boolean isFireproof() {
        return isFireproof;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.getBlockEntity(pos) instanceof BarrelBlockEntity barrelEntity) {
            EnchantmentHelper.get(itemStack).forEach((enchantment, level) -> barrelEntity.getEnchantmentContainer().setEnchantmentLevel(enchantment, level));
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock()) && world.getBlockEntity(pos) instanceof BarrelBlockEntity barrel && barrel.getMode() instanceof ItemMode mode) {
            ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), mode.getStack());
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    // Milking
    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        var blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof BarrelBlockEntity barrel && entity instanceof LivingEntity livingEntity) {
            if (FabricaeExNihilo.CONFIG.modules.barrels.enableBleeding) {
                var thorns = barrel.getEnchantmentContainer().getEnchantmentLevel(Enchantments.THORNS);
                if (thorns > 0
                        && barrel.fluidStorage.simulateInsert(FluidVariant.of(BloodFluid.STILL), 1, null) >= 1
                        && livingEntity.damage(livingEntity.getDamageSources().cactus(), thorns / 2F)) {
                    var amount = FluidConstants.BUCKET * thorns / livingEntity.getMaxHealth();
                    try (Transaction t = Transaction.openOuter()) {
                        barrel.fluidStorage.insert(FluidVariant.of(BloodFluid.STILL), (long) amount, t);
                        t.commit();
                    }
                }
            }
            if (!(livingEntity instanceof PlayerEntity) && !livingEntity.hasStatusEffect(ModEffects.MILKED) && FabricaeExNihilo.CONFIG.modules.barrels.enableMilking) {
                var recipe = MilkingRecipe.find(livingEntity.getType(), world);
                if (recipe.isPresent()) {
                    long inserted;
                    try (Transaction t = Transaction.openOuter()) {
                        inserted = barrel.fluidStorage.insert(recipe.get().getFluid(), recipe.get().getAmount(), t);
                        t.commit();
                    }
                    if (inserted > 0) {
                        livingEntity.addStatusEffect(new StatusEffectInstance(ModEffects.MILKED, recipe.get().getCooldown(), 1, false, false, false));
                    }
                }
            }
        }
        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        if (world == null || pos == null) {
            return ActionResult.PASS;
        }
        var blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof BarrelBlockEntity barrelBlock
                ? barrelBlock.activate(player, hand)
                : ActionResult.PASS;
    }

}