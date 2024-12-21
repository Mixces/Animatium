package me.mixces.animatium.util

import com.google.common.base.MoreObjects
import me.mixces.animatium.AnimatiumClient
import me.mixces.animatium.mixins.accessor.PlayerEntityAccessor
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityPose
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectUtil
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Arm
import net.minecraft.util.Hand
import net.minecraft.util.math.Vec3d
import java.util.Objects

abstract class PlayerUtils {
    companion object {
        @JvmStatic
        fun getHandMultiplier(player: PlayerEntity): Int {
            val hand = MoreObjects.firstNonNull(player.preferredHand, Hand.MAIN_HAND)
            val arm = if (hand == Hand.MAIN_HAND) player.mainArm else player.mainArm.opposite
            val client = MinecraftClient.getInstance()
            val i = if (client.options.perspective.isFirstPerson) 1 else -1
            return if (arm == Arm.RIGHT) i else -i
        }

        @JvmStatic
        fun getArmMultiplier(arm: Arm): Int {
            return if (arm == Arm.RIGHT) 1 else -1
        }

        @JvmStatic
        fun getLegacySneakingDimensions(player: PlayerEntity, defaultPose: EntityPose): EntityDimensions {
            // Changes the sneak height to the one from <=1.13.2 on Hypixel & Loyisa & Bedwars Practice & Bridger Land
            val dimensions = Objects.requireNonNull(PlayerEntityAccessor.getPoseDimensions()).getOrDefault(
                if (AnimatiumClient.isLegacySupportedVersion()) null else defaultPose,
                PlayerEntity.STANDING_DIMENSIONS
            )
            return if ((player as PlayerEntityAccessor).`canChangeIntoPose$`(EntityPose.STANDING)) {
                dimensions.withEyeHeight(1.54F)
            } else {
                dimensions
            }
        }

        @JvmStatic
        fun lerpPlayerWithEyeHeight(entity: PlayerEntity, tickDelta: Float, eyeHeight: Double): Vec3d {
            return entity.getLerpedPos(tickDelta).add(0.0, eyeHeight, 0.0)
        }

        @JvmStatic
        fun fakeHandSwing(player: PlayerEntity, hand: Hand) {
            // NOTE: Clientside fake swinging, doesn't send a packet
            if (!player.handSwinging || player.handSwingTicks >= getHandSwingDuration(player) / 2 || player.handSwingTicks < 0) {
                player.handSwingTicks = -1
                player.handSwinging = true
                player.preferredHand = hand
            }
        }

        // Fixes crash & doesn't require accesswidener
        @JvmStatic
        fun getHandSwingDuration(entity: LivingEntity): Int {
            return if (StatusEffectUtil.hasHaste(entity)) {
                6 - (1 + StatusEffectUtil.getHasteAmplifier(entity))
            } else {
                if (entity.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
                    6 + (1 + Objects.requireNonNull(entity.getStatusEffect(StatusEffects.MINING_FATIGUE))!!.amplifier) * 2
                } else {
                    6
                }
            }
        }
    }
}