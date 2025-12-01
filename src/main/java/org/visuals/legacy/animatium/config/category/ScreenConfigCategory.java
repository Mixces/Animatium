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
import org.visuals.legacy.animatium.util.enums.CameraVersion;

public class ScreenConfigCategory extends Category {
    public boolean crosshairInThirdPerson = true;
    public boolean heartFlash = true;
    public boolean centerScrollableListWidgets = true;
    public boolean listWidgetSelectedBorderColor = true;
    public boolean legacyButtonHoverTextColor = true;
    public boolean disableDebugHudBackground = true;
    public boolean debugHudTextShadow = true;
    public boolean cameraTransparentPassthrough = true;
    public boolean tooltipStyleRendering = true;
    public boolean slotHoverStyleRendering = true;
    public boolean listBackgroundGradient = true;
    public boolean effectsInventoryPosition = true;
    public boolean snappySliderMovement = true;
    public boolean hideRecipeBook = true;
    public boolean panoramaRendering = true;
    public CameraVersion cameraVersion = CameraVersion.V1_8;

    public static ConfigCategory create(final ScreenConfigCategory defaults, final ScreenConfigCategory config) {
        final ConfigCategory.Builder category = ConfigCategory.createBuilder();
        category.name(Component.translatable("animatium.category.screen"));
        category.option(booleanOption("crosshairInThirdPerson", defaults, config));
        category.option(booleanOption("heartFlash", defaults, config));
        category.option(booleanOption("centerScrollableListWidgets", defaults, config));
        category.option(booleanOption("listWidgetSelectedBorderColor", defaults, config));
        category.option(booleanOption("legacyButtonHoverTextColor", defaults, config));
        category.option(booleanOption("disableDebugHudBackground", defaults, config));
        category.option(booleanOption("debugHudTextShadow", defaults, config));
        category.option(booleanOption("cameraTransparentPassthrough", defaults, config));
        category.option(booleanOption("tooltipStyleRendering", defaults, config));
        category.option(booleanOption("slotHoverStyleRendering", defaults, config));
        category.option(booleanOption("listBackgroundGradient", defaults, config));
        category.option(booleanOption("effectsInventoryPosition", defaults, config));
        // TODO: category.option(booleanOption("snappySliderMovement", defaults, config));
        category.option(booleanOption("hideRecipeBook", defaults, config));
        category.option(booleanOption("panoramaRendering", defaults, config));
        category.option(enumOption("cameraVersion", defaults, config, CameraVersion.class));
        return category.build();
    }
}
