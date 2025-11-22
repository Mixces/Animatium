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

import lombok.experimental.UtilityClass;
import net.minecraft.resources.ResourceLocation;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.packet.AnimatiumInfoPayloadPacket;
import org.visuals.legacy.animatium.util.ConfigUtil;
import org.visuals.legacy.animatium.util.enums.ServerFeature;

import java.io.IOException;
import java.util.EnumSet;

@UtilityClass
public final class Animatium {
    public static final String MOD_ID = "@MODID@";
    public Double VERSION = Double.parseDouble("@VERSION@");
    public String DEVELOPMENT_VERSION = "@COMMIT@";

    public boolean ENABLED = true;
    public EnumSet<ServerFeature> ENABLED_SERVER_FEATURES = EnumSet.noneOf(ServerFeature.class);

    public AnimatiumInfoPayloadPacket getInfoPayload() {
        return new AnimatiumInfoPayloadPacket(VERSION, DEVELOPMENT_VERSION.isEmpty() ? null : DEVELOPMENT_VERSION);
    }

    public ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public void initialize() {
        AnimatiumConfig.load();
        try {
            ConfigUtil.load();
        } catch (IOException ignored) {
            Animatium.ENABLED = true;
            System.err.println("Failed to load animatium utility config, defaulting...");
        }
    }
}
