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
import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.OptionGroup
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import net.minecraft.network.chat.Component

object MovementConfigCategory {
    fun setup(builder: YetAnotherConfigLib.Builder, defaults: AnimatiumConfig, config: AnimatiumConfig) {
        val category = ConfigCategory.createBuilder()
        category.name(Component.translatable("animatium.category.movement"))

        // Sneaking
        run {
            val sneakingGroup = OptionGroup.createBuilder()
            sneakingGroup.name(Component.translatable("animatium.category.movement.group.sneaking"))
            sneakingGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.smoothSneaking"))
                    .description(OptionDescription.of(Component.translatable("animatium.smoothSneaking.description")))
                    .binding(
                        defaults.smoothSneaking,
                        { config.smoothSneaking },
                        { newVal -> config.smoothSneaking = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            sneakingGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.sneakAnimationInterpolation"))
                    .description(OptionDescription.of(Component.translatable("animatium.sneakAnimationInterpolation.description")))
                    .binding(
                        defaults.sneakAnimationInterpolation,
                        { config.sneakAnimationInterpolation },
                        { newVal -> config.sneakAnimationInterpolation = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            sneakingGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.fakeOldSneakEyeHeight"))
                    .description(OptionDescription.of(Component.translatable("animatium.fakeOldSneakEyeHeight.description")))
                    .binding(
                        defaults.fakeOldSneakEyeHeight,
                        { config.fakeOldSneakEyeHeight },
                        { newVal -> config.fakeOldSneakEyeHeight = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            sneakingGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.fixSneakingFeetPosition"))
                    .description(OptionDescription.of(Component.translatable("animatium.fixSneakingFeetPosition.description")))
                    .binding(
                        defaults.fixSneakingFeetPosition,
                        { config.fixSneakingFeetPosition },
                        { newVal -> config.fixSneakingFeetPosition = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            sneakingGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.oldSneakingFeetPosition"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldSneakingFeetPosition.description")))
                    .binding(
                        defaults.oldSneakingFeetPosition,
                        { config.oldSneakingFeetPosition },
                        { newVal -> config.oldSneakingFeetPosition = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            sneakingGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.syncPlayerModelWithEyeHeight"))
                    .description(OptionDescription.of(Component.translatable("animatium.syncPlayerModelWithEyeHeight.description")))
                    .binding(
                        defaults.syncPlayerModelWithEyeHeight,
                        { config.syncPlayerModelWithEyeHeight },
                        { newVal -> config.syncPlayerModelWithEyeHeight = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            sneakingGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.sneakAnimationWhileFlying"))
                    .description(OptionDescription.of(Component.translatable("animatium.sneakAnimationWhileFlying.description")))
                    .binding(
                        defaults.sneakAnimationWhileFlying,
                        { config.sneakAnimationWhileFlying },
                        { newVal -> config.sneakAnimationWhileFlying = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            category.group(sneakingGroup.build())
        }

        run {
            val capeGroup = OptionGroup.createBuilder()
            capeGroup.name(Component.translatable("animatium.category.movement.group.cape"))
            capeGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.oldCapeMovement"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldCapeMovement.description")))
                    .binding(
                        defaults.oldCapeMovement,
                        { config.oldCapeMovement },
                        { newVal -> config.oldCapeMovement = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            capeGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.dontClampCapeLean"))
                    .description(OptionDescription.of(Component.translatable("animatium.dontClampCapeLean.description")))
                    .binding(
                        defaults.dontClampCapeLean,
                        { config.dontClampCapeLean },
                        { newVal -> config.dontClampCapeLean = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            capeGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.capeSwingRotation"))
                    .description(OptionDescription.of(Component.translatable("animatium.capeSwingRotation.description")))
                    .binding(
                        defaults.capeSwingRotation,
                        { config.capeSwingRotation },
                        { newVal -> config.capeSwingRotation = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            capeGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.capeChestplateTranslation"))
                    .description(OptionDescription.of(Component.translatable("animatium.capeChestplateTranslation.description")))
                    .binding(
                        defaults.capeChestplateTranslation,
                        { config.capeChestplateTranslation },
                        { newVal -> config.capeChestplateTranslation = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            capeGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.oldCapeSneakPosition"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldCapeSneakPosition.description")))
                    .binding(
                        defaults.oldCapeSneakPosition,
                        { config.oldCapeSneakPosition },
                        { newVal -> config.oldCapeSneakPosition = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            category.group(capeGroup.build())
        }

        // Other
        run {
            val otherGroup = OptionGroup.createBuilder()
            otherGroup.name(Component.translatable("animatium.category.movement.group.other"))
            otherGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.rotateBackwardsWalking"))
                    .description(OptionDescription.of(Component.translatable("animatium.rotateBackwardsWalking.description")))
                    .binding(
                        defaults.rotateBackwardsWalking,
                        { config.rotateBackwardsWalking },
                        { newVal -> config.rotateBackwardsWalking = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            otherGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.uncapBlockingHeadRotation"))
                    .description(OptionDescription.of(Component.translatable("animatium.uncapBlockingHeadRotation.description")))
                    .binding(
                        defaults.uncapBlockingHeadRotation,
                        { config.uncapBlockingHeadRotation },
                        { newVal -> config.uncapBlockingHeadRotation = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            otherGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.removeHeadRotationInterpolation"))
                    .description(OptionDescription.of(Component.translatable("animatium.removeHeadRotationInterpolation.description")))
                    .binding(
                        defaults.removeHeadRotationInterpolation,
                        { config.removeHeadRotationInterpolation },
                        { newVal -> config.removeHeadRotationInterpolation = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            otherGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.fixVerticalBobbingTilt"))
                    .description(OptionDescription.of(Component.translatable("animatium.fixVerticalBobbingTilt.description")))
                    .binding(
                        defaults.fixVerticalBobbingTilt,
                        { config.fixVerticalBobbingTilt },
                        { newVal -> config.fixVerticalBobbingTilt = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            otherGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.oldViewBobbing"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldViewBobbing.description")))
                    .binding(
                        defaults.oldViewBobbing,
                        { config.oldViewBobbing },
                        { newVal -> config.oldViewBobbing = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            otherGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.oldDeathLimbs"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldDeathLimbs.description")))
                    .binding(
                        defaults.oldDeathLimbs,
                        { config.oldDeathLimbs },
                        { newVal -> config.oldDeathLimbs = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            otherGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.oldBowArmMovement"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldBowArmMovement.description")))
                    .binding(
                        defaults.oldBowArmMovement,
                        { config.oldBowArmMovement },
                        { newVal -> config.oldBowArmMovement = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            otherGroup.option(
                Option.createBuilder<Boolean>()
                    .name(Component.translatable("animatium.oldDamageTilt"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldDamageTilt.description")))
                    .binding(
                        defaults.oldDamageTilt,
                        { config.oldDamageTilt },
                        { newVal -> config.oldDamageTilt = newVal })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
            )
            category.group(otherGroup.build())
        }

        builder.category(category.build())
    }
}