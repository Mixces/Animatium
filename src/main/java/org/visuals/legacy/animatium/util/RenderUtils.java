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

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import org.joml.Matrix3x2fStack;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.mixins.accessor.ClientLevelDataAccessor;

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

    public void drawBuffer(RenderPipeline renderPipeline, RenderTarget renderTarget, MeshData meshData, Consumer<RenderPass> renderPassConsumer) {
        try {
            GpuBufferSlice dynamicTransforms = DynamicTransformsBuilder.of().build();
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
        } catch (Throwable throwable) {
            if (meshData != null) {
                try {
                    meshData.close();
                } catch (Throwable var14) {
                    throwable.addSuppressed(var14);
                }
            }

            throw throwable;
        }

        meshData.close();
    }
}
