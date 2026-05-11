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

package btw.lowercase.renderer.buffer;

import btw.lowercase.renderer.vertex.VertexLayout;
import btw.lowercase.renderer.vertex.VertexLayouts;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public interface Geometry extends AutoCloseable {
    static Indexed texturedScreenQuad(final Matrix3x2f pose, final int width, final int height) {
        return Indexed.compile(VertexLayouts.TEXTURED_QUAD, 4, vertexConsumer -> {
            vertexConsumer.addVertexWith2DPose(pose, width, height).setUv(0.0F, 1.0F);
            vertexConsumer.addVertexWith2DPose(pose, width, 0.0F).setUv(1.0F, 1.0F);
            vertexConsumer.addVertexWith2DPose(pose, 0.0F, 0.0F).setUv(1.0F, 0.0F);
            vertexConsumer.addVertexWith2DPose(pose, 0.0F, height).setUv(0.0F, 0.0F);
        });
    }

    boolean persistent();

    void close();

    record Basic(int firstVertex, int vertexCount) implements Geometry {
        @Override
        public boolean persistent() {
            return true;
        }

        @Override
        public void close() {
        }
    }

    record Indexed(VertexLayout vertexLayout, GpuBuffer vertexBuffer, int indexCount,
                   boolean persistent) implements Geometry {
        private static Indexed compile(final @NonNull VertexLayout vertexLayout, final int vertexCount, final Consumer<VertexConsumer> vertexConsumer, final boolean persistent) {
            try (final ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(vertexLayout.vertexFormat().getVertexSize() * vertexCount)) {
                final BufferBuilder builder = new BufferBuilder(byteBufferBuilder, vertexLayout.primitiveTopology(), vertexLayout.vertexFormat());
                vertexConsumer.accept(builder);
                try (final MeshData meshData = builder.buildOrThrow()) {
                    final GpuDevice device = RenderSystem.getDevice();
                    final GpuBuffer vertexBuffer = device.createBuffer(() -> "Vertex buffer for " + vertexLayout.vertexFormat(), GpuBuffer.USAGE_VERTEX, meshData.vertexBuffer());
                    return new Indexed(vertexLayout, vertexBuffer, meshData.drawState().indexCount(), persistent);
                }
            }
        }

        public static Indexed compile(final VertexLayout vertexLayout, final int vertexCount, final Consumer<VertexConsumer> vertexConsumer) {
            return compile(vertexLayout, vertexCount, vertexConsumer, false);
        }

        public static Indexed compilePersistent(final VertexLayout vertexLayout, final int vertexCount, final Consumer<VertexConsumer> vertexConsumer) {
            return compile(vertexLayout, vertexCount, vertexConsumer, true);
        }

        @Override
        public void close() {
            this.vertexBuffer.close();
        }
    }
}
