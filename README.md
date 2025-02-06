# Animatium

Join our discord: https://discord.gg/U48eDmst68

## License

This project is licensed under the GPL-3.0 license.

## Download

You can download the latest releases from Modrinth [here](https://modrinth.com/mod/animatium) or from
the github releases page [here](https://github.com/Legacy-Visuals-Project/Animatium/releases/).

## Dependencies

This mod uses [YACL](https://modrinth.com/mod/yacl) as it's config library of choice. Make sure you install the correct
version to prevent crashing.

## Notes

If you are using Lunar Client, some features will not work as intended.
If you find anything else not working, please report it on the GitHub and we will try to fix it.
Do note that, Lunar does take priority in some places for enabled/disabled settings.
These features currently won't work properly:

- 2d dropped items (Lunar probably needs to update their sodium lmao)

## Support

Have any issues or need support? Feel free to use
our [issue tracker](https://github.com/Legacy-Visuals-Project/Animatium/issues) to address that. If you are reporting a
crash, make sure you include information about the mods you are using and attach any relevant log files you have. If you
want to suggest features, join our [discord](https://discord.gg/U48eDmst68)!

# Available Config Categories

<details>
  <summary>Quality of Life</summary>

## 🪶 Quality of Life

- minimalViewBobbing
    - Description: Removes the view bobbing from tilting the world.
    - Type: BOOLEAN
- showNametagInThirdperson
    - Description: Show the player nametag whilst in third-person.
    - Type: BOOLEAN
- hideNameTagBackground
    - Description: Remove the nametag background.
    - Type: BOOLEAN
- applyTextShadowToNametag
    - Description: Make the nametag use text shadow.
    - Type: BOOLEAN
- oldDebugHudTextColor
    - Description: Makes the debug hud text color white again.
    - Type: BOOLEAN
- persistentBlockOutline
    - Description: Always show block outline, no matter the gamemode or state.
    - Type: BOOLEAN

- alwaysShowSharpParticles
    - Description: Always show the sharpness particles when damaging/hitting an entity.
    - Type: BOOLEAN
- disableRecipeAndTutorialToasts
    - Description: Disable recipe and tutorial toasts.
    - Type: BOOLEAN
- showArmWhileInvisible
    - Description: Shows the arm as partially visible whilst invisible, like spectator mode or invisibly effect.
    - Type: BOOLEAN
- fakeMissPenaltySwing
    - Description: In vanilla Minecraft, if the player has missed their hit, there will be a 10 ms delay on top of the
      attack cooldown before they can can swing again. Enable this feature to play a fake swing animation during that 10
      ms delay to match <=1.7.x.
    - Type: BOOLEAN
- showUsageSwingingParticles
    - Description: Shows fake block-breaking particles during usage swinging to match <=1.7.x.
    - Type: BOOLEAN
- disableEntityDeathTopple
    - Description: Prevents the dying entity from rotating 90 degrees.
    - Type: BOOLEAN
- deepRedHurtTint
    - Description: Modifies the entity damage tint alpha to be less like in Oranges Old Animations mod.
    - Type: BOOLEAN
- disableParticlePhysics
    - Description: Allows particles to bypass collision logic.
    - Type: BOOLEAN
- hideFirstpersonParticles
    - Description: Hides potion particle effects coming from you whilst in first-person.
    - Type: BOOLEAN
- dontClearChat
    - Description: Stops minecraft from clearing chat.
    - Type: BOOLEAN
- dontCloseChat
    - Description: Stops minecraft from closing the chat screen on teleport/some situations.
    - Type: BOOLEAN

### Fixes

- fixMirrorArmSwing
    - Description: Fix the left-arm swing mirroring.
    - Type: BOOLEAN
- upMinPixelTransparencyLimit
    - Description: Makes the minimum 0-transparency value less than or equal to 0.1. This fixes textures with invisible
      pixels that cause issues.
    - Type: BOOLEAN
- fixOffHandUsingPose
    - Description: Stops the offhand from using the NONE pose with a held item while using an item in the mainhand like
      in <=1.17.
    - Type: BOOLEAN
  </details>

<details>
  <summary>Movement</summary>

## 🏃 Movement

### Sneaking

- removeSmoothSneaking
    - Description: Removes the smooth sneaking camera animation, making it like it was in 1.8-1.12.2.
    - Type: BOOLEAN
- oldSneakAnimationInterpolation
    - Description: Brings back the <=1.7.x sneaking camera animation interpolation.
    - Type: BOOLEAN
- fakeOldSneakEyeHeight
    - Description: Changes the sneak eye height to be as it was in <=1.13.2 visually.
    - Type: BOOLEAN
- fixSneakingFeetPosition
    - Description: Fixes the sneaking model offset to be like <=1.11.x.
    - Type: BOOLEAN
- oldSneakingFeetPosition
    - Description: Fixes the sneaking model offset to be like <1.14?
    - Type: BOOLEAN
- syncPlayerModelWithEyeHeight
    - Description: Synchronizes the player model to the eye height like in <=1.7.x.
    - Type: BOOLEAN
- sneakAnimationWhileFlying
    - Description: Shows the sneaking animation in third-person whilst flying down like in <=1.13.x.
    - Type: BOOLEAN

### Cape

- oldCapeMovement
    - Description: Changes the cape model movement to be how it used to be in <=1.12.x.
    - Type: BOOLEAN
- dontClampCapeLean
    - Description: Removes the cape lean restriction. Disable this to match OptiFine cape physics.
    - Type: BOOLEAN
- capeSwingRotation
    - Description: Stops the cape from swinging in unison with the body while the player is swinging their arm like in <
      =1.20.x.
    - Type: BOOLEAN
- capeChestplateTranslation
    - Description: Stops equipping a chestplate causing the cape to be translated a few pixels away like in <=1.15.x
    - Type: BOOLEAN
- oldCapeSneakPosition
    - Description: Positions the cape while sneaking similarly to <=1.7.x
    - Type: BOOLEAN

### Other

- rotateBackwardsWalking
    - Description: Rotates the entity body sideways when walking backwards like it was in <=1.11.2.
    - Type: BOOLEAN
- uncapBlockingHeadRotation
    - Description: Reverts the change in 1.20.2, making head rotation when blocking as it used to be.
    - Type: BOOLEAN
- removeHeadRotationInterpolation
    - Description: Removes the head rotation interpolation like in <=1.7.x.
    - Type: BOOLEAN
- fixVerticalBobbingTilt
    - Description: Brings back the camera tilting when falling/flying up like it was in <=1.13.x. (Fixes MC-225335)
    - Type: BOOLEAN
- oldViewBobbing
    - Description: Undoes the 1.21.2+ view bobbing change where when sneaking, your hand still moves normally.
    - Type: BOOLEAN
- oldDeathLimbs
    - Description: Makes entities continue their animation even upon death.
    - Type: BOOLEAN
- oldBowArmMovement
    - Description: Restores old player body movement in third-person when using the bow like in <=1.7?
    - Type: BOOLEAN
- oldDamageTilt
    - Description: Reverts the damage tilt to it's old logic which will tilt in one direction <1.19.4.
    - Type: BOOLEAN
  </details>

<details>
  <summary>Screen</summary>

## 📷 Screen

- showCrosshairInThirdperson
    - Description: Show crosshair whilst in thirdperson like in <=1.8.x.
    - Type: BOOLEAN
- fixHighAttackSpeedIndicator
    - Description: Hides the attack indicator when you have such a high attack speed. (Fixes MC-268420)
    - Type: BOOLEAN
- removeHeartFlash
    - Description: Remove heart blinking like in <=1.7.x.
    - Type: BOOLEAN
- fixTextStrikethroughStyle
    - Description: Changes the text strikethrough position to make it look like it did in <=1.12.2.
    - Type: BOOLEAN
- centerScrollableListWidgets
    - Description: Center scrollable list widgets like <=1.7.x.
    - Type: BOOLEAN
- oldListWidgetSelectedBorderColor
    - Description: Returns the old list widget selected border color from <=1.15?
    - Type: BOOLEAN
- oldButtonTextColors
    - Description: Bring back the old yellow hover/grayish text colors like in <=1.14.4.
    - Type: BOOLEAN
- removeDebugHudBackground
    - Description: Remove the F3 Debug Hud background.
    - Type: BOOLEAN
- debugHudTextShadow
    - Description: Add text-shadow to F3 Debug Hud.
    - Type: BOOLEAN
- oldChatVisual
    - Description: Restores the old chatbox position/visual from <=1.8.
    - Type: BOOLEAN
- disableCameraTransparentPassthrough
    - Description: Stops camera passthrough in thirdperson in glass/etc like in <=1.15.
    - Type: BOOLEAN
- oldTooltipStyleRendering
    - Description: Restores the corners of the tooltip texture that were removed in 1.21.2.
    - Type: BOOLEAN
    - Note: If you are using a resource pack with a custom tooltip texture, turn this setting OFF to not cause issues!
- oldSlotHoverStyleRendering
    - Description: Restores the old inventory slot hover visual to how it was prior to 1.21.2.
    - Type: BOOLEAN
    - Note: If you are using a resource pack with a custom slot hover texture, turn this setting OFF to not cause
      issues!
- cameraVersion
    - Description: Change the camera position to be as it was in said version range.
    - Type: ENUM
        - 1.8 and below (V1_8)
        - 1.9 through to 1.13.2 (V1_9_V1_13_2)
        - 1.14 through to 1.14.3 (V1_14_V1_14_3)
        - LATEST
  </details>

<details>
  <summary>Items</summary>

## 🥍 Items

### Fishing Rod

- oldFishingRodTextureStackCheck
    - Description: Brings back old fishing rod stack texture check from <=1.8.
    - Type: BOOLEAN
- fishingRodLineInterpolation
    - Description: Correctly interpolates the fishing rod cast line with the eye height from <1.14?
    - Type: BOOLEAN
- noMoveFishingRodLine
    - Description: Does not move the fishing rod cast line while sneaking when viewed in the third person mode from <
      =1.7.
    - Type: BOOLEAN
- oldFishingRodLinePositionThirdPerson
    - Description: Adjusts the position of the fishing rod cast line horizontally like in <=1.7.
    - Type: BOOLEAN
- oldFishingRodLineThickness
    - Description: Restores the old fishing rod line thickness from <1.13?
    - Type: BOOLEAN
- thinFishingRodLineThickness
    - Description: Makes the fishing rod line super thin. Overrides the above setting.
    - Type: BOOLEAN
- useStickModelWhenCastInThirdperson
    - Description: Makes the fishing rod model in third-person a stick when cast like in <=1.7.x.
    - Type: BOOLEAN
- fixCastLineCheck
    - Description: Fixes the arm logic for casting the fishing rod.
    - Type: BOOLEAN
- fixCastLineSwing
    - Description: Fixes the swing logic for casting the fishing rod.
    - Type: BOOLEAN

### Fixes

- fixEquipAnimation
    - Description: Instead of comparing item stacks directly to determine the equip animation, compare the durability
      and stack count of the items like in <=1.8.x.
    - Type: BOOLEAN
- removeEquipAnimationOnItemUse
    - Description: Fixes the blocking animation which plays the equip animation on use, and others.
    - Type: BOOLEAN
- removeClientsideBlockingDelay
    - Description: Removes the pesky blocking delay that modern clients have. Shouldn't flag on servers.
    - Type: BOOLEAN
- removeItemUsageVisualInGUI
    - Description: Removes item usage animation whilst inside a GUI, for example removes continuous visual blocking,
      etc.
    - Type: BOOLEAN
- fixFireballClientsideVisual
    - Description: Makes fire charges not place fire clientside like in older mc versions. Doesn't cause issues on
      servers, and is clientside only.
    - Type: BOOLEAN

### Enchantment Glint

- oldGlintSpeed
    - Description: Restores the old enchantment glint speed like in <=1.8.x.
    - Type: BOOLEAN
- disableGlintOnItemDrops2D
    - Description: Disables the enchantment glint on dropped items. Intended to be used along side the 2D dropped items
      feature to match <1.7.x.
    - Type: BOOLEAN
- disableGlintOnItemFramed2D
    - Description: Disables the enchantment glint on framed items. Intended to be used along side the 2D framed items
      feature to match <1.7.x.
    - Type: BOOLEAN

### 2D Drops

- itemDropsFaceCamera
    - Description: Makes item entities face the camera / use camera yaw like <=1.7.x when fast graphics.
    - Type: BOOLEAN
- itemDropsFaceCameraRotationFix
    - Description: Makes 2d item drops also face the camera pitch.
    - Type: BOOLEAN
- itemDrops2D
    - Description: Makes item entities render 2D when it's an item (not blocks).
    - Type: BOOLEAN
- itemFramed2D
    - Description: Makes framed items render 2D (not blocks).
    - Type: BOOLEAN
- item2DColors
    - Description: Restores the old color of 2D items by swapping the Y and Z components of the vertex normal.
    - Type: BOOLEAN

### Item Transformations

- tiltItemPositions
    - Description: Tilts the held item position to make held items look like they did in <=1.7.x.
    - Type: BOOLEAN
- tiltItemPositionsInThirdperson
    - Description: Tilts the third-person held item position to make held items look like they did in <=1.7.x.
    - Type: BOOLEAN
- oldThinBlockPositions
    - Description: Translates the held item position of blocks like carpet/slabs/daylight sensors/pressure plates to
      look like how they did in <=1.7.x.
    - Type: BOOLEAN
- oldSkullPosition
    - Description: Positions the skull block items' held/gui positions to be how it was in 1.8.x.
    - Type: BOOLEAN
- fishingRodVersion
    - Description: Positions the fishing rod's first-person position to be how it was in said version range.
    - Type: ENUM
        - 1.7 and below (V1_7)
        - 1.8 (V1_8)
        - LATEST

### Other

- applyItemSwingUsage
    - Description: Block hitting (apply swing offset in item usage code).
    - Type: BOOLEAN
- disableItemUsingTextureInGui
    - Description: Disables the item usage texture in the GUI like in <=1.8.x (mainly rod/bow/crossbow).
    - Type: BOOLEAN
- oldDurabilityBarColors
    - Description: Restores the old durability damage colors from <1.11.
    - Type: BOOLEAN
- oldItemRarities
    - Description: Restores the old rarities for items visually from <1.21.2. (also old trident rarity from <1.21)
    - Type: BOOLEAN
- showHeldItemInBoat
    - Description: Shows your held item while you're in a moving boat like <=1.8.x.
    - Type: BOOLEAN
  </details>

<details>
  <summary>Old Settings</summary>

## 🛠️ Old Settings

- legacyThirdpersonSwordBlockingPosition
    - Description: Brings back the old third-person arm blocking rotations from <=1.7
    - Type: BOOLEAN
- lockBlockingArmRotation
    - Description: Locks the third-person blocking arm rotation.
    - Type: BOOLEAN
- disableProjectileAgeCheck
    - Description: Render projectile at all ages <=1.15?
    - Type: BOOLEAN
- oldBlockMiningProgress
    - Description: Bring back the old block mining progress <=1.18?
    - Type: BOOLEAN
- disableInventoryEntityScissor
    - Description: Allows the inventory entity model to render fully.
    - Type: BOOLEAN
- legacyBlockOutlineRendering
    - Description: Restores the legacy block outline rendering from <=1.14.4.
    - Type: BOOLEAN
- hideModelWhilstSleeping
    - Description: Hides the player model whilst sleeping like in <=1.12? Only affects you.
    - Type: BOOLEAN
- entityArmorHurtTint
    - Description: Tints the armor when an entity is damaged like in <=1.7.x.
    - Type: BOOLEAN
- forceItemGlintOnEntity
    - Description: Forces the glint on armor to use the item glint texture. This therefore unifies the glint texture
      like in older mc versions.
    - Type: BOOLEAN
- forceMaxGlintProperties
    - Description: Forces the glint to use the maximum speed and strength by default like in older mc versions.
    - Type: BOOLEAN
- oldArmorHurtRendering
    - Description: Restores the old armor hurt tint rendering from ~1.8.
    - Type: BOOLEAN
- forceHighAttackSpeedVisual
    - Description: Fakes the high attack speed visual, which stops the attack cooldown animation on items like the
      sword.
    - Type: BOOLEAN
- disableEntityGlowOutline
    - Description: Disables the 1.9+ glow effect from rendering.
    - Type: BOOLEAN
- disableModernCombatSounds
    - Description: Disables the 1.9+ combat sounds that were added.
    - Type: BOOLEAN
- disableModernCombatParticles
    - Description: Disables the 1.9+ combat particles that were added.
    - Type: BOOLEAN
- oldHeldItemArmLogic
    - Description: In 1.8, the player's arm (when viewed from the first person POV) will be positioned at an angle when
      holding an item. This is only truly visible when going from an empty slot to an item. This happens due it applying
      the held item arm rotation meant for the third person model.
    - Type: BOOLEAN
- oldFlameDimensions
    - Description: Makes the third person flame rendering use a fixed width and height to match <=1.12.x.
    - Type: BOOLEAN
- oldFlameOffset
    - Description: Brings back the third person flame offset from <=1.7.x.
    - Type: BOOLEAN

### Sky

- oldBlueVoidSky
    - Description: Brings back the forgotten blue void part of the sky. (Fixes MC-257056)
    - Type: BOOLEAN
- oldSkyHorizonHeight
    - Description: Changes the horizon height to how it was in <=1.16.5.
    - Type: BOOLEAN
- oldVoidSkyFogHeight
    - Description: Restores the old void sky fog height to what it was in <=1.21.1. (Fixes MC-279472)
    - Type: BOOLEAN
- oldCloudHeight
    - Description: Changes the cloud height back to 128 like in <=1.16.5.
    - Type: BOOLEAN
  </details>

## Server Features

We currently have one payload which servers can use to change game functionality for pvp. Only servers can
enable/disable this, to not cause issues on other servers.
If more are requested/wanted, we will add and update this here.

### Payloads

#### Set Features (Clientbound)

Allows the server to enable/disable server-only features that enhance gameplay.

| Feature Name          | Identifier            | Description                                                             |
|-----------------------|:----------------------|:------------------------------------------------------------------------|
| Miss Penalty          | miss_penalty          | Turn on/off the swing miss penalty                                      |
| Left Click Item Usage | left_click_item_usage | Turn on/off the ability to start using a item whilst holding left click |

| Identifier             | Field Name    | Field Type     | Description                                                                                  |
|------------------------|:--------------|:---------------|:---------------------------------------------------------------------------------------------|
| animatium:set_features | Features List | List\<String\> | List of features the client should use currently, maps to Feature above or skips if unknown. |

#### Info (Serverbound)

Sent to the server when receiving the "minecraft:register" payload, and it contains "Animatium".

| Identifier     | Field Name          | Field Type      | Description                                                                         |
|----------------|---------------------|-----------------|-------------------------------------------------------------------------------------|
| animatium:info | Version             | Double          | Current release version of the mod.                                                 |
|                | Development Version | Optional String | Current dev build commit of the mod. Only provided when mod is a development build. |