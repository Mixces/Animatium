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

package org.visuals.legacy.animatium.handler.rendering

import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.DepthTestFunction
import com.mojang.blaze3d.platform.DestFactor
import com.mojang.blaze3d.platform.SourceFactor
import com.mojang.blaze3d.shaders.UniformType
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.RenderPipelines
import org.visuals.legacy.animatium.Animatium.location

object AnimatiumPipelines {
    // Panorama
    @JvmField
    val PANORAMA_BLEND = BlendFunction(
        SourceFactor.SRC_ALPHA,
        DestFactor.ONE_MINUS_SRC_ALPHA,
        SourceFactor.ONE,
        DestFactor.ZERO
    )

    @JvmField
    val TEXTURED_QUAD = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
        .withSampler("Sampler0")
        .buildSnippet()

    @JvmField
    val LEGACY_PANORAMA_SNIPPET = RenderPipeline.builder(TEXTURED_QUAD)
        .withVertexShader(location("core/legacy_panorama"))
        .withFragmentShader(location("core/legacy_panorama"))
        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
        .withDepthWrite(false)
        .withCull(false)
        .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
        .buildSnippet()

    @JvmField
    val LEGACY_PANORAMA_1 = RenderPipelines.register(
        RenderPipeline.builder(LEGACY_PANORAMA_SNIPPET)
            .withLocation(location("pipeline/legacy_panorama_1"))
            .withBlend(PANORAMA_BLEND)
            .build()
    )

    @JvmField
    val LEGACY_PANORAMA_2 = RenderPipelines.register(
        RenderPipeline.builder(LEGACY_PANORAMA_SNIPPET)
            .withLocation(location("pipeline/legacy_panorama_2"))
            .withBlend(PANORAMA_BLEND)
            .withColorWrite(true, false)
            .build()
    )

    @JvmField
    val LEGACY_PANORAMA_BLUR = RenderPipelines.register(
        RenderPipeline.builder(TEXTURED_QUAD)
            .withLocation(location("pipeline/legacy_panorama_blur"))
            .withVertexShader(location("core/legacy_panorama_blur"))
            .withFragmentShader(location("core/legacy_panorama_blur"))
            .withBlend(PANORAMA_BLEND)
            .withColorWrite(true, false)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .withDepthWrite(false)
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS)
            .build()
    )

    // Sky
    @JvmField
    val VOID_BOX_SNIPPET =
        RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
            .withVertexShader("core/position_color")
            .withFragmentShader("core/position_color")
            .withDepthWrite(false)
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
            .buildSnippet()

    @JvmField
    val VOID_BOX = RenderPipelines.register(
        RenderPipeline.builder(VOID_BOX_SNIPPET)
            .withLocation(location("pipeline/void_box"))
            .build()
    )

    @JvmField
    val LEGACY_SKY_SNIPPET =
        RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
            .withLocation(location("pipeline/legacy_sky"))
            .withVertexShader(location("core/legacy_sky"))
            .withFragmentShader(location("core/legacy_sky"))
            .withDepthWrite(false)
            .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
            .buildSnippet()

    @JvmField
    val LEGACY_SKY =
        RenderPipelines.register(
            RenderPipeline.builder(LEGACY_SKY_SNIPPET)
                .withLocation(location("pipeline/legacy_sky"))
                .build()
        )

    @JvmField
    val LEGACY_SKY_PLANAR_FOG: RenderPipeline =
        RenderPipelines.register(
            RenderPipeline.builder(LEGACY_SKY_SNIPPET)
                .withLocation(location("pipeline/legacy_sky_planar_fog"))
                .withShaderDefine("PLANAR_FOG")
                .build()
        )

    fun getSkyPipeline(planar: Boolean) = if (planar)
        LEGACY_SKY_PLANAR_FOG
    else
        LEGACY_SKY

    // Color Boost
    @JvmField
    val COLOR_BOOST_BLIT: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder()
            .withLocation(location("pipeline/colorboost"))
            .withVertexShader("core/screenquad")
            .withFragmentShader(location("core/colorboost"))
            .withSampler("Sampler0")
            .withVertexFormat(DefaultVertexFormat.EMPTY, VertexFormat.Mode.TRIANGLES)
            .build()
    )

    // Lighting
    @JvmField
    val LEGACY_LIGHTMAP: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder()
            .withLocation(location("pipeline/legacy_lightmap"))
            .withVertexShader("core/screenquad")
            .withFragmentShader(location("core/legacy_lightmap"))
            .withUniform("LightmapInfo", UniformType.UNIFORM_BUFFER)
            .withVertexFormat(DefaultVertexFormat.EMPTY, VertexFormat.Mode.TRIANGLES)
            .build()
    )
}