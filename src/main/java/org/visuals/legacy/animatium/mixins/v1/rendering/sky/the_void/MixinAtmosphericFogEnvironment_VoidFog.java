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

package org.visuals.legacy.animatium.mixins.v1.rendering.sky.the_void;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.Utils;

@Mixin(AtmosphericFogEnvironment.class)
public abstract class MixinAtmosphericFogEnvironment_VoidFog {
    @Definition(id = "fogData", local = @Local(type = FogData.class, argsOnly = true))
    @Definition(id = "environmentalEnd", field = "Lnet/minecraft/client/renderer/fog/FogData;environmentalEnd:F")
    @Expression("fogData.environmentalEnd = ?")
    @WrapOperation(method = "setupFog", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private void animatium$voidFog(
            FogData instance,
            float value,
            Operation<Void> original,
            @Local(argsOnly = true) Entity entity,
            @Local(argsOnly = true) BlockPos blockPos,
            @Local(argsOnly = true) ClientLevel clientLevel,
            @Local(argsOnly = true) float renderDistance,
            @Local(argsOnly = true) DeltaTracker deltaTracker
    ) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.voidFog && Utils.hasFog1_7(clientLevel)) {
            float viewDistance = renderDistance;
            double val = ((animatium$getLightLevel(clientLevel, entity) & 15728640) >> 20) / 16.0 + (Mth.lerp(deltaTracker.getGameTimeDeltaTicks(), entity.yOld, entity.yo) + 4.0) / 32.0;
            if (val < 1.0) {
                if (val < 0.0) {
                    val = 0.0;
                }

                val *= val;
                float scaledRenderDistance = 100.0F * (float) val;
                if (scaledRenderDistance < 5.0F) {
                    scaledRenderDistance = 5.0F;
                }

                if (viewDistance > scaledRenderDistance) {
                    viewDistance = scaledRenderDistance;
                }

                value = viewDistance;
            }
        }

        original.call(instance, value);
    }

    @Unique
    private int animatium$getLightLevel(ClientLevel clientLevel, Entity entity) {
        final BlockPos blockPos = BlockPos.containing(entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ());
        return clientLevel.isLoaded(blockPos) ? animatium$getLightColor(clientLevel, blockPos) : 0;
    }

    @Unique
    private int animatium$getLightColor(BlockAndTintGetter blockAndTintGetter, BlockPos blockPos) {
        // TODO: LevelRenderer.getLightColor(blockAndTintGetter, blockPos);
        final int sky = blockAndTintGetter.getBrightness(LightLayer.SKY, blockPos);
        final int block = Math.max(blockAndTintGetter.getBrightness(LightLayer.BLOCK, blockPos), 0);
        return LightTexture.pack(block, sky);
    }
}
