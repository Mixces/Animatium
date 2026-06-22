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

package org.visuals.legacy.animatium.handler

import net.minecraft.client.gui.components.debug.DebugEntryCategory
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer
import net.minecraft.client.gui.components.debug.DebugScreenEntry
import net.minecraft.network.chat.Component
import net.minecraft.world.level.Level
import net.minecraft.world.level.chunk.LevelChunk
import org.visuals.legacy.animatium.Animatium
import org.visuals.legacy.animatium.AnimatiumConstants
import org.visuals.legacy.animatium.util.enums.ServerFeature
import java.util.*

class AnimatiumDebugEntry : DebugScreenEntry {
    companion object {
        val CATEGORY = DebugEntryCategory(Component.translatable("animatium.category.debug"), Float.MAX_VALUE)
        val GROUP = Animatium.location("debug")
    }

    override fun display(
        displayer: DebugScreenDisplayer,
        serverOrClientLevel: Level?,
        clientChunk: LevelChunk?,
        serverChunk: LevelChunk?
    ) {
        val list = arrayListOf<String>()
        list.add("Animatium " + AnimatiumConstants.VERSION + (if (AnimatiumConstants.IS_DEVELOPMENT) " - Development Version (" + AnimatiumConstants.DEVELOPMENT_VERSION + ")" else ""))
        if (!Animatium.ENABLED_SERVER_FEATURES.isEmpty()) {
            list.add("Enabled Server Features:")
            if (Animatium.hasServerFeature(ServerFeature.ALL)) {
                Arrays.stream(ServerFeature.VALUES).forEach { feature -> list.add(" - " + feature.getName()) }
            } else {
                Animatium.ENABLED_SERVER_FEATURES.forEach { feature -> list.add(" - " + feature.getName()) }
            }
        }

        displayer.addToGroup(GROUP, list)
    }

    override fun isAllowed(reducedDebugInfo: Boolean): Boolean = true

    override fun category(): DebugEntryCategory = CATEGORY
}