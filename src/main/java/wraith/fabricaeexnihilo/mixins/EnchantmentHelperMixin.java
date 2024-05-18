package wraith.fabricaeexnihilo.mixins;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.resource.featuretoggle.FeatureSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.fabricaeexnihilo.util.BonusEnchantingManager;
import java.util.Collections;
import java.util.List;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    /**
     * Inject into getHighestApplicableEnchantmentsAtPower (used by EnchantmentHelper and thus enchantment tables) and
     * check item tags for the applicability of enchantments. The enchantment table skips Enchantment$isAcceptableItem
     * and goes straight for the EnchantmentType's member ... which are all overridden with anonymous functions ....
     *
     * @param level       Enchantment setup power
     * @param stack       Stack to be enchanted
     * @param treasureAllowed Include treasure enchantments?
     * @param cir         Callback info.
     */
    @Inject(method = "getPossibleEntries", at = @At(value = "RETURN"))
    private static void fabricaeexnihilo$injectEnchantments(FeatureSet enabledFeatures, int level, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
        var list = cir.getReturnValue();
        Registries.ENCHANTMENT.stream()
                .filter(enchantment -> (treasureAllowed || !enchantment.isTreasure()) && BonusEnchantingManager.DATA.getOrDefault(enchantment, Collections.emptyList()).contains(stack.getItem()))
                .map(enchantmentLevelEntry -> {
                    for (var i = enchantmentLevelEntry.getMaxLevel(); i > enchantmentLevelEntry.getMinLevel() - 1; i--) {
                        if (level >= enchantmentLevelEntry.getMinPower(i) && level <= enchantmentLevelEntry.getMaxPower(i)) {
                            return new EnchantmentLevelEntry(enchantmentLevelEntry, i);
                        }
                    }
                    return new EnchantmentLevelEntry(enchantmentLevelEntry, 1);
                })
                .filter(tag -> list.stream().noneMatch(entry -> entry.enchantment == tag.enchantment && entry.level == tag.level))
                .forEach(list::add);
    }
}
