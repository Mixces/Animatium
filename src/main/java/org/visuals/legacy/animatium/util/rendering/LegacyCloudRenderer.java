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

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.*;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CloudRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.visuals.legacy.animatium.Animatium;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

/**
 * CODE SOURCED FROM 1.21.5 AND MODIFIED TO WORK IN LATEST
 * THIS CODE WAS MADE BY MOJANG STUDIOS
 */
public final class LegacyCloudRenderer extends SimplePreparableReloadListener<Optional<CloudRenderer.TextureData>> implements AutoCloseable {
    public static final LegacyCloudRenderer INSTANCE = new LegacyCloudRenderer();

    private static final RenderPipeline.Snippet CLOUDS_SNIPPET = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
            .withVertexShader(Animatium.location("core/legacy_clouds"))
            .withFragmentShader("core/rendertype_clouds")
            .withDepthStencilState(DepthStencilState.DEFAULT)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .buildSnippet();

    public static final RenderPipeline CLOUDS = RenderPipelines.register(RenderPipeline.builder(CLOUDS_SNIPPET)
            .withLocation(Animatium.location("pipeline/legacy_clouds"))
            .build());

    public static final RenderPipeline FLAT_CLOUDS = RenderPipelines.register(RenderPipeline.builder(CLOUDS_SNIPPET)
            .withLocation(Animatium.location("pipeline/legacy_flat_clouds"))
            .withCull(false)
            .build());

    public static final RenderPipeline CLOUDS_DEPTH_ONLY = RenderPipelines.register(RenderPipeline.builder(CLOUDS_SNIPPET)
            .withLocation("pipeline/clouds_depth_only")
            .withColorTargetState(new ColorTargetState(Optional.of(BlendFunction.TRANSLUCENT), GpuFormat.RGBA8_UNORM, ColorTargetState.WRITE_NONE))
            .build());

    private boolean needsRebuild = true;
    private CloudRenderer.RelativeCameraPos prevRelativeCameraPos = CloudRenderer.RelativeCameraPos.INSIDE_CLOUDS;
    private CloudStatus prevType;
    private int prevCellX = Integer.MIN_VALUE;
    private int prevCellZ = Integer.MIN_VALUE;
    private CloudRenderer.TextureData textureData;
    private GpuBuffer vertexBuffer = null;
    private int indexCount = 0;

    private void setupMesh(RenderPipeline pipeline, int cellX, int cellZ, CloudStatus cloudStatus, CloudRenderer.RelativeCameraPos relativeCameraPos) {
        final int colorA = ARGB.colorFromFloat(0.8F, 0.7F, 0.7F, 0.7F);
        final int colorB = ARGB.colorFromFloat(0.8F, 1.0F, 1.0F, 1.0F);
        final int colorC = ARGB.colorFromFloat(0.8F, 0.9F, 0.9F, 0.9F);
        final int colorD = ARGB.colorFromFloat(0.8F, 0.8F, 0.8F, 0.8F);

        final ByteBufferBuilder byteBufferBuilder = new ByteBufferBuilder(52 * 64 * 64 * DefaultVertexFormat.POSITION_COLOR.getVertexSize());
        final BufferBuilder builder = new BufferBuilder(byteBufferBuilder, pipeline.getPrimitiveTopology(), Objects.requireNonNull(pipeline.getVertexFormatBinding(0)));
        this.buildMesh(relativeCameraPos, builder, cellX, cellZ, colorA, colorB, colorC, colorD, cloudStatus == CloudStatus.FANCY);
        try (final MeshData meshData = builder.build()) {
            if (meshData == null) {
                this.indexCount = 0;
            } else {
                this.indexCount = meshData.drawState().indexCount();
                if (this.vertexBuffer != null && this.vertexBuffer.size() >= meshData.vertexBuffer().remaining()) {
                    RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.vertexBuffer.slice(), meshData.vertexBuffer());
                } else {
                    if (this.vertexBuffer != null) {
                        this.vertexBuffer.close();
                        this.vertexBuffer = null;
                    }

                    this.vertexBuffer = RenderSystem.getDevice().createBuffer(() -> "Cloud vertex buffer", GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_COPY_DST, meshData.vertexBuffer());
                }
            }
        }
    }

    private void buildMesh(CloudRenderer.RelativeCameraPos relativeCameraPos, BufferBuilder builder, int cellX, int cellZ, int i3, int i4, int i5, int i6, boolean fancy) {
        if (this.textureData != null) {
            final int width = this.textureData.width();
            for (int y = -32; y <= 32; y++) {
                final int modX = Math.floorMod(cellZ + y, this.textureData.height());
                for (int x = -32; x <= 32; x++) {
                    final int modZ = Math.floorMod(cellX + x, width);
                    final long cellData = this.textureData.cells()[modZ + modX * width];
                    if (cellData != 0L) {
                        final int cellColor = (int) (cellData >> 4 & 4294967295L);
                        if (fancy) {
                            final int bottomColor = ARGB.multiply(i3, cellColor);
                            final int topColor = ARGB.multiply(i4, cellColor);
                            final int sideColor = ARGB.multiply(i5, cellColor);
                            final int frontColor = ARGB.multiply(i6, cellColor);
                            final float f = x * 12.0F;
                            final float f2 = f + 12.0F;
                            final float f5 = y * 12.0F;
                            final float f6 = f5 + 12.0F;
                            if (relativeCameraPos != CloudRenderer.RelativeCameraPos.BELOW_CLOUDS) {
                                builder.addVertex(f, 4.0F, f5).setColor(topColor);
                                builder.addVertex(f, 4.0F, f6).setColor(topColor);
                                builder.addVertex(f2, 4.0F, f6).setColor(topColor);
                                builder.addVertex(f2, 4.0F, f5).setColor(topColor);
                            }

                            if (relativeCameraPos != CloudRenderer.RelativeCameraPos.ABOVE_CLOUDS) {
                                builder.addVertex(f2, 0.0F, f5).setColor(bottomColor);
                                builder.addVertex(f2, 0.0F, f6).setColor(bottomColor);
                                builder.addVertex(f, 0.0F, f6).setColor(bottomColor);
                                builder.addVertex(f, 0.0F, f5).setColor(bottomColor);
                            }

                            if (CloudRenderer.isNorthEmpty(cellData) && y > 0) {
                                builder.addVertex(f, 0.0F, f5).setColor(frontColor);
                                builder.addVertex(f, 4.0F, f5).setColor(frontColor);
                                builder.addVertex(f2, 4.0F, f5).setColor(frontColor);
                                builder.addVertex(f2, 0.0F, f5).setColor(frontColor);
                            }

                            if (CloudRenderer.isSouthEmpty(cellData) && y < 0) {
                                builder.addVertex(f2, 0.0F, f6).setColor(frontColor);
                                builder.addVertex(f2, 4.0F, f6).setColor(frontColor);
                                builder.addVertex(f, 4.0F, f6).setColor(frontColor);
                                builder.addVertex(f, 0.0F, f6).setColor(frontColor);
                            }

                            if (CloudRenderer.isWestEmpty(cellData) && x > 0) {
                                builder.addVertex(f, 0.0F, f6).setColor(sideColor);
                                builder.addVertex(f, 4.0F, f6).setColor(sideColor);
                                builder.addVertex(f, 4.0F, f5).setColor(sideColor);
                                builder.addVertex(f, 0.0F, f5).setColor(sideColor);
                            }

                            if (CloudRenderer.isEastEmpty(cellData) && x < 0) {
                                builder.addVertex(f2, 0.0F, f5).setColor(sideColor);
                                builder.addVertex(f2, 4.0F, f5).setColor(sideColor);
                                builder.addVertex(f2, 4.0F, f6).setColor(sideColor);
                                builder.addVertex(f2, 0.0F, f6).setColor(sideColor);
                            }

                            if (Math.abs(x) <= 1 && Math.abs(y) <= 1) {
                                builder.addVertex(f2, 4.0F, f5).setColor(topColor);
                                builder.addVertex(f2, 4.0F, f6).setColor(topColor);
                                builder.addVertex(f, 4.0F, f6).setColor(topColor);
                                builder.addVertex(f, 4.0F, f5).setColor(topColor);

                                builder.addVertex(f, 0.0F, f5).setColor(bottomColor);
                                builder.addVertex(f, 0.0F, f6).setColor(bottomColor);
                                builder.addVertex(f2, 0.0F, f6).setColor(bottomColor);
                                builder.addVertex(f2, 0.0F, f5).setColor(bottomColor);

                                builder.addVertex(f2, 0.0F, f5).setColor(frontColor);
                                builder.addVertex(f2, 4.0F, f5).setColor(frontColor);
                                builder.addVertex(f, 4.0F, f5).setColor(frontColor);
                                builder.addVertex(f, 0.0F, f5).setColor(frontColor);
                                builder.addVertex(f, 0.0F, f6).setColor(frontColor);
                                builder.addVertex(f, 4.0F, f6).setColor(frontColor);
                                builder.addVertex(f2, 4.0F, f6).setColor(frontColor);
                                builder.addVertex(f2, 0.0F, f6).setColor(frontColor);

                                builder.addVertex(f, 0.0F, f5).setColor(sideColor);
                                builder.addVertex(f, 4.0F, f5).setColor(sideColor);
                                builder.addVertex(f, 4.0F, f6).setColor(sideColor);
                                builder.addVertex(f, 0.0F, f6).setColor(sideColor);
                                builder.addVertex(f2, 0.0F, f6).setColor(sideColor);
                                builder.addVertex(f2, 4.0F, f6).setColor(sideColor);
                                builder.addVertex(f2, 4.0F, f5).setColor(sideColor);
                                builder.addVertex(f2, 0.0F, f5).setColor(sideColor);
                            }
                        } else {
                            final float f = x * 12.0F;
                            final float f2 = f + 12.0F;
                            final float f3 = y * 12.0F;
                            final float f4 = f3 + 12.0F;
                            builder.addVertex(f, 0.0F, f3).setColor(ARGB.multiply(i4, cellColor));
                            builder.addVertex(f, 0.0F, f4).setColor(ARGB.multiply(i4, cellColor));
                            builder.addVertex(f2, 0.0F, f4).setColor(ARGB.multiply(i4, cellColor));
                            builder.addVertex(f2, 0.0F, f3).setColor(ARGB.multiply(i4, cellColor));
                        }
                    }
                }
            }
        }
    }

    public void render(int cloudColor, CloudStatus cloudStatus, float height, Vec3 cameraOffset, float ticks) {
        if (this.textureData != null) {
            double x = cameraOffset.x + ticks * 0.030000001F;
            double z = cameraOffset.z + 3.96F;
            final double scaledWidth = this.textureData.width() * 12.0;
            final double scaledHeight = this.textureData.height() * 12.0;
            x -= Mth.floor(x / scaledWidth) * scaledWidth;
            z -= Mth.floor(z / scaledHeight) * scaledHeight;
            final int cellX = Mth.floor(x / 12.0);
            final int cellZ = Mth.floor(z / 12.0);

            final float offsetBottom = (float) (height - cameraOffset.y);
            final float offsetTop = offsetBottom + 4.0F;
            final CloudRenderer.RelativeCameraPos relativeCameraPos = offsetTop < 0.0F ? CloudRenderer.RelativeCameraPos.ABOVE_CLOUDS : (offsetBottom > 0.0F ? CloudRenderer.RelativeCameraPos.BELOW_CLOUDS : CloudRenderer.RelativeCameraPos.INSIDE_CLOUDS);

            final RenderPipeline pipeline = cloudStatus == CloudStatus.FANCY ? CLOUDS : FLAT_CLOUDS;
            if (this.needsRebuild || cellX != this.prevCellX || cellZ != this.prevCellZ || relativeCameraPos != this.prevRelativeCameraPos || cloudStatus != this.prevType) {
                this.needsRebuild = false;
                this.prevRelativeCameraPos = relativeCameraPos;
                this.prevType = cloudStatus;
                this.prevCellX = cellX;
                this.prevCellZ = cellZ;
                this.setupMesh(pipeline, cellX, cellZ, cloudStatus, relativeCameraPos);
            }

            if (this.indexCount != 0) {
                final float f5 = (float) (x - cellX * 12.0F);
                final float f6 = (float) (z - cellZ * 12.0F);
                final Vector3f offset = new Vector3f(-f5, offsetBottom, -f6);
                if (pipeline != FLAT_CLOUDS) {
                    this.draw(CLOUDS_DEPTH_ONLY, offset, cloudColor);
                }

                this.draw(pipeline, offset, cloudColor);
            }
        }
    }

    private void draw(final RenderPipeline pipeline, final Vector3f offset, final int color) {
        RenderTarget cloudsTarget = Minecraft.getInstance().levelRenderer.cloudsTarget();
        try (final ImmediateRenderer renderer = ImmediateRenderer.of("Legacy Clouds")) {
            renderer.setPipeline(pipeline);
            if (cloudsTarget == null) {
                cloudsTarget = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            }

            final RenderSystem.AutoStorageIndexBuffer autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(renderer.getPipeline().getPrimitiveTopology());
            renderer.setup(this.vertexBuffer, autoStorageIndexBuffer.getBuffer(this.indexCount), autoStorageIndexBuffer.type(), this.indexCount);
            renderer.setDynamicTransforms(renderer.getDynamicTransforms()
                    .withShaderColor(ARGB.color(1.0F, color))
                    .withModelOffset(offset));
            renderer.drawTo(cloudsTarget);
        }
    }

    public void markForRebuild() {
        this.needsRebuild = true;
    }

    @Override
    protected @NotNull Optional<CloudRenderer.TextureData> prepare(final ResourceManager resourceManager, final ProfilerFiller profilerFiller) {
        try {
            final Optional<CloudRenderer.TextureData> optionalTextureData;
            try (final InputStream inputStream = resourceManager.open(CloudRenderer.TEXTURE_LOCATION); final NativeImage nativeImage = NativeImage.read(inputStream);) {
                final int width = nativeImage.getWidth();
                final int height = nativeImage.getHeight();
                final long[] cells = new long[width * height];
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        final int pixel = nativeImage.getPixel(x, y);
                        if (!CloudRenderer.isCellEmpty(pixel)) {
                            cells[x + y * width] = CloudRenderer.packCellData(
                                    pixel,
                                    CloudRenderer.isCellEmpty(nativeImage.getPixel(x, Math.floorMod(y - 1, height))),
                                    CloudRenderer.isCellEmpty(nativeImage.getPixel(Math.floorMod(x + 1, height), y)),
                                    CloudRenderer.isCellEmpty(nativeImage.getPixel(x, Math.floorMod(y + 1, height))),
                                    CloudRenderer.isCellEmpty(nativeImage.getPixel(Math.floorMod(x - 1, height), y))
                            );
                        }
                    }
                }

                optionalTextureData = Optional.of(new CloudRenderer.TextureData(cells, width, height));
            }

            return optionalTextureData;
        } catch (IOException ignored) {
            return Optional.empty();
        }
    }

    @Override
    protected void apply(Optional<CloudRenderer.TextureData> optional, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        this.textureData = optional.orElse(null);
        this.needsRebuild = true;
    }

    @Override
    public void close() {
        if (this.vertexBuffer != null) {
            this.vertexBuffer.close();
        }
    }
}
