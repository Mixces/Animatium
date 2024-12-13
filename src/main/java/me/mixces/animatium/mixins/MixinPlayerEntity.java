package me.mixces.animatium.mixins;

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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

import static net.minecraft.entity.player.PlayerEntity.STANDING_DIMENSIONS;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {
    @Shadow
    @Final
    private static Map<EntityPose, EntityDimensions> POSE_DIMENSIONS;

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
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

    @Inject(method = "getBaseDimensions", at = @At("HEAD"), cancellable = true)
    private void animatium$oldSneakEyeHeight(EntityPose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        if (AnimatiumConfig.oldSneakEyeHeight && pose.equals(EntityPose.CROUCHING)) {
            // Changes the sneak height to the one from <=1.13.2 on Hypixel & Loyisa
            boolean oldMechanics = Animatium.shouldApplyOldSneaking();
            // TODO: Fix camera on servers that aren't hypixel
            cir.setReturnValue(POSE_DIMENSIONS.getOrDefault(oldMechanics ? null : pose, STANDING_DIMENSIONS).withEyeHeight(1.54F));
        }
    }
}
