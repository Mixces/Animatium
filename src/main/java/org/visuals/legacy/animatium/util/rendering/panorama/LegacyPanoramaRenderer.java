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

package org.visuals.legacy.animatium.util.rendering.panorama;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.Projection;
import net.minecraft.client.renderer.ProjectionMatrixBuffer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.joml.Matrix3x2f;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.visuals.legacy.animatium.util.Utils;
import org.visuals.legacy.animatium.util.rendering.DynamicTransforms;
import org.visuals.legacy.animatium.util.rendering.Geometry;
import org.visuals.legacy.animatium.util.rendering.ImmediateRenderer;

import java.util.Objects;

// Ported code of the old <=1.12.2 panorama renderer (w/ blur)
public class LegacyPanoramaRenderer {
    private static final RenderPass.RenderArea VIEWPORT = new RenderPass.RenderArea(0, 0, 256, 256);
    private static final Vector4f CLEAR_COLOR = new Vector4f(0.0F, 0.0F, 0.0F, 1.0F);

    private static final Identifier CUBE_MAP_LOCATION = Identifier.withDefaultNamespace("textures/gui/title/background/panorama");

    private static final Matrix4f BASE_PANORAMA_MATRIX = new Matrix4f()
            .rotateX(Utils.toRadians(180.0F))
            .rotateZ(Utils.toRadians(90.0F));

    private static final Matrix4f[] PANORAMA_FACE_ROTATION = new Matrix4f[]{
            new Matrix4f(),
            new Matrix4f().rotateY((float) Math.toRadians(90.0F)),
            new Matrix4f().rotateY((float) Math.toRadians(180.0F)),
            new Matrix4f().rotateY((float) Math.toRadians(-90.0F)),
            new Matrix4f().rotateX((float) Math.toRadians(90.0F)),
            new Matrix4f().rotateX((float) Math.toRadians(-90.0F))
    };

    private static final Geometry PANORAMA_GEOMETRY = Geometry.compile(PanoramaPipelines.LEGACY_PANORAMA_1, true, 24, vertexConsumer -> {
        for (int panoramaIdx = 0; panoramaIdx < 6; panoramaIdx++) {
            final Matrix4f pose = PANORAMA_FACE_ROTATION[panoramaIdx];
            vertexConsumer.addVertex(pose, -1.0F, -1.0F, 1.0F);
            vertexConsumer.addVertex(pose, 1.0F, -1.0F, 1.0F);
            vertexConsumer.addVertex(pose, 1.0F, 1.0F, 1.0F);
            vertexConsumer.addVertex(pose, -1.0F, 1.0F, 1.0F);
        }
    });

    private static final Projection projection;
    private static final ProjectionMatrixBuffer projectionMatrixBuffer;
    private static final MainTarget panoramaTarget;
    private static final GpuTexture backgroundTexture;
    private static final GpuTextureView backgroundTextureView;

    private static PanoramaRenderState state;
    private static float spin = 0.0F;

    static {
        final GpuDevice device = RenderSystem.getDevice();
        projection = new Projection();
        projectionMatrixBuffer = new ProjectionMatrixBuffer("Legacy Panorama");
        panoramaTarget = new MainTarget(256, 256);
        backgroundTexture = device.createTexture(() -> "Legacy Panorama Temp Texture", GpuTexture.USAGE_TEXTURE_BINDING | GpuTexture.USAGE_RENDER_ATTACHMENT | GpuTexture.USAGE_COPY_SRC | GpuTexture.USAGE_COPY_DST, GpuFormat.RGBA8_UNORM, panoramaTarget.width, panoramaTarget.height, 1, 1);
        backgroundTextureView = device.createTextureView(backgroundTexture);
        clearTargets();
    }

    public static void render() {
        renderPanorama();
        blurPanorama();
    }

    public static void extractRenderState(final GuiGraphicsExtractor graphics, final int width, final int height, final float tickDelta) {
        state = new PanoramaRenderState(graphics.pose(), width, height, spin += tickDelta);
        graphics.guiRenderState.addGuiElement(new BlitTexture(graphics.pose(), backgroundTextureView, width, height));
    }

    private static void renderPanorama() {
        projection.setupPerspective(0.05F, 10.0F, 120.0F, 1.0F, 1.0F);
        RenderSystem.backupProjectionMatrix();
        RenderSystem.setProjectionMatrix(projectionMatrixBuffer.getBuffer(projection), ProjectionType.PERSPECTIVE);
        try (final ImmediateRenderer renderer = ImmediateRenderer.of(() -> "Legacy Panorama Cubemap")) {
            renderer.setRenderArea(VIEWPORT);

            RenderPipeline pipeline = PanoramaPipelines.LEGACY_PANORAMA_1;
            for (int layer = 0; layer < 64; layer++) {
                final float x = (layer % 8 / 8.0F - 0.5F) / 64.0F;
                final float y = ((float) layer / 8 / 8.0F - 0.5F) / 64.0F;
                final Matrix4f layerMatrix = new Matrix4f(BASE_PANORAMA_MATRIX)
                        .translate(x, y, 0.0F)
                        .rotateX(Utils.toRadians(Mth.sin(state.spin / 400.0F) * 25.0F + 20.0F)) // xRot
                        .rotateY(Utils.toRadians(-state.spin * 0.1F)); // yRot

                renderer.setPipeline(pipeline);
                renderer.setup(PANORAMA_GEOMETRY);

                final int color = ARGB.white(1.0F / (layer + 1.0F));
                renderer.setTexture(0, CUBE_MAP_LOCATION);
                renderer.drawTo(panoramaTarget, DynamicTransforms.builder()
                        .withModelViewMatrix(layerMatrix)
                        .withShaderColor(color));

                pipeline = PanoramaPipelines.LEGACY_PANORAMA_2;
            }
        }

        RenderSystem.restoreProjectionMatrix();
    }

    private static void blurPanorama() {
        final RenderPipeline pipeline = PanoramaPipelines.LEGACY_PANORAMA_BLUR;
        try (final ImmediateRenderer renderer = ImmediateRenderer.of(() -> "Legacy Panorama Blur")) {
            renderer.setPipeline(pipeline);
            renderer.setRenderArea(VIEWPORT);

            renderer.setup(Geometry.compile(pipeline, 12, vertexConsumer -> {
                for (int cycle = 0; cycle < 3; ++cycle) {
                    final int color = ARGB.white(1.0F / (cycle + 1.0F));
                    final float growth = (cycle - 1.5F) / 256.0F;
                    vertexConsumer.addVertexWith2DPose(state.pose, state.width, state.height).setUv(0.0F + growth, 1.0F).setColor(color);
                    vertexConsumer.addVertexWith2DPose(state.pose, state.width, 0.0F).setUv(1.0F + growth, 1.0F).setColor(color);
                    vertexConsumer.addVertexWith2DPose(state.pose, 0.0F, 0.0F).setUv(1.0F + growth, 0.0F).setColor(color);
                    vertexConsumer.addVertexWith2DPose(state.pose, 0.0F, state.height).setUv(0.0F + growth, 0.0F).setColor(color);
                }
            }));

            renderer.setTexture(0, backgroundTextureView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            for (int iter = 0; iter < 7; ++iter) {
                RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(Objects.requireNonNull(panoramaTarget.getColorTexture()), backgroundTexture, 0, 0, 0, 0, 0, panoramaTarget.width, panoramaTarget.height);
                renderer.drawGuiTo(panoramaTarget, DynamicTransforms.builder());
            }
        }
    }

    public static void close() {
        projectionMatrixBuffer.close();
        backgroundTexture.close();
        backgroundTextureView.close();
        panoramaTarget.destroyBuffers();
    }

    @SuppressWarnings("DataFlowIssue")
    private static void clearTargets() {
        final GpuDevice device = RenderSystem.getDevice();
        device.createCommandEncoder().clearColorAndDepthTextures(panoramaTarget.getColorTexture(), CLEAR_COLOR, panoramaTarget.getDepthTexture(), 0.0F);
        device.createCommandEncoder().clearColorTexture(backgroundTexture, CLEAR_COLOR);
    }

    private record BlitTexture(Matrix3x2f pose, GpuTextureView textureView,
                               int width, int height) implements GuiElementRenderState {
        @Override
        public void buildVertices(final VertexConsumer vertexConsumer) {
            final int color = ARGB.white(1.0F);
            final float aspect = this.width > this.height ? 120.0F / this.width : 120.0F / this.height;
            final float sw = this.width * aspect / 256.0F;
            final float sh = this.height * aspect / 256.0F;
            vertexConsumer.addVertexWith2DPose(this.pose, 0.0F, this.height).setUv(0.5F - sh, 0.5F + sw).setColor(color);
            vertexConsumer.addVertexWith2DPose(this.pose, this.width, this.height).setUv(0.5F - sh, 0.5F - sw).setColor(color);
            vertexConsumer.addVertexWith2DPose(this.pose, this.width, 0.0F).setUv(0.5F + sh, 0.5F - sw).setColor(color);
            vertexConsumer.addVertexWith2DPose(this.pose, 0.0F, 0.0F).setUv(0.5F + sh, 0.5F + sw).setColor(color);
        }

        @Override
        public @NonNull RenderPipeline pipeline() {
            return RenderPipelines.GUI_TEXTURED;
        }

        @Override
        public @NonNull TextureSetup textureSetup() {
            return TextureSetup.singleTexture(this.textureView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
        }

        @Override
        public @Nullable ScreenRectangle scissorArea() {
            return null;
        }

        @Override
        public @NonNull ScreenRectangle bounds() {
            return new ScreenRectangle(0, 0, this.width, this.height);
        }
    }

    private record PanoramaRenderState(Matrix3x2f pose, int width, int height, float spin) {
    }
}
