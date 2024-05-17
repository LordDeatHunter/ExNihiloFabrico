package wraith.fabricaeexnihilo.modules.crucibles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CrucibleBlock extends BlockWithEntity {
    private static final VoxelShape SHAPE;

    static {
        SHAPE = VoxelShapes.union(
                createCuboidShape(0.0, 0.0, 0.0, 3.0, 3.0, 3.0),
                createCuboidShape(0.0, 0.0, 13.0, 3.0, 3.0, 16.0),
                createCuboidShape(13.0, 0.0, 0.0, 16.0, 3.0, 3.0),
                createCuboidShape(13.0, 0.0, 13.0, 16.0, 3.0, 16.0),
                createCuboidShape(0.0, 3.0, 0.0, 16.0, 16.0, 16.0)
        );
    }

    private final boolean isFireproof;

    public CrucibleBlock(Settings settings, boolean fireproof) {
        super(settings);
        this.isFireproof = fireproof;
    }

    public static final MapCodec<CrucibleBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    createSettingsCodec(),
                    Codec.BOOL.fieldOf("isFireproof").forGetter(block -> block.isFireproof)
            ).apply(instance, CrucibleBlock::new)
    );

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CrucibleBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, CrucibleBlockEntity.TYPE, (world1, blockPos, blockState, crucibleEntity) -> crucibleEntity.tick());
    }

    public boolean isFireproof() {
        return this.isFireproof;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (fromPos == null || !fromPos.equals(pos == null ? null : pos.down()) || world == null) {
            return;
        }
        if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucible) {
            crucible.updateHeat();
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (world == null || world.isClient() || pos == null) {
            return;
        }
        if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucible) {
            crucible.updateHeat();
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world == null) {
            return;
        }
        if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucible) {
            EnchantmentHelper.getEnchantments(itemStack).getEnchantmentsMap().forEach(e -> crucible.getEnchantments().setEnchantmentLevel(e.getKey().value(), e.getIntValue()));
            crucible.updateHeat();
        }
    }

    @Override
    public ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        if (world == null || pos == null) {
            return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        if (world.getBlockEntity(pos) instanceof CrucibleBlockEntity crucible) {
            return crucible.activate(player, hand);
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hitResult);
    }

}