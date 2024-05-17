package wraith.fabricaeexnihilo.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.recipe.RecipeEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import wraith.fabricaeexnihilo.modules.tools.CrookItem;
import wraith.fabricaeexnihilo.modules.tools.HammerItem;
import wraith.fabricaeexnihilo.recipe.ToolRecipe;

import java.util.List;

@Mixin(AbstractBlock.class)
public abstract class BlockHarvestMixin {
    @ModifyReturnValue(method = "getDroppedStacks", at = @At("RETURN"))
    public List<ItemStack> fabricaeexnihilo$applyToolRecipes(List<ItemStack> original, BlockState state, LootContextParameterSet.Builder builder) {
        ItemStack tool = builder.get(LootContextParameters.TOOL);
        List<RecipeEntry<ToolRecipe>> recipes;
        if (CrookItem.isCrook(tool))
            recipes = ToolRecipe.find(ToolRecipe.ToolType.CROOK, state, builder.getWorld());
        else if (HammerItem.isHammer(tool))
            recipes = ToolRecipe.find(ToolRecipe.ToolType.HAMMER, state, builder.getWorld());
        else return original;

        return recipes.stream()
                .map(RecipeEntry::value)
                .map(ToolRecipe::getResult)
                .map(loot -> loot.createStack(builder.getWorld().random))
                .toList();
    }
}
