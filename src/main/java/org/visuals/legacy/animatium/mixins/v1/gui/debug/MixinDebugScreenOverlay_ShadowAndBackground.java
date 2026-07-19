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

package org.visuals.legacy.animatium.mixins.v1.gui.debug;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(DebugScreenOverlay.class)
public abstract class MixinDebugScreenOverlay_ShadowAndBackground {
    @WrapWithCondition(method = "extractLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V"))
    private boolean animatium$removeDebugBackground(final GuiGraphicsExtractor instance, final int x0, final int y0, final int x1, final int y1, final int col) {
        return !Animatium.isEnabled() || !AnimatiumConfig.instance().screen.disableDebugHudBackground;
    }

    @ModifyArg(method = "extractLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)V"), index = 5)
    private boolean animatium$addDebugShadow(final boolean dropShadow) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.debugHudTextShadow) {
            return true;
        } else {
            return dropShadow;
        }
    }

    @ModifyArg(method = "extractLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)V"), index = 4)
    private int animatium$debugHudTextColor(final int color) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().extras.debugHudTextColor) {
            return -1;
        } else {
            return color;
        }
    }
}
