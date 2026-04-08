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

package org.visuals.legacy.animatium.mixins.v1.rendering.outlines;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer_EntityOutlines {
    @Unique
    private GpuTexture animatium$blankTexture = null;

    @Unique
    private GpuTextureView animatium$blankTextureView = null;

    @WrapOperation(method = "doEntityOutline", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;blitAndBlendToTexture(Lcom/mojang/blaze3d/textures/GpuTextureView;)V"))
    private void animatium$entityGlowOutline(RenderTarget instance, GpuTextureView gpuTextureView, Operation<Void> original) {
        GpuTextureView textureView = gpuTextureView;
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.disableEntityGlowOutline) {
            final GpuDevice device = RenderSystem.getDevice();
            if (this.animatium$blankTexture == null) {
                this.animatium$blankTexture = device.createTexture(() -> "Blank", 15, GpuFormat.RGBA8_UINT, 1, 1, 1, 1);
            }

            if (this.animatium$blankTextureView == null) {
                this.animatium$blankTextureView = device.createTextureView(this.animatium$blankTexture);
            }

            textureView = this.animatium$blankTextureView;
        }

        original.call(instance, textureView);
    }
}
