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

import com.mojang.blaze3d.opengl.GlTextureView;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.ARGB;
import org.joml.Matrix3x2f;
import org.visuals.legacy.animatium.Animatium;

@UtilityClass
// Ported code of the old <=1.12.2 panorama renderer (w/ blur)
public class PanoramaRendererUtility {
    private static final RenderPipeline.Snippet TEXTURE_SNIPPET =
            RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
                    .withVertexShader("core/position_tex")
                    .withFragmentShader("core/position_tex")
                    .withSampler("Sampler0")
                    .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
                    .buildSnippet();

    private static final RenderPipeline BLUR_TEXTURED_PIPELINE =
            RenderPipeline.builder(TEXTURE_SNIPPET)
                    .withLocation(Animatium.location("pipeline/blur_texture"))
                    .withColorWrite(true, false)
                    .withBlend(new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO))
                    .build();

    private static final RenderPipeline BASIC_TEXTURED_PIPELINE =
            RenderPipeline.builder(TEXTURE_SNIPPET)
                    .withLocation(Animatium.location("pipeline/basic_texture"))
                    .build();

    // To be called after ``cubeMap.render``
    public void render(Matrix3x2f matrix, GlTextureView textureView, int width, int height) {
        final RenderTarget renderTarget = Minecraft.getInstance().getMainRenderTarget();
        for (int i = 0; i < 6; ++i) {
            PanoramaRendererUtility.writeAndBlitBlurTexture(matrix, renderTarget, textureView, width, height);
        }

        PanoramaRendererUtility.renderFinalTexture(renderTarget, textureView, width, height);
    }

    public void writeAndBlitBlurTexture(Matrix3x2f matrix, RenderTarget renderTarget, GlTextureView texture, int width, int height) {
        texture.texture().setTextureFilter(FilterMode.LINEAR, false);
        // Ensures enough width/height for it to not crash when window is resized
        if (renderTarget.width >= 256 && renderTarget.height >= 256) {
            RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(
                    renderTarget.getColorTexture(),
                    texture.texture(),
                    0, // mips?
                    0, 0, // srcXY
                    0, 0, // dstXY
                    256, 256 // w/h
            );
        }

        final RenderPipeline pipeline = BLUR_TEXTURED_PIPELINE;
        try (ByteBufferBuilder byteBufferBuilder = new ByteBufferBuilder(pipeline.getVertexFormat().getVertexSize() * 12)) {
            final BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, pipeline.getVertexFormatMode(), pipeline.getVertexFormat());
            for (int i = 0; i < 3; ++i) {
                final float growth = (float) (i - 1) / 256.0F;
                final int color = ARGB.colorFromFloat(1.0F / (float) (i + 1), 1.0F, 1.0F, 1.0F);
                bufferBuilder.addVertexWith2DPose(matrix, width, height).setUv(0.0F + growth, 1.0F).setColor(color);
                bufferBuilder.addVertexWith2DPose(matrix, width, 0.0F).setUv(1.0F + growth, 1.0F).setColor(color);
                bufferBuilder.addVertexWith2DPose(matrix, 0.0F, 0.0F).setUv(1.0F + growth, 0.0F).setColor(color);
                bufferBuilder.addVertexWith2DPose(matrix, 0.0F, height).setUv(0.0F + growth, 0.0F).setColor(color);
            }

            RenderUtils.drawBuffer(pipeline, renderTarget, bufferBuilder.buildOrThrow(), (pass) -> {
                pass.bindSampler("Sampler0", texture);
            });
        }
    }

    public void renderFinalTexture(RenderTarget renderTarget, GlTextureView texture, int width, int height) {
        float aspect = 120.0F / (float) (Math.max(width, height));
        float sw = (float) width * aspect / 256.0F;
        float sh = (float) height * aspect / 256.0F;
        final int color = ARGB.white(1.0F);
        final RenderPipeline pipeline = BASIC_TEXTURED_PIPELINE;
        try (ByteBufferBuilder byteBufferBuilder = new ByteBufferBuilder(pipeline.getVertexFormat().getVertexSize() * 4)) {
            final BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, pipeline.getVertexFormatMode(), pipeline.getVertexFormat());
            bufferBuilder.addVertex(0.0F, height, 0.0F).setUv(0.5F - sh, 0.5F + sw).setColor(color);
            bufferBuilder.addVertex(width, height, 0.0F).setUv(0.5F - sh, 0.5F - sw).setColor(color);
            bufferBuilder.addVertex(width, 0.0F, 0.0F).setUv(0.5F + sh, 0.5F - sw).setColor(color);
            bufferBuilder.addVertex(0.0F, 0.0F, 0.0F).setUv(0.5F + sh, 0.5F + sw).setColor(color);
            RenderUtils.drawBuffer(pipeline, renderTarget, bufferBuilder.buildOrThrow(), (pass) -> {
                pass.bindSampler("Sampler0", texture);
            });
        }
    }
}
