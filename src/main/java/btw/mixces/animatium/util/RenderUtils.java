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

import btw.mixces.animatium.AnimatiumClient;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Consumer;

public final class RenderUtils {
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
            if (((ClientLevelDataAccessor) level.getLevelData()).animatium$isFlatWorld()) {
                return 0.0D;
            } else {
                return 63.0D;
            }
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

    public static void drawBuffer(BufferBuilder builder, RenderTarget renderTarget, RenderPipeline renderPipeline) {
        GpuBuffer vertexBuffer;
        GpuBuffer indexBuffer;
        VertexFormat.IndexType indexType;
        int indexCount;
        try (MeshData meshData = builder.buildOrThrow()) {
            indexCount = meshData.drawState().indexCount();
            vertexBuffer = renderPipeline.getVertexFormat().uploadImmediateVertexBuffer(meshData.vertexBuffer());
            if (meshData.indexBuffer() == null) {
                RenderSystem.AutoStorageIndexBuffer autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(meshData.drawState().mode());
                indexBuffer = autoStorageIndexBuffer.getBuffer(meshData.drawState().indexCount());
                indexType = autoStorageIndexBuffer.type();
            } else {
                indexBuffer = renderPipeline.getVertexFormat().uploadImmediateIndexBuffer(meshData.indexBuffer());
                indexType = meshData.drawState().indexType();
            }
        } catch (Exception e) {
            vertexBuffer = null;
            indexBuffer = null;
            indexType = null;
            indexCount = 0;
        }

        if (vertexBuffer == null) {
            throw new RuntimeException("Vertex buffer was null when trying to render buffer.");
        }

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> "Immediate Rendering", renderTarget.getColorTextureView(), OptionalInt.empty(), renderTarget.useDepth ? renderTarget.getDepthTextureView() : null, OptionalDouble.empty())) {
            renderPass.setPipeline(renderPipeline);
            renderPass.setVertexBuffer(0, vertexBuffer);
            renderPass.setIndexBuffer(indexBuffer, indexType);
            for (int i = 0; i < 12; i++) {
                GpuTextureView gpuTexture = RenderSystem.getShaderTexture(i);
                if (gpuTexture != null) {
                    renderPass.bindSampler("Sampler" + i, gpuTexture);
                }
            }

            renderPass.drawIndexed(0, 0, indexCount, 1);
        }
    }

    // Sky Stuff
    public static void buildSkyHalf(VertexConsumer vertexConsumer, float y, boolean bottom) {
        final int width = 64;
        for (int k = -384; k <= 384; k += width) {
            for (int l = -384; l <= 384; l += width) {
                float g = (float) k;
                float h = (float) (k + width);
                if (bottom) {
                    // Swap them
                    float b = g;
                    g = h;
                    h = b;
                }

                vertexConsumer.addVertex(g, y, (float) l);
                vertexConsumer.addVertex(h, y, (float) l);
                vertexConsumer.addVertex(h, y, (float) (l + width));
                vertexConsumer.addVertex(g, y, (float) (l + width));
            }
        }
    }

    private static GpuBuffer animatium$blueVoidBuffer = null;

    public static GpuBuffer getBlueVoidBuffer() {
        return animatium$blueVoidBuffer;
    }

    public static void initializeBlueVoidSky() {
        animatium$blueVoidBuffer = initializeSky((builder) -> buildSkyHalf(builder, -16.0F, true));
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

    public static void renderBlueVoidSky(Minecraft minecraft, ClientLevel level, int skyColor, double depth) {
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.translate(0.0F, -((float) (depth - 16.0)), 0.0F);

        Vector3f skyColorVec = ARGB.vector3fFromRGB24(skyColor);
        GpuBufferSlice transforms = DynamicTransformsBuilder.of()
                .withShaderColor(new Vector3f(skyColorVec.x * 0.2F + 0.04F, skyColorVec.y * 0.2F + 0.04F, skyColorVec.z * 0.6F + 0.1F))
                .build();

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> "Blue void disc", minecraft.getMainRenderTarget().getColorTextureView(), OptionalInt.empty(), minecraft.getMainRenderTarget().getDepthTextureView(), OptionalDouble.empty())) {
            RenderSystem.AutoStorageIndexBuffer autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
            renderPass.setPipeline(AnimatiumConfig.instance().planarSkyFog ? AnimatiumClient.LEGACY_SKY_PLANAR_FOG_PIPELINE : AnimatiumClient.LEGACY_SKY_PIPELINE);
            renderPass.setVertexBuffer(0, animatium$blueVoidBuffer);
            renderPass.setIndexBuffer(autoStorageIndexBuffer.getBuffer(6), autoStorageIndexBuffer.type());
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", transforms);
            renderPass.drawIndexed(0, 0, 1014, 1);
        }

        modelViewStack.popMatrix();
    }
}
