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

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(Gui.class)
public abstract class MixinGui_OldCrosshairPosition {
	@Definition(id = "guiGraphics", local = @Local(type = GuiGraphics.class, argsOnly = true))
	@Definition(id = "guiWidth", method = "Lnet/minecraft/client/gui/GuiGraphics;guiWidth()I")
	@Expression("(guiGraphics.guiWidth() - 15) / 2")
	@ModifyExpressionValue(method = "renderCrosshair", at = @At("MIXINEXTRAS:EXPRESSION"))
	private int animatium$oldCrosshairPosition(int original) {
		if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.oldCrosshairPosition) {
			original++; // like seriously, this is all it takes LMAO
		}

		return original;
	}
}
