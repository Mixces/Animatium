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

package org.visuals.legacy.animatium.mixins.v1.gui.hover_text_color;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(AbstractSliderButton.class)
public abstract class MixinAbstractSliderButton_LegacyTextHoverColor extends AbstractWidget {
	public MixinAbstractSliderButton_LegacyTextHoverColor(final int x, final int y, final int width, final int height, final Component message) {
		super(x, y, width, height, message);
	}

	@ModifyConstant(method = "renderWidget", constant = @Constant(intValue = 0xFFFFFFFF))
	private int animatium$renderWidget$old$textColor(final int constant) {
		if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.legacyWidgetHoverTextColor) {
			return !this.active ? 0xFFE0E0E0 : (this.isHoveredOrFocused() ? 0xFFFFFFA0 : 0xFFE0E0E0);
		} else {
			return constant;
		}
	}
}
