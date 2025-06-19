# Animatium

Join our discord: https://discord.gg/U48eDmst68

## License

This project is licensed under the GPL-3.0 license w/ Minecraft Linking Exception.

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
  <summary>Movement</summary>

## 🏃 Movement

### Sneaking

- smoothSneaking
    - Description: Enable/disable the smooth sneaking camera animation, making it like it was in 1.8-1.12.2.
    - Type: BOOLEAN
- sneakAnimationInterpolation
    - Description: Brings back the sneaking camera animation interpolation from <=1.7.x.
    - Type: BOOLEAN
- fakeOldSneakEyeHeight
    - Description: Changes the sneak eye height to be as it was in <=1.13.2 visually.
    - Type: BOOLEAN
- sneakingFeetPosition
    - Description: Fixes the sneaking model offset to be like <1.14?
    - Type: BOOLEAN
- syncPlayerModelWithEyeHeight
    - Description: Synchronizes the player model to the eye height like in <=1.7.x.
    - Type: BOOLEAN
- sneakAnimationWhileFlying
    - Description: Shows the sneaking animation in third-person whilst flying down like in <=1.13.x.
    - Type: BOOLEAN

### Cape

- capeMovement
    - Description: Changes the cape model movement to be how it used to be in <=1.12.x.
    - Type: BOOLEAN
- clampCapeLean
    - Description: Removes the cape lean restriction. Disable this to match OptiFine cape physics.
    - Type: BOOLEAN
- capeSwingRotation
    - Description: Enable/disable the cape from swinging in unison with the body while the player is swinging their arm.
      Turn this off to be like it was in <=1.20.x.
    - Type: BOOLEAN
- capeChestplateTranslation
    - Description: Stops equipping a chestplate causing the cape to be translated a few pixels away like in <=1.15.x
    - Type: BOOLEAN
- capeSneakPosition
    - Description: Positions the cape while sneaking similarly to <=1.7.x
    - Type: BOOLEAN

### Other

- rotateBackwardsWalking
    - Description: Rotates the entity body sideways when walking backwards like it was in <=1.11.2.
    - Type: BOOLEAN
- uncapBlockingHeadRotation
    - Description: Reverts the change in 1.20.2, making head rotation when blocking as it used to be.
    - Type: BOOLEAN
- headRotationInterpolation
    - Description: Enable/disable the head rotation interpolation like in <=1.7.x.
    - Type: BOOLEAN
- fixVerticalBobbingTilt
    - Description: Brings back the camera tilting when falling/flying up like it was in <=1.13.x. (Fixes MC-225335)
    - Type: BOOLEAN
- viewBobbing
    - Description: Undoes the 1.21.2+ view bobbing change where when sneaking, your hand still moves normally.
    - Type: BOOLEAN
- deathLimbs
    - Description: Makes entities continue their animation even upon death.
    - Type: BOOLEAN
- bowArmMovement
    - Description: Restores old player body movement in third-person when using the bow like in <=1.7?
    - Type: BOOLEAN
- damageTilt
    - Description: Reverts the damage tilt to it's old logic which will tilt in one direction like in <=1.19.4.
    - Type: BOOLEAN
- offsetHurtTime
    - Description: Offsets the damage tilt time by -1 like in <=1.7.
    - Type: BOOLEAN
  </details>

<details>
  <summary>Screen</summary>

## 📷 Screen

- crosshairInThirdPerson
    - Description: Show crosshair whilst in thirdperson like in <=1.8.x.
    - Type: BOOLEAN
- fixHighAttackSpeedIndicator
    - Description: Hides the attack indicator when you have such a high attack speed. (Fixes MC-268420)
    - Type: BOOLEAN
- heartFlash
    - Description: Remove heart blinking like in <=1.7.x.
    - Type: BOOLEAN
- fixTextStrikethroughStyle
    - Description: Changes the text strikethrough position to make it look like it did in <=1.12.2.
    - Type: BOOLEAN
- centerScrollableListWidgets
    - Description: Center scrollable list widgets like <=1.7.x.
    - Type: BOOLEAN
- listWidgetSelectedBorderColor
    - Description: Returns the old list widget selected border color from <=1.15?
    - Type: BOOLEAN
- buttonTextColors
    - Description: Bring back the old yellow hover/grayish text colors like in <=1.14.4.
    - Type: BOOLEAN
- debugHudBackground
    - Description: Enable/disable the F3 Debug Hud background.
    - Type: BOOLEAN
- debugHudTextShadow
    - Description: Add text-shadow to F3 Debug Hud.
    - Type: BOOLEAN
- cameraTransparentPassthrough
    - Description: Enable/disable camera passthrough in thirdperson in glass/etc like in <=1.15.
    - Type: BOOLEAN
- tooltipStyleRendering
    - Description: Restores the corners of the tooltip texture that were removed in 1.21.2.
    - Type: BOOLEAN
    - Note: If you are using a resource pack with a custom tooltip texture, turn this setting OFF to not cause issues!
- slotHoverStyleRendering
    - Description: Restores the old inventory slot hover visual to how it was prior to 1.21.2.
    - Type: BOOLEAN
    - Note: If you are using a resource pack with a custom slot hover texture, turn this setting OFF to not cause
      issues!
- effectsInventoryPosition
    - Description: Restores the old potion effects status position in the inventory to like it was in <=1.11/1.19.
    - Type: BOOLEAN
- recipeBook
    - Description: Hides the recipe book from the inventory, including the button.
    - Type: BOOLEAN
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

- fishingRodTextureStackCheck
    - Description: Enable/disable the legacy texture check for fishing rod. Turning this on will bring back the old
      fishing rod stack texture check from <=1.8.
    - Type: BOOLEAN
- fishingRodLineInterpolation
    - Description: Correctly interpolates the fishing rod cast line with the eye height from <1.14?
    - Type: BOOLEAN
- noMoveFishingRodLine
    - Description: Does not move the fishing rod cast line while sneaking when viewed in the third person mode from <
      =1.7.
    - Type: BOOLEAN
- fishingRodLinePositionThirdPerson
    - Description: Adjusts the position of the fishing rod cast line horizontally like in <=1.7.
    - Type: BOOLEAN
- fishingRodLineThickness
    - Description: Restores the old fishing rod line thickness from <1.13?
    - Type: BOOLEAN
- thinFishingRodLineThickness
    - Description: Makes the fishing rod line super thin. Overrides the above setting.
    - Type: BOOLEAN
- stickModelWhenCastInThirdperson
    - Description: Makes the fishing rod model in third-person a stick when cast like in <=1.7.x.
    - Type: BOOLEAN

### Fixes

- equipAnimationOnItemUse
    - Description: Enable/disable the equip animation when blocking/etc.
    - Type: BOOLEAN
- itemUsageVisualInGUI
    - Description: Enable/disable item usage animation whilst inside a GUI, for example the continuous visual blocking,
      etc.
    - Type: BOOLEAN

### Enchantment Glint

- glintSpeed
    - Description: Restores the old enchantment glint speed like in <=1.8.x.
    - Type: BOOLEAN
- glintOnItemDrops2D
    - Description: Disables the enchantment glint on dropped items. Intended to be used along side the 2D dropped items
      feature to match <1.7.x.
    - Type: BOOLEAN
- glintOnItemFramed2D
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
- itemColors2D
    - Description: Restores the old color of 2D items by swapping the Y and Z components of the vertex normal.
    - Type: BOOLEAN

### Item Transformations

- itemPositions
    - Description: Tilts the held item position to make held items look like they did in <=1.7.x.
    - Type: BOOLEAN
- itemPositionsInThirdPerson
    - Description: Tilts the third-person held item position to make held items look like they did in <=1.7.x.
    - Type: BOOLEAN
- thinBlockPositions
    - Description: Translates the held item position of blocks like carpet/slabs/daylight sensors/pressure plates to
      look like how they did in <=1.7.x.
    - Type: BOOLEAN
- skullPosition
    - Description: Positions the skull block items' held/gui positions to be how it was in 1.8.x.
    - Type: BOOLEAN
- fishingRodVersion
    - Description: Positions the fishing rod's first-person position to be how it was in said version range.
    - Type: ENUM
        - 1.7 and below (V1_7)
        - 1.8 (V1_8)
        - LATEST

### Other

- itemUsageSwinging
    - Description: Block hitting (apply swing offset in item usage code).
    - Type: BOOLEAN
- swingOnUse
    - Description: Enable/disable the swing animation while using items (except the fishing rod) <=1.15.
    - Type: BOOLEAN
- swingOnDrop
    - Description: Enable/disable the swing animation while dropping items from either your hotbar or your inventory <
      =1.15.
    - Type: BOOLEAN
- swingOnEntityInteract
    - Description: Enable/disable the swing animation when interacting with interactable entities such as villagers <
      =1.15.
    - Type: BOOLEAN
- itemUsingTextureInGui
    - Description: Enable/disable the item usage texture in the GUI, disabling restores it to be like in <=1.8.x (mainly
      rod/bow/crossbow).
    - Type: BOOLEAN
- durabilityBarColors
    - Description: Restores the old durability damage colors from <1.11.
    - Type: BOOLEAN
- itemRarities
    - Description: Restores the old rarities for items visually from <1.21.2. (also old trident rarity from <1.21)
    - Type: BOOLEAN
- heldItemVisibilityInBoat
    - Description: Enable/disable held item visibility while you're in a moving boat. Turn this on to be like it was
      in <=1.8.x.
    - Type: BOOLEAN
  </details>

<details>
  <summary>Fixes</summary>

## 🪶 Fixes

- fixSneakingFeetPosition
    - Description: Fixes the sneaking model offset to be like in <=1.11.
    - Type: BOOLEAN
- fixMirrorArmSwing
    - Description: Fix the left-arm swing mirroring.
    - Type: BOOLEAN
- fixOffHandUsingPose
    - Description: Stops the offhand from using the NONE pose with a held item while using an item in the mainhand like
      in <=1.17.
    - Type: BOOLEAN
- fixCastLineCheck
    - Description: Fixes the arm logic for casting the fishing rod.
    - Type: BOOLEAN
- fixCastLineSwing
    - Description: Fixes the swing logic for casting the fishing rod.
    - Type: BOOLEAN
- fixEquipAnimation
    - Description: Instead of comparing item stacks directly to determine the equip animation, compare the durability
      and stack count of the items like in <=1.8.x.
    - Type: BOOLEAN
- fixFireballClientsideVisual
    - Description: Makes fire charges not place fire clientside like in older mc versions. Doesn't cause issues on
      servers, and is clientside only.
    - Type: BOOLEAN
- upMinPixelTransparencyLimit
    - Description: Makes the minimum 0-transparency value less than or equal to 0.1. This fixes textures with invisible
      pixels that cause issues.
    - Type: BOOLEAN

</details>

<details>
  <summary>Old Settings</summary>

## 🛠️ Old Settings

- thirdPersonSwordBlockingPosition
    - Description: Brings back the old third-person arm blocking rotations from <=1.7
    - Type: BOOLEAN
- lockBlockingArmRotation
    - Description: Enable/disable the third-person blocking arm rotation being locked in place.
    - Type: BOOLEAN
- projectileAgeCheck
    - Description: Render projectile at all ages <=1.15?
    - Type: BOOLEAN
- blockMiningProgress
    - Description: Bring back the old block mining progress <=1.18?
    - Type: BOOLEAN
- inventoryEntityScissor
    - Description: Allows the inventory entity model to render fully.
    - Type: BOOLEAN
- blockOutlineRendering
    - Description: Restores the legacy block outline rendering from <=1.14.4.
    - Type: BOOLEAN
- modelWhilstSleeping
    - Description: Enable/disable the player model rendering whilst sleeping like in <=1.12? Only affects you.
    - Type: BOOLEAN
- entityArmorHurtTint
    - Description: Tints the armor when an entity is damaged like in <=1.7.x.
    - Type: BOOLEAN
- itemGlintOnEntity
    - Description: Forces the glint on armor to use the item glint texture. This therefore unifies the glint texture
      like in older mc versions.
    - Type: BOOLEAN
- maxGlintProperties
    - Description: Forces the glint to use the maximum speed and strength by default like in older mc versions.
    - Type: BOOLEAN
- armorHurtRendering
    - Description: Restores the old armor hurt tint rendering from ~1.8.
    - Type: BOOLEAN
- glintRendering
    - Description: Restores the old item/armor glint rendering found in <=1.14.4.
    - Type: BOOLEAN
- highAttackSpeedVisual
    - Description: Fakes the high attack speed visual, which stops the attack cooldown animation on items like the
      sword.
    - Type: BOOLEAN
- entityGlowOutline
    - Description: Enable/disable the 1.9+ glow effect from rendering.
    - Type: BOOLEAN
- modernCombatSounds
    - Description: Enable/disable the 1.9+ combat sounds that were added.
    - Type: BOOLEAN
- modernCombatParticles
    - Description: Enable/disable the 1.9+ combat particles that were added.
    - Type: BOOLEAN
- heldItemArmLogic
    - Description: In 1.8, the player's arm (when viewed from the first person POV) will be positioned at an angle when
      holding an item. This is only truly visible when going from an empty slot to an item. This happens due it applying
      the held item arm rotation meant for the third person model.
    - Type: BOOLEAN
- flameDimensions
    - Description: Makes the third person flame rendering use a fixed width and height to match <=1.12.x.
    - Type: BOOLEAN
- flameOffset
    - Description: Brings back the third person flame offset from <=1.7.x.
    - Type: BOOLEAN

### Sky

- blueVoidSky
    - Description: Brings back the forgotten blue void part of the sky. (Fixes MC-257056)
    - Type: BOOLEAN
- skyHorizonHeight
    - Description: Changes the horizon height to how it was in <=1.16.5.
    - Type: BOOLEAN
- planarSkyFog
    - Description: Changes the sky fog to use the planar type. This fog is also known as OptiFine Fast Fog.
    - Type: BOOLEAN
- cloudHeight
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

#### Request Info (Clientbound)

| Identifier             | Field Name | Field Type | Description                                                                         |
|------------------------|:-----------|:-----------|:------------------------------------------------------------------------------------|
| animatium:request_info |            |            | Sends the animatium:info payload back containing information about the mod version. |

#### Info (Serverbound)

Sent to the server when receiving the "minecraft:register" payload, and it contains "Animatium".

| Identifier     | Field Name          | Field Type      | Description                                                                         |
|----------------|---------------------|-----------------|-------------------------------------------------------------------------------------|
| animatium:info | Version             | Double          | Current release version of the mod.                                                 |
|                | Development Version | Optional String | Current dev build commit of the mod. Only provided when mod is a development build. |