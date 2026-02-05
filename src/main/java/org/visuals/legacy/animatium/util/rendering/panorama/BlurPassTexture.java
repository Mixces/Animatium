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
