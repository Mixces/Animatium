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
    - Description: Removes the smooth sneaking camera animation, making it like it was in 1.8-1.12.2.
    - Type: BOOLEAN
- [X] oldSneakAnimationInterpolation
    - Description: Brings back the <=1.7.x sneaking camera animation interpolation.
    - Type: BOOLEAN
- [X] oldSneakEyeHeight
    - Description: Changes the sneak eye height to be as it was <=1.13.2.
    - Type: BOOLEAN
- [X] fixSneakingFeetPosition
    - Description: Fixes the sneaking model offset to be like <=1.11.x.
    - Type: BOOLEAN
- [X] oldSneakingFeetPosition
    - Description: Fixes the sneaking model offset to be like <1.14?
    - Type: BOOLEAN
- [X] syncPlayerModelWithEyeHeight
    - Description: Synchronizes the player model to the eye height like in <=1.7.x.
    - Type: BOOLEAN
- [X] sneakAnimationWhileFlying
    - Description: Shows the sneaking animation in third-person whilst flying down like in <=1.13.x.
    - Type: BOOLEAN

## 🪶 Quality of Life

- [X] minimalViewBobbing
    - Description: Removes the view bobbing from tilting the world.
    - Type: BOOLEAN
- [X] showNametagInThirdperson
    - Description: Show the player nametag whilst in third-person.
    - Type: BOOLEAN
- [X] hideNameTagBackground
    - Description: Remove the nametag background.
    - Type: BOOLEAN
- [X] applyTextShadowToNametag
    - Description: Make the nametag use text shadow.
    - Type: BOOLEAN
- [X] oldDebugHudTextColor
    - Description: Makes the debug hud text color white again.
    - Type: BOOLEAN
- [X] fixMirrorArmSwing
    - Description: Fix the left-arm swing mirroring.
    - Type: BOOLEAN
- [X] persistentBlockOutline
    - Description: Always show block outline, no matter the gamemode or state.
    - Type: BOOLEAN
- [X] alwaysAllowUsageSwinging
    - Description: Allows you to hold left click whilst using item to swing the arm always, not just when looking at a
      block. Requires applyItemSwingUsage to be enabled.
    - Type: BOOLEAN
    - Requires:
        - applyItemSwingUsage
- [X] alwaysShowSharpParticles
    - Description: Always show the sharpness particles when damaging/hitting an entity.
    - Type: BOOLEAN
- [ ] forceItemGlintOnEntity
    - Description: Makes the entity glint use the same texture as the item glint like it was in <=1.19.
    - Type: BOOLEAN
- [X] disableRecipeAndTutorialToasts
  - Description: Disable recipe and tutorial toasts.
  - Type: BOOLEAN
- [X] disablePoseUpdates
  - Description: Stops the server from updating your pose/animations. (Fixes MC-159163)
  - Type: BOOLEAN

## 🏃 Movement

- [X] rotateBackwardsWalking
    - Description: Rotates the entity body sideways when walking backwards like it was in <=1.11.2.
    - Type: BOOLEAN
- [X] uncapBlockingHeadRotation
    - Description: Reverts the change in 1.20.2, making head rotation when blocking as it used to be.
    - Type: BOOLEAN
- [X] removeHeadRotationInterpolation
    - Description: Removes the head rotation interpolation like in <=1.7.x.
    - Type: BOOLEAN
- [X] fixVerticalBobbingTilt
    - Description: Brings back the camera tilting when falling/flying up like it was in <=1.13.x. (Fixes MC-225335)
    - Type: BOOLEAN
- [X] oldDeathLimbs
    - Description: Makes entities continue their animation even upon death.
    - Type: BOOLEAN
- [X] fixBowArmMovement
    - Description: Restores old player body movement in third-person when using the bow like in <=1.7?
    - Type: BOOLEAN
- [ ] oldCapeMovement
    - Description: Changes the cape model movement to be how it used to be in <=1.8? Currently broken and doesn't work
      properly.
    - Type: BOOLEAN

## 🎣 Fishing Rod

- [X] oldFishingRodTextureStackCheck
    - Description: Brings back old fishing rod stack texture check from <=1.8.
    - Type: BOOLEAN
- [X] fishingRodLineInterpolation
    - Description: Correctly interpolates the fishing rod cast line with the eye height from <1.14?
    - Type: BOOLEAN
- [X] noMoveFishingRodLine
    - Description: Does not move the fishing rod cast line while sneaking when viewed in the third person mode from <
      =1.7.
    - Type: BOOLEAN
- [X] oldFishingRodLinePositionThirdPerson
    - Description: Adjusts the position of the fishing rod cast line horizontally like in <=1.7.
    - Type: BOOLEAN
- [X] fixCastLineCheck
    - Description: Fixes the arm logic for casting the fishing rod.
    - Type: BOOLEAN
- [X] fixCastLineSwing
    - Description: Fixes the swing logic for casting the fishing rod.
    - Type: BOOLEAN

## 🛠️ Old Settings

- [X] tiltItemPositions
    - Description: Tilts the held item position to make held items look like they did in <=1.7.x.
    - Type: BOOLEAN
- [X] tiltItemPositions
    - Description: Tilts the third-person held item position to make held items look like they did in <=1.7.x.
    - Type: BOOLEAN
- [X] legacyThirdpersonSwordBlockingPosition
    - Description: Brings back the old third-person sword blocking look from <=1.7.
    - Type: BOOLEAN
- [X] lockBlockingArmRotation
    - Description: Locks the third-person blocking arm rotation.
    - Type: BOOLEAN
- [X] applyItemSwingUsage
    - Description: Block hitting (apply swing offset in item usage code).
    - Type: BOOLEAN
- [X] removeEquipAnimationOnItemUse
    - Description: Fixes the blocking animation which plays the equip animation on use, and others.
    - Type: BOOLEAN
- [X] showCrosshairInThirdperson
    - Description: Show crosshair whilst in thirdperson like in <=1.8.x.
    - Type: BOOLEAN
- [X] removeHeartFlash
    - Description: Remove heart blinking like in <=1.7.x.
    - Type: BOOLEAN
- [X] fixTextStrikethroughStyle
    - Description: Changes the text strikethrough position to make it look like it did in <=1.12.2.
    - Type: BOOLEAN
- [X] centerScrollableListWidgets
    - Description: Center scrollable list widgets like <=1.7.x.
    - Type: BOOLEAN
- [X] oldListWidgetSelectedBorderColor
    - Description: Returns the old list widget selected border color from <=1.15?
    - Type: BOOLEAN
- [X] oldBlueVoidSky
    - Description: Brings back the forgotten blue void part of the sky. (Fixes MC-257056)
    - Type: BOOLEAN
- [X] oldSkyHorizonHeight
    - Description: Changes the horizon height to how it was in <=1.16.5.
    - Type: BOOLEAN
- [X] oldCloudHeight
    - Description: Changes the cloud height back to 128 like in <=1.16.5.
    - Type: BOOLEAN
- [X] oldButtonTextColors
    - Description: Bring back the old yellow hover/grayish text colors like in <=1.14.4.
    - Type: BOOLEAN
- [X] removeDebugHudBackground
    - Description: Remove the F3 Debug Hud background.
    - Type: BOOLEAN
- [X] debugHudTextShadow
    - Description: Add text-shadow to F3 Debug Hud.
    - Type: BOOLEAN
- [X] oldChatPosition
    - Description: Moves chat down 12 pixels like in <=1.8.x.
    - Type: BOOLEAN
- [X] oldProjectilePosition
    - Description: Brings back old projectile position <=1.8.x.
    - Type: BOOLEAN
- [X] disableProjectileAgeCheck
    - Description: Render projectile at all ages <=1.15?
    - Type: BOOLEAN
- [X] oldBlockMiningProgress
    - Description: Bring back the old block mining progress <=1.18?
    - Type: BOOLEAN
- [X] disableItemUsingTextureInGui
    - Description: Disables the item usage texture in the GUI like in <=1.8.x (mainly rod/bow/crossbow).
    - Type: BOOLEAN
- [X] disableInventoryEntityScissor
    - Description: Allows the inventory entity model to render fully.
    - Type: BOOLEAN
- [X] disableCameraTransparentPassthrough
    - Description: Stops camera passthrough in thirdperson in glass/etc.
    - Type: BOOLEAN
- [X] itemDropsFaceCamera
    - Description: Makes item entities face the camera / use camera yaw like <=1.7.x when fast graphics.
    - Type: BOOLEAN
- [X] itemDrops2D
    - Description: Makes item entities render 2D when it's an item (not blocks).
    - Type: BOOLEAN
- [X] oldDurabilityBarColors
    - Description: Restores the old durability damage colors from <1.11.
    - Type: BOOLEAN
- [X] oldItemRarities
    - Description: Restores the old rarities for items visually from <1.21.2. (also old trident rarity from <1.21)
    - Type: BOOLEAN
- [X] removeClientsideBlockingDelay
    - Description: Removes the pesky blocking delay that modern clients have. Shouldn't flag on servers.
    - Type: BOOLEAN
- [X] cameraVersion
    - Description: Change the camera position to be as it was in said version range.
    - Type: ENUM
        - 1.8 and below (V1_8)
        - 1.9 through to 1.13.2 (V1_9_V1_13_2)
        - 1.14 through to 1.14.3 (V1_14_V1_14_3)
        - LATEST