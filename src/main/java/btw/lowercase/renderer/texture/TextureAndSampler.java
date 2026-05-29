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
import org.jspecify.annotations.NonNull;

public record TextureAndSampler(GpuTextureView textureView, GpuSampler sampler) {
    public static TextureAndSampler get(final Identifier location) {
        final TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        final AbstractTexture texture = textureManager.getTexture(location);
        return new TextureAndSampler(texture.getTextureView(), texture.getSampler());
    }

    public static @NonNull TextureAndSampler get(final int index, final TextureSetup setup) {
        if (index == 0) {
            return new TextureAndSampler(setup.texure0(), setup.sampler0());
        } else if (index == 1) {
            return new TextureAndSampler(setup.texure1(), setup.sampler1());
        } else if (index == 2) {
            return new TextureAndSampler(setup.texure2(), setup.sampler2());
        } else {
            throw new IndexOutOfBoundsException("There is no texture and sampler with index " + index + "!");
        }
    }
}
