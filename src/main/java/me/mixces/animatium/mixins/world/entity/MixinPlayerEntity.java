package me.mixces.animatium.mixins.world.entity;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import me.mixces.animatium.config.AnimatiumConfig;
import me.mixces.animatium.util.PlayerUtils;
import me.mixces.animatium.util.ViewBobbingStorage;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {
    @Shadow
    public abstract void addEnchantedHitParticles(Entity target);

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "attack", at = @At("HEAD"))
    private void animatium$alwaysShowSharpParticles(Entity target, CallbackInfo ci) {
        if (AnimatiumConfig.getInstance().getAlwaysShowSharpParticles()) {
            this.addEnchantedHitParticles(target);
        }
    }

    @WrapWithCondition(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addEnchantedHitParticles(Lnet/minecraft/entity/Entity;)V"))
    private boolean animatium$disableDefaultSharpParticles(PlayerEntity instance, Entity target) {
        return !AnimatiumConfig.getInstance().getAlwaysShowSharpParticles();
    }

    @Inject(method = "getMaxRelativeHeadRotation", at = @At(value = "RETURN"), cancellable = true)
    private void animatium$uncapBlockingHeadRotation(CallbackInfoReturnable<Float> cir) {
        if (AnimatiumConfig.getInstance().getUncapBlockingHeadRotation()) {
            cir.setReturnValue(50F);
        }
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setMovementSpeed(F)V", shift = At.Shift.AFTER))
    private void animatium$updateBobbingTiltValues(CallbackInfo ci) {
        if (AnimatiumConfig.getInstance().getFixVerticalBobbingTilt()) {
            ViewBobbingStorage bobbingAccessor = (ViewBobbingStorage) this;
            float g = this.isOnGround() || this.getHealth() <= 0.0F ? 0.0F : (float) (Math.atan(-this.getVelocity().y * (double) 0.2F) * 15.0F);
            bobbingAccessor.animatium$setBobbingTilt(MathHelper.lerp(0.8F, bobbingAccessor.animatium$getBobbingTilt(), g));
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;updatePose()V"))
    private void animatium$updateDimensions(CallbackInfo ci) {
        if (AnimatiumConfig.getInstance().getOldSneakEyeHeight()) {
            calculateDimensions();
        }
    }

    @Inject(method = "getBaseDimensions", at = @At("RETURN"), cancellable = true)
    private void animatium$oldSneakEyeHeight(EntityPose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        if (AnimatiumConfig.getInstance().getOldSneakEyeHeight() && pose.equals(EntityPose.CROUCHING)) {
            cir.setReturnValue(PlayerUtils.getLegacySneakingDimensions((PlayerEntity) (Object) this, pose));
        }
    }
}
