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

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
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
		final float aspect = this.width > this.height ? 120.0F / this.width : 120.0F / this.height;
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
		return TextureSetup.singleTexture(this.texture, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
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
