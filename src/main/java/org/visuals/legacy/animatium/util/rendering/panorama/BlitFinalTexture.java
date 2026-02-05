package org.visuals.legacy.animatium.util.rendering.panorama;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import net.minecraft.client.renderer.RenderPipelines;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public record BlitFinalTexture(
		Matrix3x2f pose,
		GpuTextureView texture,
		int width,
		int height,
		int color
) implements GuiElementRenderState {
	@Override
	public void buildVertices(VertexConsumer consumer) {
		final float aspect = 120.0F / Math.max(this.width, this.height);
		final float sw = this.width * aspect / 256.0F;
		final float sh = this.height * aspect / 256.0F;
		consumer.addVertexWith2DPose(this.pose, 0.0F, this.height).setUv(0.5F - sh, 0.5F + sw).setColor(this.color);
		consumer.addVertexWith2DPose(this.pose, this.width, this.height).setUv(0.5F - sh, 0.5F - sw).setColor(this.color);
		consumer.addVertexWith2DPose(this.pose, this.width, 0.0F).setUv(0.5F + sh, 0.5F - sw).setColor(this.color);
		consumer.addVertexWith2DPose(this.pose, 0.0F, 0.0F).setUv(0.5F + sh, 0.5F + sw).setColor(this.color);
	}

	@Override
	public @NotNull RenderPipeline pipeline() {
		return RenderPipelines.GUI_TEXTURED;
	}

	@Override
	public @NotNull TextureSetup textureSetup() {
		return TextureSetup.singleTexture(this.texture);
	}

	@Override
	public @Nullable ScreenRectangle scissorArea() {
		return null;
	}

	@Override
	public @NotNull ScreenRectangle bounds() {
		return new ScreenRectangle(0, 0, this.width, this.height).transformMaxBounds(this.pose);
	}
}
