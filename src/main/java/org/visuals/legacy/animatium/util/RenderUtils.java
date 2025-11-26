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

import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;
import org.joml.Matrix4f;
import org.joml.Vector4i;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30C;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.mixins.accessor.ClientLevelDataAccessor;
import org.visuals.legacy.animatium.mixins.accessor.GameRendererAccessor;
import org.visuals.legacy.animatium.mixins.accessor.GuiRendererAccessor;

import java.nio.IntBuffer;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Consumer;

@UtilityClass
public class RenderUtils {
    // TODO/NOTE: To be removed in 1.21.11+
    private float LINE_WIDTH = -1.0F;

    // TODO/NOTE: To be removed in 1.21.11+
    public float getLineWidth(Float def) {
        if (LINE_WIDTH == -1.0F) {
            return def == null ? RenderSystem.getShaderLineWidth() : def;
        } else {
            return LINE_WIDTH;
        }
    }

    // TODO/NOTE: To be removed in 1.21.11+
    public void setLineWidth(float width) {
        LINE_WIDTH = width;
    }

    public double getLevelHorizonHeight(ClientLevel level) {
        if (AnimatiumConfig.instance().other.skyHorizonHeight) {
            return ((ClientLevelDataAccessor) level.getLevelData()).animatium$isFlatWorld() ? 0.0D : 63.0D;
        } else {
            return level.getLevelData().getHorizonHeight(level);
        }
    }

    public void fillVerticalLine(GuiGraphics context, int x, int y, int length, int color) {
        context.fill(x, y, x + 1, y + length, color);
    }

    public void fillVerticalGradientLine(GuiGraphics context, int x, int y, int length, int startColor, int endColor) {
        context.fillGradient(x, y, x + 1, y + length, startColor, endColor);
    }

    public void fillHorizontalLine(GuiGraphics context, int x, int y, int length, int color) {
        context.fill(x, y, x + length, y + 1, color);
    }

    public void fillRectangle(GuiGraphics context, int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + height, color);
    }

    public void drawScaledText(GuiGraphics guiGraphics, Font font, String text, int x, int y, float scale) {
        final Matrix3x2fStack stack = guiGraphics.pose();
        stack.pushMatrix();
        final float originX = stack.m20;
        final float originY = stack.m21;
        stack.setTranslation(0.0F, 0.0F);
        stack.scale(scale, scale);
        stack.setTranslation(originX, originY);
        guiGraphics.drawCenteredString(font, text, (int) (x / scale), (int) (y / scale), 0xFFFFFFFF);
        stack.popMatrix();
    }

    public void drawWithPipeline(
            final RenderTarget renderTarget,
            final RenderPipeline renderPipeline,
            final MeshData meshData,
            final RenderOverrides renderOverrides,
            final Consumer<RenderPass> renderPassConsumer
    ) {
        try {
            GpuBuffer vertexBuffer = renderPipeline.getVertexFormat().uploadImmediateVertexBuffer(meshData.vertexBuffer());
            GpuBuffer indexBuffer;
            VertexFormat.IndexType indexType;
            if (meshData.indexBuffer() == null) {
                RenderSystem.AutoStorageIndexBuffer autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(meshData.drawState().mode());
                indexBuffer = autoStorageIndexBuffer.getBuffer(meshData.drawState().indexCount());
                indexType = autoStorageIndexBuffer.type();
            } else {
                indexBuffer = renderPipeline.getVertexFormat().uploadImmediateIndexBuffer(meshData.indexBuffer());
                indexType = meshData.drawState().indexType();
            }

            final GpuTextureView colorTextureView = RenderSystem.outputColorTextureOverride != null ? RenderSystem.outputColorTextureOverride : renderTarget.getColorTextureView();
            final GpuTextureView depthTextureView = renderTarget.useDepth ? (RenderSystem.outputDepthTextureOverride != null ? RenderSystem.outputDepthTextureOverride : renderTarget.getDepthTextureView()) : null;
            try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Immediate draw for " + renderPipeline, colorTextureView, OptionalInt.empty(), depthTextureView, OptionalDouble.empty())) {
                final int prevFramebufferId = renderOverrides.applyFramebuffer();
                final IntBuffer viewportBuffer = renderOverrides.applyViewport();

                renderPass.setPipeline(renderPipeline);
                renderPass.setVertexBuffer(0, vertexBuffer);
                renderPass.setIndexBuffer(indexBuffer, indexType);
                for (int i = 0; i < 12; ++i) {
                    final GpuTextureView shaderTexture = RenderSystem.getShaderTexture(i);
                    if (shaderTexture != null) {
                        renderPass.bindSampler("Sampler" + i, shaderTexture);
                    }
                }

                RenderSystem.bindDefaultUniforms(renderPass);
                renderPassConsumer.accept(renderPass);
                renderPass.drawIndexed(0, 0, meshData.drawState().indexCount(), 1);
                if (viewportBuffer != null) {
                    GlStateManager._viewport(viewportBuffer.get(), viewportBuffer.get(), viewportBuffer.get(), viewportBuffer.get());
                }

                GlStateManager._glBindFramebuffer(GL30C.GL_FRAMEBUFFER, prevFramebufferId);
            }
        } catch (Throwable throwable) {
            if (meshData != null) {
                try {
                    meshData.close();
                } catch (Throwable meshDataThrowable) {
                    throwable.addSuppressed(meshDataThrowable);
                }
            }

            throw throwable;
        }

        meshData.close();
    }

    public void drawWithPipeline(
            final RenderTarget renderTarget,
            final RenderPipeline renderPipeline,
            final MeshData meshData,
            final Consumer<RenderPass> renderPassConsumer
    ) {
        drawWithPipeline(renderTarget, renderPipeline, meshData, RenderOverrides.DEFAULT, renderPassConsumer);
    }

    public void drawInGui(final RenderTarget renderTarget, final GuiElementRenderState element, final RenderOverrides renderOverrides) {
        final Minecraft minecraft = Minecraft.getInstance();
        final Window window = minecraft.getWindow();
        final GameRendererAccessor gameRendererAccessor = (GameRendererAccessor) minecraft.gameRenderer;
        final GuiRendererAccessor guiRendererAccessor = (GuiRendererAccessor) gameRendererAccessor.animatium$getGuiRenderer();
        RenderSystem.setProjectionMatrix(guiRendererAccessor.animatium$orthoMatrixBuffer().getBuffer((float) window.getWidth() / (float) window.getGuiScale(), (float) window.getHeight() / (float) window.getGuiScale()), ProjectionType.ORTHOGRAPHIC);

        final RenderPipeline pipeline = element.pipeline();
        final RenderSystem.AutoStorageIndexBuffer indexBuffer = RenderSystem.getSequentialBuffer(pipeline.getVertexFormatMode());
        final GpuDevice device = RenderSystem.getDevice();

        final BufferBuilder builder = Tesselator.getInstance().begin(pipeline.getVertexFormatMode(), pipeline.getVertexFormat());
        element.buildVertices(builder);
        GpuBuffer vertexBuffer;
        int indexCount;
        try (final MeshData meshData = builder.buildOrThrow()) {
            vertexBuffer = device.createBuffer(() -> "Immediate GUI Vertex Buffer", GpuBuffer.USAGE_VERTEX, meshData.vertexBuffer());
            indexCount = meshData.drawState().indexCount();
        }

        final GpuBufferSlice dynamicTransforms = DynamicTransformsBuilder.of()
                .withModelViewMatrix(new Matrix4f().setTranslation(0.0F, 0.0F, -11000.0F))
                .build();

        final GpuTextureView colorTextureView = RenderSystem.outputColorTextureOverride != null ? RenderSystem.outputColorTextureOverride : renderTarget.getColorTextureView();
        final GpuTextureView depthTextureView = renderTarget.useDepth ? (RenderSystem.outputDepthTextureOverride != null ? RenderSystem.outputDepthTextureOverride : renderTarget.getDepthTextureView()) : null;
        try (final RenderPass renderPass = device.createCommandEncoder().createRenderPass(() -> "Immediate GUI RenderPass", colorTextureView, OptionalInt.empty(), depthTextureView, OptionalDouble.empty())) {
            final int prevFramebufferId = renderOverrides.applyFramebuffer();
            final IntBuffer viewportBuffer = renderOverrides.applyViewport();

            renderPass.setPipeline(pipeline);
            renderPass.setVertexBuffer(0, vertexBuffer);
            renderPass.setIndexBuffer(indexBuffer.getBuffer(indexCount), indexBuffer.type());

            final TextureSetup textureSetup = element.textureSetup();
            if (textureSetup.texure0() != null) {
                renderPass.bindSampler("Sampler0", textureSetup.texure0());
            }

            if (textureSetup.texure1() != null) {
                renderPass.bindSampler("Sampler1", textureSetup.texure1());
            }

            if (textureSetup.texure2() != null) {
                renderPass.bindSampler("Sampler2", textureSetup.texure2());
            }

            renderPass.setUniform("DynamicTransforms", dynamicTransforms);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.drawIndexed(0, 0, indexCount, 1);
            if (viewportBuffer != null) {
                GlStateManager._viewport(viewportBuffer.get(), viewportBuffer.get(), viewportBuffer.get(), viewportBuffer.get());
            }

            GlStateManager._glBindFramebuffer(GL30C.GL_FRAMEBUFFER, prevFramebufferId);
        }
    }

    public record RenderOverrides(@Nullable Vector4i viewport, int framebufferId) {
        public static final RenderOverrides DEFAULT = new RenderOverrides(null, -1);

        public int applyFramebuffer() {
            final int prevFbo = GL11.glGetInteger(GL30C.GL_FRAMEBUFFER_BINDING);
            if (this.framebufferId != -1) {
                GlStateManager._glBindFramebuffer(GL30C.GL_FRAMEBUFFER, this.framebufferId);
            }

            return prevFbo;
        }

        public @Nullable IntBuffer applyViewport() {
            IntBuffer viewportBuffer = null;
            if (this.viewport != null) {
                viewportBuffer = BufferUtils.createIntBuffer(4);
                GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewportBuffer);
                GlStateManager._viewport(this.viewport.x, this.viewport.y, this.viewport.z, this.viewport.w);
            }

            return viewportBuffer;
        }
    }
}
