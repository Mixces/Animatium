/**
 * Animatium
 * The all-you-could-want legacy animations mod for modern minecraft versions.
 * Brings back animations from the 1.7/1.8 era and more.
 * <p>
 * Copyright (C) 2024-2027 lowercasebtw
 * Copyright (C) 2024-2027 mixces
 * Copyright (C) 2024-2027 Contributors to the project retain their copyright
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

package org.visuals.legacy.animatium.mixins.v1.rendering.sky;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.renderer.CloudRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.oit.OitRenderPassProvider;
import net.minecraft.client.renderer.oit.OitStage;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.handler.rendering.clouds.LegacyCloudRenderer;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer_OldCloudRendering {
    @Inject(method = "close", at = @At("TAIL"))
    private void animatium$closeLegacyClouds(final CallbackInfo ci) {
        LegacyCloudRenderer.INSTANCE.close();
    }

    @WrapOperation(method = "invalidateCompiledGeometry", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/CloudRenderer;markForRebuild()V"))
    private void animatium$markLegacyCloudsForRebuild$1(final CloudRenderer instance, final Operation<Void> original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.oldCloudRendering) {
            LegacyCloudRenderer.INSTANCE.markForRebuild();
        } else {
            original.call(instance);
        }
    }

    @WrapOperation(method = "prepareTranslucents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/CloudRenderer;prepare(ILnet/minecraft/client/CloudStatus;FILnet/minecraft/world/phys/Vec3;JF)V"))
    private void animatium$prepareLegacyClouds(final CloudRenderer instance, final int color, final CloudStatus cloudStatus, final float bottomY, final int range, final Vec3 cameraPosition, final long gameTime, final float tickDelta, final Operation<Void> original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.oldCloudRendering) {
            LegacyCloudRenderer.INSTANCE.prepare(color, cloudStatus, bottomY, cameraPosition, tickDelta);
        } else {
            original.call(instance, color, cloudStatus, bottomY, range, cameraPosition, gameTime, tickDelta);
        }
    }

    @WrapWithCondition(method = "executeClassicTransparency", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/CloudRenderer;render(Lnet/minecraft/client/CloudStatus;Lcom/mojang/renderpearl/api/commands/RenderPass;)V"))
    private boolean animatium$disableVanillaCloudRendering$normal(final CloudRenderer instance, final CloudStatus cloudStatus, final RenderPass renderPass) {
        return !Animatium.isEnabled() || !AnimatiumConfig.instance().other.oldCloudRendering;
    }

    @WrapWithCondition(method = "executeOit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/CloudRenderer;renderOit(Lnet/minecraft/client/CloudStatus;Lnet/minecraft/client/renderer/oit/OitStage;Lnet/minecraft/client/renderer/oit/OitRenderPassProvider$Parameters;)V"))
    private boolean animatium$disableVanillaCloudRendering$oit(final CloudRenderer instance, final CloudStatus cloudStatus, final OitStage stage, final OitRenderPassProvider.Parameters params) {
        return !Animatium.isEnabled() || !AnimatiumConfig.instance().other.oldCloudRendering;
    }

    @Inject(method = "lambda$addMainPass$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;executeOutline(Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher$PreparedFrame;)V", shift = At.Shift.BEFORE))
    private void animatium$renderLegacyClouds(final GpuBufferSlice terrainFog, final boolean useImprovedTransparency, final ChunkSectionsToRender chunkSectionsToRender, final FeatureRenderDispatcher.PreparedFrame featureFrame, final boolean hasAlwaysOnTopGizmos, final boolean consistentDepthRequired, final CallbackInfo ci) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.oldCloudRendering) {
            LegacyCloudRenderer.INSTANCE.render();
        }
    }

    @WrapWithCondition(method = "endFrame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/CloudRenderer;endFrame()V"))
    private boolean animatium$endLegacyCloudsFrame(final CloudRenderer instance) {
        return !Animatium.isEnabled() || !AnimatiumConfig.instance().other.oldCloudRendering;
    }
}
