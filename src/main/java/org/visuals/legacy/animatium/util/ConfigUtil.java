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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.Strictness;
import lombok.experimental.UtilityClass;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@UtilityClass
public class ConfigUtil {
    private final Gson GSON = new GsonBuilder().setStrictness(Strictness.LENIENT).create();
    private final File CONFIG_FILE = new File(FabricLoader.getInstance().getGameDir().toFile(), "animatium_utility.json");
    private JsonObject data = new JsonObject();

    public static void load() throws IOException {
        if (CONFIG_FILE.exists()) {
            data = GSON.fromJson(Files.readString(CONFIG_FILE.toPath()), JsonObject.class);
        } else {
            if (!save()) {
                System.err.println("Failed to save animatium utility config...");
            }
        }
    }

    public static boolean bool(String name) {
        if (data.has(name)) {
            return data.get(name).getAsBoolean();
        } else {
            data.addProperty(name, false);
            return false;
        }
    }

    public static void put(String name, boolean value) {
        data.addProperty(name, value);
    }

    public static boolean save() {
        boolean success = true;
        try {
            if (!CONFIG_FILE.exists()) {
                success = CONFIG_FILE.createNewFile();
            }

            if (success) {
                Files.writeString(CONFIG_FILE.toPath(), GSON.toJson(data));
            }
        } catch (Exception exception) {
            success = false;
        }

        return success;
    }
}
