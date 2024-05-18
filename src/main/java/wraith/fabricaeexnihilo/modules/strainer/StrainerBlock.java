package wraith.fabricaeexnihilo.modules.strainer;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;

public class StrainerBlock extends BlockWithEntity implements Waterloggable {

    public static final VoxelShape SHAPE = VoxelShapes.union(
            createCuboidShape(0, 0, 0, 2, 16, 2),
            createCuboidShape(14, 0, 14, 16, 16, 16),
            createCuboidShape(0, 0, 14, 2, 16, 16),
            createCuboidShape(14, 0, 0, 16, 16, 2),
            VoxelShapes.combineAndSimplify(
                    createCuboidShape(0.5, 2, 0.5, 15.5, 15, 15.5),
                    createCuboidShape(1.5, 2, 1.5, 14.5, 15, 14.5),
                    BooleanBiFunction.ONLY_FIRST
            )
    );
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final MapCodec<StrainerBlock> CODEC = createCodec(StrainerBlock::new);

    public StrainerBlock(Settings settings) {
        super(settings.nonOpaque());
        setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StrainerBlockEntity(pos, state);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, StrainerBlockEntity.TYPE, StrainerBlockEntity::tick);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        if (world.getBlockEntity(pos) instanceof StrainerBlockEntity strainer) {
            ItemScatterer.spawn(world, pos, strainer.getInventory());
            world.updateComparators(pos, this);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        return onUse(state, world, pos, player, hand, hitResult);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        return onUse(state, world, pos, player, player.getActiveHand(), hit).toActionResult();
    }

    public ItemActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof StrainerBlockEntity strainer)) {
            FabricaeExNihilo.LOGGER.warn("Strainer has wrong type of block entity at {}", pos);
            return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        for (int i = 0; i < strainer.getInventory().size(); i++) {
            ItemStack stack = strainer.getInventory().get(i);
            if (stack.isEmpty())
                continue;
            strainer.getInventory().set(i, ItemStack.EMPTY);
            player.getInventory().offerOrDrop(stack);
            strainer.markDirty();
            strainer.markForUpdate();
            return ItemActionResult.SUCCESS;
        }

        return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }
}
