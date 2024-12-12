package me.mixces.animatium.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BipedEntityRenderer.class)
public abstract class MixinBipedEntityRenderer {
    @WrapOperation(method = "updateBipedRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isInSneakingPose()Z"))
    private static boolean sneakAnimationWhileFlying(LivingEntity instance, Operation<Boolean> original) {
        if (AnimatiumConfig.sneakAnimationWhileFlying) {
            return instance.isSneaking() || instance.isInSneakingPose();
        } else {
            return original.call(instance);
        }
    }
}
