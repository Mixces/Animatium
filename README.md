# Animatium

erm what the sigma

## License

This project is licensed under the LGPL-2.1 license.

## Download

Currently, there are no releases. If you want to use a development build, you can get
them [here](https://github.com/Legacy-Visuals-Project/Animatium/actions).

# Available Config Categories

## 💨 Sneaking

- [X] removeSmoothSneaking
    - Description: TODO
    - Type: BOOLEAN
- [X] oldSneakAnimationInterpolation
    - Description: TODO
    - Type: BOOLEAN
- [X] oldSneakEyeHeight
    - Description: TODO
    - Type: BOOLEAN
- [X] fixSneakingFeetPosition
    - Description: TODO
    - Type: BOOLEAN
- [X] oldSneakingFeetPosition
    - Description: TODO
    - Type: BOOLEAN
- [X] syncPlayerModelWithEyeHeight
    - Description: TODO
    - Type: BOOLEAN
- [X] sneakAnimationWhileFlying
    - Description: TODO
    - Type: BOOLEAN

## 🪶 Quality of Life

- [X] minimalViewBobbing
    - Description: TODO
    - Type: BOOLEAN
- [X] showNametagInThirdperson
    - Description: TODO
    - Type: BOOLEAN
- [X] hideNameTagBackground
    - Description: TODO
    - Type: BOOLEAN
- [X] applyTextShadowToNametag
    - Description: TODO
    - Type: BOOLEAN
- [X] fixMirrorArmSwing
    - Description: TODO
    - Type: BOOLEAN
- [X] persistentBlockOutline
    - Description: Always show block outline, no matter the gamemode or state.
    - Type: BOOLEAN
- [X] alwaysAllowUsageSwinging
    - Description: Allows you to hold left click whilst using item to swing the arm always, not just when looking at a
      block.
    - Type: BOOLEAN
    - Requires:
        - applyItemSwingUsage
- [X] alwaysShowSharpParticles
    - Description: Always show sharp particles when damaging an entity
    - Type: BOOLEAN
- [ ] forceItemGlintOnEntity
    - Description: Makes the entity glint use the same texture as the item glint like in <=1.19
    - Type: BOOLEAN

## 🏃 Movement

- [X] rotateBackwardsWalking
    - Description: TODO
    - Type: BOOLEAN
- [X] uncapBlockingHeadRotation
    - Description: TODO
    - Type: BOOLEAN
- [X] removeHeadRotationInterpolation
    - Description: TODO
    - Type: BOOLEAN
- [X] fixVerticalBobbingTilt
    - Description: TODO
    - Type: BOOLEAN
- [X] oldDeathLimbs
    - Description: TODO
    - Type: BOOLEAN
- [X] fixBowArmMovement
    - Description: TODO
    - Type: BOOLEAN
- [ ] oldCapeMovement
    - Description: TODO
    - Type: BOOLEAN

## 🎣 Fishing Rod

- [X] oldFishingRodTextureStackCheck
    - Description: TODO
    - Type: BOOLEAN
- [X] fishingRodLineInterpolation
    - Description: TODO
    - Type: BOOLEAN
- [X] noMoveFishingRodLine
    - Description: TODO
    - Type: BOOLEAN
- [X] oldFishingRodLinePositionThirdPerson
    - Description: TODO
    - Type: BOOLEAN
- [X] fixCastLineCheck
    - Description: TODO
    - Type: BOOLEAN
- [X] fixCastLineSwing
    - Description: TODO
    - Type: BOOLEAN

## 🛠️ Old Settings

- [X] tiltItemPositions
    - Description: TODO
    - Type: BOOLEAN
- [X] lockBlockingArmRotation
    - Description: TODO
    - Type: BOOLEAN
- [X] applyItemSwingUsage
    - Description: TODO
    - Type: BOOLEAN
- [X] removeEquipAnimationOnItemUse
    - Description: TODO
    - Type: BOOLEAN
- [X] showCrosshairInThirdperson
    - Description: TODO
    - Type: BOOLEAN
- [X] removeHeartFlash
    - Description: TODO
    - Type: BOOLEAN
- [X] fixTextStrikethroughStyle
    - Description: TODO
    - Type: BOOLEAN
- [X] centerScrollableListWidgets
    - Description: TODO
    - Type: BOOLEAN
- [X] oldBlueVoidSky
    - Description: TODO
    - Type: BOOLEAN
- [X] oldSkyHorizonHeight
    - Description: TODO
    - Type: BOOLEAN
- [X] oldCloudHeight
    - Description: TODO
    - Type: BOOLEAN
- [X] oldButtonTextColors
    - Description: TODO
    - Type: BOOLEAN
- [X] removeDebugHudBackground
    - Description: TODO
    - Type: BOOLEAN
- [X] debugHudTextShadow
    - Description: TODO
    - Type: BOOLEAN
- [X] oldChatPosition
    - Description: TODO
    - Type: BOOLEAN
- [X] oldProjectilePosition
    - Description: TODO
    - Type: BOOLEAN
- [X] disableProjectileAgeCheck
    - Description: TODO
    - Type: BOOLEAN
- [X] oldBlockMiningProgress
    - Description: TODO
    - Type: BOOLEAN
- [X] disableItemUsingTextureInGui
    - Description: Disables the item usage texture in the GUI like in <=1.8 (mainly rod/bow/crossbow)
    - Type: BOOLEAN
- [X] disableInventoryEntityScissor
    - Description: Allows the inventory entity model to render fully
    - Type: BOOLEAN
- [X] disableCameraTransparentPassthrough
    - Description: Stops camera passthrough in thirdperson in glass/etc
    - Type: BOOLEAN
- [X] itemDropsFaceCamera
    - Description: Makes item entities face the camera / use camera yaw like <=1.7 when fast graphics
    - Type: BOOLEAN
- [X] cameraVersion
    - Description: TODO
    - Type: ENUM
        - 1.8 and below (v1_8)
        - 1.9 through to 1.13.2 (v1_9_v1_13_2)
        - 1.14 through to 1.14.3 (v1_14_v1_14_3)
        - LATEST