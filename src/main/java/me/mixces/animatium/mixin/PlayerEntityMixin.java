package me.mixces.animatium.mixin;

import com.google.common.collect.ImmutableMap;
import com.mojang.authlib.GameProfile;
import me.mixces.animatium.Animatium;
import me.mixces.animatium.mixin.access.IEntityMixin;
import me.mixces.animatium.mixin.access.ILivingEntityMixin;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumMap;
import java.util.Map;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {

    @Shadow
    @Final
    @Mutable
    private static Map<EntityPose, EntityDimensions> POSE_DIMENSIONS;

    @Shadow
    @Final
    public static Vec3d VEHICLE_ATTACHMENT_POS;

    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    //todo: fix this for half blocks
    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void animatium$modifyEyeHeight(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo ci) {
        if (Animatium.CONFIG.OLD_EYE_HEIGHT) {
            /* trick to modify immutablemap */
            Map<EntityPose, EntityDimensions> modifiableMap = new EnumMap<>(POSE_DIMENSIONS);
            modifiableMap.put(
                    EntityPose.CROUCHING,
                    EntityDimensions.changing(0.6F, 1.5F)
                            .withEyeHeight(1.54F)
                            .withAttachments(EntityAttachments.builder().add(EntityAttachmentType.VEHICLE, VEHICLE_ATTACHMENT_POS))
            );
            POSE_DIMENSIONS = ImmutableMap.copyOf(modifiableMap);
        }
    }

    @Inject(
            method = "tickMovement",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;strideDistance:F",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            )
    )
    private void animatium$getPitchFromVelocity(CallbackInfo ci) {
        final PlayerEntity entity = (PlayerEntity) (Object) this;
        final double velocityY = ((IEntityMixin) entity).invokeGetVelocity().y;
        float f1 = (float) (Math.atan(-velocityY * 0.2F) * 15.0F);
        if (((IEntityMixin) entity).invokeIsOnGround() || ((ILivingEntityMixin) entity).invokeGetHealth() <= 0.0F) {
            f1 = 0.0F;
        }
        animatium$cameraPitch += (f1 - animatium$cameraPitch) * 0.8F;
    }

    @Redirect(
            method = "updatePose",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/PlayerAbilities;flying:Z"
            )
    )
    private boolean animatium$sneakFlying(PlayerAbilities instance) {
        return !Animatium.CONFIG.OLD_FLYING && instance.flying;
    }
}
