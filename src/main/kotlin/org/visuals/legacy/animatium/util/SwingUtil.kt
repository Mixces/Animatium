/**
 * Animatium
 * The all-you-could-want legacy animations mod for modern minecraft versions.
 * Brings back animations from the 1.7/1.8 era and more.
 * <p>
 * Copyright (C) 2024-2027 lowercasebtw
 * Copyright (C) 2024-2027 mixces
 * Copyright (C) 2024-2027 Contributors to the project retain their copyright
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

import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.renderer.entity.state.HumanoidRenderState
import net.minecraft.network.protocol.game.ClientboundSwingAnimationPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.effect.MobEffectUtil
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.component.SwingAnimation
import org.visuals.legacy.animatium.Animatium
import org.visuals.legacy.animatium.config.AnimatiumConfig
import org.visuals.legacy.animatium.mixins.accessor.LivingEntityAccessor
import org.visuals.legacy.animatium.mixins.accessor.LivingEntity_SwingStateAccessor
import java.util.*
import kotlin.math.exp
import kotlin.math.max

fun attackArm(state: HumanoidRenderState) = state.useItemHand.asArm(state.mainArm)

fun swingState(livingEntity: LivingEntity) = (livingEntity as LivingEntityAccessor).`animatium$getSwingState`()

fun activeSwing(livingEntity: LivingEntity) = (swingState(livingEntity) as LivingEntity_SwingStateAccessor).`animatium$getCurrentSwing`()

fun swingingArm(livingEntity: LivingEntity) = activeSwing(livingEntity)?.hand

// Fake Swinging, Doesn't Send A Packet
fun Player.fakeHandSwing(hand: InteractionHand) {
    val swingState = swingState(this)
    if (!swingState.isSwinging) {
        val animation = SwingAnimation.DEFAULT
        swingState.startIfAble(hand, animation, (this as LivingEntityAccessor).`animatium$getModifiedSwingDuration`(animation))
    }
}

// TODO: 26.3 / Check if this is proper/right and doesn't flag servers
// Sends necessary swing packets, without playing the player hand swing animation
fun LocalPlayer.sendSwingPacket(hand: InteractionHand): Boolean {
    val level = this.level()
    return if (!this.isSwinging && level is ServerLevel) {
        level.chunkSource.sendToTrackingPlayers(this, ClientboundSwingAnimationPacket(this, hand, SwingAnimation.DEFAULT))
        true
    } else {
        false
    }
}

/**
 * Code sourced from Animatium Legacy & Modified for Modern Use
 */
fun LivingEntity.getItemSwingSpeed(animation: SwingAnimation, fallback: Int): Int {
    val extras = AnimatiumConfig.instance().extras
    if (Animatium.isEnabled() && extras.customSwingSpeed) {
        val swingDuration = animation.duration()
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