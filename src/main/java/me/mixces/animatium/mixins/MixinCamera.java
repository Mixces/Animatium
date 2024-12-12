package me.mixces.animatium.mixins;

import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.BlockView;
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
    private void removeSmoothSneaking(CallbackInfo ci) {
        if (AnimatiumConfig.removeSmoothSneaking) {
            this.lastCameraY = cameraY;
            this.cameraY = this.focusedEntity.getStandingEyeHeight();
        }
    }

    @Inject(method = "update", at = @At(value = "TAIL"))
    private void oldCameraVersion(BlockView area, Entity entity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (AnimatiumConfig.cameraVersion != AnimatiumConfig.CameraVersion.LATEST) {
            // TODO: Fix accuracies for different states, like in bed, in third person, etc...
            final int ordinal = AnimatiumConfig.cameraVersion.ordinal();
            if (ordinal <= AnimatiumConfig.CameraVersion.v1_14_v1_14_3.ordinal()) {
                // <= 1.14.3
                if (!thirdPerson && !(entity instanceof LivingEntity && ((LivingEntity) entity).isSleeping())) {
                    this.moveBy(-0.05000000074505806F, 0.0F, 0.0F);
                }

                // <= 1.13.2
                if (ordinal <= AnimatiumConfig.CameraVersion.v1_9_v1_13_2.ordinal()) {
                    this.moveBy(0.1F, 0.0F, 0.0F);

                    // <= 1.8
                    if (ordinal == AnimatiumConfig.CameraVersion.v1_8.ordinal()) {
                        this.moveBy(-0.15F, 0, 0); // unfixing parallax
                    }
                }
            }
        }
    }
}
