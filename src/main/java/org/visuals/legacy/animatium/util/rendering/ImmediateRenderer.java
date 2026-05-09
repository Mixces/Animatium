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

import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderPassDescriptor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.Projection;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.*;
import org.lwjgl.system.MemoryStack;
import org.visuals.legacy.animatium.mixins.accessor.GameRendererAccessor;
import org.visuals.legacy.animatium.mixins.accessor.GuiRendererAccessor;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ImmediateRenderer implements AutoCloseable {
    // Data
    private final Map<String, TextureAndSampler> textures;
    private final Map<String, Uniform<?>> uniforms;

    @Getter
    private final Supplier<String> name;
    @Getter
    private final RenderPassDescriptor descriptor;
    @Getter
    @Setter
    private RenderPipeline pipeline;
    @Getter
    @Setter
    private @Nullable Matrix4f projectionMatrix;

    // Internal
    private Geometry geometry;
    private GpuBuffer uniformBuffer;
    @Getter
    private boolean setup;

    private ImmediateRenderer(final RenderPassDescriptor descriptor) {
        // Data
        this.textures = new HashMap<>();
        this.uniforms = new HashMap<>();
        this.name = descriptor.label();
        this.descriptor = descriptor;
        this.pipeline = null;

        // Internal
        this.geometry = null;
        this.uniformBuffer = null;
        this.setup = false;
    }

    public static ImmediateRenderer of(final RenderPassDescriptor descriptor) {
        return new ImmediateRenderer(descriptor);
    }

    public static ImmediateRenderer of(final Supplier<String> label, final RenderTarget renderTarget) {
        return of(RenderUtils.createDescriptor(label, renderTarget));
    }

    public static ImmediateRenderer of(final Supplier<String> label) {
        return of(label, Minecraft.getInstance().gameRenderer.mainRenderTarget());
    }

    public void setup(final Geometry geometry) {
        if (this.pipeline == null || this.pipeline != geometry.pipeline()) {
            throw new RuntimeException("Cannot setup renderer with geometry of mismatching pipelines!");
        }

        if (this.geometry != null && this.geometry != geometry) {
            this.geometry.close();
        }

        this.geometry = geometry;
        this.setup = true;
    }

    public void setTexture(final int id, final GpuTextureView textureView, final GpuSampler sampler) {
        this.textures.put("Sampler" + id, new TextureAndSampler(textureView, sampler));
    }

    public void setTexture(final int id, final GpuTextureView textureView) {
        this.setTexture(id, textureView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
    }

    public void setTexture(final int id, final Identifier resourceLocation) {
        final AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(resourceLocation);
        this.setTexture(id, texture.getTextureView(), texture.getSampler());
    }

    public void setTextures(final TextureSetup textureSetup) {
        final GpuTextureView texture0 = textureSetup.texure0();
        if (texture0 != null) {
            this.setTexture(0, texture0, textureSetup.sampler0());
        }

        final GpuTextureView texture1 = textureSetup.texure1();
        if (texture1 != null) {
            this.setTexture(1, texture1, textureSetup.sampler1());
        }

        final GpuTextureView texture2 = textureSetup.texure2();
        if (texture2 != null) {
            this.setTexture(2, texture2, textureSetup.sampler2());
        }
    }

    public void setUniform(final String name, final int value) {
        this.uniforms.put(name, new Uniform<>(Uniform.Type.INT, value));
    }

    public void setUniform(final String name, final int... value) {
        this.uniforms.put(name, new Uniform<>(Uniform.Type.INT_ARRAY, value));
    }

    public void setUniform(final String name, final float value) {
        this.uniforms.put(name, new Uniform<>(Uniform.Type.FLOAT, value));
    }

    public void setUniform(final String name, final float... value) {
        this.uniforms.put(name, new Uniform<>(Uniform.Type.FLOAT_ARRAY, value));
    }

    public void setUniform(final String name, final Vector2ic value) {
        this.uniforms.put(name, new Uniform<>(Uniform.Type.VECTOR2I, value));
    }

    public void setUniform(final String name, final Vector2fc value) {
        this.uniforms.put(name, new Uniform<>(Uniform.Type.VECTOR2F, value));
    }

    public void setUniform(final String name, final Vector3ic value) {
        this.uniforms.put(name, new Uniform<>(Uniform.Type.VECTOR3I, value));
    }

    public void setUniform(final String name, final Vector3fc value) {
        this.uniforms.put(name, new Uniform<>(Uniform.Type.VECTOR3F, value));
    }

    public void setUniform(final String name, final Vector4ic value) {
        this.uniforms.put(name, new Uniform<>(Uniform.Type.VECTOR4I, value));
    }

    public void setUniform(final String name, final Vector4fc value) {
        this.uniforms.put(name, new Uniform<>(Uniform.Type.VECTOR4F, value));
    }

    public void setUniform(final String name, final Matrix4fc value) {
        this.uniforms.put(name, new Uniform<>(Uniform.Type.MATRIX4F, value));
    }

    public void draw(final DynamicTransforms.Builder dynamicTransforms) {
        if (!this.setup) {
            throw new RuntimeException("Cannot draw because renderer has not been setup yet!");
        } else if (this.pipeline == null) {
            throw new RuntimeException("Cannot draw without a pipeline bound!");
        } else {
            if (this.projectionMatrix != null) {
                RenderSystem.backupProjectionMatrix();
                try (final MemoryStack stack = MemoryStack.stackPush()) {
                    final ByteBuffer byteBuffer = Std140Builder.onStack(stack, RenderSystem.PROJECTION_MATRIX_UBO_SIZE).putMat4f(this.projectionMatrix).get();
                    try (final GpuBuffer buffer = RenderSystem.getDevice().createBuffer(() -> "Immediate Projection Buffer", 136, byteBuffer)) {
                        final int properties = this.projectionMatrix.properties();
                        ProjectionType projectionType;
                        if ((properties & Matrix4f.PROPERTY_PERSPECTIVE) != 0) {
                            projectionType = ProjectionType.PERSPECTIVE;
                        } else if ((properties & Matrix4f.PROPERTY_ORTHONORMAL) != 0) {
                            projectionType = ProjectionType.ORTHOGRAPHIC;
                        } else {
                            throw new RuntimeException("Unknown projection type");
                        }

                        RenderSystem.setProjectionMatrix(buffer.slice(), projectionType);
                    }
                }
            }

            final GpuBufferSlice transforms = dynamicTransforms.build();
            final GpuBufferSlice uniformData = this.setupUniformsBuffer();
            final RenderSystem.AutoStorageIndexBuffer autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(this.pipeline.getPrimitiveTopology());
            try (RenderPass pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(this.descriptor)) {
                pass.setPipeline(this.pipeline);
                for (final Map.Entry<String, TextureAndSampler> entry : this.textures.entrySet()) {
                    final TextureAndSampler textureAndSampler = entry.getValue();
                    pass.bindTexture(entry.getKey(), textureAndSampler.textureView, textureAndSampler.sampler);
                }

                pass.setVertexBuffer(0, this.geometry.vertexBuffer().slice());
                pass.setIndexBuffer(autoStorageIndexBuffer.getBuffer(this.geometry.indexCount()), autoStorageIndexBuffer.type());

                RenderSystem.bindDefaultUniforms(pass);
                pass.setUniform("DynamicTransforms", transforms);
                if (uniformData != null) {
                    pass.setUniform("Data", uniformData);
                }

                pass.drawIndexed(0, 0, this.geometry.indexCount(), 1);
            }

            if (this.projectionMatrix != null) {
                RenderSystem.restoreProjectionMatrix();
            }
        }
    }

    public void drawGui(final DynamicTransforms.Builder dynamicTransforms) {
        final Minecraft minecraft = Minecraft.getInstance();
        final Window window = minecraft.getWindow();
        final GuiRendererAccessor guiRendererAccessor = (GuiRendererAccessor) ((GameRendererAccessor) minecraft.gameRenderer).animatium$getGuiRenderer();

        RenderSystem.backupProjectionMatrix();
        final Projection projection = guiRendererAccessor.animatium$projection();
        projection.setupOrtho(1000.0F, 11000.0F, (float) window.getWidth() / (float) window.getGuiScale(), (float) window.getHeight() / (float) window.getGuiScale(), true);
        RenderSystem.setProjectionMatrix(guiRendererAccessor.animatium$orthoMatrixBuffer().getBuffer(projection), ProjectionType.ORTHOGRAPHIC);

        this.draw(dynamicTransforms.withModelViewMatrix(new Matrix4f(dynamicTransforms.getModelViewMatrix()).setTranslation(0.0F, 0.0F, -11000.0F)));
        RenderSystem.restoreProjectionMatrix();
    }

    public void drawGui() {
        drawGui(DynamicTransforms.builder());
    }

    private @Nullable GpuBufferSlice setupUniformsBuffer() {
        if (this.uniforms.isEmpty()) {
            return null;
        } else {
            int size = 0;
            for (final Uniform<?> uniform : this.uniforms.values()) {
                size += uniform.size();
            }

            try (final MemoryStack stack = MemoryStack.stackPush()) {
                final Std140Builder builder = Std140Builder.onStack(stack, size);
                for (final Uniform<?> uniform : this.uniforms.values()) {
                    switch (uniform.type()) {
                        case INT -> builder.putInt((int) uniform.value());
                        case INT_ARRAY -> {
                            int[] array = (int[]) uniform.value();
                            for (int value : array) {
                                builder.putFloat(value);
                            }
                        }

                        case FLOAT -> builder.putFloat((float) uniform.value());
                        case FLOAT_ARRAY -> {
                            float[] array = (float[]) uniform.value();
                            for (float value : array) {
                                builder.putFloat(value);
                            }
                        }

                        case VECTOR2I -> builder.putIVec2((Vector2ic) uniform.value());
                        case VECTOR2F -> builder.putVec2((Vector2fc) uniform.value());
                        case VECTOR3I -> builder.putIVec3((Vector3ic) uniform.value());
                        case VECTOR3F -> builder.putVec3((Vector3fc) uniform.value());
                        case VECTOR4I -> builder.putIVec4((Vector4ic) uniform.value());
                        case VECTOR4F -> builder.putVec4((Vector4fc) uniform.value());
                        case MATRIX4F -> builder.putMat4f((Matrix4fc) uniform.value());
                    }
                }

                if (this.uniformBuffer != null) {
                    RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.uniformBuffer.slice(), builder.get());
                } else {
                    this.uniformBuffer = RenderSystem.getDevice().createBuffer(() -> this.name + " Uniform Buffer", GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_COPY_DST, builder.get());
                }
            }

            return this.uniformBuffer.slice(0, size);
        }
    }

    @Override
    public void close() {
        this.textures.clear();
        this.uniforms.clear();
        if (this.geometry != null) {
            this.geometry.close();
        }

        if (this.uniformBuffer != null) {
            this.uniformBuffer = null;
        }
    }

    private record TextureAndSampler(GpuTextureView textureView, GpuSampler sampler) {
    }
}
