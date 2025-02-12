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

package btw.mixces.animatium.config

import btw.mixces.animatium.config.category.ItemsConfigCategory
import btw.mixces.animatium.config.category.MovementConfigCategory
import btw.mixces.animatium.config.category.OtherConfigCategory
import btw.mixces.animatium.config.category.QOLConfigCategory
import btw.mixces.animatium.config.category.ScreenConfigCategory
import btw.mixces.animatium.util.CameraVersion
import btw.mixces.animatium.util.FishingRodVersion
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler
import dev.isxander.yacl3.config.v2.api.SerialEntry
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder
import dev.isxander.yacl3.platform.YACLPlatform
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

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
                builder.title(Component.translatable("animatium.title"))
                QOLConfigCategory.setup(builder, defaults, config)
                MovementConfigCategory.setup(builder, defaults, config)
                ScreenConfigCategory.setup(builder, defaults, config)
                ItemsConfigCategory.setup(builder, defaults, config)
                OtherConfigCategory.setup(builder, defaults, config)
                builder
            } as YetAnotherConfigLib).generateScreen(parent)
        }

        @JvmStatic
        fun load() {
            CONFIG.load()
        }

        @JvmStatic
        fun instance(): AnimatiumConfig {
            return CONFIG.instance() as AnimatiumConfig
        }
    }

    // TODO/NOTE: Category for just fixes?

    // (QOL)
    @SerialEntry var minimalViewBobbing = false
    @SerialEntry var showNametagInThirdperson = false
    @SerialEntry var hideNameTagBackground = false
    @SerialEntry var applyTextShadowToNametag = false
    @SerialEntry var oldDebugHudTextColor = false
    @SerialEntry var persistentBlockOutline = false
    @SerialEntry var allowOffhandUsageSwinging = false
    @SerialEntry var alwaysShowSharpParticles = false
    @SerialEntry var disableRecipeAndTutorialToasts = false
    @SerialEntry var showArmWhileInvisible = false
    @SerialEntry var fakeMissPenaltySwing = false
    @SerialEntry var showUsageSwingingParticles = false
    @SerialEntry var disableEntityDeathTopple = false
    @SerialEntry var deepRedHurtTint = false
    @SerialEntry var disableParticlePhysics = false
    @SerialEntry var hideFirstpersonParticles = false
    @SerialEntry var dontClearChat = false
    @SerialEntry var dontCloseChat = false
    // (QOL) Fixes
    @SerialEntry var fixMirrorArmSwing = true
    @SerialEntry var upMinPixelTransparencyLimit = true
    @SerialEntry var fixOffHandUsingPose = true

    // (Movement)
    // (Movement) Sneaking
    @SerialEntry var removeSmoothSneaking = false
    @SerialEntry var oldSneakAnimationInterpolation = false
    @SerialEntry var fakeOldSneakEyeHeight = false
    @SerialEntry var fixSneakingFeetPosition = true
    @SerialEntry var oldSneakingFeetPosition = true
    @SerialEntry var syncPlayerModelWithEyeHeight = true
    @SerialEntry var sneakAnimationWhileFlying = true
    // (Movement) Other
    @SerialEntry var rotateBackwardsWalking = true
    @SerialEntry var uncapBlockingHeadRotation = true
    @SerialEntry var removeHeadRotationInterpolation = true
    @SerialEntry var fixVerticalBobbingTilt = true
    @SerialEntry var oldViewBobbing = true
    @SerialEntry var oldDeathLimbs = true
    @SerialEntry var oldBowArmMovement = true
    @SerialEntry var oldDamageTilt = true
    // (Movement) Cape
    @SerialEntry var oldCapeMovement = true
    @SerialEntry var dontClampCapeLean = false
    @SerialEntry var capeSwingRotation = true
    @SerialEntry var capeChestplateTranslation = true
    @SerialEntry var oldCapeSneakPosition = false

    // (Screen)
    @SerialEntry var showCrosshairInThirdperson = true
    @SerialEntry var fixHighAttackSpeedIndicator = true
    @SerialEntry var removeHeartFlash = true
    @SerialEntry var fixTextStrikethroughStyle = true
    @SerialEntry var centerScrollableListWidgets = true
    @SerialEntry var oldListWidgetSelectedBorderColor = true
    @SerialEntry var oldButtonTextColors = true
    @SerialEntry var removeDebugHudBackground = true
    @SerialEntry var debugHudTextShadow = true
    @SerialEntry var oldChatVisual = true
    @SerialEntry var disableCameraTransparentPassthrough = true
    @SerialEntry var oldTooltipStyleRendering = true
    @SerialEntry var oldSlotHoverStyleRendering = true
    @SerialEntry var oldEffectsInventoryPosition = true
    @SerialEntry var hideRecipeBook = true
    @SerialEntry var cameraVersion = CameraVersion.V1_8

    // (Items)
    // (Items) Fishing Rod
    @SerialEntry var oldFishingRodTextureStackCheck = true
    @SerialEntry var fishingRodLineInterpolation = true
    @SerialEntry var noMoveFishingRodLine = false
    @SerialEntry var oldFishingRodLinePositionThirdPerson = true
    @SerialEntry var oldFishingRodLineThickness = true
    @SerialEntry var thinFishingRodLineThickness = false
    @SerialEntry var useStickModelWhenCastInThirdperson = true
    @SerialEntry var fixCastLineCheck = true
    @SerialEntry var fixCastLineSwing = true
    // (Items) Fixes
    @SerialEntry var fixEquipAnimation = true
    @SerialEntry var removeEquipAnimationOnItemUse = true
    @SerialEntry var removeClientsideBlockingDelay = true // TODO: To be removed in 1.21.5+
    @SerialEntry var removeItemUsageVisualInGUI = true
    @SerialEntry var fixFireballClientsideVisual = true
    // (Items) Enchantment Glint
    @SerialEntry var oldGlintSpeed = true
    @SerialEntry var disableGlintOnItemDrops2D = false
    @SerialEntry var disableGlintOnItemFramed2D = false
    // (Items) 2D Drops
    @SerialEntry var itemDropsFaceCamera = true
    @SerialEntry var itemDropsFaceCameraRotationFix = false
    @SerialEntry var itemDrops2D = true
    @SerialEntry var itemFramed2D = true
    @SerialEntry var item2DColors = true
    // (Items) Transformations
    @SerialEntry var tiltItemPositions = true
    @SerialEntry var tiltItemPositionsInThirdperson = true
    @SerialEntry var oldThinBlockPositions = true
    @SerialEntry var oldSkullPosition = true
    @SerialEntry var fishingRodVersion = FishingRodVersion.V1_7
    // (Items) Other
    @SerialEntry var applyItemSwingUsage = true
    @SerialEntry var disableSwingOnUse = true
    @SerialEntry var disableSwingOnDrop = true
    @SerialEntry var disableSwingOnEntityInteract = true
    @SerialEntry var disableItemUsingTextureInGui = true
    @SerialEntry var oldDurabilityBarColors = true
    @SerialEntry var oldItemRarities = true
    @SerialEntry var showHeldItemInBoat = true
    @SerialEntry var oldItemPickupPosition = true

    // (Other)
    @SerialEntry var legacyThirdpersonSwordBlockingPosition = true
    @SerialEntry var lockBlockingArmRotation = true
    @SerialEntry var disableProjectileAgeCheck = true
    @SerialEntry var oldBlockMiningProgress = true
    @SerialEntry var disableInventoryEntityScissor = true
    @SerialEntry var legacyBlockOutlineRendering = true
    @SerialEntry var hideModelWhilstSleeping = true
    @SerialEntry var entityArmorHurtTint = true
    @SerialEntry var forceItemGlintOnEntity = true
    @SerialEntry var forceMaxGlintProperties = true
    @SerialEntry var oldArmorHurtRendering = false
    @SerialEntry var oldGlintRendering = false
    @SerialEntry var forceHighAttackSpeedVisual = false
    @SerialEntry var disableEntityGlowOutline = false
    @SerialEntry var disableModernCombatSounds = true
    @SerialEntry var disableModernCombatParticles = true // TODO/NOTE: Fix, it's broken on servers
    @SerialEntry var oldHeldItemArmLogic = true
    @SerialEntry var oldFlameDimensions = true
    @SerialEntry var oldFlameOffset = true
    // (Other) Sky
    @SerialEntry var oldBlueVoidSky = true
    @SerialEntry var oldSkyHorizonHeight = true
    @SerialEntry var oldVoidSkyFogHeight = true
    @SerialEntry var oldCloudHeight = true
}