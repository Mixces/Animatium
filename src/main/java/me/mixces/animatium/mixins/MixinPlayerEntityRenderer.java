package me.mixces.animatium.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntityRenderer.class)
public abstract class MixinPlayerEntityRenderer {
    @WrapOperation(method = "getPositionOffset(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;)Lnet/minecraft/util/math/Vec3d;", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;isInSneakingPose:Z"))
    private boolean animatium$fixSneakingFeetPosition(PlayerEntityRenderState instance, Operation<Boolean> original) {
        if (AnimatiumConfig.fixSneakingFeetPosition) {
            return false;
        } else {
            return original.call(instance);
        }
    }

    // TODO/NOTE: Fix inaccuracies/brokenness
    @WrapOperation(method = "updateCape", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerpAngleDegrees(FFF)F"))
    private static float undoLerpAngles(float delta, float start, float end, Operation<Float> original) {
        if (AnimatiumConfig.oldCapeMovement) {
            return MathHelper.lerp(delta, start, end);
        } else {
            return original.call(delta, start, end);
        }
    }

    @WrapOperation(method = "updateCape", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F", ordinal = 1))
    private static float undoClampOne(float value, float min, float max, Operation<Float> original) {
        if (AnimatiumConfig.oldCapeMovement) {
            return value;
        } else {
            return original.call(value, min, max);
        }
    }

    @WrapOperation(method = "updateCape", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F", ordinal = 2))
    private static float undoClampTwo(float value, float min, float max, Operation<Float> original) {
        if (AnimatiumConfig.oldCapeMovement) {
            return value;
        } else {
            return original.call(value, min, max);
        }
    }
}
