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

package org.visuals.legacy.animatium.mixins.v1.gui.loading_screen;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.rendering.LegacyLogoTexture;

import java.util.function.IntSupplier;

@Mixin(LoadingOverlay.class)
public abstract class MixinLoadingOverlay_LegacyLoadingScreen {
    @Unique
    private static final Identifier animatium$MOJANG_LOGO = Identifier.withDefaultNamespace("textures/gui/title/mojang.png");

    @Inject(method = "registerTextures", at = @At("TAIL"))
    private static void animatium$loadTextures(TextureManager textureManager, CallbackInfo ci) {
        textureManager.registerAndLoad(animatium$MOJANG_LOGO, new LegacyLogoTexture(animatium$MOJANG_LOGO));
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/function/IntSupplier;getAsInt()I"))
    private int animatium$replaceBackgroundColor(IntSupplier instance, Operation<Integer> original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.legacyLoadingScreen) {
            return ARGB.white(1.0F);
        } else {
            return original.call(instance);
        }
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/LoadingOverlay;replaceAlpha(II)I"))
    private int animatium$disableFade(int color, int alpha, Operation<Integer> original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.legacyLoadingScreen) {
            return color;
        } else {
            return original.call(color, alpha);
        }
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIIIIII)V", ordinal = 0))
    private void animatium$changeLogo(GuiGraphics instance, RenderPipeline pipeline, Identifier atlas, int x, int y, float u, float v, int width, int height, int uWidth, int vHeight, int textureWidth, int textureHeight, int color, Operation<Void> original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.legacyLoadingScreen) {
            final int size = 256;
            instance.blit(RenderPipelines.GUI_TEXTURED, animatium$MOJANG_LOGO, (instance.guiWidth() - size) / 2, (instance.guiHeight() - size) / 2, 0, 0, size, size, size, size, size, size);
        } else {
            original.call(instance, pipeline, atlas, x, y, u, v, width, height, uWidth, vHeight, textureWidth, textureHeight, color);
        }
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIIIIII)V", ordinal = 1))
    private boolean animatium$disableSecondLogoDraw(GuiGraphics instance, RenderPipeline pipeline, Identifier atlas, int x, int y, float u, float v, int width, int height, int uWidth, int vHeight, int textureWidth, int textureHeight, int color) {
        return !Animatium.isEnabled() || !AnimatiumConfig.instance().screen.legacyLoadingScreen;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/LoadingOverlay;drawProgressBar(Lnet/minecraft/client/gui/GuiGraphics;IIIIF)V"))
    private boolean animatium$disableProgressBar(LoadingOverlay instance, GuiGraphics guiGraphics, int minX, int minY, int maxX, int maxY, float partialTick) {
        return !Animatium.isEnabled() || !AnimatiumConfig.instance().screen.legacyLoadingScreen;
    }
}
