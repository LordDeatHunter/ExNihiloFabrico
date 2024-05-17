package wraith.fabricaeexnihilo.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public abstract class BaseRecipe<T extends RecipeContext> implements Recipe<T> {

    @Override
    public final boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack craft(T inventory, RegistryWrapper.WrapperLookup lookup) {
        return getResult(lookup).copy();
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return getDisplayStack();
    }

    @Override
    public abstract boolean matches(T context, World world);

    /**
     * Get an itemstack that represents the result of this recipe
     *
     * @return An itemstack representing the output of the recipe. Only used for visuals.
     */
    public abstract ItemStack getDisplayStack();

}
