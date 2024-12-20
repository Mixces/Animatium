package me.mixces.animatium.mixins.world.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;abs(F)F"))
    private float animatium$rotateBackwardsWalking(float value, Operation<Float> original) {
        if (AnimatiumConfig.getInstance().rotateBackwardsWalking) {
            return 0F;
        } else {
            return original.call(value);
        }
    }

    @WrapOperation(method = "turnHead", at = @At(value = "INVOKE", target = "Ljava/lang/Math;abs(F)F"))
    private float animatium$removeHeadRotationInterpolation(float g, Operation<Float> original) {
        if (AnimatiumConfig.getInstance().rotateBackwardsWalking) {
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

    @WrapOperation(method = "lerpHeadYaw", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerpAngleDegrees(DDD)D"))
    public double animatium$removeHeadRotationInterpolation(double delta, double start, double end, Operation<Double> original) {
        if (AnimatiumConfig.getInstance().removeHeadRotationInterpolation) {
            return end;
        } else {
            return original.call(delta, start, end);
        }
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;tickStatusEffects()V", shift = At.Shift.BEFORE))
    private void animatium$updatePreviousBobbingTiltValue(CallbackInfo ci) {
        if (AnimatiumConfig.getInstance().fixVerticalBobbingTilt) {
            this.animatium$setPreviousBobbingTilt(this.animatium$getBobbingTilt());
        }
    }

    @ModifyExpressionValue(method = "getBlockingItem", at = @At(value = "CONSTANT", args = "intValue=5"))
    private int animatium$removeClientsideBlockingDelay(int original) {
        if (AnimatiumConfig.getInstance().removeClientsideBlockingDelay) {
            return 0;
        } else {
            return original;
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
