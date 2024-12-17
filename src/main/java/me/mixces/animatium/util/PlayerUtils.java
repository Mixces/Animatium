package me.mixces.animatium.util;

import com.google.common.base.MoreObjects;
import me.mixces.animatium.mixins.accessor.PlayerEntityAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

import static me.mixces.animatium.Animatium.isLegacySupportedVersion;

public abstract class PlayerUtils {
    public static int getHandMultiplier(PlayerEntity player) {
        Hand hand = MoreObjects.firstNonNull(player.preferredHand, Hand.MAIN_HAND);
        Arm arm = hand == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
        MinecraftClient client = MinecraftClient.getInstance();
        assert client != null;
        int i = client.options.getPerspective().isFirstPerson() ? 1 : -1;
        return arm == Arm.RIGHT ? i : -i;
    }

    public static EntityDimensions getLegacySneakingDimensions(PlayerEntity player, EntityPose defaultPose) {
        // Changes the sneak height to the one from <=1.13.2 on Hypixel & Loyisa & Bedwars Practice & Bridger Land
        EntityDimensions dimensions = Objects.requireNonNull(PlayerEntityAccessor.getPoseDimensions()).getOrDefault(isLegacySupportedVersion() ? null : defaultPose, PlayerEntity.STANDING_DIMENSIONS);
        if (((PlayerEntityAccessor) player).canChangeIntoPose$(EntityPose.STANDING)) {
            return dimensions.withEyeHeight(1.54F);
        } else {
            return dimensions;
        }
    }

    public static Vec3d lerpPlayerWithEyeHeight(PlayerEntity entity, float tickDelta, float eyeHeight) {
        return entity.getLerpedPos(tickDelta).add(0, eyeHeight, 0);
    }

    public static void fakeHandSwing(PlayerEntity player, Hand hand) {
        // NOTE: Clientside fake swinging, doesn't send a packet
        if (!player.handSwinging || player.handSwingTicks >= getHandSwingDuration(player) / 2 || player.handSwingTicks < 0) {
            player.handSwingTicks = -1;
            player.handSwinging = true;
            player.preferredHand = hand;
        }
    }

    // Fixes crash & doesn't require accesswidener
    private static int getHandSwingDuration(LivingEntity entity) {
        if (StatusEffectUtil.hasHaste(entity)) {
            return 6 - (1 + StatusEffectUtil.getHasteAmplifier(entity));
        } else {
            return entity.hasStatusEffect(StatusEffects.MINING_FATIGUE) ? 6 + (1 + Objects.requireNonNull(entity.getStatusEffect(StatusEffects.MINING_FATIGUE)).getAmplifier()) * 2 : 6;
        }
    }
}
