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

package btw.mixces.animatium.config;

import btw.mixces.animatium.config.category.*;
import btw.mixces.animatium.util.CameraVersion;
import btw.mixces.animatium.util.FishingRodVersion;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.platform.YACLPlatform;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public final class AnimatiumConfig {
    private static final ConfigClassHandler<AnimatiumConfig> CONFIG = ConfigClassHandler.createBuilder(AnimatiumConfig.class)
            .serializer((config) -> GsonConfigSerializerBuilder.create(config)
                    .setPath(YACLPlatform.getConfigDir().resolve("animatium.json"))
                    .build()
            ).build();

    public static Screen getConfigScreen(@Nullable Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, (defaults, config, builder) -> {
            builder.title(Component.translatable("animatium.title"));
            builder.category(QOLConfigCategory.setup(defaults, config));
            builder.category(MovementConfigCategory.setup(defaults, config));
            builder.category(ScreenConfigCategory.setup(defaults, config));
            builder.category(ItemsConfigCategory.setup(defaults, config));
            builder.category(OtherConfigCategory.setup(defaults, config));
            return builder;
        }).generateScreen(parent);
    }

    public static void load() {
        CONFIG.load();
    }

    public static AnimatiumConfig instance() {
        return CONFIG.instance();
    }

    // TODO/NOTE: Category for just fixes?

    // (QOL)
    @SerialEntry public boolean minimalViewBobbing = false;
    @SerialEntry public boolean nameTagInThirdperson = false;
    @SerialEntry public boolean nameTagBackground = true;
    @SerialEntry public boolean nameTagTextShadow = false;
    @SerialEntry public boolean debugHudTextColor = false;
    @SerialEntry public boolean persistentBlockOutline = false;
    @SerialEntry public boolean offhandUsageSwinging = false;
    @SerialEntry public boolean alwaysSharpParticles = false;
    @SerialEntry public boolean recipeAndTutorialToasts = false;
    @SerialEntry public boolean visibleArmWhileInvisible = false;
    @SerialEntry public boolean fakeMissPenaltySwing = false;
    @SerialEntry public boolean dontMoveBlueVoid = false;
    @SerialEntry public boolean usageSwingingParticles = false;
    @SerialEntry public boolean entityDeathTopple = true;
    @SerialEntry public boolean deepRedHurtTint = false;
    @SerialEntry public boolean particlePhysics = true;
    @SerialEntry public boolean firstPersonParticles = true;
    @SerialEntry public boolean dontClearChat = false;
    @SerialEntry public boolean dontCloseChat = false;
    // (QOL) Fixes
    @SerialEntry public boolean fixMirrorArmSwing = true;
    @SerialEntry public boolean upMinPixelTransparencyLimit = true;
    @SerialEntry public boolean fixOffHandUsingPose = true;

    // (Movement)
    // (Movement) Sneaking
    @SerialEntry public boolean smoothSneaking = true;
    @SerialEntry public boolean sneakAnimationInterpolation = false;
    @SerialEntry public boolean fakeOldSneakEyeHeight = false; // TODO/NOTE: continue from here including this
    @SerialEntry public boolean fixSneakingFeetPosition = true;
    @SerialEntry public boolean oldSneakingFeetPosition = true;
    @SerialEntry public boolean syncPlayerModelWithEyeHeight = false;
    @SerialEntry public boolean sneakAnimationWhileFlying = true;
    // (Movement) Other
    @SerialEntry public boolean rotateBackwardsWalking = true;
    @SerialEntry public boolean uncapBlockingHeadRotation = true;
    @SerialEntry public boolean removeHeadRotationInterpolation = true;
    @SerialEntry public boolean fixVerticalBobbingTilt = true;
    @SerialEntry public boolean oldViewBobbing = true;
    @SerialEntry public boolean oldDeathLimbs = true;
    @SerialEntry public boolean oldBowArmMovement = true;
    @SerialEntry public boolean oldDamageTilt = false;
    // (Movement) Cape
    @SerialEntry public boolean oldCapeMovement = true;
    @SerialEntry public boolean dontClampCapeLean = false;
    @SerialEntry public boolean capeSwingRotation = true;
    @SerialEntry public boolean capeChestplateTranslation = true;
    @SerialEntry public boolean oldCapeSneakPosition = false;

    // (Screen)
    @SerialEntry public boolean showCrosshairInThirdperson = false;
    @SerialEntry public boolean fixHighAttackSpeedIndicator = true;
    @SerialEntry public boolean removeHeartFlash = true;
    @SerialEntry public boolean fixTextStrikethroughStyle = true;
    @SerialEntry public boolean centerScrollableListWidgets = false;
    @SerialEntry public boolean oldListWidgetSelectedBorderColor = true;
    @SerialEntry public boolean oldButtonTextColors = true;
    @SerialEntry public boolean removeDebugHudBackground = false;
    @SerialEntry public boolean debugHudTextShadow = false;
    @SerialEntry public boolean disableCameraTransparentPassthrough = false;
    @SerialEntry public boolean oldTooltipStyleRendering = true;
    @SerialEntry public boolean oldSlotHoverStyleRendering = true;
    @SerialEntry public boolean oldEffectsInventoryPosition = true;
    @SerialEntry public boolean hideRecipeBook = false;
    @SerialEntry public CameraVersion cameraVersion = CameraVersion.V1_8;

    // (Items)
    // (Items) Fishing Rod
    @SerialEntry public boolean oldFishingRodTextureStackCheck = true;
    @SerialEntry public boolean fishingRodLineInterpolation = true;
    @SerialEntry public boolean noMoveFishingRodLine = false;
    @SerialEntry public boolean oldFishingRodLinePositionThirdPerson = true;
    @SerialEntry public boolean oldFishingRodLineThickness = true;
    @SerialEntry public boolean thinFishingRodLineThickness = false;
    @SerialEntry public boolean useStickModelWhenCastInThirdperson = true;
    @SerialEntry public boolean fixCastLineCheck = true;
    @SerialEntry public boolean fixCastLineSwing = true;
    // (Items) Fixes
    @SerialEntry public boolean fixEquipAnimation = true;
    @SerialEntry public boolean removeEquipAnimationOnItemUse = true;
    @SerialEntry public boolean removeItemUsageVisualInGUI = true;
    @SerialEntry public boolean fixFireballClientsideVisual = true;
    // (Items) Enchantment Glint
    @SerialEntry public boolean oldGlintSpeed = true;
    @SerialEntry public boolean disableGlintOnItemDrops2D = false;
    @SerialEntry public boolean disableGlintOnItemFramed2D = false;
    // (Items) 2D Drops
    @SerialEntry public boolean itemDropsFaceCamera = true;
    @SerialEntry public boolean itemDropsFaceCameraRotationFix = false;
    @SerialEntry public boolean itemDrops2D = true;
    @SerialEntry public boolean itemFramed2D = true;
    @SerialEntry public boolean item2DColors = true;
    // (Items) Transformations
    @SerialEntry public boolean tiltItemPositions = true;
    @SerialEntry public boolean tiltItemPositionsInThirdperson = true;
    @SerialEntry public boolean oldThinBlockPositions = true;
    @SerialEntry public boolean oldSkullPosition = true;
    @SerialEntry public FishingRodVersion fishingRodVersion = FishingRodVersion.V1_7;
    // (Items) Other
    @SerialEntry public boolean applyItemSwingUsage = true;
    @SerialEntry public boolean disableSwingOnUse = true;
    @SerialEntry public boolean disableSwingOnDrop = true;
    @SerialEntry public boolean disableSwingOnEntityInteract = true;
    @SerialEntry public boolean disableItemUsingTextureInGui = false;
    @SerialEntry public boolean oldDurabilityBarColors = true;
    @SerialEntry public boolean oldItemRarities = true;
    @SerialEntry public boolean showHeldItemInBoat = true;
    @SerialEntry public boolean oldItemPickupPosition = true;

    // (Other)
    @SerialEntry public boolean oldThirdpersonSwordBlockingPosition = true;
    @SerialEntry public boolean lockBlockingArmRotation = true;
    @SerialEntry public boolean disableProjectileAgeCheck = true;
    @SerialEntry public boolean oldBlockMiningProgress = true;
    @SerialEntry public boolean disableInventoryEntityScissor = true;
    @SerialEntry public boolean oldBlockOutlineRendering = true;
    @SerialEntry public boolean hideModelWhilstSleeping = true;
    @SerialEntry public boolean entityArmorHurtTint = true;
    @SerialEntry public boolean forceItemGlintOnEntity = true;
    @SerialEntry public boolean forceMaxGlintProperties = true;
    @SerialEntry public boolean oldArmorHurtRendering = false;
    @SerialEntry public boolean oldGlintRendering = false;
    @SerialEntry public boolean forceHighAttackSpeedVisual = false;
    @SerialEntry public boolean disableEntityGlowOutline = false;
    @SerialEntry public boolean disableModernCombatSounds = false;
    @SerialEntry public boolean disableModernCombatParticles = false; // TODO/NOTE: Fix, it's broken on servers
    @SerialEntry public boolean oldHeldItemArmLogic = true;
    @SerialEntry public boolean oldFlameDimensions = true;
    @SerialEntry public boolean oldFlameOffset = true;
    // (Other) Sky
    @SerialEntry public boolean oldBlueVoidSky = true;
    @SerialEntry public boolean oldSkyHorizonHeight = true;
    @SerialEntry public boolean oldVoidSkyFogHeight = true; // TODO: To be removed in 1.21.5+
    @SerialEntry public boolean oldCloudHeight = true;
}
