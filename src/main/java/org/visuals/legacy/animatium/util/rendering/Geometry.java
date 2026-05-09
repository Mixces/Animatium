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
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import org.joml.Matrix3x2f;

import java.util.function.Consumer;

public record Geometry(RenderPipeline pipeline, GpuBuffer vertexBuffer, int indexCount,
                       boolean persistent) implements AutoCloseable {
    public static Geometry texturedScreenQuad(final RenderPipeline pipeline, final Matrix3x2f pose, final int width, final int height) {
        if (pipeline.getPrimitiveTopology() != PrimitiveTopology.QUADS) {
            throw new RuntimeException("Only quads");
        } else {
            return compile(pipeline, 4, vertexConsumer -> {
                vertexConsumer.addVertexWith2DPose(pose, width, height).setUv(0.0F, 1.0F);
                vertexConsumer.addVertexWith2DPose(pose, width, 0.0F).setUv(1.0F, 1.0F);
                vertexConsumer.addVertexWith2DPose(pose, 0.0F, 0.0F).setUv(1.0F, 0.0F);
                vertexConsumer.addVertexWith2DPose(pose, 0.0F, height).setUv(0.0F, 0.0F);
            });
        }
    }

    private static Geometry compile(final RenderPipeline pipeline, final int vertexCount, final Consumer<VertexConsumer> vertexConsumer, final boolean persistent) {
        final VertexFormat format = pipeline.getVertexFormatBinding(0);
        assert format != null;
        try (final ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(format.getVertexSize() * vertexCount)) {
            final BufferBuilder builder = new BufferBuilder(byteBufferBuilder, pipeline.getPrimitiveTopology(), format);
            vertexConsumer.accept(builder);
            try (final MeshData meshData = builder.buildOrThrow()) {
                final GpuDevice device = RenderSystem.getDevice();
                final GpuBuffer vertexBuffer = device.createBuffer(() -> "Vertex buffer for " + pipeline, GpuBuffer.USAGE_VERTEX, meshData.vertexBuffer());
                return new Geometry(pipeline, vertexBuffer, meshData.drawState().indexCount(), persistent);
            }
        }
    }

    public static Geometry compile(final RenderPipeline pipeline, final int vertexCount, final Consumer<VertexConsumer> vertexConsumer) {
        return compile(pipeline, vertexCount, vertexConsumer, false);
    }

    public static Geometry compilePersistent(final RenderPipeline pipeline, final int vertexCount, final Consumer<VertexConsumer> vertexConsumer) {
        return compile(pipeline, vertexCount, vertexConsumer, true);
    }

    @Override
    public void close() {
        if (!this.persistent) {
            this.forceClose();
        }
    }

    public void forceClose() {
        this.vertexBuffer.close();
    }
}
