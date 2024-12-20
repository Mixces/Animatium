package me.mixces.animatium.config

import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.api.controller.EnumControllerBuilder
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler
import dev.isxander.yacl3.config.v2.api.SerialEntry
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder
import dev.isxander.yacl3.platform.YACLPlatform
import me.mixces.animatium.util.CameraVersion
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class AnimatiumConfig {
    companion object {
        private val CONFIG = ConfigClassHandler.createBuilder(AnimatiumConfig::class.java)
            .serializer { config ->
                GsonConfigSerializerBuilder.create(config)
                    .setPath(YACLPlatform.getConfigDir().resolve("animatium.json"))
                    .build()
            }.build()

        @JvmStatic
        fun getConfigScreen(parent: Screen?): Screen {
            return (YetAnotherConfigLib.create(CONFIG) { defaults: AnimatiumConfig, config: AnimatiumConfig, builder: YetAnotherConfigLib.Builder ->
                builder.title(Text.translatable("animatium.title"))

                run {
                    // Sneaking Category
                    val category = ConfigCategory.createBuilder()
                    category.name(Text.translatable("animatium.category.sneaking"))
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.removeSmoothSneaking"))
                            .description(OptionDescription.of(Text.translatable("animatium.removeSmoothSneaking.description")))
                            .binding(
                                defaults.removeSmoothSneaking,
                                { config.removeSmoothSneaking },
                                { newVal -> config.removeSmoothSneaking = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.oldSneakAnimationInterpolation"))
                            .description(OptionDescription.of(Text.translatable("animatium.oldSneakAnimationInterpolation.description")))
                            .binding(
                                defaults.oldSneakAnimationInterpolation,
                                { config.oldSneakAnimationInterpolation },
                                { newVal -> config.oldSneakAnimationInterpolation = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.oldSneakEyeHeight"))
                            .description(OptionDescription.of(Text.translatable("animatium.oldSneakEyeHeight.description")))
                            .binding(
                                defaults.oldSneakEyeHeight,
                                { config.oldSneakEyeHeight },
                                { newVal -> config.oldSneakEyeHeight = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.fixSneakingFeetPosition"))
                            .description(OptionDescription.of(Text.translatable("animatium.fixSneakingFeetPosition.description")))
                            .binding(
                                defaults.fixSneakingFeetPosition,
                                { config.fixSneakingFeetPosition },
                                { newVal -> config.fixSneakingFeetPosition = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.oldSneakingFeetPosition"))
                            .description(OptionDescription.of(Text.translatable("animatium.oldSneakingFeetPosition.description")))
                            .binding(
                                defaults.oldSneakingFeetPosition,
                                { config.oldSneakingFeetPosition },
                                { newVal -> config.oldSneakingFeetPosition = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.syncPlayerModelWithEyeHeight"))
                            .description(OptionDescription.of(Text.translatable("animatium.syncPlayerModelWithEyeHeight.description")))
                            .binding(
                                defaults.syncPlayerModelWithEyeHeight,
                                { config.syncPlayerModelWithEyeHeight },
                                { newVal -> config.syncPlayerModelWithEyeHeight = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.sneakAnimationWhileFlying"))
                            .description(OptionDescription.of(Text.translatable("animatium.sneakAnimationWhileFlying.description")))
                            .binding(
                                defaults.sneakAnimationWhileFlying,
                                { config.sneakAnimationWhileFlying },
                                { newVal -> config.sneakAnimationWhileFlying = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    builder.category(category.build())
                }

                run {
                    // Quality of Life Category
                    val category = ConfigCategory.createBuilder()
                    category.name(Text.translatable("animatium.category.qol"))
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.minimalViewBobbing"))
                            .description(OptionDescription.of(Text.translatable("animatium.minimalViewBobbing.description")))
                            .binding(
                                defaults.minimalViewBobbing,
                                { config.minimalViewBobbing },
                                { newVal -> config.minimalViewBobbing = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.showNametagInThirdperson"))
                            .description(OptionDescription.of(Text.translatable("animatium.showNametagInThirdperson.description")))
                            .binding(
                                defaults.showNametagInThirdperson,
                                { config.showNametagInThirdperson },
                                { newVal -> config.showNametagInThirdperson = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.hideNameTagBackground"))
                            .description(OptionDescription.of(Text.translatable("animatium.hideNameTagBackground.description")))
                            .binding(
                                defaults.hideNameTagBackground,
                                { config.hideNameTagBackground },
                                { newVal -> config.hideNameTagBackground = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.applyTextShadowToNametag"))
                            .description(OptionDescription.of(Text.translatable("animatium.applyTextShadowToNametag.description")))
                            .binding(
                                defaults.applyTextShadowToNametag,
                                { config.applyTextShadowToNametag },
                                { newVal -> config.applyTextShadowToNametag = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.oldDebugHudTextColor"))
                            .description(OptionDescription.of(Text.translatable("animatium.oldDebugHudTextColor.description")))
                            .binding(
                                defaults.oldDebugHudTextColor,
                                { config.oldDebugHudTextColor },
                                { newVal -> config.oldDebugHudTextColor = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.fixMirrorArmSwing"))
                            .description(OptionDescription.of(Text.translatable("animatium.fixMirrorArmSwing.description")))
                            .binding(
                                defaults.fixMirrorArmSwing,
                                { config.fixMirrorArmSwing },
                                { newVal -> config.fixMirrorArmSwing = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.persistentBlockOutline"))
                            .description(OptionDescription.of(Text.translatable("animatium.persistentBlockOutline.description")))
                            .binding(
                                defaults.persistentBlockOutline,
                                { config.persistentBlockOutline },
                                { newVal -> config.persistentBlockOutline = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.alwaysAllowUsageSwinging"))
                            .description(OptionDescription.of(Text.translatable("animatium.alwaysAllowUsageSwinging.description")))
                            .binding(
                                defaults.alwaysAllowUsageSwinging,
                                { config.alwaysAllowUsageSwinging },
                                { newVal -> config.alwaysAllowUsageSwinging = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.alwaysShowSharpParticles"))
                            .description(OptionDescription.of(Text.translatable("animatium.alwaysShowSharpParticles.description")))
                            .binding(
                                defaults.alwaysShowSharpParticles,
                                { config.alwaysShowSharpParticles },
                                { newVal -> config.alwaysShowSharpParticles = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.forceItemGlintOnEntity"))
                            .description(OptionDescription.of(Text.translatable("animatium.forceItemGlintOnEntity.description")))
                            .binding(
                                defaults.forceItemGlintOnEntity,
                                { config.forceItemGlintOnEntity },
                                { newVal -> config.forceItemGlintOnEntity = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.disableRecipeAndTutorialToasts"))
                            .description(OptionDescription.of(Text.translatable("animatium.disableRecipeAndTutorialToasts.description")))
                            .binding(
                                defaults.disableRecipeAndTutorialToasts,
                                { config.disableRecipeAndTutorialToasts },
                                { newVal -> config.disableRecipeAndTutorialToasts = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.disablePoseUpdates"))
                            .description(OptionDescription.of(Text.translatable("animatium.disablePoseUpdates.description")))
                            .binding(
                                defaults.disablePoseUpdates,
                                { config.disablePoseUpdates },
                                { newVal -> config.disablePoseUpdates = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    builder.category(category.build())
                }

                run {
                    // Movement Category
                    val category = ConfigCategory.createBuilder()
                    category.name(Text.translatable("animatium.category.movement"))
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.rotateBackwardsWalking"))
                            .description(OptionDescription.of(Text.translatable("animatium.rotateBackwardsWalking.description")))
                            .binding(
                                defaults.rotateBackwardsWalking,
                                { config.rotateBackwardsWalking },
                                { newVal -> config.rotateBackwardsWalking = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.uncapBlockingHeadRotation"))
                            .description(OptionDescription.of(Text.translatable("animatium.uncapBlockingHeadRotation.description")))
                            .binding(
                                defaults.uncapBlockingHeadRotation,
                                { config.uncapBlockingHeadRotation },
                                { newVal -> config.uncapBlockingHeadRotation = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.removeHeadRotationInterpolation"))
                            .description(OptionDescription.of(Text.translatable("animatium.removeHeadRotationInterpolation.description")))
                            .binding(
                                defaults.removeHeadRotationInterpolation,
                                { config.removeHeadRotationInterpolation },
                                { newVal -> config.removeHeadRotationInterpolation = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.fixVerticalBobbingTilt"))
                            .description(OptionDescription.of(Text.translatable("animatium.fixVerticalBobbingTilt.description")))
                            .binding(
                                defaults.fixVerticalBobbingTilt,
                                { config.fixVerticalBobbingTilt },
                                { newVal -> config.fixVerticalBobbingTilt = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.oldDeathLimbs"))
                            .description(OptionDescription.of(Text.translatable("animatium.oldDeathLimbs.description")))
                            .binding(
                                defaults.oldDeathLimbs,
                                { config.oldDeathLimbs },
                                { newVal -> config.oldDeathLimbs = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.fixBowArmMovement"))
                            .description(OptionDescription.of(Text.translatable("animatium.fixBowArmMovement.description")))
                            .binding(
                                defaults.fixBowArmMovement,
                                { config.fixBowArmMovement },
                                { newVal -> config.fixBowArmMovement = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.oldCapeMovement"))
                            .description(OptionDescription.of(Text.translatable("animatium.oldCapeMovement.description")))
                            .binding(
                                defaults.oldCapeMovement,
                                { config.oldCapeMovement },
                                { newVal -> config.oldCapeMovement = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    builder.category(category.build())
                }

                run {
                    // Fishing Rod Category
                    val category = ConfigCategory.createBuilder()
                    category.name(Text.translatable("animatium.category.fishing_rod"))
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.oldFishingRodTextureStackCheck"))
                            .description(OptionDescription.of(Text.translatable("animatium.oldFishingRodTextureStackCheck.description")))
                            .binding(
                                defaults.oldFishingRodTextureStackCheck,
                                { config.oldFishingRodTextureStackCheck },
                                { newVal -> config.oldFishingRodTextureStackCheck = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.fishingRodLineInterpolation"))
                            .description(OptionDescription.of(Text.translatable("animatium.fishingRodLineInterpolation.description")))
                            .binding(
                                defaults.fishingRodLineInterpolation,
                                { config.fishingRodLineInterpolation },
                                { newVal -> config.fishingRodLineInterpolation = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.noMoveFishingRodLine"))
                            .description(OptionDescription.of(Text.translatable("animatium.noMoveFishingRodLine.description")))
                            .binding(
                                defaults.noMoveFishingRodLine,
                                { config.noMoveFishingRodLine },
                                { newVal -> config.noMoveFishingRodLine = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.oldFishingRodLinePositionThirdPerson"))
                            .description(OptionDescription.of(Text.translatable("animatium.oldFishingRodLinePositionThirdPerson.description")))
                            .binding(
                                defaults.oldFishingRodLinePositionThirdPerson,
                                { config.oldFishingRodLinePositionThirdPerson },
                                { newVal -> config.oldFishingRodLinePositionThirdPerson = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.fixCastLineCheck"))
                            .description(OptionDescription.of(Text.translatable("animatium.fixCastLineCheck.description")))
                            .binding(
                                defaults.fixCastLineCheck,
                                { config.fixCastLineCheck },
                                { newVal -> config.fixCastLineCheck = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.fixCastLineSwing"))
                            .description(OptionDescription.of(Text.translatable("animatium.fixCastLineSwing.description")))
                            .binding(
                                defaults.fixCastLineSwing,
                                { config.fixCastLineSwing },
                                { newVal -> config.fixCastLineSwing = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    builder.category(category.build())
                }

                run {
                    // Other Category
                    val category = ConfigCategory.createBuilder()
                    category.name(Text.translatable("animatium.category.other"))
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.tiltItemPositions"))
                            .description(OptionDescription.of(Text.translatable("animatium.tiltItemPositions.description")))
                            .binding(
                                defaults.tiltItemPositions,
                                { config.tiltItemPositions },
                                { newVal -> config.tiltItemPositions = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.tiltItemPositionsInThirdperson"))
                            .description(OptionDescription.of(Text.translatable("animatium.tiltItemPositionsInThirdperson.description")))
                            .binding(
                                defaults.tiltItemPositionsInThirdperson,
                                { config.tiltItemPositionsInThirdperson },
                                { newVal -> config.tiltItemPositionsInThirdperson = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.legacyThirdpersonSwordBlockingPosition"))
                            .description(OptionDescription.of(Text.translatable("animatium.legacyThirdpersonSwordBlockingPosition.description")))
                            .binding(
                                defaults.legacyThirdpersonSwordBlockingPosition,
                                { config.legacyThirdpersonSwordBlockingPosition },
                                { newVal -> config.legacyThirdpersonSwordBlockingPosition = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.lockBlockingArmRotation"))
                            .description(OptionDescription.of(Text.translatable("animatium.lockBlockingArmRotation.description")))
                            .binding(
                                defaults.lockBlockingArmRotation,
                                { config.lockBlockingArmRotation },
                                { newVal -> config.lockBlockingArmRotation = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.applyItemSwingUsage"))
                            .description(OptionDescription.of(Text.translatable("animatium.applyItemSwingUsage.description")))
                            .binding(
                                defaults.applyItemSwingUsage,
                                { config.applyItemSwingUsage },
                                { newVal -> config.applyItemSwingUsage = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.removeEquipAnimationOnItemUse"))
                            .description(OptionDescription.of(Text.translatable("animatium.removeEquipAnimationOnItemUse.description")))
                            .binding(
                                defaults.removeEquipAnimationOnItemUse,
                                { config.removeEquipAnimationOnItemUse },
                                { newVal -> config.removeEquipAnimationOnItemUse = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.showCrosshairInThirdperson"))
                            .description(OptionDescription.of(Text.translatable("animatium.showCrosshairInThirdperson.description")))
                            .binding(
                                defaults.showCrosshairInThirdperson,
                                { config.showCrosshairInThirdperson },
                                { newVal -> config.showCrosshairInThirdperson = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.removeHeartFlash"))
                            .description(OptionDescription.of(Text.translatable("animatium.removeHeartFlash.description")))
                            .binding(
                                defaults.removeHeartFlash,
                                { config.removeHeartFlash },
                                { newVal -> config.removeHeartFlash = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.fixTextStrikethroughStyle"))
                            .description(OptionDescription.of(Text.translatable("animatium.fixTextStrikethroughStyle.description")))
                            .binding(
                                defaults.fixTextStrikethroughStyle,
                                { config.fixTextStrikethroughStyle },
                                { newVal -> config.fixTextStrikethroughStyle = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.centerScrollableListWidgets"))
                            .description(OptionDescription.of(Text.translatable("animatium.centerScrollableListWidgets.description")))
                            .binding(
                                defaults.centerScrollableListWidgets,
                                { config.centerScrollableListWidgets },
                                { newVal -> config.centerScrollableListWidgets = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.oldListWidgetSelectedBorderColor"))
                            .description(OptionDescription.of(Text.translatable("animatium.oldListWidgetSelectedBorderColor.description")))
                            .binding(
                                defaults.oldListWidgetSelectedBorderColor,
                                { config.oldListWidgetSelectedBorderColor },
                                { newVal -> config.oldListWidgetSelectedBorderColor = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.oldBlueVoidSky"))
                            .description(OptionDescription.of(Text.translatable("animatium.oldBlueVoidSky.description")))
                            .binding(
                                defaults.oldBlueVoidSky,
                                { config.oldBlueVoidSky },
                                { newVal -> config.oldBlueVoidSky = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.oldSkyHorizonHeight"))
                            .description(OptionDescription.of(Text.translatable("animatium.oldSkyHorizonHeight.description")))
                            .binding(
                                defaults.oldSkyHorizonHeight,
                                { config.oldSkyHorizonHeight },
                                { newVal -> config.oldSkyHorizonHeight = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.oldCloudHeight"))
                            .description(OptionDescription.of(Text.translatable("animatium.oldCloudHeight.description")))
                            .binding(
                                defaults.oldCloudHeight,
                                { config.oldCloudHeight },
                                { newVal -> config.oldCloudHeight = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.oldButtonTextColors"))
                            .description(OptionDescription.of(Text.translatable("animatium.oldButtonTextColors.description")))
                            .binding(
                                defaults.oldButtonTextColors,
                                { config.oldButtonTextColors },
                                { newVal -> config.oldButtonTextColors = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.removeDebugHudBackground"))
                            .description(OptionDescription.of(Text.translatable("animatium.removeDebugHudBackground.description")))
                            .binding(
                                defaults.removeDebugHudBackground,
                                { config.removeDebugHudBackground },
                                { newVal -> config.removeDebugHudBackground = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.debugHudTextShadow"))
                            .description(OptionDescription.of(Text.translatable("animatium.debugHudTextShadow.description")))
                            .binding(
                                defaults.debugHudTextShadow,
                                { config.debugHudTextShadow },
                                { newVal -> config.debugHudTextShadow = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.oldChatPosition"))
                            .description(OptionDescription.of(Text.translatable("animatium.oldChatPosition.description")))
                            .binding(
                                defaults.oldChatPosition,
                                { config.oldChatPosition },
                                { newVal -> config.oldChatPosition = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.oldProjectilePosition"))
                            .description(OptionDescription.of(Text.translatable("animatium.oldProjectilePosition.description")))
                            .binding(
                                defaults.oldProjectilePosition,
                                { config.oldProjectilePosition },
                                { newVal -> config.oldProjectilePosition = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.disableProjectileAgeCheck"))
                            .description(OptionDescription.of(Text.translatable("animatium.disableProjectileAgeCheck.description")))
                            .binding(
                                defaults.disableProjectileAgeCheck,
                                { config.disableProjectileAgeCheck },
                                { newVal -> config.disableProjectileAgeCheck = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.oldBlockMiningProgress"))
                            .description(OptionDescription.of(Text.translatable("animatium.oldBlockMiningProgress.description")))
                            .binding(
                                defaults.oldBlockMiningProgress,
                                { config.oldBlockMiningProgress },
                                { newVal -> config.oldBlockMiningProgress = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.disableItemUsingTextureInGui"))
                            .description(OptionDescription.of(Text.translatable("animatium.disableItemUsingTextureInGui.description")))
                            .binding(
                                defaults.disableItemUsingTextureInGui,
                                { config.disableItemUsingTextureInGui },
                                { newVal -> config.disableItemUsingTextureInGui = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.disableInventoryEntityScissor"))
                            .description(OptionDescription.of(Text.translatable("animatium.disableInventoryEntityScissor.description")))
                            .binding(
                                defaults.disableInventoryEntityScissor,
                                { config.disableInventoryEntityScissor },
                                { newVal -> config.disableInventoryEntityScissor = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.disableCameraTransparentPassthrough"))
                            .description(OptionDescription.of(Text.translatable("animatium.disableCameraTransparentPassthrough.description")))
                            .binding(
                                defaults.disableCameraTransparentPassthrough,
                                { config.disableCameraTransparentPassthrough },
                                { newVal -> config.disableCameraTransparentPassthrough = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.itemDropsFaceCamera"))
                            .description(OptionDescription.of(Text.translatable("animatium.itemDropsFaceCamera.description")))
                            .binding(
                                defaults.itemDropsFaceCamera,
                                { config.itemDropsFaceCamera },
                                { newVal -> config.itemDropsFaceCamera = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.itemDrops2D"))
                            .description(OptionDescription.of(Text.translatable("animatium.itemDrops2D.description")))
                            .binding(
                                defaults.itemDrops2D,
                                { config.itemDrops2D },
                                { newVal -> config.itemDrops2D = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.oldDurabilityBarColors"))
                            .description(OptionDescription.of(Text.translatable("animatium.oldDurabilityBarColors.description")))
                            .binding(
                                defaults.oldDurabilityBarColors,
                                { config.oldDurabilityBarColors },
                                { newVal -> config.oldDurabilityBarColors = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.oldItemRarities"))
                            .description(OptionDescription.of(Text.translatable("animatium.oldItemRarities.description")))
                            .binding(
                                defaults.oldItemRarities,
                                { config.oldItemRarities },
                                { newVal -> config.oldItemRarities = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(
                        Option.createBuilder<Boolean>()
                            .name(Text.translatable("animatium.removeClientsideBlockingDelay"))
                            .description(OptionDescription.of(Text.translatable("animatium.removeClientsideBlockingDelay.description")))
                            .binding(
                                defaults.removeClientsideBlockingDelay,
                                { config.removeClientsideBlockingDelay },
                                { newVal -> config.removeClientsideBlockingDelay = newVal })
                            .controller(TickBoxControllerBuilder::create)
                            .build()
                    )
                    category.option(Option.createBuilder<CameraVersion>()
                        .name(Text.translatable("animatium.cameraVersion"))
                        .description(OptionDescription.of(Text.translatable("animatium.cameraVersion.description")))
                        .binding(
                            defaults.cameraVersion,
                            { config.cameraVersion },
                            { newVal -> config.cameraVersion = newVal })
                        .controller { opt ->
                            EnumControllerBuilder.create(opt).enumClass(CameraVersion::class.java)
                                .formatValue { it -> Text.translatable("animatium.enum.CameraVersion." + it.name) }
                        }
                        .build())
                    builder.category(category.build())
                }

                builder
            } as YetAnotherConfigLib).generateScreen(parent)
        }

        @JvmStatic
        fun load() {
            CONFIG.load()
        }

        @JvmStatic
        fun getInstance(): AnimatiumConfig {
            return CONFIG.instance() as AnimatiumConfig
        }
    }

    // Sneaking
    @SerialEntry var removeSmoothSneaking = false
    @SerialEntry var oldSneakAnimationInterpolation = true
    @SerialEntry var oldSneakEyeHeight = true
    @SerialEntry var fixSneakingFeetPosition = true
    @SerialEntry var oldSneakingFeetPosition = false // TODO/NOTE: Might need a better name.
    @SerialEntry var syncPlayerModelWithEyeHeight = false
    @SerialEntry var sneakAnimationWhileFlying = true

    // QOL
    @SerialEntry var minimalViewBobbing = true
    @SerialEntry var showNametagInThirdperson = true
    @SerialEntry var hideNameTagBackground = true
    @SerialEntry var applyTextShadowToNametag = true
    @SerialEntry var oldDebugHudTextColor = true
    @SerialEntry var fixMirrorArmSwing = true
    @SerialEntry var persistentBlockOutline = true
    @SerialEntry var alwaysAllowUsageSwinging = true
    @SerialEntry var alwaysShowSharpParticles = true
    @SerialEntry var forceItemGlintOnEntity = false
    @SerialEntry var disableRecipeAndTutorialToasts = false
    @SerialEntry var disablePoseUpdates = false

    // Movement
    @SerialEntry var rotateBackwardsWalking = true
    @SerialEntry var uncapBlockingHeadRotation = true
    @SerialEntry var removeHeadRotationInterpolation = true
    @SerialEntry var fixVerticalBobbingTilt = true
    @SerialEntry var oldDeathLimbs = true
    @SerialEntry var fixBowArmMovement = true
    @SerialEntry var oldCapeMovement = false // TODO/NOTE: Currently not accurate/broken.

    // Fishing Rod
    @SerialEntry var oldFishingRodTextureStackCheck = true
    @SerialEntry var fishingRodLineInterpolation = true
    @SerialEntry var noMoveFishingRodLine = true
    @SerialEntry var oldFishingRodLinePositionThirdPerson = true
    @SerialEntry var fixCastLineCheck = true
    @SerialEntry var fixCastLineSwing = true

    // Other
    @SerialEntry var tiltItemPositions = true
    @SerialEntry var tiltItemPositionsInThirdperson = true
    @SerialEntry var legacyThirdpersonSwordBlockingPosition = true
    @SerialEntry var lockBlockingArmRotation = true
    @SerialEntry var applyItemSwingUsage = true
    @SerialEntry var removeEquipAnimationOnItemUse = true
    @SerialEntry var showCrosshairInThirdperson = true
    @SerialEntry var removeHeartFlash = true
    @SerialEntry var fixTextStrikethroughStyle = true
    @SerialEntry var centerScrollableListWidgets = true
    @SerialEntry var oldListWidgetSelectedBorderColor = true
    @SerialEntry var oldBlueVoidSky = true
    @SerialEntry var oldSkyHorizonHeight = true
    @SerialEntry var oldCloudHeight = true
    @SerialEntry var oldButtonTextColors = true
    @SerialEntry var removeDebugHudBackground = true
    @SerialEntry var debugHudTextShadow = true
    @SerialEntry var oldChatPosition = true
    @SerialEntry var oldProjectilePosition = true
    @SerialEntry var disableProjectileAgeCheck = true
    @SerialEntry var oldBlockMiningProgress = true
    @SerialEntry var disableItemUsingTextureInGui = true
    @SerialEntry var disableInventoryEntityScissor = true
    @SerialEntry var disableCameraTransparentPassthrough = true
    @SerialEntry var itemDropsFaceCamera = true
    @SerialEntry var itemDrops2D = true
    @SerialEntry var oldDurabilityBarColors = true
    @SerialEntry var oldItemRarities = true
    @SerialEntry var removeClientsideBlockingDelay = true
    @SerialEntry var cameraVersion = CameraVersion.V1_8
}