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

package org.visuals.legacy.animatium.mixins.v1.entity.particles;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(Player.class)
public abstract class MixinPlayer_AlwaysSharpParticles {
	@Shadow
	public abstract void magicCrit(final Entity entityHit);

	@Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;skipAttackInteraction(Lnet/minecraft/world/entity/Entity;)Z", shift = At.Shift.AFTER))
	private void animatium$alwaysSharpParticles(final Entity target, final CallbackInfo ci) {
		if (Animatium.isEnabled() && AnimatiumConfig.instance().extras.alwaysSharpParticles) {
			this.magicCrit(target);
		}
	}

	@WrapWithCondition(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;magicCrit(Lnet/minecraft/world/entity/Entity;)V"))
	private boolean animatium$disableVanillaCrit(final Player instance, final Entity entityHit) {
		return !Animatium.isEnabled() || !AnimatiumConfig.instance().extras.alwaysSharpParticles;
	}
}
