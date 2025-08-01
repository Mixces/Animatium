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
 */

package btw.mixces.animatium.mixins.screen;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.RenderUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.opengl.GlTextureView;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.joml.Matrix3x2f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PanoramaRenderer.class)
public abstract class MixinPanoramaRenderer {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Unique
    private static final RenderPipeline.Snippet animatium$TEXTURE_SNIPPET =
            RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
                    .withVertexShader("core/position_tex")
                    .withFragmentShader("core/position_tex")
                    .withSampler("Sampler0")
                    .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
                    .buildSnippet();

    @Unique
    private static final RenderPipeline animatium$BLUR_TEXTURE =
            RenderPipeline.builder(animatium$TEXTURE_SNIPPET)
                    .withLocation(AnimatiumClient.id("pipeline/blur_texture"))
                    .withColorWrite(true, false)
                    .withBlend(new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO))
                    .build();

    @Unique
    private static final RenderPipeline animatium$BASIC_TEXTURE =
            RenderPipeline.builder(animatium$TEXTURE_SNIPPET)
                    .withLocation(AnimatiumClient.id("pipeline/basic_texture"))
                    .build();

    @Unique
    private GlTexture animatium$backgroundTexture;

    @Unique
    private GlTextureView animatium$backgroundTextureView;

    @Inject(method = "render", at = @At("HEAD"))
    private void animatium$panoramaStart(GuiGraphics guiGraphics, int i, int j, boolean bl, CallbackInfo ci) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().panoramaRendering) {
            if (animatium$backgroundTexture == null) {
                GpuDevice device = RenderSystem.getDevice();
                animatium$backgroundTexture = (GlTexture) device.createTexture(() -> "Background texture", 15, TextureFormat.RGBA8, 256, 256, 1, 1);
                animatium$backgroundTextureView = (GlTextureView) device.createTextureView(animatium$backgroundTexture);
            }

            GlStateManager._viewport(0, 0, 256, 256);
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/CubeMap;render(Lnet/minecraft/client/Minecraft;FF)V", ordinal = 0, shift = At.Shift.AFTER))
    private void animatium$panoramaFinish(GuiGraphics guiGraphics, int width, int height, boolean bl, CallbackInfo ci) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().panoramaRendering) {
            RenderTarget renderTarget = minecraft.getMainRenderTarget();
            for (int i = 0; i < 6; ++i) {
                this.animatium$writeAndBlitBlurTexture(guiGraphics, renderTarget, animatium$backgroundTextureView, width, height);
            }

            GlStateManager._viewport(0, 0, renderTarget.width, renderTarget.height);
            animatium$renderFinalTexture(renderTarget, animatium$backgroundTextureView, width, height);
        }
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIFFIIIIII)V"))
    private void animatium$panoramaGradient(GuiGraphics instance, RenderPipeline renderPipeline, ResourceLocation resourceLocation, int i, int j, float f, float g, int width, int height, int m, int n, int o, int p, Operation<Void> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().panoramaRendering) {
            instance.fillGradient(0, 0, width, height, -2130706433, 16777215);
            instance.fillGradient(0, 0, width, height, 0, Integer.MIN_VALUE);
        } else {
            original.call(instance, renderPipeline, resourceLocation, i, j, f, g, width, height, m, n, o, p);
        }
    }

    @Unique
    private void animatium$writeAndBlitBlurTexture(GuiGraphics drawContext, RenderTarget renderTarget, GlTextureView texture, int width, int height) {
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

        ByteBufferBuilder byteBufferBuilder = new ByteBufferBuilder(DefaultVertexFormat.POSITION_TEX_COLOR.getVertexSize() * 12);
        BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        Matrix3x2f matrix = drawContext.pose();
        for (int i = 0; i < 3; ++i) {
            float growth = (float) (i - 1) / 256.0F;
            int color = ARGB.colorFromFloat(1.0F / (float) (i + 1), 1.0F, 1.0F, 1.0F);
            bufferBuilder.addVertexWith2DPose(matrix, width, height).setUv(0.0F + growth, 1.0F).setColor(color);
            bufferBuilder.addVertexWith2DPose(matrix, width, 0.0F).setUv(1.0F + growth, 1.0F).setColor(color);
            bufferBuilder.addVertexWith2DPose(matrix, 0.0F, 0.0F).setUv(1.0F + growth, 0.0F).setColor(color);
            bufferBuilder.addVertexWith2DPose(matrix, 0.0F, height).setUv(0.0F + growth, 0.0F).setColor(color);
        }

        RenderSystem.setShaderTexture(0, texture);
        RenderUtils.drawBuffer(bufferBuilder, renderTarget, animatium$BLUR_TEXTURE);
    }

    @Unique
    private void animatium$renderFinalTexture(RenderTarget renderTarget, GlTextureView texture, int width, int height) {
        float f = 120.0F / (float) (Math.max(width, height));
        float g = (float) height * f / 256.0F;
        float h = (float) width * f / 256.0F;
        int color = ARGB.white(1.0F);
        ByteBufferBuilder byteBufferBuilder = new ByteBufferBuilder(DefaultVertexFormat.POSITION_TEX_COLOR.getVertexSize() * 4);
        BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferBuilder.addVertex(0.0F, height, 0.0F).setUv(0.5F - g, 0.5F + h).setColor(color);
        bufferBuilder.addVertex(width, height, 0.0F).setUv(0.5F - g, 0.5F - h).setColor(color);
        bufferBuilder.addVertex(width, 0.0F, 0.0F).setUv(0.5F + g, 0.5F - h).setColor(color);
        bufferBuilder.addVertex(0.0F, 0.0F, 0.0F).setUv(0.5F + g, 0.5F + h).setColor(color);
        RenderSystem.setShaderTexture(0, texture);
        RenderUtils.drawBuffer(bufferBuilder, renderTarget, animatium$BASIC_TEXTURE);
    }
}
