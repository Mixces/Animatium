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

package btw.lowercase.renderer.texture;

import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public record TextureAndSampler(GpuTextureView textureView, GpuSampler sampler) {
    public static TextureAndSampler get(final Identifier location) {
        final TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        final AbstractTexture texture = textureManager.getTexture(location);
        return new TextureAndSampler(texture.getTextureView(), texture.getSampler());
    }

    public static @Nullable TextureAndSampler get(final int index, final TextureSetup setup) {
        final GpuTextureView texture0 = setup.texure0();
        if (index == 0 && texture0 != null) {
            return new TextureAndSampler(texture0, setup.sampler0());
        }

        final GpuTextureView texture1 = setup.texure1();
        if (index == 1 && texture1 != null) {
            return new TextureAndSampler(texture1, setup.sampler1());
        }

        final GpuTextureView texture2 = setup.texure2();
        if (index == 2 && texture2 != null) {
            return new TextureAndSampler(texture2, setup.sampler2());
        }

        return null;
    }
}
