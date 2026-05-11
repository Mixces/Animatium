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

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.BlendFactor;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import org.visuals.legacy.animatium.Animatium;

import java.util.Optional;

public class AnimatiumPipelines {
    // Panorama
    public static final BlendFunction PANORAMA_BLEND = new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA, BlendFactor.ONE, BlendFactor.ZERO);

    public static final RenderPipeline.Snippet TEXTURED_QUAD = RenderPipeline.builder()
            .withBindGroupLayout(BindGroupLayouts.GLOBALS)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .buildSnippet();

    public static final RenderPipeline.Snippet LEGACY_PANORAMA_SNIPPET = RenderPipeline.builder(TEXTURED_QUAD)
            .withVertexShader(Animatium.location("core/legacy_panorama"))
            .withFragmentShader(Animatium.location("core/legacy_panorama"))
            .withDepthStencilState(RenderUtils.NO_DEPTH_WRITE)
            .withCull(false)
            .withVertexBinding(0, DefaultVertexFormat.POSITION)
            .buildSnippet();

    public static final RenderPipeline LEGACY_PANORAMA_1 = RenderPipelines.register(RenderPipeline.builder(LEGACY_PANORAMA_SNIPPET)
            .withLocation(Animatium.location("pipeline/legacy_panorama_1"))
            .withColorTargetState(new ColorTargetState(Optional.of(PANORAMA_BLEND), GpuFormat.RGBA8_UNORM, ColorTargetState.WRITE_ALL))
            .build());

    public static final RenderPipeline LEGACY_PANORAMA_2 = RenderPipelines.register(RenderPipeline.builder(LEGACY_PANORAMA_SNIPPET)
            .withLocation(Animatium.location("pipeline/legacy_panorama_2"))
            .withColorTargetState(new ColorTargetState(Optional.of(PANORAMA_BLEND), GpuFormat.RGBA8_UNORM, ColorTargetState.WRITE_COLOR))
            .build());

    public static final RenderPipeline LEGACY_PANORAMA_BLUR = RenderPipelines.register(RenderPipeline.builder(TEXTURED_QUAD)
            .withLocation(Animatium.location("pipeline/legacy_panorama_blur"))
            .withVertexShader(Animatium.location("core/legacy_panorama_blur"))
            .withFragmentShader(Animatium.location("core/legacy_panorama_blur"))
            .withColorTargetState(new ColorTargetState(Optional.of(PANORAMA_BLEND), GpuFormat.RGBA8_UNORM, ColorTargetState.WRITE_COLOR))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX)
            .build());

    // Color Boost
    public static final RenderPipeline COLOR_BOOST_BLIT = RenderPipelines.register(RenderPipeline.builder()
            .withLocation(Animatium.location("pipeline/colorboost"))
            .withVertexShader("core/screenquad")
            .withFragmentShader(Animatium.location("core/colorboost"))
            .withBindGroupLayout(BindGroupLayout.builder().withSampler("Sampler0").build())
            .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
            .build());
}
