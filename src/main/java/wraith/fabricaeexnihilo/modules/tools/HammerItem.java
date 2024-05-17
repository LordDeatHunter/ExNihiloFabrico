package wraith.fabricaeexnihilo.modules.tools;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.modules.ModTags;

public class HammerItem extends ToolItem {

    public HammerItem(ToolMaterial material, Item.Settings settings) {
        super(material, settings);
    }

    public static boolean isHammer(ItemStack stack) {
        return stack.getItem() instanceof HammerItem || stack.isIn(ModTags.HAMMERS);
    }

    @Override
    public float getMiningSpeed(ItemStack stack, BlockState state) {
        return isCorrectForDrops(stack, state) ? getMaterial().getMiningSpeedMultiplier() : 1F;
    }

    @Override
    public boolean isCorrectForDrops(ItemStack stack, BlockState state) {
        return state.isIn(ModTags.HAMMERABLES);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && state.getHardness(world, pos) != 0.0f) {
            stack.damage(1, miner.getRandom(),
                    miner instanceof ServerPlayerEntity serverPlayerEntity ? serverPlayerEntity : null,
                    () -> miner.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        return true;
    }
}
