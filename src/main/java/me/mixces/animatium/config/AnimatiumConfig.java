package me.mixces.animatium.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class AnimatiumConfig extends MidnightConfig {
    @Entry
    public static boolean rotateBackwardsWalking = true; // Backwards walking rotating the body like in <=1.11

    @Entry
    public static boolean uncapBlockingHeadRotation = true; // Reverts the change in 1.20.2, making head rotation when blocking as it used to be

    @Entry
    public static boolean removeHeadRotationInterpolation = true; // Removes the head rotation interpolation

    @Entry
    public static boolean tiltItemPositions = true; // Tilt items to look like they do in 1.7

    @Entry
    public static boolean lockBlockingArmRotation = true; // Locks the third-person blocking arm rotation

    @Entry
    public static boolean applyItemSwingUsage = true; // Block hitting (apply swing offset in item usage code)

    @Entry
    public static boolean removeEquipAnimationOnItemUse = true; // Fixes the blocking animation which plays the equip animation on use, and others

    @Entry
    public static boolean fixVerticalBobbingTilt = true; // Fixes MC-225335

    @Entry
    public static boolean showCrosshairInThirdperson = true; // Show crosshair whilst in thirdperson like in <=1.8

    @Entry
    public static boolean showNametagInThirdperson = true; // Show the player nametag in thirdperson

    @Entry
    public static boolean hideNameTagBackground = true; // Hide the nametag background

    @Entry
    public static boolean applyTextShadowToNametag = true; // Apply text shadow to nametag

    @Entry
    public static boolean removeSmoothSneaking = true; // Removes the smooth sneaking animation, making it like 1.8-1.12.2

    @Entry
    public static boolean oldSneakEyeHeight = true; // Changes the sneak eye height to be as it was <=1.13.2

    @Entry
    public static boolean fixSneakingFeetPosition = true; // Fixes the sneaking model offset to be like <=1.11

    @Entry
    public static boolean sneakAnimationWhileFlying = true; // Shows the sneaking animation in third-person whilst flying down like in <=1.13

    @Entry
    public static boolean removeHeartFlash = true; // Remove heart blinking like in <=1.7

    @Entry
    public static boolean oldDeathLimbs = true; // from testing, the only difference is you always fall sideways?

    @Entry
    public static boolean fixTextStrikethroughStyle = true; // Changes the text strikethrough position to make it look like it did in <=1.12.2

    @Entry
    public static CameraVersion cameraVersion = CameraVersion.v1_8; // Change the camera position to be as it was in said version range

    public enum CameraVersion {
        v1_8,
        v1_9_v1_13_2,
        v1_14_v1_14_3,
        LATEST
    }
}
