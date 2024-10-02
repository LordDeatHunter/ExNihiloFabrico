package wraith.fabricaeexnihilo.util;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

/**
 * Utility class for an inventory that isn't able to contain any items.
 */
public interface EmptyInventory extends RecipeInput {
    @Override
    default int getSize() {
        return 0;
    }

    // Pretend to not be empty in order for RecipeManager.getFirstMatch to work.
    @Override
    default boolean isEmpty() {
        return false;
    }

    @Override
    default ItemStack getStackInSlot(int slot) {
        throw new UnsupportedOperationException("This inventory is empty!");
    }
}
