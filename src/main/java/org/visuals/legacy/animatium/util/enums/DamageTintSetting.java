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

package org.visuals.legacy.animatium.util.enums;

import net.minecraft.util.ARGB;

import java.util.function.Function;

public enum DamageTintSetting {
    V1_7(brightness -> ARGB.colorFromFloat(0.4F, brightness, 0.0F, 0.0F)),
    V1_8(ARGB.colorFromFloat(0.3F, 1.0F, 0.0F, 0.0F)),
    V1_8_ORANGE_MARSHALL(ARGB.colorFromFloat(0.5F, 1.0F, 0.0F, 0.0F)),
    VANILLA(null); // Doesn't matter, any code will fall out if the setting is set to this (NOTE: 1.15 was when the alpha changed from 0.3F to 0.6980392156862745F)

    private final Function<Float, Integer> colorGetter;

    DamageTintSetting(final Function<Float, Integer> colorGetter) {
        this.colorGetter = colorGetter;
    }

    DamageTintSetting(final int color) {
        this((brightness) -> color);
    }

    public int getColor(final float brightness) {
        return this.colorGetter.apply(brightness);
    }
}
