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

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.state.AvatarRenderState
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import java.util.*

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

fun Player.getPosWithEyeHeight(tickDelta: Float, eyeHeight: Double) =
    this.getPosition(tickDelta).add(0.0, eyeHeight, 0.0)

fun Entity.getScale() = if (this is LivingEntity) this.scale else 1.0F