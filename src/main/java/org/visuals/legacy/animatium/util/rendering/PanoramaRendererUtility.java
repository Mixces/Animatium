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
import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.opengl.GlTextureView;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import net.minecraft.client.renderer.CachedPerspectiveProjectionMatrixBuffer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import org.joml.Matrix4f;
import org.joml.Vector4i;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.util.Utils;

@UtilityClass
// Ported code of the old <=1.12.2 panorama renderer (w/ blur)
public class PanoramaRendererUtility {
    private final BlendFunction PANORAMA_BLEND = new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);

    private final RenderPipeline LEGACY_PANORAMA =
            RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
                    .withLocation(Animatium.location("pipeline/legacy_panorama"))
                    .withVertexShader(Animatium.location("core/legacy_panorama"))
                    .withFragmentShader(Animatium.location("core/legacy_panorama"))
                    .withCull(false)
                    .withDepthWrite(false)
                    .withBlend(PANORAMA_BLEND)
                    // .withColorWrite(true, false) // TODO/NOTE: Causes it to not render (alpha becomes 0.0?!??!?!)
                    .withSampler("Sampler0")
                    .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
                    .build();

    private final RenderPipeline LEGACY_PANORAMA_BLUR =
            RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET)
                    .withLocation(Animatium.location("pipeline/legacy_panorama_blur"))
                    .withVertexShader(Animatium.location("core/legacy_panorama_blur"))
                    .withFragmentShader(Animatium.location("core/legacy_panorama_blur"))
                    .withBlend(PANORAMA_BLEND)
                    .withColorWrite(true, false)
                    .build();

    private final Vector4i VIEWPORT = new Vector4i(0, 0, 256, 256);

    private final CachedPerspectiveProjectionMatrixBuffer projectionMatrixBuffer = new CachedPerspectiveProjectionMatrixBuffer("panorama", 0.05F, 10.0F);
    private final MainTarget panoramaTarget = new MainTarget(256, 256);
    private final GlTexture backgroundTexture;
    private final GlTextureView backgroundTextureView;
    private float spin = 0.0F;

    static {
        final GpuDevice device = RenderSystem.getDevice();
        backgroundTexture = (GlTexture) device.createTexture(() -> "Background texture", 15, TextureFormat.RGBA8, 256, 256, 1, 1);
        backgroundTexture.setTextureFilter(FilterMode.LINEAR, FilterMode.LINEAR, false);
        backgroundTextureView = (GlTextureView) device.createTextureView(backgroundTexture);
        device.createCommandEncoder().clearDepthTexture(panoramaTarget.getDepthTexture(), 1.0F);
    }

    public void render(final GuiGraphics guiGraphics, final int width, final int height) {
        renderPanorama(width, height);
        for (int pass = 0; pass < 7; ++pass) {
            panoramaBlurPass(guiGraphics.pose(), width, height);
        }

        guiGraphics.guiRenderState.submitGuiElement(new BlitFinalTexture(guiGraphics.pose(), backgroundTextureView, width, height, ARGB.white(1.0F)));
    }

    private void renderPanorama(final int width, final int height) {
        RenderSystem.setProjectionMatrix(projectionMatrixBuffer.getBuffer(width, height, 120.0F), ProjectionType.PERSPECTIVE);
        final Matrix4f rootMatrix = new Matrix4f().identity().rotateX(Utils.toRadians(180.0F)).rotateZ(Utils.toRadians(90.0F));
        for (int layer = 0; layer < 64; layer++) {
            float x = (layer % 8 / 8.0F - 0.5F) / 64.0F;
            float y = ((float) layer / 8 / 8.0F - 0.5F) / 64.0F;
            final Matrix4f layerMatrix = new Matrix4f(rootMatrix).translate(x, y, 0.0F).rotateX(Utils.toRadians(getXRot())).rotateY(Utils.toRadians(getYRot()));
            for (int panoramaIdx = 0; panoramaIdx < 6; panoramaIdx++) {
                final Matrix4f faceMatrix = new Matrix4f(layerMatrix);
                if (panoramaIdx == 1) {
                    faceMatrix.rotateY(Utils.toRadians(90.0F));
                } else if (panoramaIdx == 2) {
                    faceMatrix.rotateY(Utils.toRadians(180.0F));
                } else if (panoramaIdx == 3) {
                    faceMatrix.rotateY(Utils.toRadians(-90.0F));
                } else if (panoramaIdx == 4) {
                    faceMatrix.rotateX(Utils.toRadians(90.0F));
                } else if (panoramaIdx == 5) {
                    faceMatrix.rotateX(Utils.toRadians(-90.0F));
                }

                try (final Renderer renderer = Renderer.of("Panorama")) {
                    renderer.setPipeline(LEGACY_PANORAMA);
                    renderer.setViewport(VIEWPORT);
                    renderer.setFramebuffer(panoramaTarget);
                    renderer.setDynamicTransforms(renderer.getDynamicTransforms().withModelViewMatrix(faceMatrix));

                    final int currentLayer = layer;
                    renderer.setup((vertexConsumer) -> {
                        final int color = ARGB.white(255.0F / (currentLayer + 1.0F));
                        vertexConsumer.addVertex(-1.0F, -1.0F, 1.0F).setUv(0.0F, 0.0F).setColor(color);
                        vertexConsumer.addVertex(1.0F, -1.0F, 1.0F).setUv(1.0F, 0.0F).setColor(color);
                        vertexConsumer.addVertex(1.0F, 1.0F, 1.0F).setUv(1.0F, 1.0F).setColor(color);
                        vertexConsumer.addVertex(-1.0F, 1.0F, 1.0F).setUv(0.0F, 1.0F).setColor(color);
                    }, 4);

                    renderer.setTexture(0, getPanoramaTexture(panoramaIdx));
                    renderer.draw();
                }
            }
        }
    }

    private void panoramaBlurPass(final Matrix3x2f pose, final int width, final int height) {
        RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(panoramaTarget.getColorTexture(), backgroundTexture, 0, 0, 0, 0, 0, 256, 256);
        try (final Renderer renderer = Renderer.of("Panorama Blur Pass")) {
            renderer.setPipeline(LEGACY_PANORAMA_BLUR);
            renderer.setViewport(VIEWPORT);
            renderer.setFramebuffer(panoramaTarget);
            renderer.setup((vertexConsumer) -> {
                for (int cycle = 0; cycle < 3; cycle++) {
                    final int color = ARGB.white(1.0F / (cycle + 1.0F));
                    final float growth = (cycle - 1.0F) / 256.0F;
                    vertexConsumer.addVertexWith2DPose(pose, width, height).setUv(0.0F + growth, 1.0F).setColor(color);
                    vertexConsumer.addVertexWith2DPose(pose, width, 0.0F).setUv(1.0F + growth, 1.0F).setColor(color);
                    vertexConsumer.addVertexWith2DPose(pose, 0.0F, 0.0F).setUv(1.0F + growth, 0.0F).setColor(color);
                    vertexConsumer.addVertexWith2DPose(pose, 0.0F, height).setUv(0.0F + growth, 0.0F).setColor(color);
                }
            }, 12);
            renderer.setTexture(0, backgroundTextureView);
            renderer.drawInGui();
        }
    }

    public void update(float tickDelta) {
        spin += tickDelta;
    }

    public float getXRot() {
        return Mth.sin(spin / 400.0F) * 25.0F + 20.0F;
    }

    public float getYRot() {
        return -spin * 0.1F;
    }

    public ResourceLocation getPanoramaTexture(int side) {
        return ResourceLocation.withDefaultNamespace("textures/gui/title/background/panorama_" + side + ".png");
    }

    private record BlitFinalTexture(
            Matrix3x2f pose,
            GpuTextureView texture,
            int width, int height,
            int color
    ) implements GuiElementRenderState {
        @Override
        public void buildVertices(VertexConsumer consumer) {
            final float aspect = 120.0F / Math.max(this.width, this.height);
            final float sw = this.width * aspect / 256.0F;
            final float sh = this.height * aspect / 256.0F;
            consumer.addVertexWith2DPose(this.pose, 0.0F, this.height).setUv(0.5F - sh, 0.5F + sw).setColor(this.color);
            consumer.addVertexWith2DPose(this.pose, this.width, this.height).setUv(0.5F - sh, 0.5F - sw).setColor(this.color);
            consumer.addVertexWith2DPose(this.pose, this.width, 0.0F).setUv(0.5F + sh, 0.5F - sw).setColor(this.color);
            consumer.addVertexWith2DPose(this.pose, 0.0F, 0.0F).setUv(0.5F + sh, 0.5F + sw).setColor(this.color);
        }

        @Override
        public @NotNull RenderPipeline pipeline() {
            return RenderPipelines.GUI_TEXTURED;
        }

        @Override
        public @NotNull TextureSetup textureSetup() {
            return TextureSetup.singleTexture(this.texture);
        }

        @Override
        public @Nullable ScreenRectangle scissorArea() {
            return null;
        }

        @Override
        public @NotNull ScreenRectangle bounds() {
            return new ScreenRectangle(0, 0, this.width, this.height).transformMaxBounds(this.pose);
        }
    }
}
