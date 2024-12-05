package me.mixces.animatium.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mixces.animatium.Animatium;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.util.math.MathHelper;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {

    @ModifyExpressionValue(
            method = "getPositionOffset(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;)Lnet/minecraft/util/math/Vec3d;",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;isInSneakingPose:Z"
            )
    )
    private boolean animatium$disableSneakOffset(boolean original) {
        return !Animatium.CONFIG.THIRD_PERSON_SNEAKING && original;
    }

    //todo: fix cape logic
//    @Redirect(
//            method = "updateCape",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/util/math/MathHelper;lerpAngleDegrees(FFF)F"
//            )
//    )
//    private static float animatium$useLerp(float delta, float start, float end) {
//        return MathHelper.lerp(delta, start, end);
//    }
//
//    @Redirect(
//            method = "updateCape",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F",
//                    ordinal = 2
//            )
//    )
//    private static float animatium$removeClamp(float value, float min, float max) {
//        return value;
//    }
}
