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

package org.visuals.legacy.animatium;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.AnimatiumDebugEntry;
import org.visuals.legacy.animatium.util.config.ConfigUtil;
import org.visuals.legacy.animatium.util.enums.ServerFeature;

import java.util.EnumSet;

@UtilityClass
public final class Animatium {
	public static final EnumSet<ServerFeature> ENABLED_SERVER_FEATURES = EnumSet.noneOf(ServerFeature.class);
	@Getter
	private final Logger logger = LogManager.getLogger(Animatium.class);
	@Getter
	private boolean enabled = true;

	public void setEnabled(boolean enabled) {
		Animatium.enabled = enabled;
		ConfigUtil.put("enabled", enabled);
	}

	public boolean hasServerFeature(ServerFeature feature) {
		final boolean hasAll = ENABLED_SERVER_FEATURES.contains(ServerFeature.ALL) || (AnimatiumConstants.IS_DEVELOPMENT && Minecraft.getInstance().isLocalServer());
		return hasAll || ENABLED_SERVER_FEATURES.contains(feature);
	}

	public ResourceLocation location(String path) {
		return ResourceLocation.fromNamespaceAndPath(AnimatiumConstants.MOD_ID, path);
	}

	public void initialize() {
		if (AnimatiumConstants.IS_DEVELOPMENT) {
			SharedConstants.IS_RUNNING_IN_IDE = true;
		}

		AnimatiumConfig.load();
		try {
			ConfigUtil.load();
			System.err.println("Successfully loaded the animatium utility config!");
		} catch (Exception ignored) {
			enabled = ConfigUtil.bool("enabled");
			System.err.println("Failed to load animatium utility config, defaulting...");
		}

		DebugScreenEntries.register(AnimatiumDebugEntry.GROUP, new AnimatiumDebugEntry());
	}
}
