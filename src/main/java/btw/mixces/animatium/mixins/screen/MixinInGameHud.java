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
 */

package btw.mixces.animatium.mixins.screen;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.CameraType;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Function;

@Mixin(Gui.class)
public abstract class MixinInGameHud {
    @WrapOperation(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/CameraType;isFirstPerson()Z"))
    private boolean animatium$showCrosshairInThirdperson(CameraType instance, Operation<Boolean> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().showCrosshairInThirdperson) {
            return true;
        } else {
            return original.call(instance);
        }
    }

    @WrapWithCondition(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Ljava/util/function/Function;Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 2))
    private boolean animatium$fixHighAttackSpeedIndicator(GuiGraphics instance, Function<ResourceLocation, RenderType> function, ResourceLocation resourceLocation, int i, int j, int k, int l, @Local float f) {
        if (AnimatiumConfig.instance().fixHighAttackSpeedIndicator) {
            return (int) (f * 17.0F) != 0;
        } else {
            return true;
        }
    }

    @WrapWithCondition(method = "renderHearts", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderHeart(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Gui$HeartType;IIZZZ)V"))
    private boolean animatium$removeHeartFlash(Gui instance, GuiGraphics guiGraphics, Gui.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half) {
        return !AnimatiumClient.isEnabled() || !AnimatiumConfig.instance().removeHeartFlash || !blinking || type == Gui.HeartType.CONTAINER;
    }
}
