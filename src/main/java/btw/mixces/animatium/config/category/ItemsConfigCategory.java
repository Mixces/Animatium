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
 */

package btw.mixces.animatium.config.category;

import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.enums.FishingRodVersion;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;

public final class ItemsConfigCategory {
    public static ConfigCategory setup(AnimatiumConfig defaults, AnimatiumConfig config) {
        ConfigCategory.Builder category = ConfigCategory.createBuilder();
        category.name(Component.translatable("animatium.category.items"));

        // Fishing Rod
        {
            OptionGroup.Builder fishingRodGroup = OptionGroup.createBuilder();
            fishingRodGroup.name(Component.translatable("animatium.category.items.group.fishing_rod"));
            fishingRodGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.fishingRodTextureStackCheck"))
                    .description(OptionDescription.of(Component.translatable("animatium.fishingRodTextureStackCheck.description")))
                    .binding(
                            defaults.fishingRodTextureStackCheck,
                            () -> config.fishingRodTextureStackCheck,
                            (newVal) -> config.fishingRodTextureStackCheck = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            fishingRodGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.fishingRodLineInterpolation"))
                    .description(OptionDescription.of(Component.translatable("animatium.fishingRodLineInterpolation.description")))
                    .binding(
                            defaults.fishingRodLineInterpolation,
                            () -> config.fishingRodLineInterpolation,
                            (newVal) -> config.fishingRodLineInterpolation = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            fishingRodGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.noMoveFishingRodLine"))
                    .description(OptionDescription.of(Component.translatable("animatium.noMoveFishingRodLine.description")))
                    .binding(
                            defaults.noMoveFishingRodLine,
                            () -> config.noMoveFishingRodLine,
                            (newVal) -> config.noMoveFishingRodLine = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            fishingRodGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.fishingRodLinePositionThirdPerson"))
                    .description(OptionDescription.of(Component.translatable("animatium.fishingRodLinePositionThirdPerson.description")))
                    .binding(
                            defaults.fishingRodLinePositionThirdPerson,
                            () -> config.fishingRodLinePositionThirdPerson,
                            (newVal) -> config.fishingRodLinePositionThirdPerson = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            fishingRodGroup.option(
                    Option.<Boolean>createBuilder()
                            .name(Component.translatable("animatium.fishingRodLineThickness"))
                            .description(OptionDescription.of(Component.translatable("animatium.fishingRodLineThickness.description")))
                            .binding(
                                    defaults.fishingRodLineThickness,
                                    () -> config.fishingRodLineThickness,
                                    (newVal) -> config.fishingRodLineThickness = newVal)
                            .controller(TickBoxControllerBuilder::create)
                            .build());
            fishingRodGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.thinFishingRodLineThickness"))
                    .description(OptionDescription.of(Component.translatable("animatium.thinFishingRodLineThickness.description")))
                    .binding(
                            defaults.thinFishingRodLineThickness,
                            () -> config.thinFishingRodLineThickness,
                            (newVal) -> config.thinFishingRodLineThickness = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            fishingRodGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.stickModelWhenCastInThirdperson"))
                    .description(OptionDescription.of(Component.translatable("animatium.stickModelWhenCastInThirdperson.description")))
                    .binding(
                            defaults.stickModelWhenCastInThirdperson,
                            () -> config.stickModelWhenCastInThirdperson,
                            (newVal) -> config.stickModelWhenCastInThirdperson = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            category.group(fishingRodGroup.build());
        }

        // Fixes
        {
            OptionGroup.Builder itemFixesGroup = OptionGroup.createBuilder();
            itemFixesGroup.name(Component.translatable("animatium.category.items.group.item_fixes"));
            itemFixesGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.equipAnimationOnItemUse"))
                    .description(OptionDescription.of(Component.translatable("animatium.equipAnimationOnItemUse.description")))
                    .binding(
                            defaults.equipAnimationOnItemUse,
                            () -> config.equipAnimationOnItemUse,
                            (newVal) -> config.equipAnimationOnItemUse = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            itemFixesGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.itemUsageVisualInGUI"))
                    .description(OptionDescription.of(Component.translatable("animatium.itemUsageVisualInGUI.description")))
                    .binding(
                            defaults.itemUsageVisualInGUI,
                            () -> config.itemUsageVisualInGUI,
                            (newVal) -> config.itemUsageVisualInGUI = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            category.group(itemFixesGroup.build());
        }

        // Glint
        {
            OptionGroup.Builder glintGroup = OptionGroup.createBuilder();
            glintGroup.name(Component.translatable("animatium.category.items.group.glint"));
            glintGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.glintSpeed"))
                    .description(OptionDescription.of(Component.translatable("animatium.glintSpeed.description")))
                    .binding(
                            defaults.glintSpeed,
                            () -> config.glintSpeed,
                            (newVal) -> config.glintSpeed = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            glintGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.glintOnItemDrops2D"))
                    .description(OptionDescription.of(Component.translatable("animatium.glintOnItemDrops2D.description")))
                    .binding(
                            defaults.glintOnItemDrops2D,
                            () -> config.glintOnItemDrops2D,
                            (newVal) -> config.glintOnItemDrops2D = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            glintGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.glintOnItemFramed2D"))
                    .description(OptionDescription.of(Component.translatable("animatium.glintOnItemFramed2D.description")))
                    .binding(
                            defaults.glintOnItemFramed2D,
                            () -> config.glintOnItemFramed2D,
                            (newVal) -> config.glintOnItemFramed2D = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            category.group(glintGroup.build());
        }

        // 2d Drops
        {
            OptionGroup.Builder drops2dGroup = OptionGroup.createBuilder();
            drops2dGroup.name(Component.translatable("animatium.category.items.group.2d_drops"));
            drops2dGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.itemDropsFaceCamera"))
                    .description(OptionDescription.of(Component.translatable("animatium.itemDropsFaceCamera.description")))
                    .binding(
                            defaults.itemDropsFaceCamera,
                            () -> config.itemDropsFaceCamera,
                            (newVal) -> config.itemDropsFaceCamera = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            drops2dGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.itemDropsFaceCameraRotationFix"))
                    .description(OptionDescription.of(Component.translatable("animatium.itemDropsFaceCameraRotationFix.description")))
                    .binding(
                            defaults.itemDropsFaceCameraRotationFix,
                            () -> config.itemDropsFaceCameraRotationFix,
                            (newVal) -> config.itemDropsFaceCameraRotationFix = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            drops2dGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.itemDrops2D"))
                    .description(OptionDescription.of(Component.translatable("animatium.itemDrops2D.description")))
                    .binding(
                            defaults.itemDrops2D,
                            () -> config.itemDrops2D,
                            (newVal) -> config.itemDrops2D = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            drops2dGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.itemFramed2D"))
                    .description(OptionDescription.of(Component.translatable("animatium.itemFramed2D.description")))
                    .binding(
                            defaults.itemFramed2D,
                            () -> config.itemFramed2D,
                            (newVal) -> config.itemFramed2D = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            drops2dGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.itemColors2D"))
                    .description(OptionDescription.of(Component.translatable("animatium.itemColors2D.description")))
                    .binding(
                            defaults.itemColors2D,
                            () -> config.itemColors2D,
                            (newVal) -> config.itemColors2D = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            category.group(drops2dGroup.build());
        }

        // 2d Drops
        {
            OptionGroup.Builder transformationsGroup = OptionGroup.createBuilder();
            transformationsGroup.name(Component.translatable("animatium.category.items.group.transformations"));
            transformationsGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.itemPositions"))
                    .description(OptionDescription.of(Component.translatable("animatium.itemPositions.description")))
                    .binding(
                            defaults.itemPositions,
                            () -> config.itemPositions,
                            (newVal) -> config.itemPositions = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            transformationsGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.itemPositionsInThirdPerson"))
                    .description(OptionDescription.of(Component.translatable("animatium.itemPositionsInThirdPerson.description")))
                    .binding(
                            defaults.itemPositionsInThirdPerson,
                            () -> config.itemPositionsInThirdPerson,
                            (newVal) -> config.itemPositionsInThirdPerson = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            transformationsGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.thinBlockPositions"))
                    .description(OptionDescription.of(Component.translatable("animatium.thinBlockPositions.description")))
                    .binding(
                            defaults.thinBlockPositions,
                            () -> config.thinBlockPositions,
                            (newVal) -> config.thinBlockPositions = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            transformationsGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.skullPosition"))
                    .description(OptionDescription.of(Component.translatable("animatium.skullPosition.description")))
                    .binding(
                            defaults.skullPosition,
                            () -> config.skullPosition,
                            (newVal) -> config.skullPosition = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            transformationsGroup.option(Option.<FishingRodVersion>createBuilder()
                    .name(Component.translatable("animatium.fishingRodVersion"))
                    .description(OptionDescription.of(Component.translatable("animatium.fishingRodVersion.description")))
                    .binding(
                            defaults.fishingRodVersion,
                            () -> config.fishingRodVersion,
                            (newVal) -> config.fishingRodVersion = newVal)
                    .controller((opt) ->
                            EnumControllerBuilder.create(opt)
                                    .enumClass(FishingRodVersion.class)
                                    .formatValue((it) -> Component.translatable("animatium.enum.FishingRodVersion." + it.name())))
                    .build());
            category.group(transformationsGroup.build());
        }

        // Other
        {
            OptionGroup.Builder otherGroup = OptionGroup.createBuilder();
            otherGroup.name(Component.translatable("animatium.category.items.group.other"));
            otherGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.itemUsageSwinging"))
                    .description(OptionDescription.of(Component.translatable("animatium.itemUsageSwinging.description")))
                    .binding(
                            defaults.itemUsageSwinging,
                            () -> config.itemUsageSwinging,
                            (newVal) -> config.itemUsageSwinging = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            otherGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.swingOnUse"))
                    .description(OptionDescription.of(Component.translatable("animatium.swingOnUse.description")))
                    .binding(
                            defaults.swingOnUse,
                            () -> config.swingOnUse,
                            (newVal) -> config.swingOnUse = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            otherGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.swingOnDrop"))
                    .description(OptionDescription.of(Component.translatable("animatium.swingOnDrop.description")))
                    .binding(
                            defaults.swingOnDrop,
                            () -> config.swingOnDrop,
                            (newVal) -> config.swingOnDrop = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            otherGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.swingOnEntityInteract"))
                    .description(OptionDescription.of(Component.translatable("animatium.swingOnEntityInteract.description")))
                    .binding(
                            defaults.swingOnEntityInteract,
                            () -> config.swingOnEntityInteract,
                            (newVal) -> config.swingOnEntityInteract = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            otherGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.itemUsingTextureInGui"))
                    .description(OptionDescription.of(Component.translatable("animatium.itemUsingTextureInGui.description")))
                    .binding(
                            defaults.itemUsingTextureInGui,
                            () -> config.itemUsingTextureInGui,
                            (newVal) -> config.itemUsingTextureInGui = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            otherGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.durabilityBarColors"))
                    .description(OptionDescription.of(Component.translatable("animatium.durabilityBarColors.description")))
                    .binding(
                            defaults.durabilityBarColors,
                            () -> config.durabilityBarColors,
                            (newVal) -> config.durabilityBarColors = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            otherGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.itemRarities"))
                    .description(OptionDescription.of(Component.translatable("animatium.itemRarities.description")))
                    .binding(
                            defaults.itemRarities,
                            () -> config.itemRarities,
                            (newVal) -> config.itemRarities = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            otherGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.heldItemVisibilityInBoat"))
                    .description(OptionDescription.of(Component.translatable("animatium.heldItemVisibilityInBoat.description")))
                    .binding(
                            defaults.heldItemVisibilityInBoat,
                            () -> config.heldItemVisibilityInBoat,
                            (newVal) -> config.heldItemVisibilityInBoat = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            otherGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.itemPickupPosition"))
                    .description(OptionDescription.of(Component.translatable("animatium.itemPickupPosition.description")))
                    .binding(
                            defaults.itemPickupPosition,
                            () -> config.itemPickupPosition,
                            (newVal) -> config.itemPickupPosition = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            category.group(otherGroup.build());
        }

        return category.build();
    }
}