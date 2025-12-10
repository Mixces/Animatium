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

public class ExtrasConfigCategory extends Category {
	public boolean minimalViewBobbing = false;
	public boolean showNameTagInThirdPerson = false;
	public boolean hideNameTagBackground = false;
	public boolean nameTagTextShadow = false;
	public boolean debugHudTextColor = false;
	public boolean offhandUsageSwinging = false;
	public boolean alwaysUsageSwing = false;
	public boolean alwaysSharpParticles = false;
	public boolean disableRecipeAndTutorialToasts = false;
	public boolean showArmWhileInvisible = false;
	public boolean fakeMissPenaltySwing = false;
	public boolean dontMoveBlueVoid = false;
	public boolean disableEntityDeathTopple = false;
	public boolean deepRedHurtTint = false;
	public boolean disableParticlePhysics = false;
	public boolean disableFirstPersonParticles = false;
	public boolean dontClearChat = false;
	public boolean dontCloseChat = false;
	public boolean oldWaterColorEffects = false;
	// TODO: For 3.1
	// public float itemSwingSpeed = 0.0F;
	// public float hasteSwingSpeed = 0.0F;
	// public float miningFatigueSwingSpeed = 0.0F;
	// public boolean ignoreHasteSpeed = false;
	// public boolean ignoreMiningFatigueSpeed = false;

	public static ConfigCategory create(final ExtrasConfigCategory defaults, final ExtrasConfigCategory config) {
		final ConfigCategory.Builder category = ConfigCategory.createBuilder();
		category.name(Component.translatable("animatium.category.extras"));
		category.option(booleanOption("minimalViewBobbing", defaults, config));
		category.option(booleanOption("showNameTagInThirdPerson", defaults, config));
		category.option(booleanOption("hideNameTagBackground", defaults, config));
		category.option(booleanOption("nameTagTextShadow", defaults, config));
		category.option(booleanOption("debugHudTextColor", defaults, config));
		category.option(booleanOption("offhandUsageSwinging", defaults, config));
		category.option(booleanOption("alwaysUsageSwing", defaults, config));
		category.option(booleanOption("alwaysSharpParticles", defaults, config));
		if (!Mods.HAS_SODIUM_EXTRAS) {
			category.option(booleanOption("disableRecipeAndTutorialToasts", defaults, config));
		}

		category.option(booleanOption("showArmWhileInvisible", defaults, config));
		category.option(booleanOption("fakeMissPenaltySwing", defaults, config));
		category.option(booleanOption("dontMoveBlueVoid", defaults, config));
		category.option(booleanOption("disableEntityDeathTopple", defaults, config));
		category.option(booleanOption("deepRedHurtTint", defaults, config));
		category.option(booleanOption("disableParticlePhysics", defaults, config));
		category.option(booleanOption("disableFirstPersonParticles", defaults, config));
		category.option(booleanOption("dontClearChat", defaults, config));
		category.option(booleanOption("dontCloseChat", defaults, config));
		category.option(booleanOption("oldWaterColorEffects", defaults, config));

		// TODO: For 3.1
		// {
		//     final OptionGroup.Builder itemSwingCategory = OptionGroup.createBuilder();
		//     itemSwingCategory.name(Component.translatable("animatium.category.extras.item_swing"));
		//     itemSwingCategory.option(floatSliderOption("itemSwingSpeed", defaults, config, -1.0F, 1.0F, 0.1F));
		//     itemSwingCategory.option(floatSliderOption("hasteSwingSpeed", defaults, config, -1.0F, 1.0F, 0.1F));
		//     itemSwingCategory.option(floatSliderOption("miningFatigueSwingSpeed", defaults, config, -1.0F, 1.0F, 0.1F));
		//     itemSwingCategory.option(booleanOption("ignoreHasteSpeed", defaults, config));
		//     itemSwingCategory.option(booleanOption("ignoreMiningFatigueSpeed", defaults, config));
		//     category.group(itemSwingCategory.build());
		// }

		return category.build();
	}
}
