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

package org.visuals.legacy.animatium.util;

import com.mojang.blaze3d.opengl.GlDevice;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.opengl.GlTextureView;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.visuals.legacy.animatium.Animatium;

@UtilityClass
// Ported code of the old <=1.12.2 panorama renderer (w/ blur)
public class PanoramaRendererUtility {
    private GpuTextureView backgroundTextureView = null;
    private float spin = 0.0F;

    /**
     * In PanoramaRenderer, call this method before ``cubeMap.render``
     */
    public void setup(final RenderTarget renderTarget) {
        if (backgroundTextureView == null) {
            final GpuDevice device = RenderSystem.getDevice();
            final GpuTexture backgroundTexture = device.createTexture(() -> "Background texture", 15, TextureFormat.RGBA8, 256, 256, 1, 1);
            backgroundTextureView = device.createTextureView(backgroundTexture);
        }

        renderTarget.resize(256, 256);
    }

    /**
     * In PanoramaRenderer, call this method after ``cubeMap.render``
     *
     * @param guiGraphics The GuiGraphics
     * @param width       Screen width
     * @param height      Screen Height
     */
    public void render(final GuiGraphics guiGraphics, final RenderTarget renderTarget, final int width, final int height) {
        for (int i = 0; i < 7; ++i) {
            guiGraphics.guiRenderState.submitGuiElement(new BlurTextureBlit(guiGraphics.pose(), renderTarget, backgroundTextureView, width, height));
        }

        final Window window = Minecraft.getInstance().getWindow();
        renderTarget.resize(window.getWidth(), window.getHeight());
        guiGraphics.guiRenderState.submitGuiElement(new FinalTextureBlit(guiGraphics.pose(), backgroundTextureView, width, height, 120.0F / (float) (Math.max(width, height))));
    }

    /**
     * In PanoramaRenderer, call this method before ``cubeMap.render`` and after PanoramaRenderUtility#setup
     *
     * @param tickDelta The current game tick value
     */
    public void update(float tickDelta) {
        spin += tickDelta;
    }

    public float getXRot() {
        return Mth.sin(spin / 400.0F) * 25.0F + 20.0F;
    }

    public float getYRot() {
        return -spin * 0.1F;
    }

    private record BlurTextureBlit(Matrix3x2f pose, RenderTarget renderTarget, GpuTextureView textureView, int width, int height) implements GuiElementRenderState {
        public BlurTextureBlit {
            textureView.texture().setTextureFilter(FilterMode.LINEAR, FilterMode.LINEAR, false); // NOTE: Doesn't actually set the parameters till later on
            if (textureView instanceof GlTextureView glTextureView) {
                final int oldFbo = GL11.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING);
                GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, ((GlTexture) renderTarget.getColorTexture()).getFbo(((GlDevice) RenderSystem.getDevice()).directStateAccess(), renderTarget.getDepthTexture()));
                GlStateManager._bindTexture(glTextureView.texture().glId());
                GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
                GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, oldFbo);
            }
        }

        @Override
        public void buildVertices(VertexConsumer consumer) {
            for (int i = 0; i < 3; ++i) {
                final float growth = (float) (i - 1) / 256.0F;
                final int color = ARGB.white(1.0F / (float) (i + 1));
                consumer.addVertexWith2DPose(this.pose, this.width, this.height).setUv(0.0F + growth, 1.0F).setColor(color);
                consumer.addVertexWith2DPose(this.pose, this.width, 0.0F).setUv(1.0F + growth, 1.0F).setColor(color);
                consumer.addVertexWith2DPose(this.pose, 0.0F, 0.0F).setUv(1.0F + growth, 0.0F).setColor(color);
                consumer.addVertexWith2DPose(this.pose, 0.0F, this.height).setUv(0.0F + growth, 0.0F).setColor(color);
            }
        }

        @Override
        public @NotNull RenderPipeline pipeline() {
            return RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET)
                    .withLocation(Animatium.location("pipeline/blur_texture"))
                    .withColorWrite(true, false)
                    .withBlend(new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO))
                    .build();
        }

        @Override
        public @NotNull TextureSetup textureSetup() {
            return TextureSetup.singleTexture(this.textureView);
        }

        @Override
        public @Nullable ScreenRectangle scissorArea() {
            return null;
        }

        @Override
        public @NotNull ScreenRectangle bounds() {
            return new ScreenRectangle(0, 0, this.width, this.height).transformMaxBounds(pose);
        }
    }

    private record FinalTextureBlit(Matrix3x2f pose, GpuTextureView textureView, int width, int height, float aspect) implements GuiElementRenderState {
        @Override
        public void buildVertices(VertexConsumer consumer) {
            final int color = ARGB.white(1.0F);
            final float sw = (float) this.width * this.aspect / 256.0F;
            final float sh = (float) this.height * this.aspect / 256.0F;
            consumer.addVertexWith2DPose(this.pose, 0.0F, this.height).setUv(0.5F - sh, 0.5F + sw).setColor(color);
            consumer.addVertexWith2DPose(this.pose, this.width, this.height).setUv(0.5F - sh, 0.5F - sw).setColor(color);
            consumer.addVertexWith2DPose(this.pose, this.width, 0.0F).setUv(0.5F + sh, 0.5F - sw).setColor(color);
            consumer.addVertexWith2DPose(this.pose, 0.0F, 0.0F).setUv(0.5F + sh, 0.5F + sw).setColor(color);
        }

        @Override
        public @NotNull RenderPipeline pipeline() {
            return RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET)
                    .withLocation(Animatium.location("pipeline/basic_texture"))
                    .build();
        }

        @Override
        public @NotNull TextureSetup textureSetup() {
            return TextureSetup.singleTexture(this.textureView);
        }

        @Override
        public @Nullable ScreenRectangle scissorArea() {
            return null;
        }

        @Override
        public @NotNull ScreenRectangle bounds() {
            return new ScreenRectangle(0, 0, this.width, this.height).transformMaxBounds(pose);
        }
    }
}
