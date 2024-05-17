package wraith.fabricaeexnihilo.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import wraith.fabricaeexnihilo.modules.base.EnchantableBlockEntity;
import wraith.fabricaeexnihilo.modules.base.EnchantmentContainer;

import java.util.List;

public class CopyEnchantmentsLootFunction extends ConditionalLootFunction {
    public static final MapCodec<CopyEnchantmentsLootFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> addConditionsField(instance).apply(instance, CopyEnchantmentsLootFunction::new)
    );

    public static final LootFunctionType<CopyEnchantmentsLootFunction> TYPE = new LootFunctionType<>(CODEC);

    protected CopyEnchantmentsLootFunction(List<LootCondition> conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        if (context.get(LootContextParameters.BLOCK_ENTITY) instanceof EnchantableBlockEntity enchantable && !enchantable.getEnchantmentContainer().getEnchantments().isEmpty()) {
            EnchantmentContainer.addEnchantments(stack, enchantable.getEnchantmentContainer());
        }
        return stack;
    }

    @Override
    public LootFunctionType<CopyEnchantmentsLootFunction> getType() {
        return TYPE;
    }

    public static CopyEnchantmentsLootFunction.Builder builder() {
        return new CopyEnchantmentsLootFunction.Builder();
    }

    public static class Builder extends ConditionalLootFunction.Builder<CopyEnchantmentsLootFunction.Builder> {
        @Override
        protected CopyEnchantmentsLootFunction.Builder getThisBuilder() {
            return this;
        }

        @Override
        public LootFunction build() {
            return new CopyEnchantmentsLootFunction(getConditions());
        }
    }
}
