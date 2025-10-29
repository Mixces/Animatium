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

import net.fabricmc.loader.api.FabricLoader;
import org.visuals.legacy.animatium.AnimatiumClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class ConfigUtil {
    private static final File STATE_FILE = new File(FabricLoader.getInstance().getGameDir().toFile(), "animatium_state.txt");

    private ConfigUtil() {
    }

    public static void loadState() throws IOException {
        if (STATE_FILE.exists()) {
            AnimatiumClient.ENABLED = Files.readString(STATE_FILE.toPath()).equals("true");
        } else {
            if (!saveState()) {
                System.err.println("Failed to save enabled state...");
            }
        }
    }

    public static boolean saveState() {
        boolean success = true;
        try {
            if (!STATE_FILE.exists()) {
                success = STATE_FILE.createNewFile();
            }

            if (success) {
                Files.writeString(STATE_FILE.toPath(), String.valueOf(AnimatiumClient.ENABLED));
            }
        } catch (Exception exception) {
            success = false;
        }

        return success;
    }
}
