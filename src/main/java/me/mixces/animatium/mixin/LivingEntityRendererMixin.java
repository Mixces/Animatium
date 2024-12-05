package me.mixces.animatium.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mixces.animatium.hook.EntityRenderDispatcherHook;
import me.mixces.animatium.mixin.access.ICameraMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<S extends LivingEntityRenderState> {

    @ModifyExpressionValue(
            method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;isAlive()Z"
            )
    )
    private boolean animatium$bypassAliveCheck(boolean original) {
        return true;
    }

    @Inject(
            method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V",
                    ordinal = 1
            )
    )
    private void animatium$thirdPersonSneak(S livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        final Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        if (livingEntityRenderState instanceof PlayerEntityRenderState && MinecraftClient.getInstance().player != null) {
            if (((PlayerEntityRenderState) livingEntityRenderState).id == MinecraftClient.getInstance().player.getId()) {
                final float lerpCamera = MathHelper.lerp(EntityRenderDispatcherHook.tickDelta.get(), ((ICameraMixin) camera).getLastCameraY(), ((ICameraMixin) camera).getCameraY());
                matrixStack.translate(0.0F, 1.62F - lerpCamera, 0.0F);
            }
        }
    }
}
