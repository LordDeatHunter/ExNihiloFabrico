package wraith.fabricaeexnihilo.mixins;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.fabricaeexnihilo.util.BonusEnchantingManager;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    /**
     * Inject into getHighestApplicableEnchantmentsAtPower (used by EnchantmentHelper and thus enchantment tables) and
     * check item tags for the applicability of enchantments. The enchantment table skips Enchantment$isAcceptableItem
     * and goes straight for the EnchantmentType's member ... which are all overridden with anonymous functions ....
     *
     * @param stack       Stack to be enchanted
     * @param cir         Callback info.
     */
    @Inject(method = "method_60143", at = @At(value = "RETURN"), cancellable = true)
    private static void fabricaeexnihilo$injectEnchantments(ItemStack stack, boolean bl, RegistryEntry<Enchantment> entry, CallbackInfoReturnable<Boolean> cir) {
        if(cir.getReturnValueZ() || entry == null || entry.getKey().isEmpty()) return;
        var key = entry.getKey().get();
        if (!BonusEnchantingManager.DATA.containsKey(key)) return;
        if (!BonusEnchantingManager.DATA.get(key).contains(stack.getItem())) return;
        cir.setReturnValue(true);
        cir.cancel();
    }
}
