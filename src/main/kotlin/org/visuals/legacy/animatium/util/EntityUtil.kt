package org.visuals.legacy.animatium.util

import com.google.common.base.MoreObjects
import net.minecraft.client.Minecraft
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState
import net.minecraft.client.renderer.entity.state.AvatarRenderState
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ClientboundAnimatePacket
import net.minecraft.network.protocol.game.ServerboundSwingPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.effect.MobEffectUtil
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.LevelReader
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import org.visuals.legacy.animatium.Animatium
import org.visuals.legacy.animatium.config.AnimatiumConfig
import org.visuals.legacy.animatium.mixins.accessor.LivingEntityAccessor
import java.util.*
import kotlin.math.exp
import kotlin.math.max

fun getHandMultiplier(player: Player): Int {
    val hand = MoreObjects.firstNonNull(player.swingingArm, InteractionHand.MAIN_HAND)
    val direction = (if (Minecraft.getInstance().options.cameraType.isFirstPerson) 1 else -1)
    return direction * getHandMultiplier(player, hand)
}

fun getHandMultiplier(player: Player, hand: InteractionHand): Int {
    return getArmMultiplier(if (hand == InteractionHand.MAIN_HAND) player.mainArm else player.mainArm.opposite)
}

fun getArmMultiplier(arm: HumanoidArm): Int {
    return if (arm == HumanoidArm.RIGHT) 1 else -1
}

fun getPosWithEyeHeight(player: Player, tickDelta: Float, eyeHeight: Double): Vec3 {
    return player.getPosition(tickDelta).add(0.0, eyeHeight, 0.0)
}

fun isBlockingArm(arm: HumanoidArm, armedEntityState: ArmedEntityRenderState): Boolean {
    return (arm == HumanoidArm.LEFT && armedEntityState.leftArmPose == HumanoidModel.ArmPose.BLOCK) ||
            (arm == HumanoidArm.RIGHT && armedEntityState.rightArmPose == HumanoidModel.ArmPose.BLOCK)
}

fun fakeHandSwing(player: Player, hand: InteractionHand) {
    // Fake Swinging, Doesn't Send A Packet
    if (isNotSwinging(player)) {
        player.swingTime = -1
        player.swinging = true
        player.swingingArm = hand
    }
}

// Sends necessary swing packets, without playing the player hand swing animation
fun sendSwingPacket(player: LocalPlayer, hand: InteractionHand) {
    val level = player.level()
    if (isNotSwinging(player) && level is ServerLevel) {
        val swingHand =
            if (hand == InteractionHand.MAIN_HAND) ClientboundAnimatePacket.SWING_MAIN_HAND else ClientboundAnimatePacket.SWING_OFF_HAND
        level.chunkSource.sendToTrackingPlayers(player, ClientboundAnimatePacket(player, swingHand))
    }

    player.connection.send(ServerboundSwingPacket(hand))
}

fun isNotSwinging(player: Player): Boolean {
    return !player.swinging || player.swingTime >= (player as LivingEntityAccessor).`animatium$getSwingDuration`() / 2 || player.swingTime < 0
}

fun applySwingWhilstMining(level: ClientLevel?, player: Player, hitResult: HitResult?) {
    val activeHand = player.usedItemHand
    val hand = if (AnimatiumConfig.instance().extras.offhandUsageSwinging) activeHand else InteractionHand.MAIN_HAND
    if (activeHand == hand) {
        if (hitResult != null && hitResult.type == HitResult.Type.BLOCK) {
            val blockHitResult = hitResult as BlockHitResult
            if (level != null && !level.getBlockState(blockHitResult.blockPos).isAir) {
                level.addBreakingBlockEffect(blockHitResult.blockPos, blockHitResult.direction)
            }
        } else if (!AnimatiumConfig.instance().extras.alwaysUsageSwing) {
            return
        }

        fakeHandSwing(player, hand)
    }
}

/**
 * Can always safely assume that if this returns true, the provided render-state is AvatarRenderState
 *
 * @return Entity id matches the client player id
 */
fun isSelf(livingEntityRenderState: LivingEntityRenderState): Boolean {
    val player = Minecraft.getInstance().player
    return player != null && livingEntityRenderState is AvatarRenderState && livingEntityRenderState.id == player.id
}

fun isSelf(entity: Entity?): Boolean {
    val player = Minecraft.getInstance().player
    return player != null && entity != null && entity.id == player.id
}

fun getBrightness(reader: LevelReader, blockPos: BlockPos): Float {
    val amount = reader.getMaxLocalRawBrightness(blockPos) / 15.0F
    return Mth.lerp(reader.dimensionType().ambientLight(), amount / (4.0F - 3.0F * amount), 1.0F)
}

fun getBrightness(entity: Entity): Float {
    // Older versions inspected from the foot position, not eye position
    return getBrightness(entity.level(), entity.blockPosition())
}

/**
 * Code sourced from Animatium Legacy & Modified for Modern Use
 */
fun getItemSwingSpeed(entity: LivingEntity, fallback: Int): Int {
    val extras = AnimatiumConfig.instance().extras
    if (Animatium.isEnabled() && extras.customSwingSpeed) {
        val swingingHand = if (entity.swingingArm != null) entity.swingingArm!! else InteractionHand.MAIN_HAND
        val stack = entity.getItemInHand(swingingHand)
        val swingDuration = stack.swingAnimation.duration()

        val itemSwingSpeed = extras.itemSwingSpeed
        val hasteSwingSpeed = extras.hasteSwingSpeed
        val miningFatigueSwingSpeed = extras.miningFatigueSwingSpeed
        if (!(itemSwingSpeed == 0.0F && hasteSwingSpeed == 0.0F && miningFatigueSwingSpeed == 0.0F)) {
            if (MobEffectUtil.hasDigSpeed(entity) && !extras.ignoreHasteSpeed) {
                val durationOffset =
                    swingDuration - (1 + MobEffectUtil.getDigSpeedAmplification(entity))
                return max((durationOffset * exp(-hasteSwingSpeed)).toInt(), 1)
            } else if (entity.hasEffect(MobEffects.MINING_FATIGUE) && !extras.ignoreMiningFatigueSpeed) {
                val durationOffset =
                    swingDuration + (1 + Objects.requireNonNull(entity.getEffect(MobEffects.MINING_FATIGUE))!!.amplifier) * 2
                return max((durationOffset * exp(-miningFatigueSwingSpeed)).toInt(), 1)
            } else {
                return max((swingDuration * exp(-itemSwingSpeed)).toInt(), 1)
            }
        }
    }

    return fallback
}