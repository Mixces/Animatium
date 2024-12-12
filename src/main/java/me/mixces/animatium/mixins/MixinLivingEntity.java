package me.mixces.animatium.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.animatium.config.AnimatiumConfig;
import me.mixces.animatium.util.ViewBobbingStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements ViewBobbingStorage {
    @Unique
    private float animatium$bobbingTilt = 0.0F;

    @Unique
    private float animatium$previousBobbingTilt = 0.0F;

    public MixinLivingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public float bodyYaw;

    @Shadow
    public float headYaw;

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;abs(F)F"))
    private float rotateBackwardsWalking(float value, Operation<Float> original) {
        if (AnimatiumConfig.rotateBackwardsWalking) {
            return 0F;
        } else {
            return original.call(value);
        }
    }

    @WrapOperation(method = "turnHead", at = @At(value = "INVOKE", target = "Ljava/lang/Math;abs(F)F"))
    private float removeHeadRotationInterpolation(float g, Operation<Float> original) {
        if (AnimatiumConfig.rotateBackwardsWalking) {
            g = MathHelper.clamp(g, -75.0F, 75.0F);
            this.bodyYaw = this.getYaw() - g;
            if (Math.abs(g) > 50.0F) {
                this.bodyYaw += g * 0.2F;
            }
            return Float.MIN_VALUE;
        } else {
            return original.call(g);
        }
    }

    @Inject(method = "lerpHeadYaw", at = @At("TAIL"))
    public void removeHeadRotationInterpolation(int headTrackingIncrements, double serverHeadYaw, CallbackInfo ci) {
        if (AnimatiumConfig.removeHeadRotationInterpolation) {
            headYaw = (float) serverHeadYaw;
        }
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;tickStatusEffects()V", shift = At.Shift.BEFORE))
    private void updatePreviousBobbingTiltValue(CallbackInfo ci) {
        if (AnimatiumConfig.fixVerticalBobbingTilt) {
            this.animatium$setPreviousBobbingTilt(this.animatium$getBobbingTilt());
        }
    }

    public void animatium$setBobbingTilt(float bobbingTilt) {
        this.animatium$bobbingTilt = bobbingTilt;
    }

    public void animatium$setPreviousBobbingTilt(float previousBobbingTilt) {
        this.animatium$previousBobbingTilt = previousBobbingTilt;
    }

    public float animatium$getBobbingTilt() {
        return animatium$bobbingTilt;
    }

    public float animatium$getPreviousBobbingTilt() {
        return animatium$previousBobbingTilt;
    }
}
