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

package org.visuals.legacy.animatium.mixins.v1.rendering;

import btw.lowercase.renderer.Renderer;
import btw.lowercase.renderer.buffer.Geometry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuSurface;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.rendering.AnimatiumPipelines;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft_ColorBoost {
    @Shadow
    @Final
    public Gui gui;

    @Shadow
    @Final
    public GameRenderer gameRenderer;

    @Unique
    private static final Geometry.Basic animatium$boostGeometry = new Geometry.Basic(0, 3);

    @WrapOperation(method = "renderFrame", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/GpuSurface;blitFromTexture(Lcom/mojang/blaze3d/systems/CommandEncoder;Lcom/mojang/blaze3d/textures/GpuTextureView;)V"))
    private void animatium$colorBoost(final GpuSurface instance, final CommandEncoder commandEncoder, final GpuTextureView colorAttachment, final Operation<Void> original) {
        if (AnimatiumConfig.instance().extras.colorBoost && this.gui.screen() == null) {
            try (final Renderer renderer = Renderer.of(() -> "Color Boost Blit", colorAttachment, this.gameRenderer.mainRenderTarget().getDepthTextureView())) {
                renderer.setPipeline(AnimatiumPipelines.COLOR_BOOST_BLIT);
                renderer.setTexture("Sampler0", colorAttachment, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
                renderer.setup(animatium$boostGeometry);
                renderer.draw();
            }
        }

        original.call(instance, commandEncoder, colorAttachment);
    }
}
