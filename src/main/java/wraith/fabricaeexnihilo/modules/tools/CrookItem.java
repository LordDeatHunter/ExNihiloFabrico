package wraith.fabricaeexnihilo.modules.tools;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.modules.ModTags;

public class CrookItem extends ToolItem {

    public CrookItem(ToolMaterial material, Item.Settings settings) {
        super(material, settings);
    }

    public static boolean isCrook(ItemStack stack) {
        return stack.getItem() instanceof CrookItem || stack.isIn(ModTags.CROOKS);
    }

    @Override
    public float getMiningSpeed(ItemStack stack, BlockState state) {
        return isCorrectForDrops(stack, state) ? getMaterial().getMiningSpeedMultiplier() : 1F;
    }

    @Override
    public boolean isCorrectForDrops(ItemStack stack, BlockState state) {
        return state.isIn(ModTags.CROOKABLES);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && state.getHardness(world, pos) != 0.0f) {
            stack.damage(1, miner, EquipmentSlot.MAINHAND);
        }
        return true;
    }
}