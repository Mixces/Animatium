package me.mixces.animatium.mixins.world.entity;

import me.mixces.animatium.Animatium;
import me.mixces.animatium.config.AnimatiumConfig;
import me.mixces.animatium.util.ViewBobbingStorage;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {
    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 0.0F, ordinal = 6))
    private float animatium$alwaysShowSharpParticles(float original) {
        if (AnimatiumConfig.alwaysShowSharpParticles) {
            return -1;
        } else {
            return original;
        }
    }

    @Inject(method = "getMaxRelativeHeadRotation", at = @At(value = "RETURN"), cancellable = true)
    private void animatium$uncapBlockingHeadRotation(CallbackInfoReturnable<Float> cir) {
        if (AnimatiumConfig.uncapBlockingHeadRotation) {
            cir.setReturnValue(50F);
        }
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setMovementSpeed(F)V", shift = At.Shift.AFTER))
    private void animatium$updateBobbingTiltValues(CallbackInfo ci) {
        if (AnimatiumConfig.fixVerticalBobbingTilt) {
            ViewBobbingStorage bobbingAccessor = (ViewBobbingStorage) this;
            float g = this.isOnGround() || this.getHealth() <= 0.0F ? 0.0F : (float) (Math.atan(-this.getVelocity().y * (double) 0.2F) * 15.0F);
            bobbingAccessor.animatium$setBobbingTilt(MathHelper.lerp(0.8F, bobbingAccessor.animatium$getBobbingTilt(), g));
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;updatePose()V"))
    private void animatium$updateDimensions(CallbackInfo ci) {
        if (AnimatiumConfig.oldSneakEyeHeight) {
            calculateDimensions();
        }
    }

    @Inject(method = "getBaseDimensions", at = @At("RETURN"), cancellable = true)
    private void animatium$oldSneakEyeHeight(EntityPose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        if (AnimatiumConfig.oldSneakEyeHeight && pose.equals(EntityPose.CROUCHING)) {
            cir.setReturnValue(Animatium.getLegacySneakingDimensions((PlayerEntity) (Object) this, pose));
        }
    }
}
