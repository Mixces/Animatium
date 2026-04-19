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

package org.visuals.legacy.animatium.util.rendering.panorama;

import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.BlendFactor;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.experimental.UtilityClass;
import net.minecraft.client.renderer.BindGroupLayouts;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.util.rendering.RenderUtils;

@UtilityClass
public class PanoramaPipelines {
	public BlendFunction PANORAMA_BLEND = new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA, BlendFactor.ONE, BlendFactor.ZERO);

	public RenderPipeline LEGACY_PANORAMA =
			RenderPipeline.builder()
					.withLocation(Animatium.location("pipeline/legacy_panorama"))
					.withVertexShader(Animatium.location("core/legacy_panorama"))
					.withFragmentShader(Animatium.location("core/legacy_panorama"))
                    .withColorTargetState(new ColorTargetState(PANORAMA_BLEND))
                    .withDepthStencilState(RenderUtils.NO_DEPTH_WRITE)
                    .withCull(false)
                    .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
                    .withBindGroupLayout(BindGroupLayout.builder().withSampler("Sampler0").build())
					.withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
					.build();
}
