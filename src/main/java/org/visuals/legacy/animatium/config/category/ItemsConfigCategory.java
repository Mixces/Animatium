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
import org.visuals.legacy.animatium.util.enums.FishingRodVersion;

public class ItemsConfigCategory extends Category {
    // (Items) Fishing Rod
    public boolean fishingRodTextureStackCheck = true;
    public boolean fishingRodLineInterpolation = true;
    public boolean noMoveFishingRodLine = false;
    public boolean fishingRodLinePositionThirdPerson = true;
    public boolean fishingRodLineThickness = true;
    public boolean thinFishingRodLineThickness = false;
    public boolean stickModelWhenCastInThirdperson = true;
    // (Items) Enchantment Glint
    public boolean legacyGlintSpeed = true;
    public boolean glintOnItemDrops2D = true;
    public boolean glintOnItemFramed2D = true;
    // (Items) 2D Drops
    public boolean itemDropsFaceCamera = true;
    public boolean itemDropsFaceCameraRotationFix = false;
    public boolean itemDrops2D = true;
    public boolean itemFramed2D = true;
    public boolean itemColors2D = true;
    // (Items) Transformations
    public boolean itemPositions = true;
    public boolean itemPositionsInThirdPerson = true;
    public boolean thinBlockPositions = true;
    public boolean skullPosition = true;
    public FishingRodVersion fishingRodVersion = FishingRodVersion.V1_7;
    // (Items) Other
    public boolean itemUsageSwinging = true;
    public boolean swingOnUse = false;
    public boolean swingOnDrop = false;
    public boolean swingOnEntityInteract = false;
    public boolean itemUsingTextureInGui = true;
    public boolean durabilityBarColors = true;
    public boolean legacyItemRarities = true;
    public boolean heldItemVisibilityInBoat = true;
    public boolean itemPickupPosition = true;
    public boolean mobHeadIcons = true;

    public static ConfigCategory create(final ItemsConfigCategory defaults, final ItemsConfigCategory config) {
        final ConfigCategory.Builder category = ConfigCategory.createBuilder();
        category.name(Component.translatable("animatium.category.items"));

        // Fishing Rod
        {
            final OptionGroup.Builder fishingRodGroup = OptionGroup.createBuilder();
            fishingRodGroup.name(Component.translatable("animatium.category.items.group.fishing_rod"));
            fishingRodGroup.option(booleanOption("fishingRodTextureStackCheck", defaults, config));
            fishingRodGroup.option(booleanOption("fishingRodLineInterpolation", defaults, config));
            fishingRodGroup.option(booleanOption("noMoveFishingRodLine", defaults, config));
            fishingRodGroup.option(booleanOption("fishingRodLinePositionThirdPerson", defaults, config));
            fishingRodGroup.option(booleanOption("fishingRodLineThickness", defaults, config));
            fishingRodGroup.option(booleanOption("thinFishingRodLineThickness", defaults, config));
            fishingRodGroup.option(booleanOption("stickModelWhenCastInThirdperson", defaults, config));
            category.group(fishingRodGroup.build());
        }

        // Glint
        {
            final OptionGroup.Builder glintGroup = OptionGroup.createBuilder();
            glintGroup.name(Component.translatable("animatium.category.items.group.glint"));
            glintGroup.option(booleanOption("legacyGlintSpeed", defaults, config));
            glintGroup.option(booleanOption("glintOnItemDrops2D", defaults, config));
            glintGroup.option(booleanOption("glintOnItemFramed2D", defaults, config));
            category.group(glintGroup.build());
        }

        // 2D Drops
        {
            final OptionGroup.Builder drops2dGroup = OptionGroup.createBuilder();
            drops2dGroup.name(Component.translatable("animatium.category.items.group.2d_drops"));
            drops2dGroup.option(booleanOption("itemDropsFaceCamera", defaults, config));
            drops2dGroup.option(booleanOption("itemDropsFaceCameraRotationFix", defaults, config));
            // TODO: drops2dGroup.option(booleanOption("itemDrops2D", defaults, config));
            drops2dGroup.option(booleanOption("itemFramed2D", defaults, config));
            drops2dGroup.option(booleanOption("itemColors2D", defaults, config));
            category.group(drops2dGroup.build());
        }

        // 2d Drops
        {
            final OptionGroup.Builder transformationsGroup = OptionGroup.createBuilder();
            transformationsGroup.name(Component.translatable("animatium.category.items.group.transformations"));
            transformationsGroup.option(booleanOption("itemPositions", defaults, config));
            transformationsGroup.option(booleanOption("itemPositionsInThirdPerson", defaults, config));
            transformationsGroup.option(booleanOption("thinBlockPositions", defaults, config));
            transformationsGroup.option(booleanOption("skullPosition", defaults, config));
            transformationsGroup.option(enumOption("fishingRodVersion", defaults, config, FishingRodVersion.class));
            category.group(transformationsGroup.build());
        }

        // Other
        {
            final OptionGroup.Builder otherGroup = OptionGroup.createBuilder();
            otherGroup.name(Component.translatable("animatium.category.items.group.other"));
            otherGroup.option(booleanOption("itemUsageSwinging", defaults, config));
            otherGroup.option(booleanOption("swingOnUse", defaults, config));
            otherGroup.option(booleanOption("swingOnDrop", defaults, config));
            otherGroup.option(booleanOption("swingOnEntityInteract", defaults, config));
            otherGroup.option(booleanOption("itemUsingTextureInGui", defaults, config));
            otherGroup.option(booleanOption("durabilityBarColors", defaults, config));
            otherGroup.option(booleanOption("legacyItemRarities", defaults, config));
            otherGroup.option(booleanOption("heldItemVisibilityInBoat", defaults, config));
            otherGroup.option(booleanOption("itemPickupPosition", defaults, config));
            otherGroup.option(booleanOption("mobHeadIcons", defaults, config));
            category.group(otherGroup.build());
        }

        return category.build();
    }
}