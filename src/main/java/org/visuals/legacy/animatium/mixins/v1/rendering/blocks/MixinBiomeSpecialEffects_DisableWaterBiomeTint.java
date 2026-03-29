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

package org.visuals.legacy.animatium.mixins.v1.rendering.blocks;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(BiomeSpecialEffects.class)
public abstract class MixinBiomeSpecialEffects_DisableWaterBiomeTint {
    @Shadow
    @Final
    private int waterColor;

    @Definition(id = "waterColor", field = "Lnet/minecraft/world/level/biome/BiomeSpecialEffects;waterColor:I")
    @Expression("this.waterColor")
    @ModifyExpressionValue(method = "waterColor", at = @At("MIXINEXTRAS:EXPRESSION"))
    private int animatium$oldWaterColor(int original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().extras.oldWaterColorEffects) {
            if (this.waterColor == 6388580/*Swamp Water Color*/) {
                return ARGB.color(224, 255, 174);
            } else {
                return ARGB.white(1.0F);
            }
        } else {
            return original;
        }
    }
}
