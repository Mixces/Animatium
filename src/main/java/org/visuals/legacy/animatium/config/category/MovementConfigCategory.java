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

public class MovementConfigCategory extends Category {
    // (Movement) Sneaking
    public boolean smoothSneaking = true;
    public boolean sneakAnimationInterpolation = true;
    public boolean fakeOldSneakEyeHeight = false;
    public boolean sneakingFeetPosition = true;
    public boolean syncPlayerModelWithEyeHeight = true;
    public boolean sneakAnimationWhileFlying = true;
    // (Movement) Cape
    public boolean capeMovement = true;
    public boolean disableCapeLean = false;
    public boolean capeSwingRotation = true;
    public boolean capeChestplateTranslation = true;
    public boolean capeSneakPosition = true;
    // (Movement) Other
    public boolean rotateBackwardsWalking = true;
    public boolean uncapBlockingHeadRotation = true;
    public boolean disableHeadRotationInterpolation = true;
    public boolean handViewBobbingMovement = true;
    public boolean deathLimbs = true;
    public boolean bowArmMovement = true;
    public boolean legacyDamageTilt = true;
    public boolean offsetHurtTime = true;

    public static ConfigCategory setup(final MovementConfigCategory defaults, final MovementConfigCategory config) {
        final ConfigCategory.Builder category = ConfigCategory.createBuilder();
        category.name(Component.translatable("animatium.category.movement"));

        {
            final OptionGroup.Builder sneakingGroup = OptionGroup.createBuilder();
            sneakingGroup.name(Component.translatable("animatium.category.movement.group.sneaking"));
            sneakingGroup.option(booleanOption("smoothSneaking", defaults, config));
            sneakingGroup.option(booleanOption("sneakAnimationInterpolation", defaults, config));
            sneakingGroup.option(booleanOption("fakeOldSneakEyeHeight", defaults, config));
            sneakingGroup.option(booleanOption("sneakingFeetPosition", defaults, config));
            sneakingGroup.option(booleanOption("syncPlayerModelWithEyeHeight", defaults, config));
            sneakingGroup.option(booleanOption("sneakAnimationWhileFlying", defaults, config));
            category.group(sneakingGroup.build());
        }

        {
            final OptionGroup.Builder capeGroup = OptionGroup.createBuilder();
            capeGroup.name(Component.translatable("animatium.category.movement.group.cape"));
            capeGroup.option(booleanOption("capeMovement", defaults, config));
            capeGroup.option(booleanOption("disableCapeLean", defaults, config));
            capeGroup.option(booleanOption("capeSwingRotation", defaults, config));
            capeGroup.option(booleanOption("capeChestplateTranslation", defaults, config));
            capeGroup.option(booleanOption("capeSneakPosition", defaults, config));
            category.group(capeGroup.build());
        }

        {
            final OptionGroup.Builder otherGroup = OptionGroup.createBuilder();
            otherGroup.name(Component.translatable("animatium.category.movement.group.other"));
            otherGroup.option(booleanOption("rotateBackwardsWalking", defaults, config));
            otherGroup.option(booleanOption("uncapBlockingHeadRotation", defaults, config));
            otherGroup.option(booleanOption("disableHeadRotationInterpolation", defaults, config));
            otherGroup.option(booleanOption("handViewBobbingMovement", defaults, config));
            otherGroup.option(booleanOption("deathLimbs", defaults, config));
            otherGroup.option(booleanOption("bowArmMovement", defaults, config));
            otherGroup.option(booleanOption("legacyDamageTilt", defaults, config));
            otherGroup.option(booleanOption("offsetHurtTime", defaults, config));
            category.group(otherGroup.build());
        }

        return category.build();
    }
}
