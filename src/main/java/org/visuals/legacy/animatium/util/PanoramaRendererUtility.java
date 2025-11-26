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

package org.visuals.legacy.animatium.util;

import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
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
import org.joml.Matrix4fStack;
import org.visuals.legacy.animatium.Animatium;

@UtilityClass
// Ported code of the old <=1.12.2 panorama renderer (w/ blur)
public class PanoramaRendererUtility {
    private final BlendFunction PANORAMA_BLEND = new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);

    private final RenderPipeline PANORAMA =
            RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
                    .withLocation(Animatium.location("pipeline/panorama"))
                    .withVertexShader("core/position_tex_color")
                    .withFragmentShader("core/position_tex_color")
                    .withCull(false)
                    .withDepthWrite(false)
                    .withBlend(PANORAMA_BLEND)
                    .withColorWrite(true, false)
                    .withSampler("Sampler0")
                    .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
                    .build();

    private final ResourceLocation[] PANORAMA_LOCATIONS = new ResourceLocation[]{
            ResourceLocation.withDefaultNamespace("textures/gui/title/background/panorama_0.png"),
            ResourceLocation.withDefaultNamespace("textures/gui/title/background/panorama_1.png"),
            ResourceLocation.withDefaultNamespace("textures/gui/title/background/panorama_2.png"),
            ResourceLocation.withDefaultNamespace("textures/gui/title/background/panorama_3.png"),
            ResourceLocation.withDefaultNamespace("textures/gui/title/background/panorama_4.png"),
            ResourceLocation.withDefaultNamespace("textures/gui/title/background/panorama_5.png")
    };

    private CachedPerspectiveProjectionMatrixBuffer projectionMatrixBuffer = null;
    private PanoramaTarget panoramaTarget = null;
    private float spin = 0.0F;

    static {
        setup();
    }

    private void setup() {
        if (panoramaTarget == null) {
            panoramaTarget = new PanoramaTarget();
        }

        if (projectionMatrixBuffer == null) {
            projectionMatrixBuffer = new CachedPerspectiveProjectionMatrixBuffer("panorama", 0.05F, 10.0F);
        }
    }

    public void render(final GuiGraphics guiGraphics, final int width, final int height) {
        renderPanorama(PANORAMA, panoramaTarget, width, height);
        for (int layer = 0; layer < 7; ++layer) {
            RenderUtils.drawInGui(panoramaTarget, new BlitBlurTexture(guiGraphics.pose(), panoramaTarget.getColorTextureView(), width, height));
        }

        guiGraphics.guiRenderState.submitGuiElement(new BlitFinalTexture(guiGraphics.pose(), panoramaTarget.getColorTextureView(), width, height, ARGB.white(1.0F)));
    }

    private void renderPanorama(final RenderPipeline pipeline, final RenderTarget renderTarget, final int width, final int height) {
        RenderSystem.setProjectionMatrix(projectionMatrixBuffer.getBuffer(width, height, 120.0F), ProjectionType.PERSPECTIVE);
        final Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.identity();
        modelViewStack.rotateX((float) Math.toRadians(180.0F));
        modelViewStack.rotateZ((float) Math.toRadians(90.0F));
        for (int i4 = 0; i4 < 64; i4++) {
            modelViewStack.pushMatrix();
            float f2 = (i4 % 8 / 8.0F - 0.5F) / 64.0F;
            float f3 = ((float) i4 / 8 / 8.0F - 0.5F) / 64.0F;
            modelViewStack.translate(f2, f3, 0.0F);
            modelViewStack.rotateX((float) Math.toRadians(getXRot()));
            modelViewStack.rotateY((float) Math.toRadians(getYRot()));
            for (int panoramaIdx = 0; panoramaIdx < 6; panoramaIdx++) {
                modelViewStack.pushMatrix();
                if (panoramaIdx == 1) {
                    modelViewStack.rotateY((float) Math.toRadians(90.0F));
                } else if (panoramaIdx == 2) {
                    modelViewStack.rotateY((float) Math.toRadians(180.0F));
                } else if (panoramaIdx == 3) {
                    modelViewStack.rotateY((float) Math.toRadians(-90.0F));
                } else if (panoramaIdx == 4) {
                    modelViewStack.rotateX((float) Math.toRadians(90.0F));
                } else if (panoramaIdx == 5) {
                    modelViewStack.rotateX((float) Math.toRadians(-90.0F));
                }

                final GpuTextureView panoramaTexture = Minecraft.getInstance().getTextureManager().getTexture(PANORAMA_LOCATIONS[panoramaIdx]).getTextureView();
                try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(pipeline.getVertexFormat().getVertexSize() * 4)) {
                    final BufferBuilder builder = new BufferBuilder(byteBufferBuilder, pipeline.getVertexFormatMode(), pipeline.getVertexFormat());
                    final int color = ARGB.white(255.0F / (i4 + 1.0F));
                    builder.addVertex(-1.0F, -1.0F, 1.0F).setUv(0.0F, 0.0F).setColor(color);
                    builder.addVertex(1.0F, -1.0F, 1.0F).setUv(1.0F, 0.0F).setColor(color);
                    builder.addVertex(1.0F, 1.0F, 1.0F).setUv(1.0F, 1.0F).setColor(color);
                    builder.addVertex(-1.0F, 1.0F, 1.0F).setUv(0.0F, 1.0F).setColor(color);
                    final GpuBufferSlice dynamicTransforms = DynamicTransformsBuilder.of().withModelViewMatrix(modelViewStack).build();
                    RenderUtils.drawWithPipeline(renderTarget, pipeline, builder.buildOrThrow(), (pass) -> {
                        pass.setUniform("DynamicTransforms", dynamicTransforms);
                        pass.bindSampler("Sampler0", panoramaTexture);
                    });
                }

                modelViewStack.popMatrix();
            }

            modelViewStack.popMatrix();
        }

        modelViewStack.popMatrix();
    }

    public void update(float tickDelta) {
        spin += tickDelta;
    }

    private float getXRot() {
        return Mth.sin(spin / 400.0F) * 25.0F + 20.0F;
    }

    private float getYRot() {
        return -spin * 0.1F;
    }

    private record BlitBlurTexture(Matrix3x2f pose, GpuTextureView texture, int width, int height) implements GuiElementRenderState {
        @Override
        public void buildVertices(VertexConsumer consumer) {
            for (int cycle = 0; cycle < 3; cycle++) {
                final int color = ARGB.white(1.0F / (cycle + 1));
                final float growth = (cycle - 1) / 256.0F;
                consumer.addVertexWith2DPose(this.pose, this.width, this.height).setUv(0.0F + growth, 1.0F).setColor(color);
                consumer.addVertexWith2DPose(this.pose, this.width, 0.0F).setUv(1.0F + growth, 1.0F).setColor(color);
                consumer.addVertexWith2DPose(this.pose, 0.0F, 0.0F).setUv(1.0F + growth, 0.0F).setColor(color);
                consumer.addVertexWith2DPose(this.pose, 0.0F, this.height).setUv(0.0F + growth, 0.0F).setColor(color);
            }
        }

        @Override
        public @NotNull RenderPipeline pipeline() {
            return RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET)
                    .withLocation(Animatium.location("pipeline/panorama_blur"))
                    .withBlend(PANORAMA_BLEND)
                    .withColorWrite(true, false)
                    .build();
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

    public record BlitFinalTexture(Matrix3x2f pose, GpuTextureView texture, int width, int height, int color) implements GuiElementRenderState {
        @Override
        public void buildVertices(VertexConsumer consumer) {
            final float aspect = 120.0F / (Math.max(this.width, this.height));
            final float sw = this.width * aspect / panoramaTarget.width;
            final float sh = this.height * aspect / panoramaTarget.height;
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
