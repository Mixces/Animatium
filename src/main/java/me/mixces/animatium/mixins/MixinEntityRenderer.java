package me.mixces.animatium.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.EntityPose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
    @WrapOperation(method = "renderLabelIfPresent", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/state/EntityRenderState;sneaking:Z"))
    private boolean animatium$sneakAnimationWhileFlying(EntityRenderState instance, Operation<Boolean> original) {
        if (AnimatiumConfig.sneakAnimationWhileFlying && instance instanceof LivingEntityRenderState livingEntityRenderState) {
            return livingEntityRenderState.sneaking || livingEntityRenderState.isInPose(EntityPose.CROUCHING);
        } else {
            return original.call(instance);
        }
    }

    @WrapOperation(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getTextBackgroundOpacity(F)F"))
    private float animatium$hideNameTagBackground(GameOptions instance, float fallback, Operation<Float> original) {
        if (AnimatiumConfig.hideNameTagBackground) {
            return 0F;
        } else {
            return original.call(instance, fallback);
        }
    }

    @ModifyArg(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)I"), index = 4)
    private boolean animatium$applyTextShadowToNametag(boolean shadow) {
        if (AnimatiumConfig.applyTextShadowToNametag) {
            return true;
        } else {
            return shadow;
        }
    }
}
