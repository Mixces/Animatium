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

import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderDefines;

import java.util.Map;
import java.util.Optional;

public final class RenderPipelineOverrider {
    public static RenderPipeline.Builder of(final RenderPipeline pipeline) {
        final RenderPipeline.Builder builder = RenderPipeline.builder();
        builder.withVertexShader(pipeline.getVertexShader());
        builder.withFragmentShader(pipeline.getFragmentShader());

        final ColorTargetState[] colorTargetStates = pipeline.getColorTargetStates();
        for (int i = 0; i < colorTargetStates.length; ++i) {
            final ColorTargetState colorTargetState = colorTargetStates[i];
            if (colorTargetState != null) {
                builder.withColorTargetState(i, colorTargetState);
            } else {
                builder.withUnusedColorTargetState(i);
            }
        }

        builder.withDepthStencilState(Optional.ofNullable(pipeline.getDepthStencilState()));
        builder.withPolygonMode(pipeline.getPolygonMode());
        builder.withCull(pipeline.isCull());
        for (final BindGroupLayout bindGroupLayout : pipeline.getBindGroupLayouts()) {
            builder.withBindGroupLayout(bindGroupLayout);
        }

        final VertexFormat[] vertexFormats = pipeline.getVertexFormatBindings();
        for (int i = 0; i < vertexFormats.length; ++i) {
            final VertexFormat vertexFormat = vertexFormats[i];
            if (vertexFormat != null) {
                builder.withVertexBinding(i, vertexFormat);
            }
        }

        final ShaderDefines defines = pipeline.getShaderDefines();
        for (final Map.Entry<String, String> define : defines.values().entrySet()) {
            ShaderDefines.Builder definesBuilder;
            if (builder.definesBuilder.isEmpty()) {
                definesBuilder = ShaderDefines.builder();
                builder.definesBuilder = Optional.of(definesBuilder);
            } else {
                definesBuilder = builder.definesBuilder.get();
            }

            definesBuilder.define(define.getKey(), define.getValue());
        }

        builder.withPrimitiveTopology(pipeline.getPrimitiveTopology());
        return builder;
    }
}
