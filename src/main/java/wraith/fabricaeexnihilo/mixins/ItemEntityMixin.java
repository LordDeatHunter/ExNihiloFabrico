package wraith.fabricaeexnihilo.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import wraith.fabricaeexnihilo.modules.ModTags;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;getFluidHeight(Lnet/minecraft/registry/tag/TagKey;)D", ordinal = 0))
    private double applyWaterBuoyancyForCustomFluids(double original) {
        return Math.max(original, getFluidHeight(ModTags.WATER_LIKE));
    }
}
