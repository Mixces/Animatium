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

package org.visuals.legacy.animatium.util.rendering;

import btw.lowercase.renderer.Renderer;
import btw.lowercase.renderer.buffer.Geometry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;

public final class ColorBoostRenderer {
    private static final Geometry.Basic GEOMETRY = new Geometry.Basic(0, 3);

    ColorBoostRenderer() {
    }

    public static void render(final GpuTextureView colorAttachment, final GpuTextureView depthAttachment) {
        try (final Renderer renderer = Renderer.of(() -> "Color Boost Blit", colorAttachment, depthAttachment)) {
            renderer.setPipeline(AnimatiumPipelines.COLOR_BOOST_BLIT);
            renderer.setTexture("Sampler0", colorAttachment, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            renderer.draw(GEOMETRY);
        }
    }
}
