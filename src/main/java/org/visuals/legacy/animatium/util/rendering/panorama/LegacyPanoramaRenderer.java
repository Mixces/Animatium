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

import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.math.Axis;
import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.CachedPerspectiveProjectionMatrixBuffer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4i;
import org.visuals.legacy.animatium.util.Utils;
import org.visuals.legacy.animatium.util.rendering.ImmediateRenderer;

@UtilityClass
// Ported code of the old <=1.12.2 panorama renderer (w/ blur)
public class LegacyPanoramaRenderer {
	private final Vector4i VIEWPORT = new Vector4i(0, 0, 256, 256);
	private final Identifier[] PANORAMA_TEXTURES = new Identifier[]{
			Identifier.withDefaultNamespace("textures/gui/title/background/panorama_0.png"),
			Identifier.withDefaultNamespace("textures/gui/title/background/panorama_1.png"),
			Identifier.withDefaultNamespace("textures/gui/title/background/panorama_2.png"),
			Identifier.withDefaultNamespace("textures/gui/title/background/panorama_3.png"),
			Identifier.withDefaultNamespace("textures/gui/title/background/panorama_4.png"),
			Identifier.withDefaultNamespace("textures/gui/title/background/panorama_5.png")
	};

	private final CachedPerspectiveProjectionMatrixBuffer projectionMatrixBuffer = new CachedPerspectiveProjectionMatrixBuffer("panorama", 0.05F, 10.0F);
	private final MainTarget panoramaTarget = new MainTarget(256, 256);
	private final GpuTextureView backgroundTextureView;
	private float spin = 0.0F;

	static {
		final DynamicTexture dynamicTexture = new DynamicTexture(() -> "background", 256, 256, false);
		backgroundTextureView = dynamicTexture.getTextureView();
		RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(panoramaTarget.getDepthTexture(), 1.0F);
	}

	public void render(final GuiGraphics guiGraphics, final int width, final int height) {
		renderPanorama(panoramaTarget, width, height);
		for (int pass = 0; pass < 7; ++pass) {
			new BlurPassTexture(
					panoramaTarget,
					panoramaTarget.getColorTexture(),
					backgroundTextureView
			).render(guiGraphics.pose(), width, height, VIEWPORT);
		}

		guiGraphics.guiRenderState.submitGuiElement(
				new BlitFinalTexture(
						guiGraphics.pose(),
						backgroundTextureView,
						width,
						height,
						ARGB.white(1.0F)
				)
		);
	}

	private void renderPanorama(final RenderTarget renderTarget, final int width, final int height) {
		RenderSystem.setProjectionMatrix(projectionMatrixBuffer.getBuffer(width, height, 120.0F), ProjectionType.PERSPECTIVE);
		final Matrix4f rootMatrix = new Matrix4f()
				.rotateX(Utils.toRadians(180.0F))
				.rotateZ(Utils.toRadians(90.0F));
		for (int layer = 0; layer < 64; layer++) {
			final float x = (layer % 8 / 8.0F - 0.5F) / 64.0F;
			final float y = ((float) layer / 8 / 8.0F - 0.5F) / 64.0F;
			final Matrix4f layerMatrix = new Matrix4f(rootMatrix)
					.translate(x, y, 0.0F)
					.rotateX(Utils.toRadians(Mth.sin(spin / 400.0F) * 25.0F + 20.0F)) // xRot
					.rotateY(Utils.toRadians(-spin * 0.1F)); // yRot
			for (int panoramaIdx = 0; panoramaIdx < 6; panoramaIdx++) {
				final Matrix4f faceMatrix = new Matrix4f(layerMatrix);
				final Quaternionf rotation = switch (panoramaIdx) {
					case 1 -> Axis.YP.rotationDegrees(90.0F);
					case 2 -> Axis.YP.rotationDegrees(180.0F);
					case 3 -> Axis.YN.rotationDegrees(90.0F);
					case 4 -> Axis.XP.rotationDegrees(90.0F);
					case 5 -> Axis.XN.rotationDegrees(90.0F);
					default -> null;
				};
				if (rotation != null) {
					faceMatrix.rotate(rotation);
				}

				try (final ImmediateRenderer renderer = ImmediateRenderer.of("Legacy Panorama")) {
					renderer.setPipeline(PanoramaPipelines.LEGACY_PANORAMA);
					renderer.setViewport(VIEWPORT);
					renderer.setDynamicTransforms(renderer.getDynamicTransforms().withModelViewMatrix(faceMatrix));

					final int currentLayer = layer;
					renderer.setup((vertexConsumer) -> {
						final int color = ARGB.white(255.0F / (currentLayer + 1.0F));
						vertexConsumer.addVertex(-1.0F, -1.0F, 1.0F).setUv(0.0F, 0.0F).setColor(color);
						vertexConsumer.addVertex(1.0F, -1.0F, 1.0F).setUv(1.0F, 0.0F).setColor(color);
						vertexConsumer.addVertex(1.0F, 1.0F, 1.0F).setUv(1.0F, 1.0F).setColor(color);
						vertexConsumer.addVertex(-1.0F, 1.0F, 1.0F).setUv(0.0F, 1.0F).setColor(color);
					}, 4);

					renderer.setTexture(0, PANORAMA_TEXTURES[panoramaIdx]);
					renderer.drawTo(renderTarget);
				}
			}
		}
	}

	public void update(float tickDelta) {
		spin += tickDelta;
	}
}
