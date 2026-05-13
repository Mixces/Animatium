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

package btw.lowercase.renderer;

import btw.lowercase.renderer.buffer.DynamicTransforms;
import btw.lowercase.renderer.buffer.Geometry;
import btw.lowercase.renderer.texture.TextureAndSampler;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderPassDescriptor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.ProjectionMatrixBuffer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.visuals.legacy.animatium.util.compatibility.IrisPipeline;
import org.visuals.legacy.animatium.util.compatibility.IrisUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Renderer implements AutoCloseable {
    // Data
    private final Map<String, TextureAndSampler> textures = new HashMap<>();
    private final Map<String, GpuBufferSlice> uniforms = new HashMap<>();

    @Getter
    private final Supplier<String> name;
    @Getter
    private final RenderPassDescriptor descriptor;
    @Getter
    private RenderPipeline pipeline = null;
    @Getter
    private @Nullable Matrix4f projectionMatrix;

    // Internal
    private ProjectionMatrixBuffer projectionMatrixBuffer;

    private Renderer(final RenderPassDescriptor descriptor) {
        this.name = descriptor.label();
        this.descriptor = descriptor;
    }

    public static Renderer of(final RenderPassDescriptor descriptor) {
        return new Renderer(descriptor);
    }

    public static Renderer of(final Supplier<String> label, final GpuTextureView colorTextureView, @Nullable final GpuTextureView depthTextureView, final RenderPass.RenderArea renderArea) {
        final RenderPassDescriptor descriptor = RenderPassDescriptor.create(label);

        descriptor.withColorAttachment(colorTextureView);
        if (depthTextureView != null) {
            descriptor.withDepthAttachment(depthTextureView);
        }

        descriptor.withRenderArea(renderArea);
        return of(descriptor);
    }

    public static Renderer of(final Supplier<String> label, final GpuTextureView colorTextureView, @Nullable final GpuTextureView depthTextureView) {
        return of(label, colorTextureView, depthTextureView, new RenderPass.RenderArea(0, 0, colorTextureView.getWidth(0), colorTextureView.getHeight(0)));
    }

    public static Renderer of(final Supplier<String> label, final GpuTextureView colorTextureView) {
        return of(label, colorTextureView, null);
    }

    public static Renderer of(final Supplier<String> label, final RenderTarget renderTarget, final RenderPass.RenderArea renderArea) {
        final RenderPassDescriptor descriptor = RenderPassDescriptor.create(label);

        final GpuTextureView colorTextureView = RenderSystem.outputColorTextureOverride != null ? RenderSystem.outputColorTextureOverride : renderTarget.getColorTextureView();
        assert colorTextureView != null;
        descriptor.withColorAttachment(colorTextureView);
        if (renderTarget.useDepth) {
            final GpuTextureView depthTextureView = RenderSystem.outputDepthTextureOverride != null ? RenderSystem.outputDepthTextureOverride : renderTarget.getDepthTextureView();
            assert depthTextureView != null;
            descriptor.withDepthAttachment(depthTextureView);
        }

        descriptor.withRenderArea(renderArea);
        return of(descriptor);
    }

    public static Renderer of(final Supplier<String> label, final RenderTarget renderTarget) {
        return of(label, renderTarget, new RenderPass.RenderArea(0, 0, renderTarget.width, renderTarget.height));
    }

    public static Renderer of(final Supplier<String> label) {
        return of(label, Minecraft.getInstance().gameRenderer.mainRenderTarget());
    }

    public void setPipeline(final RenderPipeline pipeline, final IrisPipeline irisPipeline) {
        this.pipeline = pipeline;
        IrisUtil.assignPipeline(pipeline, irisPipeline);
    }

    public void setPipeline(final RenderPipeline pipeline) {
        final List<String> samplers = BindGroupLayout.flattenSamplers(pipeline.getBindGroupLayouts());
        IrisPipeline irisPipeline;
        if (samplers.contains("Sampler0")) {
            irisPipeline = IrisPipeline.TEXTURED;
        } else {
            irisPipeline = IrisPipeline.BASIC;
        }

        this.setPipeline(pipeline, irisPipeline);
    }

    public void setTexture(final String name, final TextureAndSampler textureAndSampler) {
        this.textures.put(name, textureAndSampler);
    }

    public void setTexture(final String name, final GpuTextureView textureView, final GpuSampler sampler) {
        this.setTexture(name, new TextureAndSampler(textureView, sampler));
    }

    public void setTexture(final String name, final Identifier location) {
        this.setTexture(name, TextureAndSampler.get(location));
    }

    public void setTextures(final TextureSetup setup) {
        this.setTexture("Sampler0", TextureAndSampler.get(0, setup));
        this.setTexture("Sampler1", TextureAndSampler.get(1, setup));
        this.setTexture("Sampler2", TextureAndSampler.get(2, setup));
    }

    public void setUniform(final String name, final GpuBufferSlice data) {
        this.uniforms.put(name, data);
    }

    public void setUniform(final String name, final GpuBuffer data) {
        this.setUniform(name, data.slice());
    }

    public void setProjectionMatrix(final Matrix4f matrix4f) {
        if (this.projectionMatrixBuffer == null) {
            this.projectionMatrixBuffer = new ProjectionMatrixBuffer("Immediate Projection Buffer for " + this.name);
        }

        this.projectionMatrix = matrix4f;
    }

    public void draw(final Geometry geometry) {
        if (this.pipeline == null) {
            throw new RuntimeException("Cannot draw without a pipeline bound!");
        } else if (geometry.isClosed()) {
            throw new RuntimeException("Cannot draw! The geometry provided has already been closed!");
        } else {
            if (this.projectionMatrixBuffer != null && this.projectionMatrix != null) {
                final int properties = this.projectionMatrix.properties();
                ProjectionType projectionType;
                if ((properties & Matrix4f.PROPERTY_PERSPECTIVE) != 0) {
                    projectionType = ProjectionType.PERSPECTIVE;
                } else {
                    projectionType = ProjectionType.ORTHOGRAPHIC;
                }

                RenderSystem.backupProjectionMatrix();
                RenderSystem.setProjectionMatrix(this.projectionMatrixBuffer.getBuffer(this.projectionMatrix), projectionType);
            }

            final GpuBufferSlice dynamicTransforms = this.uniforms.getOrDefault(DynamicTransforms.KEY, DynamicTransforms.builder().build());
            final RenderSystem.AutoStorageIndexBuffer autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(this.pipeline.getPrimitiveTopology());
            try (final RenderPass pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(this.descriptor)) {
                pass.setPipeline(this.pipeline);

                final List<BindGroupLayout> bindGroupLayouts = this.pipeline.getBindGroupLayouts();
                final List<String> descriptions = BindGroupLayout.flattenUniforms(bindGroupLayouts)
                        .stream()
                        .map(BindGroupLayout.UniformDescription::name)
                        .toList();

                RenderSystem.bindDefaultUniforms(pass);
                pass.setUniform(DynamicTransforms.KEY, dynamicTransforms);
                for (final Map.Entry<String, GpuBufferSlice> uniform : this.uniforms.entrySet()) {
                    final String name = uniform.getKey();
                    if (DynamicTransforms.KEY.equals(name)) {
                        continue; // Special Handling Above
                    }

                    if (descriptions.contains(name)) {
                        pass.setUniform(uniform.getKey(), uniform.getValue());
                    }
                }

                final List<String> samplers = BindGroupLayout.flattenSamplers(bindGroupLayouts);
                for (final Map.Entry<String, TextureAndSampler> entry : this.textures.entrySet()) {
                    final String name = entry.getKey();
                    if (samplers.contains(name)) {
                        final TextureAndSampler textureAndSampler = entry.getValue();
                        pass.bindTexture(name, textureAndSampler.textureView(), textureAndSampler.sampler());
                    }
                }

                geometry.bind(pass, autoStorageIndexBuffer);
                geometry.draw(pass);
            }

            if (!geometry.persistent()) {
                geometry.close();
            }

            if (this.projectionMatrixBuffer != null && this.projectionMatrix != null) {
                RenderSystem.restoreProjectionMatrix();
            }
        }
    }

    public void drawGui(final Geometry geometry) {
        final Window window = Minecraft.getInstance().getWindow();
        this.setProjectionMatrix(new Matrix4f().setOrtho(0.0F, (float) window.getWidth() / (float) window.getGuiScale(), (float) window.getHeight() / (float) window.getGuiScale(), 0.0F, 1000.0F, 11000.0F, RenderSystem.getDevice().getDeviceInfo().isZZeroToOne()));
        this.setUniform(DynamicTransforms.KEY, DynamicTransforms.builder()
                .withModelViewMatrix(new Matrix4f().setTranslation(0.0F, 0.0F, -11000.0F))
                .build());
        this.draw(geometry);
    }

    @Override
    public void close() {
        this.textures.clear();
        this.uniforms.clear();
        if (this.projectionMatrixBuffer != null) {
            this.projectionMatrixBuffer.close();
            this.projectionMatrixBuffer = null;
        }
    }
}
