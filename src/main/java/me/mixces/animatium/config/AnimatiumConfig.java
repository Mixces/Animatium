package me.mixces.animatium.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class AnimatiumConfig extends MidnightConfig {
    // Sneaking
    @Entry(category = "sneaking")
    public static boolean removeSmoothSneaking = false; // Removes the smooth sneaking animation, making it like 1.8-1.12.2

    @Entry(category = "sneaking")
    public static boolean oldSneakAnimationInterpolation = true; // Brings back the <=1.7 sneak animation interpolation

    @Entry(category = "sneaking")
    public static boolean oldSneakEyeHeight = true; // Changes the sneak eye height to be as it was <=1.13.2

    @Entry(category = "sneaking")
    public static boolean fixSneakingFeetPosition = true; // Fixes the sneaking model offset to be like <=1.11

    @Entry(category = "sneaking")
    //TODO/NOTE: Might need a better name
    public static boolean oldSneakingFeetPosition = false; // Fixes the sneaking model offset to be like <1.14?

    @Entry(category = "sneaking")
    public static boolean syncPlayerModelWithEyeHeight = false; // Synchronizes the player model to the eye height <=1.7

    @Entry(category = "sneaking")
    public static boolean sneakAnimationWhileFlying = true; // Shows the sneaking animation in third-person whilst flying down like in <=1.13

    // QOL
    @Entry(category = "qol")
    public static boolean minimalViewBobbing = true; // Remove bobbing from the world

    @Entry(category = "qol")
    public static boolean showNametagInThirdperson = true; // Show the player nametag in thirdperson

    @Entry(category = "qol")
    public static boolean hideNameTagBackground = true; // Hide the nametag background

    @Entry(category = "qol")
    public static boolean applyTextShadowToNametag = true; // Apply text shadow to nametag

    @Entry(category = "qol")
    public static boolean fixMirrorArmSwing = true; // Fixes arm swing mirroring

    @Entry(category = "qol")
    public static boolean persistentBlockOutline = true; // Always show block outline, no matter the gamemode or state

    @Entry(category = "qol")
    public static boolean forceItemGlintOnEntity = true; // Makes the entity glint use the same texture as the item glint like in <=1.19

    // Movement
    @Entry(category = "movement")
    public static boolean rotateBackwardsWalking = true; // Backwards walking rotating the body like in <=1.11

    @Entry(category = "movement")
    public static boolean uncapBlockingHeadRotation = true; // Reverts the change in 1.20.2, making head rotation when blocking as it used to be

    @Entry(category = "movement")
    public static boolean removeHeadRotationInterpolation = true; // Removes the head rotation interpolation

    @Entry(category = "movement")
    public static boolean fixVerticalBobbingTilt = true; // Fixes MC-225335

    @Entry(category = "movement")
    public static boolean oldDeathLimbs = true; // from testing, the only difference is you always fall sideways?

    @Entry(category = "movement")
    public static boolean fixBowArmMovement = true; // Fixes arm movement in third-person when using the bow

    @Entry(category = "movement")
    // TODO/NOTE: Currently not accurate/broken
    public static boolean oldCapeMovement = false; // Changes the cape model movement to be how it used to be

    // Animatium Config
    @Entry
    public static boolean tiltItemPositions = true; // Tilt items to look like they do in 1.7

    @Entry
    public static boolean lockBlockingArmRotation = true; // Locks the third-person blocking arm rotation

    @Entry
    public static boolean applyItemSwingUsage = true; // Block hitting (apply swing offset in item usage code)

    @Entry
    public static boolean removeEquipAnimationOnItemUse = true; // Fixes the blocking animation which plays the equip animation on use, and others

    @Entry
    public static boolean showCrosshairInThirdperson = true; // Show crosshair whilst in thirdperson like in <=1.8

    @Entry
    public static boolean removeHeartFlash = true; // Remove heart blinking like in <=1.7

    @Entry
    public static boolean fixTextStrikethroughStyle = true; // Changes the text strikethrough position to make it look like it did in <=1.12.2

    @Entry
    public static boolean centerScrollableListWidgets = true; // Center scrollable list widgets like <=1.7

    @Entry
    public static boolean oldBlueVoidSky = true; // (MC-257056) Brings back the forgotten blue void part of the sky

    @Entry
    public static boolean oldSkyHorizonHeight = true; // Changes the horizon height to how it was in <=1.16.5

    @Entry
    public static boolean oldCloudHeight = true; // Changes the cloud height back to 128 like in <=1.16.5

    @Entry
    public static boolean oldButtonTextColors = true; // Bring back the old yellow hover/grayish text colors like in <=1.14.4

    @Entry
    public static boolean removeDebugHudBackground = true; // Remove the F3 Debug Hud background

    @Entry
    public static boolean debugHudTextShadow = true; // Add text-shadow to F3 Debug Hud

    @Entry
    public static boolean oldChatPosition = true; // Moves chat down 12 pixels like in <=1.8

    @Entry
    public static CameraVersion cameraVersion = CameraVersion.v1_8; // Change the camera position to be as it was in said version range

    public enum CameraVersion {
        v1_8,
        v1_9_v1_13_2,
        v1_14_v1_14_3,
        LATEST
    }
}
