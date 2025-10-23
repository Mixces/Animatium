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
import btw.mixces.animatium.util.enums.CameraVersion;
import btw.mixces.animatium.util.enums.FishingRodVersion;
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
    @SerialEntry public boolean sneakingFeetPosition = true;
    @SerialEntry public boolean syncPlayerModelWithEyeHeight = false;
    @SerialEntry public boolean sneakAnimationWhileFlying = true;
    // (Movement) Other
    @SerialEntry public boolean rotateBackwardsWalking = true;
    @SerialEntry public boolean uncapBlockingHeadRotation = true;
    @SerialEntry public boolean headRotationInterpolation = false;
    @SerialEntry public boolean viewBobbing = true;
    @SerialEntry public boolean deathLimbs = true;
    @SerialEntry public boolean bowArmMovement = true;
    @SerialEntry public boolean damageTilt = false;
    @SerialEntry public boolean offsetHurtTime = false;
    // (Movement) Cape
    @SerialEntry public boolean capeMovement = true;
    @SerialEntry public boolean clampCapeLean = false;
    @SerialEntry public boolean capeSwingRotation = true;
    @SerialEntry public boolean capeChestplateTranslation = true;
    @SerialEntry public boolean capeSneakPosition = false;

    // (Screen)
    @SerialEntry public boolean crosshairInThirdPerson = false;
    @SerialEntry public boolean heartFlash = false;
    @SerialEntry public boolean centerScrollableListWidgets = false;
    @SerialEntry public boolean listWidgetSelectedBorderColor = true;
    @SerialEntry public boolean buttonTextColors = true;
    @SerialEntry public boolean debugHudBackground = true;
    @SerialEntry public boolean debugHudTextShadow = false;
    @SerialEntry public boolean cameraTransparentPassthrough = true;
    @SerialEntry public boolean tooltipStyleRendering = true;
    @SerialEntry public boolean slotHoverStyleRendering = true;
    @SerialEntry public boolean effectsInventoryPosition = true;
    @SerialEntry public boolean recipeBook = false;
    @SerialEntry public boolean panoramaRendering = true;
    @SerialEntry public CameraVersion cameraVersion = CameraVersion.V1_8;

    // (Items)
    // (Items) Fishing Rod
    @SerialEntry public boolean fishingRodTextureStackCheck = true;
    @SerialEntry public boolean fishingRodLineInterpolation = true;
    @SerialEntry public boolean noMoveFishingRodLine = false;
    @SerialEntry public boolean fishingRodLinePositionThirdPerson = true;
    @SerialEntry public boolean fishingRodLineThickness = true;
    @SerialEntry public boolean thinFishingRodLineThickness = false;
    @SerialEntry public boolean stickModelWhenCastInThirdperson = true;
    // (Items) Fixes
    @SerialEntry public boolean equipAnimationOnItemUse = false;
    @SerialEntry public boolean itemUsageVisualInGUI = false;
    // (Items) Enchantment Glint
    @SerialEntry public boolean glintSpeed = true;
    @SerialEntry public boolean glintOnItemDrops2D = true;
    @SerialEntry public boolean glintOnItemFramed2D = true;
    // (Items) 2D Drops
    @SerialEntry public boolean itemDropsFaceCamera = true;
    @SerialEntry public boolean itemDropsFaceCameraRotationFix = false;
    //@SerialEntry public boolean itemDrops2D = true;
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
    @SerialEntry public boolean swingOnUse = false;
    @SerialEntry public boolean swingOnDrop = false;
    @SerialEntry public boolean swingOnEntityInteract = false;
    @SerialEntry public boolean itemUsingTextureInGui = true;
    @SerialEntry public boolean durabilityBarColors = true;
    @SerialEntry public boolean itemRarities = true;
    @SerialEntry public boolean heldItemVisibilityInBoat = true;
    @SerialEntry public boolean itemPickupPosition = true;

    // (Fixes)
    @SerialEntry public boolean fixSneakingFeetPosition = true;
    @SerialEntry public boolean fixMirrorArmSwing = true;
    @SerialEntry public boolean fixOffHandUsingPose = true;
    @SerialEntry public boolean fixCastLineCheck = true;
    @SerialEntry public boolean fixCastLineSwing = true;
    @SerialEntry public boolean fixEquipAnimation = true;
    @SerialEntry public boolean fixFireballClientsideVisual = true;
    @SerialEntry public boolean fixTextStrikethroughStyle = true;
    @SerialEntry public boolean fixHighAttackSpeedIndicator = true;
    @SerialEntry public boolean fixVerticalBobbingTilt = true;
    @SerialEntry public boolean upMinPixelTransparencyLimit = true;

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
    @SerialEntry public boolean planarSkyFog = true;
    @SerialEntry public boolean cloudHeight = true;
}
