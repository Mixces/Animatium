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
import dev.isxander.yacl3.api.OptionGroup;
import net.minecraft.network.chat.Component;

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
    public boolean modelWhilstSleeping = false;
    public boolean entityArmorHurtTint = true;
    public boolean itemGlintOnEntity = true;
    public boolean maxGlintProperties = true;
    public boolean armorHurtRendering = false;
    public boolean highAttackSpeedVisual = false;
    public boolean disableEntityGlowOutline = false;
    public boolean modernCombatSounds = true;
    public boolean modernCombatParticles = true; // TODO/NOTE: Fix, it's broken on servers
    public boolean restoreParticleBlending = true;
    public boolean heldItemArmLogic = true;
    public boolean flameDimensions = true;
    public boolean flameOffset = true;
    public boolean persistentBlockOutline = false;
    public boolean oldMinimumSmoothLighting = true;
    public boolean oldCloudRendering = true;
    public boolean oldWindowIcon = true;
    public boolean voidParticles = true;
    public boolean voidFog = true;
    public boolean oldFallParticlePhysics = true;
    public boolean alwaysSteveModel = true;
    public boolean fastGrass = true;
    public boolean oldY0Height = true;
    public boolean disableRandomBlockRotations = true;

    public static ConfigCategory create(final OtherConfigCategory defaults, final OtherConfigCategory config) {
        final ConfigCategory.Builder category = ConfigCategory.createBuilder();
        category.name(Component.translatable("animatium.category.other"));

        {
            final OptionGroup.Builder skyGroup = OptionGroup.createBuilder();
            skyGroup.name(Component.translatable("animatium.category.other.group.sky"));
            skyGroup.option(booleanOption("blueVoidSky", defaults, config));
            skyGroup.option(booleanOption("planarSkyFog", defaults, config));
            skyGroup.option(booleanOption("cloudHeight", defaults, config));
            skyGroup.option(booleanOption("playerVoidBox", defaults, config));
            category.group(skyGroup.build());
        }

        category.option(booleanOption("thirdPersonSwordBlockingPosition", defaults, config));
        category.option(booleanOption("lockBlockingArmRotation", defaults, config));
        category.option(booleanOption("projectileAgeCheck", defaults, config));
        category.option(booleanOption("blockMiningProgress", defaults, config));
        category.option(booleanOption("disableInventoryEntityScissor", defaults, config));
        category.option(booleanOption("blockOutlineRendering", defaults, config));
        category.option(booleanOption("modelWhilstSleeping", defaults, config));
        category.option(booleanOption("entityArmorHurtTint", defaults, config));
        category.option(booleanOption("itemGlintOnEntity", defaults, config));
        category.option(booleanOption("maxGlintProperties", defaults, config));
        category.option(booleanOption("armorHurtRendering", defaults, config));
        category.option(booleanOption("highAttackSpeedVisual", defaults, config));
        category.option(booleanOption("disableEntityGlowOutline", defaults, config));
        category.option(booleanOption("modernCombatSounds", defaults, config));
        category.option(booleanOption("modernCombatParticles", defaults, config));
        category.option(booleanOption("restoreParticleBlending", defaults, config));
        // TODO: category.option(booleanOption("heldItemArmLogic", defaults, config));
        category.option(booleanOption("flameDimensions", defaults, config));
        category.option(booleanOption("flameOffset", defaults, config));
        category.option(booleanOption("persistentBlockOutline", defaults, config));
        category.option(booleanOption("oldMinimumSmoothLighting", defaults, config));
        category.option(booleanOption("oldCloudRendering", defaults, config));
        category.option(booleanOption("oldWindowIcon", defaults, config));
        category.option(booleanOption("voidParticles", defaults, config));
        category.option(booleanOption("voidFog", defaults, config));
        category.option(booleanOption("oldFallParticlePhysics", defaults, config));
        category.option(booleanOption("alwaysSteveModel", defaults, config));
        category.option(booleanOption("fastGrass", defaults, config));
        category.option(booleanOption("oldY0Height", defaults, config));
        category.option(booleanOption("disableRandomBlockRotations", defaults, config));
        return category.build();
    }
}
