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
 */

package btw.mixces.animatium.util

import btw.mixces.animatium.mixins.accessor.CameraAccessor
import btw.mixces.animatium.mixins.accessor.LivingEntityAccessor
import com.google.common.base.MoreObjects
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState
import net.minecraft.network.protocol.game.ClientboundAnimatePacket
import net.minecraft.network.protocol.game.ServerboundSwingPacket
import net.minecraft.server.level.ServerChunkCache
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3

object PlayerUtils {
    @JvmStatic
    fun getHandMultiplier(player: Player): Int {
        val hand = MoreObjects.firstNonNull(player.swingingArm, InteractionHand.MAIN_HAND)
        val direction = getHandMultiplier(player, hand)
        val client = Minecraft.getInstance()
        return (if (client.options.cameraType.isFirstPerson) 1 else -1) * direction
    }

    @JvmStatic
    fun getHandMultiplier(player: Player, hand: InteractionHand): Int {
        val arm = if (hand == InteractionHand.MAIN_HAND) player.mainArm else player.mainArm.opposite
        return getArmMultiplier(arm)
    }

    @JvmStatic
    fun getArmMultiplier(arm: HumanoidArm): Int {
        return if (arm == HumanoidArm.RIGHT) 1 else -1
    }

    @JvmStatic
    fun getPosWithEyeHeight(entity: Player, tickDelta: Float, eyeHeight: Double): Vec3 {
        return entity.getPosition(tickDelta).add(0.0, eyeHeight, 0.0)
    }

    @JvmStatic
    fun isBlockingArm(arm: HumanoidArm, armedEntityState: ArmedEntityRenderState): Boolean {
        return if (arm == HumanoidArm.LEFT && armedEntityState.leftArmPose == HumanoidModel.ArmPose.BLOCK) {
            true
        } else if (arm == HumanoidArm.RIGHT && armedEntityState.rightArmPose == HumanoidModel.ArmPose.BLOCK) {
            true
        } else {
            false
        }
    }

    @JvmStatic
    fun fakeHandSwing(player: Player, hand: InteractionHand) {
        // NOTE: Clientside fake swinging, doesn't send a packet
        if (!player.swinging || player.swingTime >= (player as LivingEntityAccessor).swingDuration / 2 || player.swingTime < 0) {
            player.swingTime = -1
            player.swinging = true
            player.swingingArm = hand
        }
    }

    // Sends necessary swing packets, without playing the player hand swing animation
    @JvmStatic
    fun sendSwingPacket(player: LocalPlayer, hand: InteractionHand) {
        if (!player.swinging || player.swingTime >= (player as LivingEntityAccessor).swingDuration / 2 || player.swingTime < 0) {
            if (player.level() is ServerLevel) {
                (player.level().chunkSource as ServerChunkCache).broadcast(
                    player,
                    ClientboundAnimatePacket(
                        player,
                        if (hand == InteractionHand.MAIN_HAND)
                            ClientboundAnimatePacket.SWING_MAIN_HAND
                        else
                            ClientboundAnimatePacket.SWING_OFF_HAND
                    )
                )
            }
        }

        player.connection?.send(ServerboundSwingPacket(hand))
    }

    @JvmStatic
    fun lerpCameraPosition(camera: Camera): Float {
        val cameraAccessor: CameraAccessor = camera as CameraAccessor
        return Mth.lerp(camera.partialTickTime, cameraAccessor.getEyeHeightOld(), cameraAccessor.getEyeHeight())
    }
}