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

package org.visuals.legacy.animatium.util.rendering;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.ARGB;
import org.joml.Matrix4fStack;
import org.joml.Vector4f;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.mixins.accessor.ClientLevelDataAccessor;
import org.visuals.legacy.animatium.util.compatibility.IrisPipeline;
import org.visuals.legacy.animatium.util.compatibility.IrisUtil;

import java.util.function.Consumer;

@UtilityClass
public class SkyRendererUtility {
    public final RenderPipeline.Snippet VOID_BOX_SNIPPET =
            RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
                    .withVertexShader("core/position")
                    .withFragmentShader("core/position")
                    .withDepthWrite(false)
                    .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
                    .buildSnippet();

    public final RenderPipeline VOID_BOX_PIPELINE =
            RenderPipelines.register(RenderPipeline.builder(VOID_BOX_SNIPPET)
                    .withLocation(Animatium.location("pipeline/void_box"))
                    .build());

    private final RenderPipeline.Snippet LEGACY_SKY_PIPELINE_SNIPPET =
            RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
                    .withLocation(Animatium.location("pipeline/legacy_sky"))
                    .withVertexShader(Animatium.location("core/legacy_sky"))
                    .withFragmentShader(Animatium.location("core/legacy_sky"))
                    .withDepthWrite(false)
                    .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
                    .buildSnippet();

    public final RenderPipeline LEGACY_SKY_PIPELINE =
            RenderPipelines.register(RenderPipeline.builder(LEGACY_SKY_PIPELINE_SNIPPET)
                    .withLocation(Animatium.location("pipeline/legacy_sky"))
                    .build());

    public final RenderPipeline LEGACY_SKY_PLANAR_FOG_PIPELINE =
            RenderPipelines.register(RenderPipeline.builder(LEGACY_SKY_PIPELINE_SNIPPET)
                    .withLocation(Animatium.location("pipeline/legacy_sky_planar_fog"))
                    .withShaderDefine("PLANAR_FOG")
                    .build());

    private Renderer blueVoidRenderer;
    private Renderer voidBoxRenderer;
    private GpuBuffer vertexBuffer = null;
    private int indexCount = -1;

    static {
        IrisUtil.assignPipeline(IrisPipeline.SKY_BASIC, LEGACY_SKY_PIPELINE, LEGACY_SKY_PLANAR_FOG_PIPELINE);
    }

    public RenderPipeline getLegacySkyPipeline(boolean planar) {
        return planar ? LEGACY_SKY_PLANAR_FOG_PIPELINE : LEGACY_SKY_PIPELINE;
    }

    public GpuBuffer getGpuBuffer() {
        if (vertexBuffer == null) {
            vertexBuffer = initializeSky((builder) -> buildSkyHalf(builder, -16.0F, true));
        }

        return vertexBuffer;
    }

    public void renderBlueVoid(int skyColor, double depth) {
        final Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.translate(0.0F, AnimatiumConfig.instance().extras.dontMoveBlueVoid ? 12.0F : -((float) (depth - 16.0)), 0.0F);

        if (blueVoidRenderer == null) {
            blueVoidRenderer = Renderer.of("Blue void sky disc");
        }

        final RenderPipeline pipeline = getLegacySkyPipeline(AnimatiumConfig.instance().other.planarSkyFog);
        blueVoidRenderer.setPipeline(pipeline);

        final RenderSystem.AutoStorageIndexBuffer quadsIndexBuffer = RenderSystem.getSequentialBuffer(pipeline.getVertexFormatMode());
        blueVoidRenderer.setup(getGpuBuffer(), quadsIndexBuffer.getBuffer(indexCount), quadsIndexBuffer.type(), indexCount);

        blueVoidRenderer.setDynamicTransforms(blueVoidRenderer.getDynamicTransforms().withShaderColor(new Vector4f(ARGB.redFloat(skyColor) * 0.2F + 0.04F, ARGB.greenFloat(skyColor) * 0.2F + 0.04F, ARGB.blueFloat(skyColor) * 0.6F + 0.1F, 1.0F)));
        blueVoidRenderer.draw();

        modelViewStack.popMatrix();
    }

    public void buildSkyHalf(VertexConsumer vertexConsumer, float y, boolean bottom) {
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

    public GpuBuffer initializeSky(Consumer<BufferBuilder> bufferBuilderConsumer) {
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(8112)) {
            final BufferBuilder builder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
            bufferBuilderConsumer.accept(builder);
            try (MeshData meshData = builder.buildOrThrow()) {
                indexCount = meshData.drawState().indexCount();
                return RenderSystem.getDevice().createBuffer(() -> "Static sky vertex buffer", GpuBuffer.USAGE_VERTEX, meshData.vertexBuffer());
            } catch (Exception ignored) {
                return null;
            }
        }
    }

    // TODO/NOTE: Figure out why its rendering differently than in 18w07a (last snapshot to have it)
    public void renderVoidBox(double depth) {
        if (voidBoxRenderer == null) {
            voidBoxRenderer = Renderer.of("Player Void Box");
            voidBoxRenderer.setPipeline(VOID_BOX_PIPELINE);
            voidBoxRenderer.setDynamicTransforms(voidBoxRenderer.getDynamicTransforms().withShaderColor(0xFF000000));
        }

        final float offset = -((float) (depth + 65.0));
        voidBoxRenderer.setup((vertexConsumer) -> {
            // Left
            vertexConsumer.addVertex(-1.0F, offset, 1.0F);
            vertexConsumer.addVertex(1.0F, offset, 1.0F);
            vertexConsumer.addVertex(1.0F, -1.0F, 1.0F);
            vertexConsumer.addVertex(-1.0F, -1.0F, 1.0F);

            // Right
            vertexConsumer.addVertex(-1.0F, -1.0F, -1.0F);
            vertexConsumer.addVertex(1.0F, -1.0F, -1.0F);
            vertexConsumer.addVertex(1.0F, offset, -1.0F);
            vertexConsumer.addVertex(-1.0F, offset, -1.0F);

            // Back
            vertexConsumer.addVertex(1.0F, -1.0F, -1.0F);
            vertexConsumer.addVertex(1.0F, -1.0F, 1.0F);
            vertexConsumer.addVertex(1.0F, offset, 1.0F);
            vertexConsumer.addVertex(1.0F, offset, -1.0F);

            // Front
            vertexConsumer.addVertex(-1.0F, offset, -1.0F);
            vertexConsumer.addVertex(-1.0F, offset, 1.0F);
            vertexConsumer.addVertex(-1.0F, -1.0F, 1.0F);
            vertexConsumer.addVertex(-1.0F, -1.0F, -1.0F);

            // Bottom
            vertexConsumer.addVertex(-1.0F, -1.0F, -1.0F);
            vertexConsumer.addVertex(-1.0F, -1.0F, 1.0F);
            vertexConsumer.addVertex(1.0F, -1.0F, 1.0F);
            vertexConsumer.addVertex(1.0F, -1.0F, -1.0F);
        }, 20);

        voidBoxRenderer.draw();
    }

    public double getHorizonEyeHeight(ClientLevel level, float tickDelta) {
        return Minecraft.getInstance().player.getEyePosition(tickDelta).y - getHorizonDepth(level);
    }

    public double getHorizonDepth(ClientLevel level) {
        final ClientLevel.ClientLevelData levelData = level.getLevelData();
        if (AnimatiumConfig.instance().other.skyHorizonHeight) {
            return ((ClientLevelDataAccessor) levelData).animatium$isFlatWorld() ? 0.0D : 63.0D;
        } else {
            return levelData.getHorizonHeight(level);
        }
    }

    public void close() {
        if (blueVoidRenderer != null) {
            blueVoidRenderer.close();
            blueVoidRenderer = null;
        }

        if (voidBoxRenderer != null) {
            voidBoxRenderer.close();
            voidBoxRenderer = null;
        }
    }
}
