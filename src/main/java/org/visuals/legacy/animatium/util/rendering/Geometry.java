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

import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import org.joml.Matrix3x2f;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

public record Geometry(GpuBuffer vertexBuffer,
                       GpuBuffer indexBuffer,
                       IndexType indexType,
                       int indexCount,
                       boolean ownsIndexBuffer,
                       boolean persistent) implements AutoCloseable {
    public static Geometry texturedQuad(final RenderPipeline pipeline, final Matrix3x2f pose, final int width, final int height) {
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

    public static Geometry compile(final RenderPipeline pipeline, final boolean persistent, final int vertexCount, final Consumer<VertexConsumer> vertexConsumer) {
        final VertexFormat format = pipeline.getVertexFormatBinding(0);
        assert format != null;
        try (final ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(format.getVertexSize() * vertexCount)) {
            final BufferBuilder builder = new BufferBuilder(byteBufferBuilder, pipeline.getPrimitiveTopology(), format);
            vertexConsumer.accept(builder);
            try (final MeshData meshData = builder.buildOrThrow()) {
                final GpuDevice device = RenderSystem.getDevice();
                final GpuBuffer vertexBuffer = device.createBuffer(() -> "Vertex buffer for " + pipeline, GpuBuffer.USAGE_VERTEX, meshData.vertexBuffer());
                final int indexCount = meshData.drawState().indexCount();

                GpuBuffer indexBuffer;
                IndexType indexType;
                boolean ownsIndexBuffer;

                final ByteBuffer indexByteBuffer = meshData.indexBuffer();
                if (indexByteBuffer == null) {
                    final RenderSystem.AutoStorageIndexBuffer autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(pipeline.getPrimitiveTopology());
                    indexBuffer = autoStorageIndexBuffer.getBuffer(indexCount);
                    indexType = autoStorageIndexBuffer.type();
                    ownsIndexBuffer = false;
                } else {
                    indexBuffer = device.createBuffer(() -> "Index buffer for " + pipeline, GpuBuffer.USAGE_INDEX, indexByteBuffer);
                    indexType = meshData.drawState().indexType();
                    ownsIndexBuffer = true;
                }

                return new Geometry(vertexBuffer, indexBuffer, indexType, indexCount, ownsIndexBuffer, persistent);
            }
        }
    }

    public static Geometry compile(final RenderPipeline pipeline, final int vertexCount, final Consumer<VertexConsumer> vertexConsumer) {
        return compile(pipeline, false, vertexCount, vertexConsumer);
    }

    public void render(final RenderPass pass) {
        pass.setVertexBuffer(0, this.vertexBuffer.slice());
        pass.setIndexBuffer(this.indexBuffer, this.indexType);
        pass.drawIndexed(0, 0, this.indexCount, 1);
    }

    @Override
    public void close() {
        if (!this.persistent) {
            this.forceClose();
        }
    }

    public void forceClose() {
        this.vertexBuffer.close();
        if (this.ownsIndexBuffer) {
            this.indexBuffer.close();
        }
    }
}
