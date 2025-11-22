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

package org.visuals.legacy.animatium.util;

import net.minecraft.client.gui.components.debug.DebugEntryCategory;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.visuals.legacy.animatium.Animatium;

import java.util.ArrayList;
import java.util.List;

public class AnimatiumDebugEntry implements DebugScreenEntry {
    public static final DebugEntryCategory CATEGORY = new DebugEntryCategory(Component.translatable("animatium.category.debug"), 9999.0F);
    public static final ResourceLocation GROUP = Animatium.id("debug");

    @Override
    public void display(DebugScreenDisplayer debugScreenDisplayer, @Nullable Level level, @Nullable LevelChunk levelChunk, @Nullable LevelChunk levelChunk2) {
        final List<String> list = new ArrayList<>();
        list.add("Animatium " + Animatium.VERSION + (Animatium.DEVELOPMENT_VERSION.isEmpty() ? "" : " - Development Version (" + Animatium.DEVELOPMENT_VERSION + ")"));
        if (!Animatium.ENABLED_SERVER_FEATURES.isEmpty()) {
            list.add("Enabled Server Features:");
            Animatium.ENABLED_SERVER_FEATURES.forEach((feature) -> list.add(" - " + feature.getTranslate().getString()));
        }
        debugScreenDisplayer.addToGroup(GROUP, list);
    }

    @Override
    public boolean isAllowed(boolean bl) {
        return true;
    }

    @Override
    public @NotNull DebugEntryCategory category() {
        return CATEGORY;
    }
}
