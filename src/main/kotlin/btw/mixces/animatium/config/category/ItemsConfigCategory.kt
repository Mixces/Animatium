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

package btw.mixces.animatium.config.category

import btw.mixces.animatium.config.AnimatiumConfig
import btw.mixces.animatium.util.FishingRodVersion
import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.OptionGroup
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.api.controller.EnumControllerBuilder
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import net.minecraft.network.chat.Component

object ItemsConfigCategory {
    fun setup(builder: YetAnotherConfigLib.Builder, defaults: AnimatiumConfig, config: AnimatiumConfig) {
        val category = ConfigCategory.createBuilder()
        category.name(Component.translatable("animatium.category.items"))

        // Fishing Rod
        run {
            val fishingRodGroup = OptionGroup.createBuilder()
            fishingRodGroup.name(Component.translatable("animatium.category.items.group.fishing_rod"))
            fishingRodGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.oldFishingRodTextureStackCheck"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldFishingRodTextureStackCheck.description")))
                    .binding(
                        defaults.oldFishingRodTextureStackCheck,
                        { config.oldFishingRodTextureStackCheck },
                        { newVal -> config.oldFishingRodTextureStackCheck = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            fishingRodGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.fishingRodLineInterpolation"))
                    .description(OptionDescription.of(Component.translatable("animatium.fishingRodLineInterpolation.description")))
                    .binding(
                        defaults.fishingRodLineInterpolation,
                        { config.fishingRodLineInterpolation },
                        { newVal -> config.fishingRodLineInterpolation = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            fishingRodGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.noMoveFishingRodLine"))
                    .description(OptionDescription.of(Component.translatable("animatium.noMoveFishingRodLine.description")))
                    .binding(
                        defaults.noMoveFishingRodLine,
                        { config.noMoveFishingRodLine },
                        { newVal -> config.noMoveFishingRodLine = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            fishingRodGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.oldFishingRodLinePositionThirdPerson"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldFishingRodLinePositionThirdPerson.description")))
                    .binding(
                        defaults.oldFishingRodLinePositionThirdPerson,
                        { config.oldFishingRodLinePositionThirdPerson },
                        { newVal -> config.oldFishingRodLinePositionThirdPerson = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            fishingRodGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.oldFishingRodLineThickness"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldFishingRodLineThickness.description")))
                    .binding(
                        defaults.oldFishingRodLineThickness,
                        { config.oldFishingRodLineThickness },
                        { newVal -> config.oldFishingRodLineThickness = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            fishingRodGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.thinFishingRodLineThickness"))
                    .description(OptionDescription.of(Component.translatable("animatium.thinFishingRodLineThickness.description")))
                    .binding(
                        defaults.thinFishingRodLineThickness,
                        { config.thinFishingRodLineThickness },
                        { newVal -> config.thinFishingRodLineThickness = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            fishingRodGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.useStickModelWhenCastInThirdperson"))
                    .description(OptionDescription.of(Component.translatable("animatium.useStickModelWhenCastInThirdperson.description")))
                    .binding(
                        defaults.useStickModelWhenCastInThirdperson,
                        { config.useStickModelWhenCastInThirdperson },
                        { newVal -> config.useStickModelWhenCastInThirdperson = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            fishingRodGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.fixCastLineCheck"))
                    .description(OptionDescription.of(Component.translatable("animatium.fixCastLineCheck.description")))
                    .binding(
                        defaults.fixCastLineCheck,
                        { config.fixCastLineCheck },
                        { newVal -> config.fixCastLineCheck = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            fishingRodGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.fixCastLineSwing"))
                    .description(OptionDescription.of(Component.translatable("animatium.fixCastLineSwing.description")))
                    .binding(
                        defaults.fixCastLineSwing,
                        { config.fixCastLineSwing },
                        { newVal -> config.fixCastLineSwing = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            category.group(fishingRodGroup.build())
        }

        // Fixes
        run {
            val itemFixesGroup = OptionGroup.createBuilder()
            itemFixesGroup.name(Component.translatable("animatium.category.items.group.item_fixes"))
            itemFixesGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.fixEquipAnimationItemCheck"))
                    .description(OptionDescription.of(Component.translatable("animatium.fixEquipAnimationItemCheck.description")))
                    .binding(
                        defaults.fixEquipAnimation,
                        { config.fixEquipAnimation },
                        { newVal -> config.fixEquipAnimation = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            itemFixesGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.removeEquipAnimationOnItemUse"))
                    .description(OptionDescription.of(Component.translatable("animatium.removeEquipAnimationOnItemUse.description")))
                    .binding(
                        defaults.removeEquipAnimationOnItemUse,
                        { config.removeEquipAnimationOnItemUse },
                        { newVal -> config.removeEquipAnimationOnItemUse = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            itemFixesGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.removeItemUsageVisualInGUI"))
                    .description(OptionDescription.of(Component.translatable("animatium.removeItemUsageVisualInGUI.description")))
                    .binding(
                        defaults.removeItemUsageVisualInGUI,
                        { config.removeItemUsageVisualInGUI },
                        { newVal -> config.removeItemUsageVisualInGUI = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            itemFixesGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.fixFireballClientsideVisual"))
                    .description(OptionDescription.of(Component.translatable("animatium.fixFireballClientsideVisual.description")))
                    .binding(
                        defaults.fixFireballClientsideVisual,
                        { config.fixFireballClientsideVisual },
                        { newVal -> config.fixFireballClientsideVisual = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            category.group(itemFixesGroup.build())
        }

        // Glint
        run {
            val glintGroup = OptionGroup.createBuilder()
            glintGroup.name(Component.translatable("animatium.category.items.group.glint"))
            glintGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.oldGlintSpeed"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldGlintSpeed.description")))
                    .binding(
                        defaults.oldGlintSpeed,
                        { config.oldGlintSpeed },
                        { newVal -> config.oldGlintSpeed = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            glintGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.disableGlintOnItemDrops2D"))
                    .description(OptionDescription.of(Component.translatable("animatium.disableGlintOnItemDrops2D.description")))
                    .binding(
                        defaults.disableGlintOnItemDrops2D,
                        { config.disableGlintOnItemDrops2D },
                        { newVal -> config.disableGlintOnItemDrops2D = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            glintGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.disableGlintOnItemFramed2D"))
                    .description(OptionDescription.of(Component.translatable("animatium.disableGlintOnItemFramed2D.description")))
                    .binding(
                        defaults.disableGlintOnItemFramed2D,
                        { config.disableGlintOnItemFramed2D },
                        { newVal -> config.disableGlintOnItemFramed2D = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            category.group(glintGroup.build())
        }

        // 2d Drops
        run {
            val drops2dGroup = OptionGroup.createBuilder()
            drops2dGroup.name(Component.translatable("animatium.category.items.group.2d_drops"))
            drops2dGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.itemDropsFaceCamera"))
                    .description(OptionDescription.of(Component.translatable("animatium.itemDropsFaceCamera.description")))
                    .binding(
                        defaults.itemDropsFaceCamera,
                        { config.itemDropsFaceCamera },
                        { newVal -> config.itemDropsFaceCamera = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            drops2dGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.itemDropsFaceCameraRotationFix"))
                    .description(OptionDescription.of(Component.translatable("animatium.itemDropsFaceCameraRotationFix.description")))
                    .binding(
                        defaults.itemDropsFaceCameraRotationFix,
                        { config.itemDropsFaceCameraRotationFix },
                        { newVal -> config.itemDropsFaceCameraRotationFix = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            drops2dGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.itemDrops2D"))
                    .description(OptionDescription.of(Component.translatable("animatium.itemDrops2D.description")))
                    .binding(
                        defaults.itemDrops2D,
                        { config.itemDrops2D },
                        { newVal -> config.itemDrops2D = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            drops2dGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.itemFramed2D"))
                    .description(OptionDescription.of(Component.translatable("animatium.itemFramed2D.description")))
                    .binding(
                        defaults.itemFramed2D,
                        { config.itemFramed2D },
                        { newVal -> config.itemFramed2D = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            drops2dGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.item2DColors"))
                    .description(OptionDescription.of(Component.translatable("animatium.item2DColors.description")))
                    .binding(
                        defaults.item2DColors,
                        { config.item2DColors },
                        { newVal -> config.item2DColors = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            category.group(drops2dGroup.build())
        }

        // 2d Drops
        run {
            val transformationsGroup = OptionGroup.createBuilder()
            transformationsGroup.name(Component.translatable("animatium.category.items.group.transformations"))
            transformationsGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.tiltItemPositions"))
                    .description(OptionDescription.of(Component.translatable("animatium.tiltItemPositions.description")))
                    .binding(
                        defaults.tiltItemPositions,
                        { config.tiltItemPositions },
                        { newVal -> config.tiltItemPositions = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            transformationsGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.tiltItemPositionsInThirdperson"))
                    .description(OptionDescription.of(Component.translatable("animatium.tiltItemPositionsInThirdperson.description")))
                    .binding(
                        defaults.tiltItemPositionsInThirdperson,
                        { config.tiltItemPositionsInThirdperson },
                        { newVal -> config.tiltItemPositionsInThirdperson = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            transformationsGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.oldThinBlockPositions"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldThinBlockPositions.description")))
                    .binding(
                        defaults.oldThinBlockPositions,
                        { config.oldThinBlockPositions },
                        { newVal -> config.oldThinBlockPositions = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            transformationsGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.oldSkullPosition"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldSkullPosition.description")))
                    .binding(
                        defaults.oldSkullPosition,
                        { config.oldSkullPosition },
                        { newVal -> config.oldSkullPosition = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            transformationsGroup.option(Option.createBuilder<FishingRodVersion>()
                .name(Component.translatable("animatium.fishingRodVersion"))
                .description(OptionDescription.of(Component.translatable("animatium.fishingRodVersion.description")))
                .binding(
                    defaults.fishingRodVersion,
                    { config.fishingRodVersion },
                    { newVal -> config.fishingRodVersion = newVal })
                .controller { opt ->
                    EnumControllerBuilder.create(opt).enumClass(FishingRodVersion::class.java)
                        .formatValue { Component.translatable("animatium.enum.FishingRodVersion." + it.name) }
                }
                .build())
            category.group(transformationsGroup.build())
        }

        // Other
        run {
            val otherGroup = OptionGroup.createBuilder()
            otherGroup.name(Component.translatable("animatium.category.items.group.other"))
            otherGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.applyItemSwingUsage"))
                    .description(OptionDescription.of(Component.translatable("animatium.applyItemSwingUsage.description")))
                    .binding(
                        defaults.applyItemSwingUsage,
                        { config.applyItemSwingUsage },
                        { newVal -> config.applyItemSwingUsage = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            otherGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.disableSwingOnUse"))
                    .description(OptionDescription.of(Component.translatable("animatium.disableSwingOnUse.description")))
                    .binding(
                        defaults.disableSwingOnUse,
                        { config.disableSwingOnUse },
                        { newVal -> config.disableSwingOnUse = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            otherGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.disableSwingOnDrop"))
                    .description(OptionDescription.of(Component.translatable("animatium.disableSwingOnDrop.description")))
                    .binding(
                        defaults.disableSwingOnDrop,
                        { config.disableSwingOnDrop },
                        { newVal -> config.disableSwingOnDrop = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            otherGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.disableSwingOnEntityInteract"))
                    .description(OptionDescription.of(Component.translatable("animatium.disableSwingOnEntityInteract.description")))
                    .binding(
                        defaults.disableSwingOnEntityInteract,
                        { config.disableSwingOnEntityInteract },
                        { newVal -> config.disableSwingOnEntityInteract = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            otherGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.disableItemUsingTextureInGui"))
                    .description(OptionDescription.of(Component.translatable("animatium.disableItemUsingTextureInGui.description")))
                    .binding(
                        defaults.disableItemUsingTextureInGui,
                        { config.disableItemUsingTextureInGui },
                        { newVal -> config.disableItemUsingTextureInGui = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            otherGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.oldDurabilityBarColors"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldDurabilityBarColors.description")))
                    .binding(
                        defaults.oldDurabilityBarColors,
                        { config.oldDurabilityBarColors },
                        { newVal -> config.oldDurabilityBarColors = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            otherGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.oldItemRarities"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldItemRarities.description")))
                    .binding(
                        defaults.oldItemRarities,
                        { config.oldItemRarities },
                        { newVal -> config.oldItemRarities = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            otherGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.showHeldItemInBoat"))
                    .description(OptionDescription.of(Component.translatable("animatium.showHeldItemInBoat.description")))
                    .binding(
                        defaults.showHeldItemInBoat,
                        { config.showHeldItemInBoat },
                        { newVal -> config.showHeldItemInBoat = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            otherGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.oldItemPickupPosition"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldItemPickupPosition.description")))
                    .binding(
                        defaults.oldItemPickupPosition,
                        { config.oldItemPickupPosition },
                        { newVal -> config.oldItemPickupPosition = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            category.group(otherGroup.build())
        }
        builder.category(category.build())
    }
}