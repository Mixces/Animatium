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

package org.visuals.legacy.animatium.util.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.config.category.ItemsConfigCategory;
import org.visuals.legacy.animatium.config.category.MovementConfigCategory;
import org.visuals.legacy.animatium.config.category.OtherConfigCategory;
import org.visuals.legacy.animatium.config.category.ScreenConfigCategory;
import org.visuals.legacy.animatium.mixins.accessor.GameRendererAccessor;
import org.visuals.legacy.animatium.util.enums.CameraVersion;
import org.visuals.legacy.animatium.util.enums.FishingRodVersion;

import java.util.function.Consumer;

public enum Version {
    V1_7((final AnimatiumConfig config) -> {
        final MovementConfigCategory movement = config.movement;
        movement.smoothSneaking = true;
        movement.sneakAnimationInterpolation = true;
        movement.fakeOldSneakEyeHeight = true;
        movement.sneakingFeetPosition = true;
        movement.syncPlayerModelWithEyeHeight = true;
        movement.sneakAnimationWhileFlying = true;
        movement.capeMovement = true;
        movement.disableCapeLean = false;
        movement.capeSwingRotation = false;
        movement.capeChestplateTranslation = true;
        movement.capeSneakPosition = true;
        movement.rotateBackwardsWalking = true;
        movement.uncapBlockingHeadRotation = true;
        movement.disableHeadRotationInterpolation = true;
        movement.handViewBobbingMovement = true;
        movement.deathLimbs = true;
        movement.bowArmMovement = true;
        movement.legacyDamageTilt = true;
        movement.offsetHurtTime = true;

        final ItemsConfigCategory items = config.items;
        items.fishingRodTextureStackCheck = true;
        items.fishingRodLineInterpolation = true;
        items.noMoveFishingRodLine = true;
        items.fishingRodLinePositionThirdPerson = true;
        items.fishingRodLineThickness = true;
        items.thinFishingRodLineThickness = false;
        items.stickModelWhenCastInThirdperson = true;
        items.legacyGlintSpeed = true;
        items.glintOnItemDrops2D = true;
        items.glintOnItemFramed2D = true;
        items.itemDropsFaceCamera = true;
        items.itemDropsFaceCameraRotationFix = false;
        items.itemDrops2D = true;
        items.itemFramed2D = true;
        items.itemColors2D = true;
        items.itemPositions = true;
        items.itemPositionsInThirdPerson = true;
        items.thinBlockPositions = true;
        items.skullPosition = true;
        items.fishingRodVersion = FishingRodVersion.V1_7;
        items.itemUsageSwinging = true;
        items.swingOnUse = false;
        items.swingOnDrop = false;
        items.swingOnEntityInteract = false;
        items.itemUsingTextureInGui = false;
        items.durabilityBarColors = true;
        items.legacyItemRarities = true;
        items.heldItemVisibilityInBoat = true;
        items.itemPickupPosition = true;
        items.mobHeadIcons = true;

        final ScreenConfigCategory screen = config.screen;
        screen.crosshairInThirdPerson = true;
        screen.heartFlash = true;
        screen.centerScrollableListWidgets = true;
        screen.listWidgetSelectedBorderColor = true;
        screen.legacyButtonHoverTextColor = true;
        screen.disableDebugHudBackground = true;
        screen.debugHudTextShadow = true;
        screen.cameraTransparentPassthrough = true;
        screen.tooltipStyleRendering = true;
        screen.slotHoverStyleRendering = true;
        screen.listBackgroundGradient = true;
        screen.effectsInventoryPosition = true;
        screen.snappySliderMovement = true;
        screen.hideRecipeBook = true;
        screen.panoramaRendering = true;
        screen.cameraVersion = CameraVersion.V1_8;

        final OtherConfigCategory other = config.other;
        other.thirdPersonSwordBlockingPosition = true;
        other.lockBlockingArmRotation = true;
        other.projectileAgeCheck = true; // TODO/CHECK
        other.blockMiningProgress = true;
        other.disableInventoryEntityScissor = true;
        other.blockOutlineRendering = true;
        other.modelWhilstSleeping = false;
        other.entityArmorHurtTint = true;
        other.itemGlintOnEntity = true;
        other.maxGlintProperties = true;
        other.armorHurtRendering = false;
        other.highAttackSpeedVisual = true;
        other.disableEntityGlowOutline = true;
        other.modernCombatSounds = false;
        other.modernCombatParticles = false;
        other.restoreParticleBlending = true;
        other.heldItemArmLogic = false;
        other.flameDimensions = true;
        other.flameOffset = true;
        other.disableRandomBlockRotations = true;
        other.blueVoidSky = true;
        other.skyHorizonHeight = true;
        other.planarSkyFog = true;
        other.cloudHeight = true;
    }),

    V1_8((final AnimatiumConfig config) -> {
        final MovementConfigCategory movement = config.movement;
        movement.smoothSneaking = false;
        movement.sneakAnimationInterpolation = false;
        movement.fakeOldSneakEyeHeight = true;
        movement.sneakingFeetPosition = false;
        movement.syncPlayerModelWithEyeHeight = false;
        movement.sneakAnimationWhileFlying = true;
        movement.capeMovement = true;
        movement.disableCapeLean = false;
        movement.capeSwingRotation = false;
        movement.capeChestplateTranslation = false;
        movement.capeSneakPosition = false;
        movement.rotateBackwardsWalking = true;
        movement.uncapBlockingHeadRotation = true;
        movement.disableHeadRotationInterpolation = false;
        movement.handViewBobbingMovement = true;
        movement.deathLimbs = true;
        movement.bowArmMovement = false;
        movement.legacyDamageTilt = true;
        movement.offsetHurtTime = false;

        final ItemsConfigCategory items = config.items;
        items.fishingRodTextureStackCheck = true;
        items.fishingRodLineInterpolation = true;
        items.noMoveFishingRodLine = false;
        items.fishingRodLinePositionThirdPerson = false;
        items.fishingRodLineThickness = true;
        items.thinFishingRodLineThickness = false;
        items.stickModelWhenCastInThirdperson = false;
        items.legacyGlintSpeed = true;
        items.glintOnItemDrops2D = false;
        items.glintOnItemFramed2D = false;
        items.itemDropsFaceCamera = false;
        items.itemDropsFaceCameraRotationFix = false;
        items.itemDrops2D = false;
        items.itemFramed2D = false;
        items.itemColors2D = false;
        items.itemPositions = false;
        items.itemPositionsInThirdPerson = false;
        items.thinBlockPositions = false;
        items.skullPosition = true;
        items.fishingRodVersion = FishingRodVersion.V1_8;
        items.itemUsageSwinging = false;
        items.swingOnUse = false;
        items.swingOnDrop = false;
        items.swingOnEntityInteract = false;
        items.itemUsingTextureInGui = false;
        items.durabilityBarColors = true;
        items.legacyItemRarities = true;
        items.heldItemVisibilityInBoat = true;
        items.itemPickupPosition = false;
        items.mobHeadIcons = false;

        final ScreenConfigCategory screen = config.screen;
        screen.crosshairInThirdPerson = true;
        screen.heartFlash = false;
        screen.centerScrollableListWidgets = false;
        screen.listWidgetSelectedBorderColor = true;
        screen.legacyButtonHoverTextColor = true;
        screen.disableDebugHudBackground = false;
        screen.debugHudTextShadow = false;
        screen.cameraTransparentPassthrough = true;
        screen.tooltipStyleRendering = true;
        screen.slotHoverStyleRendering = true;
        screen.listBackgroundGradient = true;
        screen.effectsInventoryPosition = true;
        screen.snappySliderMovement = true;
        screen.hideRecipeBook = true;
        screen.panoramaRendering = true;
        screen.cameraVersion = CameraVersion.V1_8;

        final OtherConfigCategory other = config.other;
        other.thirdPersonSwordBlockingPosition = false;
        other.lockBlockingArmRotation = true;
        other.projectileAgeCheck = true; // TODO/CHECK
        other.blockMiningProgress = true;
        other.disableInventoryEntityScissor = true;
        other.blockOutlineRendering = true;
        other.modelWhilstSleeping = false;
        other.entityArmorHurtTint = false;
        other.itemGlintOnEntity = true;
        other.maxGlintProperties = true;
        other.armorHurtRendering = true;
        other.highAttackSpeedVisual = true;
        other.disableEntityGlowOutline = true;
        other.modernCombatSounds = false;
        other.modernCombatParticles = false;
        other.restoreParticleBlending = true;
        other.heldItemArmLogic = true;
        other.flameDimensions = true;
        other.flameOffset = false;
        other.disableRandomBlockRotations = false;
        other.blueVoidSky = true;
        other.skyHorizonHeight = true;
        other.planarSkyFog = true;
        other.cloudHeight = true;
    }),

    V1_12((final AnimatiumConfig config) -> {
        final MovementConfigCategory movement = config.movement;
        // TODO

        final ItemsConfigCategory items = config.items;
        // TODO

        final ScreenConfigCategory screen = config.screen;
        // TODO

        final OtherConfigCategory other = config.other;
        // TODO
    }),

    MODERN((final AnimatiumConfig config) -> {
        final MovementConfigCategory movement = config.movement;
        movement.smoothSneaking = true;
        movement.sneakAnimationInterpolation = false;
        movement.fakeOldSneakEyeHeight = false;
        movement.sneakingFeetPosition = false;
        movement.syncPlayerModelWithEyeHeight = false;
        movement.sneakAnimationWhileFlying = false;
        movement.capeMovement = false;
        movement.disableCapeLean = false;
        movement.capeSwingRotation = true;
        movement.capeChestplateTranslation = false;
        movement.capeSneakPosition = false;
        movement.rotateBackwardsWalking = false;
        movement.uncapBlockingHeadRotation = false;
        movement.disableHeadRotationInterpolation = false;
        movement.handViewBobbingMovement = false;
        movement.deathLimbs = false;
        movement.bowArmMovement = false;
        movement.legacyDamageTilt = false;
        movement.offsetHurtTime = false;

        final ItemsConfigCategory items = config.items;
        items.fishingRodTextureStackCheck = false;
        items.fishingRodLineInterpolation = false;
        items.noMoveFishingRodLine = false;
        items.fishingRodLinePositionThirdPerson = false;
        items.fishingRodLineThickness = false;
        items.thinFishingRodLineThickness = false;
        items.stickModelWhenCastInThirdperson = false;
        items.legacyGlintSpeed = false;
        items.glintOnItemDrops2D = false;
        items.glintOnItemFramed2D = false;
        items.itemDropsFaceCamera = false;
        items.itemDropsFaceCameraRotationFix = false;
        items.itemDrops2D = false;
        items.itemFramed2D = false;
        items.itemColors2D = false;
        items.itemPositions = false;
        items.itemPositionsInThirdPerson = false;
        items.thinBlockPositions = false;
        items.skullPosition = false;
        items.fishingRodVersion = FishingRodVersion.VANILLA;
        items.itemUsageSwinging = false;
        items.swingOnUse = true;
        items.swingOnDrop = true;
        items.swingOnEntityInteract = true;
        items.itemUsingTextureInGui = true;
        items.durabilityBarColors = false;
        items.legacyItemRarities = false;
        items.heldItemVisibilityInBoat = false;
        items.itemPickupPosition = false;
        items.mobHeadIcons = false;

        final ScreenConfigCategory screen = config.screen;
        screen.crosshairInThirdPerson = false;
        screen.heartFlash = false;
        screen.centerScrollableListWidgets = false;
        screen.listWidgetSelectedBorderColor = false;
        screen.legacyButtonHoverTextColor = false;
        screen.disableDebugHudBackground = false;
        screen.debugHudTextShadow = false;
        screen.cameraTransparentPassthrough = false;
        screen.tooltipStyleRendering = false;
        screen.slotHoverStyleRendering = false;
        screen.listBackgroundGradient = false;
        screen.effectsInventoryPosition = false;
        screen.snappySliderMovement = false;
        screen.hideRecipeBook = false;
        screen.panoramaRendering = false;
        screen.cameraVersion = CameraVersion.VANILLA;

        final OtherConfigCategory other = config.other;
        other.thirdPersonSwordBlockingPosition = false;
        other.lockBlockingArmRotation = false;
        other.projectileAgeCheck = false; // TODO/CHECK
        other.blockMiningProgress = false;
        other.disableInventoryEntityScissor = false;
        other.blockOutlineRendering = false;
        other.modelWhilstSleeping = true;
        other.entityArmorHurtTint = false;
        other.itemGlintOnEntity = false;
        other.maxGlintProperties = false;
        other.armorHurtRendering = false;
        other.highAttackSpeedVisual = false;
        other.disableEntityGlowOutline = false;
        other.modernCombatSounds = true;
        other.modernCombatParticles = true;
        other.restoreParticleBlending = false;
        other.heldItemArmLogic = false;
        other.flameDimensions = false;
        other.flameOffset = false;
        other.disableRandomBlockRotations = false;
        other.blueVoidSky = false;
        other.skyHorizonHeight = false;
        other.planarSkyFog = false;
        other.cloudHeight = false;
    });

    private final Consumer<AnimatiumConfig> applier;

    Version(Consumer<AnimatiumConfig> applier) {
        this.applier = applier;
    }

    public void apply(final AnimatiumConfig config) {
        this.applier.accept(config);

        final Minecraft minecraft = Minecraft.getInstance();
        ((GameRendererAccessor) minecraft.gameRenderer).animatium$setOverlayTexture(new OverlayTexture());
        minecraft.reloadResourcePacks();
    }
}
