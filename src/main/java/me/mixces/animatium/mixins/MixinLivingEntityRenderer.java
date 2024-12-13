package me.mixces.animatium.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.animatium.config.AnimatiumConfig;
import me.mixces.animatium.mixins.accessor.CameraAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<S extends LivingEntityRenderState> {
    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 1))
    private void animatium$syncPlayerModelWithEyeHeight(S livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (AnimatiumConfig.syncPlayerModelWithEyeHeight) {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;
            if (livingEntityRenderState instanceof PlayerEntityRenderState state && player != null && state.id == player.getId()) {
                Camera camera = client.gameRenderer.getCamera();
                CameraAccessor cameraAccessor = (CameraAccessor) camera;
                float cameraLerpValue = MathHelper.lerp(camera.getLastTickDelta(), cameraAccessor.getLastCameraY(), cameraAccessor.getCameraY());
                matrixStack.translate(0.0F, PlayerEntity.STANDING_DIMENSIONS.eyeHeight() - cameraLerpValue, 0.0F);
            }
        }
    }

    @ModifyExpressionValue(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isAlive()Z"))
    private boolean animatium$oldDeathLimbs(boolean original) {
        if (AnimatiumConfig.oldDeathLimbs) {
            return true;
        } else {
            return original;
        }
    }

    @WrapOperation(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;D)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getCameraEntity()Lnet/minecraft/entity/Entity;"))
    private Entity animatium$showNametagInThirdperson(MinecraftClient instance, Operation<Entity> original) {
        if (AnimatiumConfig.showNametagInThirdperson) {
            return null;
        } else {
            return original.call(instance);
        }
    }
}
