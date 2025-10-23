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

package btw.mixces.animatium.util.enums;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public enum Feature {
    MISS_PENALTY("miss_penalty", "animatium.miss_penalty.description"),
    LEFT_CLICK_ITEM_USAGE("left_click_item_usage", "animatium.left_click_item_usage.description");

    private final String id;
    private final String translate;

    public static final Feature[] VALUES = values();

    public static @Nullable Feature byId(String id) {
        return Arrays.stream(VALUES).filter(feature -> feature.id.equals(id)).findFirst().orElse(null);
    }

    Feature(String id, String translate) {
        this.id = id;
        this.translate = translate;
    }

    public String getTranslateKey() {
        return translate;
    }
}
