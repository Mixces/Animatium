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
 */

package btw.mixces.animatium.util;

import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.mixins.accessor.ClientLevelDataAccessor;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Consumer;

public final class RenderUtils {
    private RenderUtils() {
    }

    private static float LINE_WIDTH = -1.0F;

    public static float getLineWidth(Float def) {
        if (LINE_WIDTH == -1.0F) {
            return def == null ? RenderSystem.getShaderLineWidth() : def;
        } else {
            return LINE_WIDTH;
        }
    }

    public static void setLineWidth(float width) {
        LINE_WIDTH = width;
    }

    public static double getLevelHorizonHeight(ClientLevel level) {
        if (AnimatiumConfig.instance().skyHorizonHeight) {
            return ((ClientLevelDataAccessor) level.getLevelData()).animatium$isFlatWorld() ? 0.0D : 63.0D;
        } else {
            return level.getLevelData().getHorizonHeight(level);
        }
    }

    public static void fillVerticalLine(GuiGraphics context, int x, int y, int length, int color) {
        context.fill(x, y, x + 1, y + length, color);
    }

    public static void fillVerticalGradientLine(GuiGraphics context, int x, int y, int length, int startColor, int endColor) {
        context.fillGradient(x, y, x + 1, y + length, startColor, endColor);
    }

    public static void fillHorizontalLine(GuiGraphics context, int x, int y, int length, int color) {
        context.fill(x, y, x + length, y + 1, color);
    }

    public static void fillRectangle(GuiGraphics context, int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + height, color);
    }

    public static void drawBuffer(RenderPipeline renderPipeline, RenderTarget renderTarget, MeshData meshData, Consumer<RenderPass> renderPassConsumer) {
        GpuBufferSlice dynamicTransforms = DynamicTransformsBuilder.of().build();
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

            GpuTextureView colorTextureView = RenderSystem.outputColorTextureOverride != null ? RenderSystem.outputColorTextureOverride : renderTarget.getColorTextureView();
            GpuTextureView depthTextureView = renderTarget.useDepth ? (RenderSystem.outputDepthTextureOverride != null ? RenderSystem.outputDepthTextureOverride : renderTarget.getDepthTextureView()) : null;
            try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Immediate draw for " + renderPipeline, colorTextureView, OptionalInt.empty(), depthTextureView, OptionalDouble.empty())) {
                renderPass.setPipeline(renderPipeline);
                for (int i = 0; i < 12; ++i) {
                    GpuTextureView textureView = RenderSystem.getShaderTexture(i);
                    if (textureView != null) {
                        renderPass.bindSampler("Sampler" + i, textureView);
                    }
                }

                renderPass.setUniform("DynamicTransforms", dynamicTransforms);
                renderPassConsumer.accept(renderPass);
                RenderSystem.bindDefaultUniforms(renderPass);
                renderPass.setVertexBuffer(0, vertexBuffer);
                renderPass.setIndexBuffer(indexBuffer, indexType);
                renderPass.drawIndexed(0, 0, meshData.drawState().indexCount(), 1);
            }
        } catch (Throwable var17) {
            if (meshData != null) {
                try {
                    meshData.close();
                } catch (Throwable var14) {
                    var17.addSuppressed(var14);
                }
            }

            throw var17;
        }

        if (meshData != null) {
            meshData.close();
        }
    }

    // Sky Stuff
    public static void buildSkyHalf(VertexConsumer vertexConsumer, float y, boolean bottom) {
        final int width = 64;
        for (int k = -384; k <= 384; k += width) {
            for (int l = -384; l <= 384; l += width) {
                float g = k;
                float h = k + width;
                if (bottom) {
                    // Swap them
                    float b = g;
                    g = h;
                    h = b;
                }

                vertexConsumer.addVertex(g, y, l);
                vertexConsumer.addVertex(h, y, l);
                vertexConsumer.addVertex(h, y, (l + width));
                vertexConsumer.addVertex(g, y, (l + width));
            }
        }
    }

    public static GpuBuffer initializeSky(Consumer<BufferBuilder> bufferBuilderConsumer) {
        VertexFormat.Mode mode = VertexFormat.Mode.QUADS;
        BufferBuilder builder = Tesselator.getInstance().begin(mode, DefaultVertexFormat.POSITION);
        bufferBuilderConsumer.accept(builder);
        try (MeshData meshData = builder.buildOrThrow()) {
            return RenderSystem.getDevice().createBuffer(() -> "Static sky vertex buffer", GpuBuffer.USAGE_COPY_DST, meshData.vertexBuffer());
        } catch (Exception ignored) {
            return null;
        }
    }
}
