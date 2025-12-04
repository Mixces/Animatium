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
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.Utils;

@Mixin(AtmosphericFogEnvironment.class)
public abstract class MixinAtmosphericFogEnvironment_VoidFog {
    // TODO: Figure out if its supposed to be a instant void effect
    @Definition(id = "environmentalEnd", field = "Lnet/minecraft/client/renderer/fog/FogData;environmentalEnd:F")
    @Expression("?.environmentalEnd = @(?)")
    @ModifyExpressionValue(method = "setupFog", at = @At("MIXINEXTRAS:EXPRESSION"))
    private float animatium$voidFog(float original, FogData fogData, Entity entity, BlockPos pos, ClientLevel clientLevel, float renderDistance, DeltaTracker deltaTracker) {
        final boolean isCreative = entity instanceof Player player && player.isCreative();
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.voidFog && Utils.hasFog1_7(clientLevel) && !isCreative) {
            final double light = animatium$getSkyLevel(clientLevel, entity) / 16.0 + (Mth.lerp(deltaTracker.getGameTimeDeltaTicks(), entity.yOld, entity.yo) + 4.0) / 32.0;
            if (light < 1.0) {
                return Math.min(renderDistance, Math.max(100.0F * (float) (Math.pow(Math.max(light, 0.0), 2)), 5.0F));
            }
        }

        return original;
    }

    @Unique
    private int animatium$getSkyLevel(ClientLevel clientLevel, Entity entity) {
        final BlockPos blockPos = BlockPos.containing(entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ());
        return clientLevel.isLoaded(blockPos) ? clientLevel.getBrightness(LightLayer.SKY, blockPos) : 0;
    }
}
