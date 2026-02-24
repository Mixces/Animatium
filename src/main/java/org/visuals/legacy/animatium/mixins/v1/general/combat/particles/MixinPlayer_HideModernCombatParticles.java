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

package org.visuals.legacy.animatium.mixins.v1.general.combat.particles;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(Player.class)
public abstract class MixinPlayer_HideModernCombatParticles {
	@WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"))
	private int animatium$modernCombatParticles$damageIndicator(final ServerLevel instance, final ParticleOptions particleOptions, final double posX, final double posY, final double posZ, final int particleCount, final double xOffset, final double yOffset, final double zOffset, final double speed, final Operation<Integer> original) {
		if (Animatium.isEnabled() && AnimatiumConfig.instance().other.disableModernCombatParticles) {
			return 0;
		} else {
			return original.call(instance, particleOptions, posX, posY, posZ, particleCount, xOffset, yOffset, zOffset, speed);
		}
	}

	@WrapWithCondition(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;sweepAttack()V"))
	private boolean animatium$modernCombatParticles$sweep(final Player instance) {
		return !Animatium.isEnabled() || !AnimatiumConfig.instance().other.disableModernCombatParticles;
	}
}
