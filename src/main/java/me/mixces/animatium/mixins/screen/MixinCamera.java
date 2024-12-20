package me.mixces.animatium.mixins.screen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.animatium.config.AnimatiumConfig;
import me.mixces.animatium.util.CameraVersion;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class MixinCamera {
    @Shadow
    private float lastCameraY;

    @Shadow
    private float cameraY;

    @Shadow
    private Entity focusedEntity;

    @Shadow
    protected abstract void moveBy(float f, float g, float h);

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", shift = At.Shift.BEFORE))
    private void animatium$removeSmoothSneaking(CallbackInfo ci) {
        if (AnimatiumConfig.getInstance().removeSmoothSneaking) {
            this.lastCameraY = cameraY;
            this.cameraY = this.focusedEntity.getStandingEyeHeight();
        }
    }

    @WrapOperation(method = "updateEyeHeight", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/render/Camera;cameraY:F"))
    private void animatium$oldSneakAnimationInterpolation(Camera instance, float value, Operation<Void> original) {
        if (AnimatiumConfig.getInstance().oldSneakAnimationInterpolation && !AnimatiumConfig.getInstance().removeSmoothSneaking && focusedEntity.getStandingEyeHeight() < cameraY) {
            cameraY = focusedEntity.getStandingEyeHeight();
        } else {
            original.call(instance, value);
        }
    }

    // TODO/NOTE: Could we also just do this in TransparentBlock?
    @WrapOperation(method = "clipToSpace", at = @At(value = "FIELD", target = "Lnet/minecraft/world/RaycastContext$ShapeType;VISUAL:Lnet/minecraft/world/RaycastContext$ShapeType;"))
    private RaycastContext.ShapeType animatium$disableCameraTransparentPassthrough(Operation<RaycastContext.ShapeType> original) {
        if (AnimatiumConfig.getInstance().disableCameraTransparentPassthrough) {
            return RaycastContext.ShapeType.OUTLINE;
        } else {
            return original.call();
        }
    }

    @Inject(method = "update", at = @At(value = "TAIL"))
    private void animatium$oldCameraVersion(BlockView area, Entity entity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (AnimatiumConfig.getInstance().cameraVersion != CameraVersion.LATEST && !thirdPerson && !(entity instanceof LivingEntity && ((LivingEntity) entity).isSleeping())) {
            // TODO: Fix bed/sleeping position
            final int ordinal = AnimatiumConfig.getInstance().cameraVersion.ordinal();
            if (ordinal <= CameraVersion.V1_14_V1_14_3.ordinal()) {
                // <= 1.14.3
                this.moveBy(-0.05000000074505806F, 0.0F, 0.0F);
                // <= 1.13.2
                if (ordinal <= CameraVersion.V1_9_V1_13_2.ordinal()) {
                    this.moveBy(0.1F, 0.0F, 0.0F);
                    // <= 1.8
                    if (ordinal == CameraVersion.V1_8.ordinal()) {
                        this.moveBy(-0.15F, 0, 0); // unfixing parallax
                    }
                }
            }
        }
    }
}
