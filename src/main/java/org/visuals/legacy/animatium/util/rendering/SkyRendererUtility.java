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

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.compatibility.IrisPipeline;
import org.visuals.legacy.animatium.util.compatibility.IrisUtil;

import java.util.function.Consumer;
import java.util.function.Function;

@UtilityClass
public class SkyRendererUtility {
    public final RenderPipeline.Snippet VOID_BOX_SNIPPET =
            RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
                    .withVertexShader("core/position")
                    .withFragmentShader("core/position")
                    .withDepthStencilState(RenderUtils.NO_DEPTH_WRITE)
                    .withVertexBinding(0, DefaultVertexFormat.POSITION)
                    .withPrimitiveTopology(PrimitiveTopology.QUADS)
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
                    .withDepthStencilState(RenderUtils.NO_DEPTH_WRITE)
                    .withVertexBinding(0, DefaultVertexFormat.POSITION)
                    .withPrimitiveTopology(PrimitiveTopology.QUADS)
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

    private static final Function<Float, Geometry> VOID_BOX_GEOMETRY = offset -> Geometry.compilePersistent(VOID_BOX_PIPELINE, 20, vertexConsumer -> {
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
    });

    private ImmediateRenderer blueVoidRenderer;
    private ImmediateRenderer voidBoxRenderer;
    private final GpuBuffer vertexBuffer = initializeSky(vertexConsumer -> buildSkyHalf(vertexConsumer, -16.0F, true));
    private int indexCount = -1;

    static {
        IrisUtil.assignPipeline(IrisPipeline.SKY_BASIC, LEGACY_SKY_PIPELINE, LEGACY_SKY_PLANAR_FOG_PIPELINE);
    }

    public RenderPipeline getLegacySkyPipeline(final boolean planar) {
        return planar ? LEGACY_SKY_PLANAR_FOG_PIPELINE : LEGACY_SKY_PIPELINE;
    }

    public GpuBuffer getGpuBuffer() {
        return vertexBuffer;
    }

    public void renderBlueVoid(final int skyColor, final double depth) {
        if (blueVoidRenderer == null) {
            blueVoidRenderer = ImmediateRenderer.of(() -> "Blue void sky disc");
        }

        final RenderPipeline pipeline = getLegacySkyPipeline(AnimatiumConfig.instance().other.planarSkyFog);
        blueVoidRenderer.setPipeline(pipeline);

        blueVoidRenderer.setup(new Geometry(pipeline, vertexBuffer, indexCount, true));
        blueVoidRenderer.draw(DynamicTransforms.builder()
                .withModelViewMatrix(RenderSystem.getModelViewMatrixCopy()
                        .translate(0.0F, AnimatiumConfig.instance().extras.dontMoveBlueVoid ? 12.0F : -((float) (depth - 16.0)), 0.0F))
                .withShaderColor(new Vector4f(ARGB.redFloat(skyColor) * 0.2F + 0.04F, ARGB.greenFloat(skyColor) * 0.2F + 0.04F, ARGB.blueFloat(skyColor) * 0.6F + 0.1F, 1.0F)));
    }

    public void buildSkyHalf(final VertexConsumer vertexConsumer, final float y, final boolean bottom) {
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

    public @Nullable GpuBuffer initializeSky(final Consumer<VertexConsumer> vertexConsumer) {
        try (final ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(8112)) {
            final BufferBuilder builder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION);
            vertexConsumer.accept(builder);
            try (final MeshData meshData = builder.buildOrThrow()) {
                indexCount = meshData.drawState().indexCount();
                return RenderSystem.getDevice().createBuffer(() -> "Sky vertex buffer", GpuBuffer.USAGE_VERTEX, meshData.vertexBuffer());
            } catch (final Exception ignored) {
                return null;
            }
        }
    }

    // TODO/NOTE: Figure out why its rendering differently than in 18w07a (last snapshot to have it)
    public void renderVoidBox(final double depth) {
        if (voidBoxRenderer == null) {
            voidBoxRenderer = ImmediateRenderer.of(() -> "Player Void Box");
            voidBoxRenderer.setPipeline(VOID_BOX_PIPELINE);
        }

        voidBoxRenderer.setup(VOID_BOX_GEOMETRY.apply(-((float) (depth + 65.0))));
        voidBoxRenderer.draw(DynamicTransforms.builder().withShaderColor(0xFF000000));
    }

    public double getHorizonEyeHeight(final ClientLevel level, final float tickDelta) {
        return Minecraft.getInstance().player.getEyePosition(tickDelta).y - level.getLevelData().getHorizonHeight(level);
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

        if (vertexBuffer != null) {
            vertexBuffer.close();
        }
    }
}
