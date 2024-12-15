package me.mixces.animatium.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.animatium.config.AnimatiumConfig;
import me.mixces.animatium.mixins.accessor.CameraAccessor;
import me.mixces.animatium.util.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.client.render.entity.state.FishingBobberEntityState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntityRenderer.class)
public abstract class MixinFishingBobberEntityRenderer extends EntityRenderer<FishingBobberEntity, FishingBobberEntityState> {
    protected MixinFishingBobberEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/FishingBobberEntityState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;peek()Lnet/minecraft/client/util/math/MatrixStack$Entry;", ordinal = 0))
    private void animatium$oldFishingBobberPosition(FishingBobberEntityState fishingBobberEntityState, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (AnimatiumConfig.oldFishingBobberPosition) {
            assert MinecraftClient.getInstance().player != null;
            int multiplier = PlayerUtils.getHandMultiplier(MinecraftClient.getInstance().player);
            // TODO: Fix line
            matrices.translate(multiplier * 0.5F, 0.0F, 0.0F);
        }
    }

    @WrapOperation(method = "getHandPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getCameraPosVec(F)Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d animatium$fishingRodLineInterpolation(PlayerEntity instance, float v, Operation<Vec3d> original) {
        if (AnimatiumConfig.fishingRodLineInterpolation) {
            CameraAccessor cameraAccessor = (CameraAccessor) dispatcher.camera;
            float eyeHeight = MathHelper.lerp(v, cameraAccessor.getLastCameraY(), cameraAccessor.getCameraY());
            return PlayerUtils.lerpPlayerWithEyeHeight(instance, v, eyeHeight);
        } else {
            return original.call(instance, v);
        }
    }

    @ModifyExpressionValue(method = "getHandPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isInSneakingPose()Z"))
    private boolean animatium$noMoveFishingRodLine(boolean original) {
        return !AnimatiumConfig.noMoveFishingRodLine && original;
    }

    @ModifyExpressionValue(method = "getHandPos", at = @At(value = "CONSTANT", args = "doubleValue=0.8"))
    private double animatium$oldFishingRodLinePositionThirdPerson(double original) {
        return original + (AnimatiumConfig.oldFishingRodLinePositionThirdPerson ? 0.05 : 0.0);
    }

//    @WrapOperation(method = "getArmHoldingRod", at = @At(value = "CONSTANT", args = "classValue=net/minecraft/item/FishingRodItem"))
//    private static boolean animatium$fixCastLineCheck(Object object, Operation<Boolean> original) {
//        boolean value = original.call(object);
//        if (AnimatiumConfig.fixCastLineCheck) {
//            MinecraftClient client = MinecraftClient.getInstance();
//            assert client.player != null;
//            value = value && !(client.player.getOffHandStack().getItem() instanceof FishingRodItem);
//        }
//        return value;
//    }

    @ModifyArg(method = "updateRenderState(Lnet/minecraft/entity/projectile/FishingBobberEntity;Lnet/minecraft/client/render/entity/state/FishingBobberEntityState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/FishingBobberEntityRenderer;getHandPos(Lnet/minecraft/entity/player/PlayerEntity;FF)Lnet/minecraft/util/math/Vec3d;"), index = 1)
    private float animatium$fixCastLineSwing(float f) {
        assert MinecraftClient.getInstance().player != null;
        int multiplier = PlayerUtils.getHandMultiplier(MinecraftClient.getInstance().player);
        return f * (AnimatiumConfig.fixCastLineSwing ? multiplier : 1);
    }
}
