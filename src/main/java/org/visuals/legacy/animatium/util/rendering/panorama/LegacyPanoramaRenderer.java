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
public final class LegacyPanoramaRenderer implements AutoCloseable {
    private static final RenderPass.RenderArea VIEWPORT = new RenderPass.RenderArea(0, 0, 256, 256);
    private static final Vector4f CLEAR_COLOR = new Vector4f(0.0F, 0.0F, 0.0F, 1.0F);
    private static final Identifier CUBE_MAP_LOCATION = Identifier.withDefaultNamespace("textures/gui/title/background/panorama");

    private static final Geometry PANORAMA_GEOMETRY = Geometry.compile(PanoramaPipelines.LEGACY_PANORAMA_1, true, 24, vertexConsumer -> {
        for (int panoramaIdx = 0; panoramaIdx < 6; panoramaIdx++) {
            final Matrix4f pose = new Matrix4f();
            switch (panoramaIdx) {
                case 1 -> pose.rotateY((float) Math.toRadians(90.0F));
                case 2 -> pose.rotateY((float) Math.toRadians(180.0F));
                case 3 -> pose.rotateY((float) Math.toRadians(-90.0F));
                case 4 -> pose.rotateX((float) Math.toRadians(90.0F));
                case 5 -> pose.rotateX((float) Math.toRadians(-90.0F));
            }

            vertexConsumer.addVertex(pose, -1.0F, -1.0F, 1.0F);
            vertexConsumer.addVertex(pose, 1.0F, -1.0F, 1.0F);
            vertexConsumer.addVertex(pose, 1.0F, 1.0F, 1.0F);
            vertexConsumer.addVertex(pose, -1.0F, 1.0F, 1.0F);
        }
    });

    public static final LegacyPanoramaRenderer INSTANCE = new LegacyPanoramaRenderer();

    private final Projection projection = new Projection();
    private final ProjectionMatrixBuffer projectionMatrixBuffer = new ProjectionMatrixBuffer("Legacy Panorama Matrix");
    private final MainTarget panoramaTarget = new MainTarget(256, 256);
    private final GpuTexture backgroundTexture;
    private final GpuTextureView backgroundTextureView;

    private @Nullable PanoramaRenderState state;

    @SuppressWarnings("DataFlowIssue")
    LegacyPanoramaRenderer() {
        final GpuDevice device = RenderSystem.getDevice();
        this.backgroundTexture = device.createTexture(() -> "Legacy Panorama Temp Texture", GpuTexture.USAGE_TEXTURE_BINDING | GpuTexture.USAGE_RENDER_ATTACHMENT | GpuTexture.USAGE_COPY_SRC | GpuTexture.USAGE_COPY_DST, GpuFormat.RGBA8_UNORM, this.panoramaTarget.width, this.panoramaTarget.height, 1, 1);
        this.backgroundTextureView = device.createTextureView(this.backgroundTexture);
        device.createCommandEncoder().clearColorAndDepthTextures(this.panoramaTarget.getColorTexture(), CLEAR_COLOR, this.panoramaTarget.getDepthTexture(), 0.0F);
        device.createCommandEncoder().clearColorTexture(this.backgroundTexture, CLEAR_COLOR);
    }

    public void render() {
        if (this.state != null) {
            this.renderCubeMap(0.0F);//this.state.spin);
            this.rotateAndBlurCubeMap(this.state.pose, this.state.width, this.state.height);
        }
    }

    public void extractRenderState(final GuiGraphicsExtractor graphics, final int width, final int height, final float tickDelta) {
        this.state = new PanoramaRenderState(graphics.pose(), width, height, this.state == null ? 0 : this.state.spin + tickDelta);
        graphics.guiRenderState.addGuiElement(new BlitTexture(graphics.pose(), this.backgroundTextureView, width, height));
    }

    private void renderCubeMap(final float spin) {
        this.projection.setupPerspective(0.05F, 10.0F, 120.0F, 1.0F, 1.0F);
        RenderSystem.backupProjectionMatrix();
        RenderSystem.setProjectionMatrix(this.projectionMatrixBuffer.getBuffer(this.projection), ProjectionType.PERSPECTIVE);
        try (final ImmediateRenderer renderer = ImmediateRenderer.of(() -> "Legacy Panorama Cubemap")) {
            renderer.setRenderArea(VIEWPORT);
            renderer.setPipeline(PanoramaPipelines.LEGACY_PANORAMA_1);
            renderer.setup(PANORAMA_GEOMETRY);
            renderer.setTexture(0, CUBE_MAP_LOCATION);
            for (int layer = 0; layer < 64; layer++) {
                final float x = (layer % 8 / 8.0F - 0.5F) / 64.0F;
                final float y = ((float) layer / 8 / 8.0F - 0.5F) / 64.0F;
                final Matrix4f modelViewMatrix = new Matrix4f()
                        .rotateX(Utils.toRadians(180.0F))
                        .rotateZ(Utils.toRadians(90.0F))
                        .translate(x, y, 0.0F)
                        .rotateX(Utils.toRadians(Mth.sin(spin / 400.0F) * 25.0F + 20.0F))
                        .rotateY(Utils.toRadians(-spin * 0.1F));
                final int color = ARGB.white(1.0F / (layer + 1.0F));
                renderer.drawTo(this.panoramaTarget, DynamicTransforms.builder().withModelViewMatrix(modelViewMatrix).withShaderColor(color));
                if (layer == 0) {
                    renderer.setPipeline(PanoramaPipelines.LEGACY_PANORAMA_2);
                }
            }
        }

        RenderSystem.restoreProjectionMatrix();
    }

    private void rotateAndBlurCubeMap(final Matrix3x2f pose, final int width, final int height) {
        final RenderPipeline pipeline = PanoramaPipelines.LEGACY_PANORAMA_BLUR;
        try (final ImmediateRenderer renderer = ImmediateRenderer.of(() -> "Legacy Panorama Blur")) {
            renderer.setPipeline(pipeline);
            renderer.setRenderArea(VIEWPORT);
            renderer.setup(Geometry.texturedScreenQuad(pipeline, pose, width, height));
            renderer.setTexture(0, this.backgroundTextureView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            for (int pass = 0; pass < 7; pass++) {
                RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(Objects.requireNonNull(this.panoramaTarget.getColorTexture()), this.backgroundTexture, 0, 0, 0, 0, 0, this.panoramaTarget.width, this.panoramaTarget.height);
                renderer.drawGuiTo(this.panoramaTarget, DynamicTransforms.builder());
            }
        }
    }

    @Override
    public void close() {
        this.projectionMatrixBuffer.close();
        this.backgroundTexture.close();
        this.backgroundTextureView.close();
        this.panoramaTarget.destroyBuffers();
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
