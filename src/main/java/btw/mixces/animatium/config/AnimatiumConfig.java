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
            builder.category(MovementConfigCategory.setup(defaults, config));
            builder.category(ScreenConfigCategory.setup(defaults, config));
            builder.category(ItemsConfigCategory.setup(defaults, config));
            builder.category(FixesConfigCategory.setup(defaults, config));
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

    // (Movement)
    // (Movement) Sneaking
    @SerialEntry public boolean smoothSneaking = true;
    @SerialEntry public boolean sneakAnimationInterpolation = false;
    @SerialEntry public boolean fakeOldSneakEyeHeight = false;
    @SerialEntry public boolean fixSneakingFeetPosition = true;
    @SerialEntry public boolean oldSneakingFeetPosition = true; //todo
    @SerialEntry public boolean syncPlayerModelWithEyeHeight = false;
    @SerialEntry public boolean sneakAnimationWhileFlying = true;
    // (Movement) Other
    @SerialEntry public boolean rotateBackwardsWalking = true;
    @SerialEntry public boolean uncapBlockingHeadRotation = true;
    @SerialEntry public boolean removeHeadRotationInterpolation = true; //todo
    @SerialEntry public boolean fixVerticalBobbingTilt = true;
    @SerialEntry public boolean oldViewBobbing = true; //todo
    @SerialEntry public boolean oldDeathLimbs = true; //todo
    @SerialEntry public boolean oldBowArmMovement = true; //todo
    @SerialEntry public boolean oldDamageTilt = false; //todo
    // (Movement) Cape
    @SerialEntry public boolean oldCapeMovement = true; //todo
    @SerialEntry public boolean dontClampCapeLean = false; //todo
    @SerialEntry public boolean capeSwingRotation = true;
    @SerialEntry public boolean capeChestplateTranslation = true;
    @SerialEntry public boolean oldCapeSneakPosition = false; //todo

    // (Screen)
    @SerialEntry public boolean showCrosshairInThirdperson = false; //todo
    @SerialEntry public boolean fixHighAttackSpeedIndicator = true; //todo
    @SerialEntry public boolean removeHeartFlash = true; //todo
    @SerialEntry public boolean fixTextStrikethroughStyle = true; //todo
    @SerialEntry public boolean centerScrollableListWidgets = false; //todo
    @SerialEntry public boolean oldListWidgetSelectedBorderColor = true; //todo
    @SerialEntry public boolean oldButtonTextColors = true; //todo
    @SerialEntry public boolean removeDebugHudBackground = false; //todo
    @SerialEntry public boolean debugHudTextShadow = false; //todo
    @SerialEntry public boolean disableCameraTransparentPassthrough = false; //todo
    @SerialEntry public boolean oldTooltipStyleRendering = true; //todo
    @SerialEntry public boolean oldSlotHoverStyleRendering = true; //todo
    @SerialEntry public boolean oldEffectsInventoryPosition = true; //todo
    @SerialEntry public boolean hideRecipeBook = false; //todo
    @SerialEntry public CameraVersion cameraVersion = CameraVersion.V1_8;

    // (Items)
    // (Items) Fishing Rod
    @SerialEntry public boolean oldFishingRodTextureStackCheck = true; //todo
    @SerialEntry public boolean fishingRodLineInterpolation = true; //todo
    @SerialEntry public boolean noMoveFishingRodLine = false; //todo
    @SerialEntry public boolean oldFishingRodLinePositionThirdPerson = true; //todo
    @SerialEntry public boolean oldFishingRodLineThickness = true; //todo
    @SerialEntry public boolean thinFishingRodLineThickness = false; //todo
    @SerialEntry public boolean useStickModelWhenCastInThirdperson = true; //todo
    @SerialEntry public boolean fixCastLineCheck = true;
    @SerialEntry public boolean fixCastLineSwing = true;
    // (Items) Fixes
    @SerialEntry public boolean fixEquipAnimation = true;
    @SerialEntry public boolean removeEquipAnimationOnItemUse = true; //todo
    @SerialEntry public boolean removeItemUsageVisualInGUI = true; //todo
    @SerialEntry public boolean fixFireballClientsideVisual = true;
    // (Items) Enchantment Glint
    @SerialEntry public boolean glintSpeed = true;
    @SerialEntry public boolean disableGlintOnItemDrops2D = false; //todo
    @SerialEntry public boolean disableGlintOnItemFramed2D = false; //todo
    // (Items) 2D Drops
    @SerialEntry public boolean itemDropsFaceCamera = true;
    @SerialEntry public boolean itemDropsFaceCameraRotationFix = false;
    @SerialEntry public boolean itemDrops2D = true;
    @SerialEntry public boolean itemFramed2D = true;
    @SerialEntry public boolean itemColors2D = true;
    // (Items) Transformations
    @SerialEntry public boolean itemPositions = true;
    @SerialEntry public boolean itemPositionsInThirdPerson = true;
    @SerialEntry public boolean thinBlockPositions = true;
    @SerialEntry public boolean skullPosition = true;
    @SerialEntry public FishingRodVersion fishingRodVersion = FishingRodVersion.V1_7;
    // (Items) Other
    @SerialEntry public boolean itemUsageSwinging = true;
    @SerialEntry public boolean disableSwingOnUse = true; //todo
    @SerialEntry public boolean disableSwingOnDrop = true; //todo
    @SerialEntry public boolean disableSwingOnEntityInteract = true; //todo
    @SerialEntry public boolean disableItemUsingTextureInGui = false; //todo
    @SerialEntry public boolean durabilityBarColors = true;
    @SerialEntry public boolean itemRarities = true;
    @SerialEntry public boolean heldItemVisibilityInBoat = true;
    @SerialEntry public boolean itemPickupPosition = true;

    // (Fixes)
    @SerialEntry public boolean fixMirrorArmSwing = true;
    @SerialEntry public boolean upMinPixelTransparencyLimit = true;
    @SerialEntry public boolean fixOffHandUsingPose = true;

    // (Other)
    @SerialEntry public boolean thirdPersonSwordBlockingPosition = true;
    @SerialEntry public boolean lockBlockingArmRotation = true;
    @SerialEntry public boolean projectileAgeCheck = false;
    @SerialEntry public boolean blockMiningProgress = true;
    @SerialEntry public boolean inventoryEntityScissor = false;
    @SerialEntry public boolean blockOutlineRendering = true;
    @SerialEntry public boolean modelWhilstSleeping = false;
    @SerialEntry public boolean entityArmorHurtTint = true;
    @SerialEntry public boolean itemGlintOnEntity = true;
    @SerialEntry public boolean maxGlintProperties = true;
    @SerialEntry public boolean armorHurtRendering = false;
    @SerialEntry public boolean glintRendering = false;
    @SerialEntry public boolean highAttackSpeedVisual = false;
    @SerialEntry public boolean entityGlowOutline = true;
    @SerialEntry public boolean modernCombatSounds = true;
    @SerialEntry public boolean modernCombatParticles = true; // TODO/NOTE: Fix, it's broken on servers
    @SerialEntry public boolean heldItemArmLogic = true;
    @SerialEntry public boolean flameDimensions = true;
    @SerialEntry public boolean flameOffset = true;
    // (Other) Sky
    @SerialEntry public boolean blueVoidSky = true;
    @SerialEntry public boolean skyHorizonHeight = true;
    @SerialEntry public boolean voidSkyFogHeight = true; // TODO: To be removed in 1.21.5+
    @SerialEntry public boolean cloudHeight = true;
}
