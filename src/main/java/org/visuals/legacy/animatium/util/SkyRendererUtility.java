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
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.ARGB;
import org.joml.Matrix4fStack;
import org.visuals.legacy.animatium.AnimatiumClient;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Consumer;

public final class SkyRendererUtility {
    private static final RenderPipeline.Snippet LEGACY_SKY_PIPELINE_SNIPPET =
            RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
                    .withLocation(AnimatiumClient.id("pipeline/legacy_sky"))
                    .withVertexShader(AnimatiumClient.id("core/legacy_sky"))
                    .withFragmentShader(AnimatiumClient.id("core/legacy_sky"))
                    .withDepthWrite(false)
                    .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
                    .buildSnippet();

    public static final RenderPipeline LEGACY_SKY_PIPELINE =
            RenderPipelines.register(RenderPipeline.builder(LEGACY_SKY_PIPELINE_SNIPPET)
                    .withLocation(AnimatiumClient.id("pipeline/legacy_sky"))
                    .build());

    public static final RenderPipeline LEGACY_SKY_PLANAR_FOG_PIPELINE =
            RenderPipelines.register(RenderPipeline.builder(LEGACY_SKY_PIPELINE_SNIPPET)
                    .withLocation(AnimatiumClient.id("pipeline/legacy_sky_planar_fog"))
                    .withShaderDefine("PLANAR_FOG")
                    .build());

    private static GpuBuffer vertexBuffer = null;

    private SkyRendererUtility() {
    }

    public static RenderPipeline getLegacySkyPipeline(boolean planar) {
        return planar ? LEGACY_SKY_PLANAR_FOG_PIPELINE : LEGACY_SKY_PIPELINE;
    }

    public static GpuBuffer getGpuBuffer() {
        if (vertexBuffer == null) {
            vertexBuffer = initializeSky((builder) -> buildSkyHalf(builder, -16.0F, true));
        }

        return vertexBuffer;
    }

    public static void renderBlueVoid(int skyColor, double depth) {
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.translate(0.0F, AnimatiumConfig.instance().extras.dontMoveBlueVoid ? 12.0F : -((float) (depth - 16.0)), 0.0F);

        float red = ARGB.redFloat(skyColor);
        float green = ARGB.greenFloat(skyColor);
        float blue = ARGB.blueFloat(skyColor);
        GpuBufferSlice transforms = DynamicTransformsBuilder.of()
                .withShaderColor(red * 0.2F + 0.04F, green * 0.2F + 0.04F, blue * 0.6F + 0.1F)
                .build();

        RenderSystem.AutoStorageIndexBuffer quadsIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
        RenderTarget renderTarget = Minecraft.getInstance().getMainRenderTarget();
        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> "Blue void sky disc", renderTarget.getColorTextureView(), OptionalInt.empty(), renderTarget.getDepthTextureView(), OptionalDouble.empty())) {
            renderPass.setPipeline(getLegacySkyPipeline(AnimatiumConfig.instance().other.planarSkyFog));
            renderPass.setVertexBuffer(0, getGpuBuffer());
            renderPass.setIndexBuffer(quadsIndexBuffer.getBuffer(6), quadsIndexBuffer.type());
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", transforms);
            renderPass.drawIndexed(0, 0, 1014, 1);
        }

        modelViewStack.popMatrix();
    }

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
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(8112)) {
            BufferBuilder builder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
            bufferBuilderConsumer.accept(builder);
            try (MeshData meshData = builder.buildOrThrow()) {
                return RenderSystem.getDevice().createBuffer(() -> "Static sky vertex buffer", GpuBuffer.USAGE_VERTEX, meshData.vertexBuffer());
            } catch (Exception ignored) {
                return null;
            }
        }
    }

    static {
        IrisUtil.assignPipeline(IrisPipeline.SKY_BASIC, LEGACY_SKY_PIPELINE, LEGACY_SKY_PLANAR_FOG_PIPELINE);
    }
}
