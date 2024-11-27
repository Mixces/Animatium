package me.mixces.animatium.mixin;

import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.entity.EntityAttachments;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

//    @Shadow
//    @Final
//    public static Vec3d VEHICLE_ATTACHMENT_POS;
//
//    @Inject(
//            method = "getBaseDimensions",
//            at = @At("HEAD"),
//            cancellable = true
//    )
//    private void animatium$oldEyeHeight(EntityPose pose, CallbackInfoReturnable<EntityDimensions> cir) {
//        if (pose == EntityPose.CROUCHING) {
//            cir.setReturnValue(EntityDimensions
//                    .changing(0.6F, 1.5F)
//                    .withEyeHeight(1.54F)
//                    .withAttachments(EntityAttachments.builder().add(EntityAttachmentType.VEHICLE, VEHICLE_ATTACHMENT_POS))
//            );
//        }
//    }
}
