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
import org.visuals.legacy.animatium.util.config.EntryBundle;
import org.visuals.legacy.animatium.util.enums.SneakAnimationSetting;

public class MovementConfigCategory extends Category {
	// (Movement) Cape
	public boolean capeMovement = true;
	public boolean disableCapeLean = false;
	public boolean capeSwingRotation = true;
	public boolean capeChestplateTranslation = true;
	public boolean capeSneakPosition = true;
	// (Movement) Other
	public SneakAnimationSetting sneakAnimation = SneakAnimationSetting.V1_7;
	public boolean fakeOldSneakEyeHeight = false;
	public boolean rotateBackwardsWalking = true;
	public boolean uncapBlockingHeadRotation = true;
	public boolean disableHeadRotationInterpolation = true;
	public boolean handViewBobbingMovement = true;
	public boolean deathLimbs = true;
	public boolean bowArmMovement = true;
	public boolean legacyDamageTilt = true;
	public boolean offsetHurtTime = true;

	public static ConfigCategory create(final MovementConfigCategory defaults, final MovementConfigCategory config) {
		final ConfigCategory.Builder category = ConfigCategory.createBuilder();
		category.name(Component.translatable("animatium.category.movement"));
		config.bundle().install(category, defaults, config);
		return category.build();
	}

	@Override
	public EntryBundle bundle() {
		final EntryBundle bundle = new EntryBundle(this, "movement");

		bundle.group((EntryBundle.Group) new EntryBundle.Group("cape")
				.booleanEntry("capeMovement")
				.booleanEntry("disableCapeLean")
				.booleanEntry("capeSwingRotation")
				.booleanEntry("capeChestplateTranslation")
				.booleanEntry("capeSneakPosition"));

		bundle.group((EntryBundle.Group) new EntryBundle.Group("other")
				.enumEntry("sneakAnimation", SneakAnimationSetting.class)
				.booleanEntry("fakeOldSneakEyeHeight")
				.booleanEntry("rotateBackwardsWalking")
				.booleanEntry("uncapBlockingHeadRotation")
				.booleanEntry("disableHeadRotationInterpolation")
				.booleanEntry("handViewBobbingMovement")
				.booleanEntry("deathLimbs")
				.booleanEntry("bowArmMovement")
				.booleanEntry("legacyDamageTilt")
				.booleanEntry("offsetHurtTime"));

		return bundle;
	}
}
