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

package org.visuals.legacy.animatium.mixins.v1.general.combat.sounds;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

import java.util.List;

@Mixin(SoundManager.class)
public abstract class MixinSoundManager_DisableModernCombatSounds {
    @Unique
    private static final List<ResourceLocation> animatium$ignoreSounds = List.of(
            SoundEvents.PLAYER_ATTACK_KNOCKBACK.location(),
            SoundEvents.PLAYER_ATTACK_SWEEP.location(),
            SoundEvents.PLAYER_ATTACK_CRIT.location(),
            SoundEvents.PLAYER_ATTACK_STRONG.location(),
            SoundEvents.PLAYER_ATTACK_WEAK.location(),
            SoundEvents.PLAYER_ATTACK_NODAMAGE.location()
    );

    @WrapOperation(method = "play", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundEngine;play(Lnet/minecraft/client/resources/sounds/SoundInstance;)Lnet/minecraft/client/sounds/SoundEngine$PlayResult;"))
    private SoundEngine.PlayResult animatium$modernCombatSounds(SoundEngine instance, SoundInstance sound, Operation<SoundEngine.PlayResult> original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.disableModernCombatSounds && animatium$ignoreSounds.contains(sound.getLocation())) {
            return null;
        } else {
            return original.call(instance, sound);
        }
    }
}
