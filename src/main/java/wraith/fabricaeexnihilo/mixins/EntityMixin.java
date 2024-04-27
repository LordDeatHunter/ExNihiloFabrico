package wraith.fabricaeexnihilo.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import wraith.fabricaeexnihilo.modules.ModTags;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract boolean updateMovementInFluid(TagKey<Fluid> tag, double speed);

    @ModifyExpressionValue(method = "checkWaterState", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;updateMovementInFluid(Lnet/minecraft/registry/tag/TagKey;D)Z"))
    private boolean fabricaeexnihilo$fixSwimming(boolean original) {
        return original || updateMovementInFluid(ModTags.WATER_LIKE, 0.014);
    }
}
