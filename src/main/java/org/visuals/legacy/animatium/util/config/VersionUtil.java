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

import dev.isxander.yacl3.config.v2.api.SerialEntry;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.config.category.*;
import org.visuals.legacy.animatium.mixins.accessor.GameRendererAccessor;

@UtilityClass
public class VersionUtil {
    public void setVersion(Version version) {
        final Minecraft minecraft = Minecraft.getInstance();
        final AnimatiumConfig config = AnimatiumConfig.instance();
        switch (version) {
            case V1_7 -> v1_7(minecraft, config);
            case V1_8 -> v1_8(minecraft, config);
            case MODERN -> modern(minecraft, config);
        }

        minecraft.reloadResourcePacks();
        ((GameRendererAccessor) minecraft.gameRenderer).animatium$setOverlayTexture(new OverlayTexture());
    }

    private void v1_7(final Minecraft minecraft, final AnimatiumConfig config) {
        final MovementConfigCategory movement = config.movement;

        final ItemsConfigCategory items = config.items;

        final ScreenConfigCategory screen = config.screen;

        final OtherConfigCategory other = config.other;
    }

    private void v1_8(final Minecraft minecraft, final AnimatiumConfig config) {
        final MovementConfigCategory movement = config.movement;

        final ItemsConfigCategory items = config.items;

        final ScreenConfigCategory screen = config.screen;

        final OtherConfigCategory other = config.other;
    }

    private void modern(final Minecraft minecraft, final AnimatiumConfig config) {
        final MovementConfigCategory movement = config.movement;

        final ItemsConfigCategory items = config.items;

        final ScreenConfigCategory screen = config.screen;

        final OtherConfigCategory other = config.other;
    }
}
