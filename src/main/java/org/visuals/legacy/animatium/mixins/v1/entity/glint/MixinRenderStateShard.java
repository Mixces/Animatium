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

package org.visuals.legacy.animatium.mixins.v1.entity.glint;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.RenderStateShard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.AnimatiumClient;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(RenderStateShard.class)
public abstract class MixinRenderStateShard {
    @ModifyExpressionValue(method = "setupGlintTexturing", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;"))
    private static Object animatium$forceMaxGlintSpeed(Object original) {
        if (AnimatiumClient.ENABLED && AnimatiumConfig.instance().other.maxGlintProperties) {
            // 100% glint speed
            return 1.0D;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(method = "setupGlintTexturing", at = @At(value = "CONSTANT", args = "doubleValue=8.0"))
    private static double animatium$glintSpeed(double original, @Local(argsOnly = true) float f) {
        if (AnimatiumClient.ENABLED && AnimatiumConfig.instance().items.glintSpeed && f == 8.0F) {
            // Value taken from 1.8
            return 1.0D;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(method = "setupGlintTexturing", at = @At(value = "CONSTANT", args = "floatValue=110000.0"))
    private static float animatium$glintSpeed$horizontal(float original, @Local(argsOnly = true) float f) {
        if (AnimatiumClient.ENABLED && AnimatiumConfig.instance().items.glintSpeed && f == 8.0F) {
            // Value taken from 1.7/1.8
            return 4873.0F;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(method = "setupGlintTexturing", at = @At(value = "CONSTANT", args = "floatValue=30000.0"))
    private static float animatium$glintSpeed$diagonal(float original, @Local(argsOnly = true) float f) {
        if (AnimatiumClient.ENABLED && AnimatiumConfig.instance().items.glintSpeed && f == 8.0F) {
            // Value taken from 1.7/1.8
            return 3000.0F;
        } else {
            return original;
        }
    }
}
