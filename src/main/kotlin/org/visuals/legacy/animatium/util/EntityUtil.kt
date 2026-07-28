/**
 * Animatium
 * The all-you-could-want legacy animations mod for modern minecraft versions.
 * Brings back animations from the 1.7/1.8 era and more.
 * <p>
 * Copyright (C) 2024-2025 lowercasebtw
 * Copyright (C) 2024-2025 mixces
 * Copyright (C) 2024-2025 Contributors to the project retain their copyright
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * "MINECRAFT" LINKING EXCEPTION TO THE GPL
 */

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

fun getHandMultiplier(player: Player, hand: InteractionHand) =
    getArmMultiplier(if (hand == InteractionHand.MAIN_HAND) player.mainArm else player.mainArm.opposite)

fun getArmMultiplier(arm: HumanoidArm) = if (arm == HumanoidArm.RIGHT) 1 else -1

fun Player.getPosWithEyeHeight(tickDelta: Float, eyeHeight: Double) =
    this.getPosition(tickDelta).add(0.0, eyeHeight, 0.0)

fun isBlockingArm(arm: HumanoidArm, armedEntityState: ArmedEntityRenderState) =
    (arm == HumanoidArm.LEFT && armedEntityState.leftArmPose == HumanoidModel.ArmPose.BLOCK) ||
            (arm == HumanoidArm.RIGHT && armedEntityState.rightArmPose == HumanoidModel.ArmPose.BLOCK)

fun Player.fakeHandSwing(hand: InteractionHand) {
    // Fake Swinging, Doesn't Send A Packet
    if (this.isNotSwinging()) {
        this.swingTime = -1
        this.swinging = true
        this.swingingArm = hand
    }
}

// Sends necessary swing packets, without playing the player hand swing animation
fun LocalPlayer.sendSwingPacket(hand: InteractionHand) {
    val level = this.level()
    if (this.isNotSwinging() && level is ServerLevel) {
        val swingHand =
            if (hand == InteractionHand.MAIN_HAND) ClientboundAnimatePacket.SWING_MAIN_HAND else ClientboundAnimatePacket.SWING_OFF_HAND
        level.chunkSource.sendToTrackingPlayers(this, ClientboundAnimatePacket(this, swingHand))
    }

    this.connection.send(ServerboundSwingPacket(hand))
}

fun Player.isNotSwinging() =
    !this.swinging || this.swingTime >= (this as LivingEntityAccessor).`animatium$getSwingDuration`() / 2 || this.swingTime < 0

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

        player.fakeHandSwing(hand)
    }
}

/**
 * Can always safely assume that if this returns true, the provided render-state is AvatarRenderState
 *
 * @return Entity id matches the client player id
 */
fun LivingEntityRenderState.isSelf(): Boolean {
    val player = Minecraft.getInstance().player
    return player != null && this is AvatarRenderState && this.id == player.id
}

fun Entity?.isSelf(): Boolean {
    val player = Minecraft.getInstance().player
    return player != null && this != null && this.id == player.id
}

fun LevelReader.getLegacyBrightness(blockPos: BlockPos): Float {
    val amount = this.getMaxLocalRawBrightness(blockPos) / 15.0F
    return Mth.lerp(this.dimensionType().ambientLight(), amount / (4.0F - 3.0F * amount), 1.0F)
}

/**
 * Code sourced from Animatium Legacy & Modified for Modern Use
 */
fun LivingEntity.getItemSwingSpeed(fallback: Int): Int {
    val extras = AnimatiumConfig.instance().extras
    if (Animatium.isEnabled() && extras.customSwingSpeed) {
        val swingingHand = if (this.swingingArm != null) this.swingingArm!! else InteractionHand.MAIN_HAND
        val stack = this.getItemInHand(swingingHand)
        val swingDuration = stack.swingAnimation.duration()

        val itemSwingSpeed = extras.itemSwingSpeed
        val hasteSwingSpeed = extras.hasteSwingSpeed
        val miningFatigueSwingSpeed = extras.miningFatigueSwingSpeed
        if (!(itemSwingSpeed == 0.0F && hasteSwingSpeed == 0.0F && miningFatigueSwingSpeed == 0.0F)) {
            if (MobEffectUtil.hasDigSpeed(this) && !extras.ignoreHasteSpeed) {
                val durationOffset =
                    swingDuration - (1 + MobEffectUtil.getDigSpeedAmplification(this))
                return max((durationOffset * exp(-hasteSwingSpeed)).toInt(), 1)
            } else if (this.hasEffect(MobEffects.MINING_FATIGUE) && !extras.ignoreMiningFatigueSpeed) {
                val durationOffset =
                    swingDuration + (1 + Objects.requireNonNull(this.getEffect(MobEffects.MINING_FATIGUE))!!.amplifier) * 2
                return max((durationOffset * exp(-miningFatigueSwingSpeed)).toInt(), 1)
            } else {
                return max((swingDuration * exp(-itemSwingSpeed)).toInt(), 1)
            }
        }
    }

    return fallback
}

fun Entity.getScale() = if (this is LivingEntity) this.scale else 1.0F