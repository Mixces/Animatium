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
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.ResourceLocation;
import org.visuals.legacy.animatium.packet.HelloPayloadPacket;

@UtilityClass
public class AnimatiumConstants {
    public final String MOD_ID = "@MODID@";
    public final Double VERSION = Double.parseDouble("@VERSION@");
    public final String DEVELOPMENT_VERSION = "@COMMIT@";
    public final boolean IS_DEVELOPMENT = Boolean.parseBoolean("@DEVELOPMENT@");

    public final ResourceLocation FAST_GRASS_MODEL_LOCATION = Animatium.location("block/fast_grass_block");
    public final ExtraModelKey<BlockStateModel> FAST_GRASS_MODEL_KEY = ExtraModelKey.create(FAST_GRASS_MODEL_LOCATION::toString);

    public HelloPayloadPacket getHelloPayload() {
        return new HelloPayloadPacket(VERSION, IS_DEVELOPMENT ? DEVELOPMENT_VERSION : null);
    }
}
