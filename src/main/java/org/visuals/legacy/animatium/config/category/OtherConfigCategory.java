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

package org.visuals.legacy.animatium.config.category;

import dev.isxander.yacl3.api.ConfigCategory;
import net.minecraft.network.chat.Component;
import org.visuals.legacy.animatium.util.compatibility.Mods;
import org.visuals.legacy.animatium.util.config.EntryBundle;
import org.visuals.legacy.animatium.util.enums.VoidFogSetting;

public class OtherConfigCategory extends Category {
	// Sky
	public boolean blueVoidSky = true;
	public boolean planarSkyFog = true;
	public boolean cloudHeight = true;
	public boolean playerVoidBox = true;
	// Other
	public boolean thirdPersonSwordBlockingPosition = true;
	public boolean lockBlockingArmRotation = true;
	public boolean projectileAgeCheck = false;
	public boolean blockMiningProgress = true;
	public boolean disableInventoryEntityScissor = false;
	public boolean blockOutlineRendering = true;
	public boolean disableModelWhilstSleeping = false;
	public boolean entityArmorHurtTint = true;
	public boolean itemGlintOnEntity = true;
	public boolean maxGlintProperties = true;
	public boolean armorHurtRendering = false;
	public boolean highAttackSpeedVisual = false;
	public boolean disableEntityGlowOutline = false;
	public boolean disableModernCombatSounds = true;
	public boolean disableModernCombatParticles = true;
	public boolean restoreParticleBlending = true;
	public boolean heldItemArmLogic = true;
	public boolean flameDimensions = true;
	public boolean flameOffset = true;
	public boolean persistentBlockOutline = false;
	public boolean oldCloudRendering = true;
	public boolean oldFallParticlePhysics = true;
	public boolean fastGrass = true;
	public boolean oldY0Height = false;
	public boolean oldWaterOverlayOpacity = true;
	public boolean oldWaterColorFog = true;
	public boolean disableRandomBlockRotations = true;
	public VoidFogSetting voidFog = VoidFogSetting.BOTH;

	public static ConfigCategory create(final OtherConfigCategory defaults, final OtherConfigCategory config) {
		final ConfigCategory.Builder category = ConfigCategory.createBuilder();
		category.name(Component.translatable("animatium.category.other"));
		config.bundle().install(category, defaults, config);
		return category.build();
	}

	@Override
	public EntryBundle bundle() {
		final EntryBundle bundle = new EntryBundle(this, "other");

		bundle.group((EntryBundle.Group) new EntryBundle.Group("sky")
				.booleanEntry("blueVoidSky")
				.booleanEntry("planarSkyFog")
				.booleanEntry("cloudHeight")
				.booleanEntry("playerVoidBox"));

		bundle.booleanEntry("thirdPersonSwordBlockingPosition");
		bundle.booleanEntry("lockBlockingArmRotation");
		bundle.booleanEntry("projectileAgeCheck");
		bundle.booleanEntry("blockMiningProgress");
		bundle.booleanEntry("disableInventoryEntityScissor");
		bundle.booleanEntry("blockOutlineRendering");
		bundle.booleanEntry("disableModelWhilstSleeping");
		if (!Mods.HAS_LUNAR_CLIENT) {
			bundle.booleanEntry("entityArmorHurtTint");
		}

		bundle.booleanEntry("itemGlintOnEntity");
		bundle.booleanEntry("maxGlintProperties");
		bundle.booleanEntry("armorHurtRendering");
		bundle.booleanEntry("highAttackSpeedVisual");
		bundle.booleanEntry("disableEntityGlowOutline");
		bundle.booleanEntry("disableModernCombatSounds");
		bundle.booleanEntry("disableModernCombatParticles");
		bundle.booleanEntry("restoreParticleBlending");
		bundle.booleanEntry("heldItemArmLogic");
		bundle.booleanEntry("flameDimensions");
		bundle.booleanEntry("flameOffset");
		bundle.booleanEntry("persistentBlockOutline");
		bundle.booleanEntry("oldCloudRendering");
		bundle.booleanEntry("oldFallParticlePhysics");
		bundle.booleanEntry("fastGrass");
		bundle.booleanEntry("oldY0Height");
		bundle.booleanEntry("oldWaterOverlayOpacity");
		bundle.booleanEntry("oldWaterColorFog");
		bundle.booleanEntry("disableRandomBlockRotations");
		bundle.enumEntry("voidFog", VoidFogSetting.class);

		return bundle;
	}
}
