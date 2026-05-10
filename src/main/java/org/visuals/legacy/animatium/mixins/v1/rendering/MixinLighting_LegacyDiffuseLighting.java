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

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.platform.Lighting;
import org.joml.Vector3fc;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(Lighting.class)
public abstract class MixinLighting_LegacyDiffuseLighting {
    @Shadow
    @Final
    private static Vector3fc DIFFUSE_LIGHT_0;

    @Shadow
    @Final
    private static Vector3fc DIFFUSE_LIGHT_1;

    @ModifyExpressionValue(method = "<init>", at = @At(value = "FIELD", target = "Lcom/mojang/blaze3d/platform/Lighting;INVENTORY_DIFFUSE_LIGHT_0:Lorg/joml/Vector3fc;", opcode = Opcodes.GETSTATIC))
    private Vector3fc animatium$legacyDiffuseLighting$useDiffuse0Inventory(final Vector3fc original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.legacyDiffuseLighting) {
            return DIFFUSE_LIGHT_0;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(method = "<init>", at = @At(value = "FIELD", target = "Lcom/mojang/blaze3d/platform/Lighting;INVENTORY_DIFFUSE_LIGHT_1:Lorg/joml/Vector3fc;", opcode = Opcodes.GETSTATIC))
    private Vector3fc animatium$legacyDiffuseLighting$useDiffuse1Inventory(final Vector3fc original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.legacyDiffuseLighting) {
            return DIFFUSE_LIGHT_1;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(method = "updateLevel", at = @At(value = "FIELD", target = "Lcom/mojang/blaze3d/platform/Lighting;NETHER_DIFFUSE_LIGHT_0:Lorg/joml/Vector3fc;", opcode = Opcodes.GETSTATIC))
    private Vector3fc animatium$legacyDiffuseLighting$useDiffuse0Nether(final Vector3fc original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.legacyDiffuseLighting) {
            return DIFFUSE_LIGHT_0;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(method = "updateLevel", at = @At(value = "FIELD", target = "Lcom/mojang/blaze3d/platform/Lighting;NETHER_DIFFUSE_LIGHT_1:Lorg/joml/Vector3fc;", opcode = Opcodes.GETSTATIC))
    private Vector3fc animatium$legacyDiffuseLighting$useDiffuse1Nether(final Vector3fc original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.legacyDiffuseLighting) {
            return DIFFUSE_LIGHT_1;
        } else {
            return original;
        }
    }
}
