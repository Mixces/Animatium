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

package org.visuals.legacy.animatium.mixins.v1.gui.screen_tweaks;

import net.minecraft.client.renderer.PanoramaRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PanoramaRenderer.class)
public abstract class MixinPanoramaRenderer_LegacyRendering {
    // TODO 3.3
	/*@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/CubeMap;render(Lnet/minecraft/client/Minecraft;FF)V", ordinal = 0))
	private void animatium$panoramaRendering(CubeMap instance, Minecraft minecraft, float xRot, float yRot, Operation<Void> original, @Local(argsOnly = true) GuiGraphics guiGraphics, @Local(argsOnly = true, ordinal = 0) int width, @Local(argsOnly = true, ordinal = 1) int height) {
		if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.panoramaRendering) {
			LegacyPanoramaRenderer.update(minecraft.getDeltaTracker().getGameTimeDeltaTicks());
			LegacyPanoramaRenderer.render(guiGraphics, width, height);
		} else {
			original.call(instance, minecraft, xRot, yRot);
		}
	}

	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIFFIIIIII)V"))
	private void animatium$panoramaGradient(GuiGraphics instance, RenderPipeline pipeline, ResourceLocation atlas, int x, int y, float u, float v, int width, int height, int uWidth, int vHeight, int textureWidth, int textureHeight, Operation<Void> original) {
		if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.panoramaRendering) {
			instance.fillGradient(0, 0, width, height, -2130706433, 16777215);
			instance.fillGradient(0, 0, width, height, 0, Integer.MIN_VALUE);
		} else {
			original.call(instance, pipeline, atlas, x, y, u, v, width, height, uWidth, vHeight, textureWidth, textureHeight);
		}
	}*/
}
