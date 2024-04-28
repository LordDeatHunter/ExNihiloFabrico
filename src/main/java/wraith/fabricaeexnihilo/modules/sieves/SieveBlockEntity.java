package wraith.fabricaeexnihilo.modules.sieves;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;
import wraith.fabricaeexnihilo.config.SieveConfig;
import wraith.fabricaeexnihilo.modules.ModBlocks;
import wraith.fabricaeexnihilo.modules.base.BaseBlockEntity;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.SieveRecipe;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.*;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;
import static wraith.fabricaeexnihilo.modules.sieves.SieveBlock.WATERLOGGED;

public class SieveBlockEntity extends BaseBlockEntity {

    public static final BlockEntityType<SieveBlockEntity> TYPE = FabricBlockEntityTypeBuilder.create(
            SieveBlockEntity::new,
            ModBlocks.SIEVES.values().toArray(new SieveBlock[0])
    ).build(null);
    public static final Identifier BLOCK_ENTITY_ID = id("sieve");
    private double progress = 0.0;
    private long lastClickTick = 0;
    private ItemStack contents = ItemStack.EMPTY;
    private ItemStack mesh = ItemStack.EMPTY;

    public SieveBlockEntity(BlockPos pos, BlockState state) {
        super(TYPE, pos, state);
    }

    public ActionResult activate(BlockState state, PlayerEntity player, Hand hand) {
        if (world == null || player == null) {
            return ActionResult.PASS;
        }

        var held = player.getStackInHand(hand == null ? player.getActiveHand() : hand);
        if (held == null) {
            held = ItemStack.EMPTY;
        }

        if (held.getItem() instanceof BucketItem) {
            return ActionResult.PASS; // Done for fluid logging
        }

        var sieves = getConnectedSieves();
        // Make Progress
        if (!contents.isEmpty()) {
            sieves.forEach(sieve -> sieve.doProgress(player));
            return ActionResult.SUCCESS;
        }

        var item = held.getItem();
        // Removing mesh
        if (!mesh.isEmpty() && player.getStackInHand(hand).isEmpty() && player.isSneaking()) {
            player.getInventory().offerOrDrop(mesh.copy());
            mesh = ItemStack.EMPTY;
            markDirty();
            return ActionResult.SUCCESS;
        } else if (mesh.isEmpty() && isValidMesh(item)) {
            // Add mesh
            mesh = ItemUtils.ofSize(held, 1);
            if (!player.isCreative()) {
                held.decrement(1);
            }
            markDirty();
            return ActionResult.SUCCESS;
        }

        // Add a block
        if (held.isEmpty() || SieveRecipe.find(held.getItem(), state.get(WATERLOGGED), Registries.ITEM.getId(mesh.getItem()), world).isEmpty()) {
            return ActionResult.PASS;
        }
        var finalHeld = held;
        sieves.forEach(sieve -> sieve.setContents(finalHeld, !player.isCreative()));
        return ActionResult.SUCCESS;
    }

    private boolean isValidMesh(Item item) {
        assert world != null;
        return world.getRecipeManager()
                .listAllOfType(ModRecipes.SIEVE)
                .stream()
                .anyMatch(recipe -> recipe.getRolls().containsKey(Registries.ITEM.getId(item)));
    }

    public void doProgress(PlayerEntity player) {
        if (world == null) {
            return;
        }
        if (world.getTime() - lastClickTick < 4) return;
        lastClickTick = world.getTime();

        var config = FabricaeExNihilo.CONFIG.get().sieves();
        var haste = player.getActiveStatusEffects().get(StatusEffects.HASTE);
        var efficiency = config.efficiency() ? EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, mesh) : 0;
        var hasteLevel = config.haste() ? (haste == null ? -1 : haste.getAmplifier()) + 1 : 0;

        progress += config.baseProgress()
                    + config.efficiencyScaleFactor() * efficiency
                    + config.hasteScaleFactor() * hasteLevel;

        //TODO: spawn some particles
        if (progress > 1.0) {
            // The utility method for multiple items is less neat to use
            for (var result : SieveRecipe.find(contents.getItem(), getCachedState().get(WATERLOGGED), Registries.ITEM.getId(mesh.getItem()), world)) {
                ItemScatterer.spawn(world, pos.getX(), pos.getY() + 1, pos.getZ(), result.createStack(world.random));
            }
            progress = 0.0;
            contents = ItemStack.EMPTY;
            markForUpdate();
        }
        markDirty();
    }

    public void dropInventory() {
        Objects.requireNonNull(world, "world");
        ItemScatterer.spawn(world, pos.getX(), pos.getY() + 1, pos.getZ(), mesh);
        ItemScatterer.spawn(world, pos.getX(), pos.getY() + 1, pos.getZ(), contents);
        mesh = ItemStack.EMPTY;
        contents = ItemStack.EMPTY;
    }

    public List<SieveBlockEntity> getConnectedSieves() {
        var sieves = new ArrayList<SieveBlockEntity>();

        if (world == null) {
            return sieves;
        }

        var tested = new HashSet<BlockPos>();
        var stack = new Stack<BlockPos>();
        stack.add(this.pos);

        while (!stack.empty()) {
            var popped = stack.pop();
            // Record that this one has been tested
            tested.add(popped);
            if (matchingSieveAt(world, popped)) {
                if (!(this.world.getBlockEntity(popped) instanceof SieveBlockEntity sieve)) {
                    continue;
                }
                sieves.add(sieve);
                // Add adjacent locations to test to the stack
                Arrays.stream(Direction.values())
                        // Horizontals
                        .filter(dir -> dir.getAxis().isHorizontal())
                        // to BlockPos
                        .map(popped::offset)
                        // Remove already tested positions
                        .filter(dir -> !tested.contains(dir) && !stack.contains(dir))
                        // Remove positions too far away
                        .filter(dir -> Math.abs(this.pos.getX() - dir.getX()) <= FabricaeExNihilo.CONFIG.get().sieves().sieveRadius() &&
                                       Math.abs(this.pos.getZ() - dir.getZ()) <= FabricaeExNihilo.CONFIG.get().sieves().sieveRadius())
                        // Add to the stack to be processed
                        .forEach(stack::add);
            }
        }

        return sieves;
    }

    public ItemStack getContents() {
        return contents;
    }

    @Nullable
    public Fluid getFluid() {
        if (world == null) {
            return null;
        }
        var state = world.getBlockState(pos);
        if (state == null) {
            return null;
        }
        if (!state.get(Properties.WATERLOGGED)) {
            return Fluids.EMPTY;
        }
        return state.getFluidState().getFluid();
    }

    public ItemStack getMesh() {
        return mesh;
    }

    public double getProgress() {
        return progress;
    }

    private boolean matchingSieveAt(@Nullable World world, BlockPos pos) {
        if (world == null) {
            return false;
        }
        if (world.getBlockEntity(pos) instanceof SieveBlockEntity sieve) {
            return ItemStack.areItemsEqual(mesh, sieve.mesh) && world.getFluidState(pos).getFluid() == getFluid();
        }
        return false;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt == null) {
            FabricaeExNihilo.LOGGER.warn("A sieve at {} is missing data.", this.pos);
            return;
        }
        readNbtWithoutWorldInfo(nbt);
    }

    public void readNbtWithoutWorldInfo(NbtCompound nbt) {
        mesh = ItemStack.fromNbt(nbt.getCompound("mesh"));
        contents = ItemStack.fromNbt(nbt.getCompound("contents"));
        progress = nbt.getDouble("progress");
    }

    public void setContents(ItemStack stack, boolean doDrain) {
        if (stack.isEmpty() || !contents.isEmpty()) {
            return;
        }
        contents = ItemUtils.ofSize(stack, 1);
        if (doDrain) {
            stack.decrement(1);
        }
        progress = 0.0;
        markDirty();
        markForUpdate();
    }

    /**
     * NBT Serialization section
     */

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        writeNbtWithoutWorldInfo(nbt);
    }

    public void writeNbtWithoutWorldInfo(NbtCompound nbt) {
        nbt.put("mesh", mesh.writeNbt(new NbtCompound()));
        nbt.put("contents", contents.writeNbt(new NbtCompound()));
        nbt.putDouble("progress", progress);
    }
}