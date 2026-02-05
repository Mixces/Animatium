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

import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import lombok.AllArgsConstructor;
import net.minecraft.util.ARGB;
import org.joml.Matrix3x2f;
import org.joml.Vector4i;
import org.visuals.legacy.animatium.util.rendering.Renderer;

@AllArgsConstructor
public class BlurPassTexture {
	private final RenderTarget renderTarget;
	private final GpuTexture sourceTexture;
	private final GpuTextureView destTextureView;

	public void render(final Matrix3x2f pose, final int width, final int height, final Vector4i viewport) {
		final GlTexture texture = (GlTexture) this.destTextureView.texture();
		texture.setTextureFilter(FilterMode.LINEAR, FilterMode.LINEAR, true);

		RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(
				sourceTexture, // source
				texture, // destination
				0, // mipLevel
				0, // destX
				0, // destY
				0, // srcX
				0, // srcY
				256, // width
				256 // height
		);

		try (final Renderer renderer = Renderer.of("Panorama Blur Pass")) {
			renderer.setPipeline(PanoramaPipelines.LEGACY_PANORAMA_BLUR);
			renderer.setFramebuffer(renderTarget);
			renderer.setViewport(viewport);
			renderer.setup((vertexConsumer) -> {
				for (int cycle = 0; cycle < 3; cycle++) {
					final int color = ARGB.white(1.0F / (cycle + 1.0F));
					final float growth = (cycle - 1.5F) / 256.0F;
					vertexConsumer.addVertexWith2DPose(pose, width, height).setUv(0.0F + growth, 1.0F).setColor(color);
					vertexConsumer.addVertexWith2DPose(pose, width, 0.0F).setUv(1.0F + growth, 1.0F).setColor(color);
					vertexConsumer.addVertexWith2DPose(pose, 0.0F, 0.0F).setUv(1.0F + growth, 0.0F).setColor(color);
					vertexConsumer.addVertexWith2DPose(pose, 0.0F, height).setUv(0.0F + growth, 0.0F).setColor(color);
				}
			}, 12);
			renderer.setTexture(0, destTextureView);
			renderer.drawInGui();
		}
	}
}
