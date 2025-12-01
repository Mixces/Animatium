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

public class FixesConfigCategory extends Category {
    public boolean fixSneakingFeetPosition = true;
    public boolean fixMirrorArmSwing = true;
    public boolean fixOffHandUsingPose = true;
    public boolean fixCastLineCheck = true;
    public boolean fixCastLineSwing = true;
    public boolean fixEquipAnimationItemCheck = true;
    public boolean fixFireballClientsideVisual = true;
    public boolean fixTextStrikethroughStyle = true;
    public boolean fixHighAttackSpeedIndicator = true;
    public boolean fixVerticalBobbingTilt = true;
    public boolean upMinPixelTransparencyLimit = true;
    public boolean fixEquipAnimationOnItemUse = true;
    public boolean fixItemUsageVisualInGUI = true;
    public boolean fixDoubleUsageVisual = true;

    public static ConfigCategory create(final FixesConfigCategory defaults, final FixesConfigCategory config) {
        final ConfigCategory.Builder category = ConfigCategory.createBuilder();
        category.name(Component.translatable("animatium.category.fixes"));
        category.option(booleanOption("fixSneakingFeetPosition", defaults, config));
        category.option(booleanOption("fixMirrorArmSwing", defaults, config));
        category.option(booleanOption("fixOffHandUsingPose", defaults, config));
        category.option(booleanOption("fixCastLineCheck", defaults, config));
        category.option(booleanOption("fixCastLineSwing", defaults, config));
        category.option(booleanOption("fixEquipAnimationItemCheck", defaults, config));
        category.option(booleanOption("fixFireballClientsideVisual", defaults, config));
        if (!Mods.HAS_VIAFABRICPLUS) {
            category.option(booleanOption("fixTextStrikethroughStyle", defaults, config));
        }

        category.option(booleanOption("fixHighAttackSpeedIndicator", defaults, config));
        category.option(booleanOption("fixVerticalBobbingTilt", defaults, config));
        category.option(booleanOption("upMinPixelTransparencyLimit", defaults, config));
        category.option(booleanOption("fixEquipAnimationOnItemUse", defaults, config));
        category.option(booleanOption("fixItemUsageVisualInGUI", defaults, config));
        category.option(booleanOption("fixDoubleUsageVisual", defaults, config));
        return category.build();
    }
}
