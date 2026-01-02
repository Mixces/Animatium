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

package org.visuals.legacy.animatium.mixins.v1.rendering.fog;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.environment.WaterFogEnvironment;
import net.minecraft.util.ARGB;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(WaterFogEnvironment.class)
public abstract class MixinWaterFogEnvironment_OldWaterColor {
	@WrapOperation(method = "getBaseColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getWaterFogColor()I"))
	private int animatium$oldWaterFogColor(Biome instance, Operation<Integer> original, ClientLevel level, Camera camera, int renderDistance, float partialTick) {
		if (Animatium.isEnabled() && AnimatiumConfig.instance().other.oldWaterColorFog) {
			float value = 0.0F;
			if (camera.getEntity() instanceof LivingEntity livingEntity) {
				value = EnchantmentHelper.getEnchantmentLevel(level.registryAccess().getOrThrow(Enchantments.RESPIRATION), livingEntity) * 0.2F;
				if (livingEntity.hasEffect(MobEffects.WATER_BREATHING)) {
					value *= 0.9F;
				}
			}

			return ARGB.colorFromFloat(1.0F, 0.02F + value, 0.02F + value, 0.2F + value);
		} else {
			return original.call(instance);
		}
	}
}
