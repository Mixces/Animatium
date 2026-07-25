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

package org.visuals.legacy.animatium.mixins.v1.rendering.fog;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.CompiledRenderPipeline;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import net.minecraft.client.renderer.SkyRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.handler.rendering.AnimatiumPipelines;
import org.visuals.legacy.animatium.handler.rendering.LegacySkyRenderer;

@Mixin(SkyRenderer.class)
public abstract class MixinSkyRenderer_PlanarFogSky {
    @Unique
    private static RenderSystem.AutoStorageIndexBuffer animatium$skyIndexBuffer;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void animatium$initSkyRenderer(final CallbackInfo ci) {
        // Load them before anything (Static Variables don't load until used, which would cause a issue in the RenderPass)
        LegacySkyRenderer.TOP_GEOMETRY.getVertexBuffer();
        LegacySkyRenderer.BOTTOM_GEOMETRY.getVertexBuffer();
        animatium$skyIndexBuffer = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
    }

    @WrapOperation(method = "renderSkyDisc", at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/api/commands/RenderPass;setPipeline(Lcom/mojang/renderpearl/api/pipeline/CompiledRenderPipeline;)V"))
    private void animatium$planarFogPipeline$skyDisc(final RenderPass instance, final CompiledRenderPipeline renderPipeline, final Operation<Void> original) {
        CompiledRenderPipeline pipeline = renderPipeline;
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.planarSkyFog) {
            pipeline = RenderSystem.getCompiledPipeline(AnimatiumPipelines.LEGACY_SKY_PLANAR_FOG);
        }

        original.call(instance, pipeline);
    }

    @WrapOperation(method = "renderSkyDisc", at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/api/commands/RenderPass;setVertexBuffer(ILcom/mojang/renderpearl/api/buffers/GpuBufferSlice;)V", ordinal = 0))
    private void animatium$planarFogPipeline$skyDisc$vertexBuffer(final RenderPass instance, final int slot, final GpuBufferSlice vertexBuffer, final Operation<Void> original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.planarSkyFog) {
            LegacySkyRenderer.TOP_GEOMETRY.bind(instance, animatium$skyIndexBuffer);
        } else {
            original.call(instance, slot, vertexBuffer);
        }
    }

    @WrapOperation(method = "renderDarkDisc", at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/api/commands/RenderPass;setPipeline(Lcom/mojang/renderpearl/api/pipeline/CompiledRenderPipeline;)V"))
    private void animatium$planarFogPipeline$darkSkyDisc(final RenderPass instance, final CompiledRenderPipeline renderPipeline, final Operation<Void> original) {
        CompiledRenderPipeline pipeline = renderPipeline;
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.planarSkyFog) {
            pipeline = RenderSystem.getCompiledPipeline(AnimatiumPipelines.LEGACY_SKY_PLANAR_FOG);
        }

        original.call(instance, pipeline);
    }

    @WrapOperation(method = "renderSkyDisc", at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/api/commands/RenderPass;draw(IIII)V", ordinal = 0))
    private void animatium$planarFogPipeline$skyDisc$draw(final RenderPass instance, final int vertexCount, final int instanceCount, final int firstVertex, final int firstInstance, final Operation<Void> original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.planarSkyFog) {
            LegacySkyRenderer.TOP_GEOMETRY.draw(instance);
        } else {
            original.call(instance, vertexCount, instanceCount, firstVertex, firstInstance);
        }
    }

    @WrapOperation(method = "renderDarkDisc", at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/api/commands/RenderPass;setVertexBuffer(ILcom/mojang/renderpearl/api/buffers/GpuBufferSlice;)V", ordinal = 0))
    private void animatium$planarFogPipeline$darkSkyDisc$vertexBuffer(final RenderPass instance, final int slot, final GpuBufferSlice vertexBuffer, final Operation<Void> original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.planarSkyFog) {
            LegacySkyRenderer.BOTTOM_GEOMETRY.bind(instance, animatium$skyIndexBuffer);
        } else {
            original.call(instance, slot, vertexBuffer);
        }
    }

    @WrapOperation(method = "renderDarkDisc", at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/api/commands/RenderPass;draw(IIII)V", ordinal = 0))
    private void animatium$planarFogPipeline$darkSkyDisc$draw(final RenderPass instance, final int vertexCount, final int instanceCount, final int firstVertex, final int firstInstance, final Operation<Void> original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.planarSkyFog) {
            LegacySkyRenderer.BOTTOM_GEOMETRY.draw(instance);
        } else {
            original.call(instance, vertexCount, instanceCount, firstVertex, firstInstance);
        }
    }
}
