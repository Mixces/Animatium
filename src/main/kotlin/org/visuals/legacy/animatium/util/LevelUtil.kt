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

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.util.Mth
import net.minecraft.world.level.Level
import org.visuals.legacy.animatium.mixins.accessor.ClientLevelDataAccessor
import kotlin.math.cos

fun ClientLevel.hasVoidFog(): Boolean {
    val levelDataAccessor = this.getLevelData() as ClientLevelDataAccessor
    return !levelDataAccessor.`animatium$isFlatWorld`() &&
            !this.dimensionType().hasCeiling() // "isDark" method from 1.7/1.8
}

fun ClientLevel.getLegacySkyDarken(): Float {
    var value = 1.0F - (Mth.cos(this.getTimeOfDay(1.0F) * (Math.PI * 2).toFloat()) * 2.0F + 0.2F)
    value = Mth.clamp(value, 0.0F, 1.0F)
    value = 1.0F - value
    value *= 1.0F - this.getRainLevel(1.0F) * 5.0F / 16.0F
    value *= 1.0F - this.getThunderLevel(1.0F) * 5.0F / 16.0F
    return value * 0.8F + 0.2F
}

fun ClientLevel.getTimeOfDay(tickDelta: Float): Double {
    var dayTime = this.defaultClockTime
    if (dayTime == 0L) {
        dayTime = 1 // 1.8 never lets the tick time be 0
    }

    if (this.dimensionType().hasFixedTime()) {
        val dimension = this.dimension()
        if (dimension == Level.NETHER) {
            dayTime = 18000L
        } else if (dimension == Level.END) {
            dayTime = 6000L
        }
    }

    val time = Math.toIntExact(dayTime % 24000L)

    var frac = (time + tickDelta) / 24000.0 - 0.25
    if (frac < 0.0) {
        ++frac
    }

    if (frac > 1.0) {
        --frac
    }

    val mul = 1.0 - ((cos(frac * Math.PI) + 1.0) / 2.0)
    frac += (mul - frac) / 3.0
    return frac
}