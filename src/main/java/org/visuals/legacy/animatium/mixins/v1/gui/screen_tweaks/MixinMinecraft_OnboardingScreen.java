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
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.visuals.legacy.animatium.screens.OnboardingScreen;
import org.visuals.legacy.animatium.util.config.ConfigUtil;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft_OnboardingScreen {
    @Definition(id = "screen", field = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;")
    @Definition(id = "guiScreen", local = @Local(type = Screen.class, argsOnly = true))
    @Expression("@(this).screen = guiScreen")
    @ModifyVariable(method = "setScreen", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.BEFORE), argsOnly = true)
    private Screen animatium$showOnboarding(Screen value) {
        if (value instanceof TitleScreen titleScreen && ConfigUtil.bool("onboarding")) {
            return new OnboardingScreen(titleScreen);
        } else {
            return value;
        }
    }
}
