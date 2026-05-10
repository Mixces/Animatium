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
import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
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
import org.visuals.legacy.animatium.util.rendering.AnimatiumPipelines;
import org.visuals.legacy.animatium.util.rendering.RenderUtils;
import org.visuals.legacy.animatium.util.rendering.renderer.DynamicTransforms;
import org.visuals.legacy.animatium.util.rendering.renderer.Geometry;
import org.visuals.legacy.animatium.util.rendering.renderer.ImmediateRenderer;
import org.visuals.legacy.animatium.util.rendering.renderer.VertexLayouts;

import java.util.Objects;

// Ported code of the old <=1.12.2 panorama renderer (w/ blur)
public final class LegacyPanoramaRenderer implements AutoCloseable {
    private static final int SAMPLES = 64;
    private static final RenderPass.RenderArea VIEWPORT = new RenderPass.RenderArea(0, 0, 256, 256);
    private static final Vector4f CLEAR_COLOR = new Vector4f(0.0F, 0.0F, 0.0F, 1.0F);
    private static final Identifier CUBE_MAP_LOCATION = Identifier.withDefaultNamespace("textures/gui/title/background/panorama");
    private static final Matrix4f CUBE_MAP_PROJECTION = new Matrix4f().setPerspective(Utils.toRadians(120.0F), 1.0F, 0.05F, 10.0F);
    private static final Geometry CUBE_MAP_GEOMETRY = Geometry.compilePersistent(VertexLayouts.POSITIONED_QUAD, 24, vertexConsumer -> {
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
            final float xRot = Mth.sin(this.state.spin / 400.0F) * 25.0F + 20.0F;
            final float yRot = -this.state.spin * 0.1F;
            this.renderCubeMap(xRot, yRot);
            this.rotateAndBlurCubeMap(this.state.pose, this.state.width, this.state.height);
        }
    }

    public void extractRenderState(final GuiGraphicsExtractor graphics, final int width, final int height, final float tickDelta) {
        final double panoramaSpeed = Minecraft.getInstance().gameRenderer.gameRenderState().optionsRenderState.panoramaSpeed;
        this.state = new PanoramaRenderState(graphics.pose(), width, height, this.state == null ? 0.0F : (float) (this.state.spin + (tickDelta * panoramaSpeed)));
        graphics.guiRenderState.addGuiElement(new BlitTexture(graphics.pose(), this.backgroundTextureView, width, height));
    }

    private void renderCubeMap(final float xRot, final float yRot) {
        try (final ImmediateRenderer renderer = ImmediateRenderer.of(RenderUtils.createDescriptor(() -> "Legacy Panorama Cubemap", this.panoramaTarget, VIEWPORT))) {
            renderer.setPipeline(AnimatiumPipelines.LEGACY_PANORAMA_1);
            renderer.setup(CUBE_MAP_GEOMETRY);
            renderer.setTexture(0, CUBE_MAP_LOCATION);
            renderer.setProjectionMatrix(CUBE_MAP_PROJECTION);
            for (int layer = 0; layer < SAMPLES; layer++) {
                final float x = (layer % 8 / 8.0F - 0.5F) / SAMPLES;
                final float y = ((float) layer / 8 / 8.0F - 0.5F) / SAMPLES;
                final Matrix4f modelViewMatrix = new Matrix4f()
                        .rotateX(Utils.toRadians(180.0F))
                        .rotateZ(Utils.toRadians(90.0F))
                        .translate(x, y, 0.0F)
                        .rotateX(Utils.toRadians(xRot))
                        .rotateY(Utils.toRadians(yRot));
                final int color = ARGB.white(1.0F / (layer + 1.0F));
                renderer.draw(DynamicTransforms.builder().withModelViewMatrix(modelViewMatrix).withShaderColor(color));
                if (layer == 0) {
                    renderer.setPipeline(AnimatiumPipelines.LEGACY_PANORAMA_2);
                }
            }
        }
    }

    private void rotateAndBlurCubeMap(final Matrix3x2f pose, final int width, final int height) {
        try (final ImmediateRenderer renderer = ImmediateRenderer.of(RenderUtils.createDescriptor(() -> "Legacy Panorama Blur", this.panoramaTarget, VIEWPORT))) {
            renderer.setPipeline(AnimatiumPipelines.LEGACY_PANORAMA_BLUR);
            renderer.setup(Geometry.texturedScreenQuad(pose, width, height));
            renderer.setTexture(0, this.backgroundTextureView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            for (int pass = 0; pass < 7; pass++) {
                RenderUtils.copyTextureToTexture(Objects.requireNonNull(this.panoramaTarget.getColorTexture()), this.backgroundTexture);
                renderer.drawGui();
            }
        }
    }

    @Override
    public void close() {
        CUBE_MAP_GEOMETRY.forceClose();
        this.backgroundTextureView.close();
        this.backgroundTexture.close();
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
