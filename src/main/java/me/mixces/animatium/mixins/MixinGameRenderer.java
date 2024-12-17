package me.mixces.animatium.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.animatium.config.AnimatiumConfig;
import me.mixces.animatium.util.ViewBobbingStorage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "bobView", at = @At("TAIL"))
    private void animatium$fixVerticalBobbingTilt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (AnimatiumConfig.fixVerticalBobbingTilt && this.client.getCameraEntity() instanceof PlayerEntity playerEntity) {
            ViewBobbingStorage bobbingAccessor = (ViewBobbingStorage) playerEntity;
            float j = MathHelper.lerp(tickDelta, bobbingAccessor.animatium$getPreviousBobbingTilt(), bobbingAccessor.animatium$getBobbingTilt());
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(j));
        }
    }

    @WrapWithCondition(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V"))
    private boolean animatium$minimalViewBobbing(GameRenderer instance, MatrixStack matrices, float tickDelta) {
        return !AnimatiumConfig.minimalViewBobbing;
    }

    @WrapOperation(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;shouldRenderBlockOutline()Z"))
    private boolean animatium$e(GameRenderer instance, Operation<Boolean> original) {
        if (AnimatiumConfig.persistentBlockOutline) {
            return true;
        } else {
            return original.call(instance);
        }
    }
}
