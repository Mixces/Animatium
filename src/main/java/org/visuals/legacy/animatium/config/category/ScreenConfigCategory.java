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

package org.visuals.legacy.animatium.config.category;

import dev.isxander.yacl3.api.ConfigCategory;
import net.minecraft.network.chat.Component;
import org.visuals.legacy.animatium.util.Utils;
import org.visuals.legacy.animatium.util.config.EntryBundle;
import org.visuals.legacy.animatium.util.enums.CameraVersion;

public class ScreenConfigCategory extends Category {
	public CameraVersion cameraVersion = CameraVersion.V1_8;
	public boolean crosshairInThirdPerson = true;
	public boolean disableHeartFlash = true;
	public boolean centerScrollableListWidgets = true;
	public boolean listWidgetSelectedBorderColor = true;
	public boolean legacyWidgetHoverTextColor = true;
	public boolean disableDebugHudBackground = true;
	public boolean debugHudTextShadow = true;
	public boolean disableCameraTransparentPassthrough = true;
	public boolean tooltipStyleRendering = true;
	public boolean slotHoverStyleRendering = true;
	public boolean listBackgroundGradient = true;
	public boolean effectsInventoryPosition = true;
	public boolean snappySliderMovement = true;
	public boolean hideRecipeBook = true;
	public boolean panoramaRendering = false;
	public boolean legacyLoadingScreen = true;
	public boolean oldChatPosition = true;
	public boolean oldCrosshairPosition = true;
	public boolean disconnectServerToTitleScreen = true;
	public boolean oldCraftingSlotsPosition = false;

	public static ConfigCategory create(final ScreenConfigCategory defaults, final ScreenConfigCategory config) {
		final ConfigCategory.Builder category = ConfigCategory.createBuilder();
		category.name(Component.translatable("animatium.category.screen"));
		config.bundle().install(category, defaults, config);
		return category.build();
	}

	@Override
	public EntryBundle bundle() {
		final EntryBundle bundle = new EntryBundle(this, "screen");

		bundle.enumEntry("cameraVersion", CameraVersion.class);
		bundle.booleanEntry("crosshairInThirdPerson");
		bundle.booleanEntry("disableHeartFlash");
		bundle.booleanEntry("centerScrollableListWidgets");
		bundle.booleanEntry("listWidgetSelectedBorderColor");
		bundle.booleanEntry("legacyWidgetHoverTextColor");
		bundle.booleanEntry("disableDebugHudBackground");
		bundle.booleanEntry("debugHudTextShadow");
		bundle.booleanEntry("disableCameraTransparentPassthrough");
		bundle.booleanEntry("tooltipStyleRendering");
		bundle.booleanEntry("slotHoverStyleRendering");
		bundle.booleanEntry("listBackgroundGradient");
		bundle.booleanEntry("effectsInventoryPosition");
		bundle.booleanEntry("snappySliderMovement");
		bundle.booleanEntry("hideRecipeBook");
		bundle.booleanEntry("panoramaRendering");
		bundle.booleanEntry("legacyLoadingScreen");
		bundle.booleanEntry("oldChatPosition");
		bundle.booleanEntry("oldCrosshairPosition");
		bundle.booleanEntry("disconnectServerToTitleScreen");
		bundle.booleanEntry("oldCraftingSlotsPosition", (option, event) -> Utils.reinitializeInventorySlots());

		return bundle;
	}
}
