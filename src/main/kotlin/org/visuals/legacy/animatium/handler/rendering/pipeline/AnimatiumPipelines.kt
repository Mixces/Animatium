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

package org.visuals.legacy.animatium.handler.rendering.pipeline

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.renderpearl.api.GpuFormat
import com.mojang.renderpearl.api.pipeline.*
import com.mojang.renderpearl.api.vertex.VertexFormat
import net.minecraft.client.renderer.BindGroupLayouts
import net.minecraft.client.renderer.RenderPipelines
import org.visuals.legacy.animatium.Animatium.location
import java.util.*

object AnimatiumPipelines {
    @JvmField
    val NO_DEPTH_WRITE = DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false)

    // Panorama
    @JvmField
    val PANORAMA_BLEND = BlendFunction(
        BlendFactor.SRC_ALPHA,
        BlendFactor.ONE_MINUS_SRC_ALPHA,
        BlendFactor.ONE,
        BlendFactor.ZERO
    )

    fun panoramaBlendState(colorMask: @ColorTargetState.WriteMask Int) =
        ColorTargetState(Optional.of(PANORAMA_BLEND), GpuFormat.RGBA8_UNORM, colorMask)

    @JvmField
    val TEXTURED_QUAD = RenderPipeline.builder(RenderPipelines.GLOBALS_SNIPPET)
        .withBindGroupLayouts(
            BindGroupLayouts.DYNAMIC_TRANSFORMS,
            BindGroupLayouts.PROJECTION,
            BindGroupLayouts.SAMPLER0
        )
        .withPrimitiveTopology(PrimitiveTopology.QUADS)
        .buildSnippet()

    @JvmField
    val LEGACY_PANORAMA_SNIPPET = RenderPipeline.builder(TEXTURED_QUAD)
        .withVertexShader(location("core/legacy_panorama"))
        .withFragmentShader(location("core/legacy_panorama"))
        .withDepthStencilState(NO_DEPTH_WRITE)
        .withCull(false)
        .withVertexFormat(DefaultVertexFormat.POSITION)
        .buildSnippet()

    @JvmField
    val LEGACY_PANORAMA_1 = RenderPipelines.register(
        RenderPipeline.builder(LEGACY_PANORAMA_SNIPPET)
            .withLocation(location("pipeline/legacy_panorama_1"))
            .withColorTargetState(panoramaBlendState(ColorTargetState.WRITE_ALL))
            .build()
    )

    @JvmField
    val LEGACY_PANORAMA_2 = RenderPipelines.register(
        RenderPipeline.builder(LEGACY_PANORAMA_SNIPPET)
            .withLocation(location("pipeline/legacy_panorama_2"))
            .withColorTargetState(panoramaBlendState(ColorTargetState.WRITE_COLOR))
            .build()
    )

    @JvmField
    val LEGACY_PANORAMA_BLUR = RenderPipelines.register(
        RenderPipeline.builder(TEXTURED_QUAD)
            .withLocation(location("pipeline/legacy_panorama_blur"))
            .withVertexShader(location("core/legacy_panorama_blur"))
            .withFragmentShader(location("core/legacy_panorama_blur"))
            .withColorTargetState(panoramaBlendState(ColorTargetState.WRITE_COLOR))
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX)
            .build()
    )

    // Sky
    @JvmField
    val VOID_BOX_SNIPPET =
        RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
            .withVertexShader("core/position_color")
            .withFragmentShader("core/position_color")
            .withDepthStencilState(NO_DEPTH_WRITE)
            .withColorTargetState(ColorTargetState.DEFAULT)
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
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
            .withDepthStencilState(NO_DEPTH_WRITE)
            .withColorTargetState(ColorTargetState.DEFAULT)
            .withVertexFormat(DefaultVertexFormat.POSITION)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
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

    // Clouds
    @JvmField
    val CLOUDS_SNIPPET = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
        .withVertexShader(location("core/legacy_clouds"))
        .withFragmentShader("core/clouds")
        .withDepthStencilState(DepthStencilState.DEFAULT)
        .withColorTargetState(ColorTargetState(BlendFunction.TRANSLUCENT))
        .withVertexFormat(DefaultVertexFormat.POSITION_COLOR)
        .withPrimitiveTopology(PrimitiveTopology.QUADS)
        .buildSnippet()

    @JvmField
    val CLOUDS = RenderPipelines.register(
        RenderPipeline.builder(CLOUDS_SNIPPET)
            .withLocation(location("pipeline/legacy_clouds"))
            .build()
    )

    @JvmField
    val FLAT_CLOUDS = RenderPipelines.register(
        RenderPipeline.builder(CLOUDS_SNIPPET)
            .withLocation(location("pipeline/legacy_flat_clouds"))
            .withCull(false)
            .build()
    )

    @JvmField
    val CLOUDS_DEPTH_ONLY = RenderPipelines.register(
        RenderPipeline.builder(CLOUDS_SNIPPET)
            .withLocation(location("pipeline/legacy_clouds_depth_only"))
            .withColorTargetState(
                ColorTargetState(
                    Optional.of(BlendFunction.TRANSLUCENT),
                    GpuFormat.RGBA8_UNORM,
                    ColorTargetState.WRITE_NONE
                )
            )
            .build()
    )

    // Color Boost
    @JvmField
    val COLOR_BOOST_BLIT: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder()
            .withLocation(location("pipeline/colorboost"))
            .withVertexShader("core/screenquad")
            .withFragmentShader(location("core/colorboost"))
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withColorTargetState(ColorTargetState.DEFAULT)
            .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
            .build()
    )

    // Lighting
    @JvmField
    val LEGACY_LIGHTMAP_INFO: BindGroupLayout = BindGroupLayout.builder()
        .withUniform("LightmapInfo", UniformType.UNIFORM_BUFFER)
        .build()

    @JvmField
    val LEGACY_LIGHTMAP: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder()
            .withLocation(location("pipeline/legacy_lightmap"))
            .withVertexShader("core/screenquad")
            .withFragmentShader(location("core/legacy_lightmap"))
            .withBindGroupLayout(LEGACY_LIGHTMAP_INFO)
            .withColorTargetState(ColorTargetState.DEFAULT)
            .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
            .build()
    )

    // Glint
    @JvmField
    val POSITION_TEX_OVERLAY = VertexFormat.builder(0)
        .addAttribute(DefaultVertexFormat.POSITION_SEMANTIC_NAME, GpuFormat.RGB32_FLOAT)
        .addAttribute(DefaultVertexFormat.UV0_SEMANTIC_NAME, GpuFormat.RG32_FLOAT)
        .addAttribute(DefaultVertexFormat.UV1_SEMANTIC_NAME, GpuFormat.RG16_SINT)
        .build()

    @JvmField
    val ARMOR_GLINT = RenderPipelines.GLINT.builder()
        .withLocation(location("pipeline/armor_glint"))
        .withVertexShader(location("core/armor_glint"))
        .withFragmentShader(location("core/armor_glint"))
        .withBindGroupLayout(BindGroupLayouts.SAMPLER1)
        .withVertexFormat(POSITION_TEX_OVERLAY)
        .build()
}