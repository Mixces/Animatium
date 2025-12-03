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

import lombok.Getter;

@Getter
public enum ServerFeature {
    MISS_PENALTY("miss_penalty"),
    LEFT_CLICK_ITEM_USAGE("left_click_item_usage"),
    MINING_ITEM_USAGE("mining_item_usage"),
    HIDE_FIRSTPERSON_ROD_BOBBER("hide_rod_bobber"),
    PICK_INFLATION("pick_inflation");

    public static final ServerFeature[] VALUES = values();
    private final String id;

    ServerFeature(String id) {
        this.id = id;
    }
}
