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

package org.visuals.legacy.animatium.util;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import org.visuals.legacy.animatium.Animatium;

import java.util.Optional;

public final class SuperSecretButton extends Button {
	private static final TagKey<SoundEvent> SUPER_SECRET_SOUNDS = TagKey.create(Registries.SOUND_EVENT, Animatium.location("super_secret_sounds"));

	public SuperSecretButton(Component message, OnPress onPress, int x, int y) {
		super(x, y, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, message, onPress, ignored -> Component.empty());
	}

	@Override
	public void playDownSound(SoundManager soundManager) {
		// TODO: Get the game to pre-load the tag so it'll play when button is pressed upon launch (if haven't gone into world yet)
		final Optional<Holder<SoundEvent>> sound = BuiltInRegistries.SOUND_EVENT.getRandomElementOf(SUPER_SECRET_SOUNDS, RandomSource.create());
		sound.ifPresent(holder -> soundManager.play(SimpleSoundInstance.forUI(holder, 0.5F)));
	}
}
