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

package org.visuals.legacy.animatium.util.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.Strictness;
import net.minecraft.client.GraphicsPreset;
import net.minecraft.client.Minecraft;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

import java.io.InputStreamReader;
import java.util.Objects;

public enum Version {
    V1_7(() -> {
        load("1_7");

        // Dynamic Fields
        final AnimatiumConfig config = AnimatiumConfig.instance();
        config.other.fastGrass = GraphicsPreset.FAST.equals(Minecraft.getInstance().options.graphicsPreset().get());
    }),
    V1_8(() -> load("1_8")),
    MODERN(() -> load("modern"));

    private static final Gson GSON = new GsonBuilder().setStrictness(Strictness.LENIENT).create();
    private final Runnable applier;

    Version(final Runnable applier) {
        this.applier = applier;
    }

    public void apply() {
        this.applier.run();
    }

    private static void load(final String name) {
        try {
            final InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(AnimatiumConfig.class.getResourceAsStream("presets/" + name + ".json")));
            final JsonObject object = GSON.fromJson(reader.readAllAsString(), JsonObject.class);
            // TODO
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }
}
