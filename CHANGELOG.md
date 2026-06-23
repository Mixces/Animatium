# Release 4.0

## General

- Reworked a lot of code & Moved a lot of it to Kotlin (kotlin da goat g)
- Config setting names are more generalized
- Scrapped the config data packet idea (no replacement)

## Changes

- All settings in the "Fixes" category now get disabled when you disable the mod "/animatium off"
- Your onboarding/preset version will now be remembered
- Decouple Fishing Rod Version from Item Positions setting
- Moved "fixEquipAnimationItemCheck" to the "Items Category" and renamed it to "equipAnimationItemCheck"
    - This setting wasn't really a fix and should have been in the "Items Category" to begin with
    - Also, added logic to re-add the old item equip use on ground logic that was missing.
        - (Try placing a block under you, your hand will do the equip animation like it did in <=1.8.x)

## New

- New secret that'll pop up when it's my birthday
- Added chat message when setting your preset/onboarding version!
- New Settings
    - Screen
        - Legacy Panorama Rendering
        - Full Width Inventory Status-Effects Background
    - Other
        - Legacy Diffuse Lighting
        - Legacy Lightmap
        - Legacy Fog Darkening
        - Damage Tint Style
            - V1_7 (Not fully accurate, will fix in future release)
            - V1_8
            - V1_8_ORANGE_MARSHALL
            - MODERN
    - Extras:
        - Damage Tint (From Animatium Legacy)
            - Damage Tint Items
            - Damage Tint Cape
        - Item Swing (From Animatium Legacy)
            - Item Swing Speed
            - Haste Swing Speed
            - Mining Fatigue Swing Speed
            - Ignore Haste Speed
            - Ignore Mining Fatigue Speed
        - Legacy Loading Progress Bar
            - Option to re-enable the loading screen progress bar when the "Legacy Loading Screen" setting is enabled.
        - Blood Particles (Orange Marshall)
            - The classic blood particles when hitting a entity from Orange Marshalls 1.8 mod "Vanilla Enhancements"
            - Accompanied by a multiplier setting to adjust the amount of particles to show (5 * multiplier)
                - (Min: 0, Max: 40, Step: 1)
            - If Orange Marshall sees this and doesn't want me to include this feature in the mod,
              I will gladly remove it upon request/contact.

## Removed

- Removed Settings
    - Force High Attack Speed Visual
    - Disable Modern Combat Sounds
    - Disable Modern Combat Particles
    - Disable Entity Glow Outline
    - Item Colors 2D (Will possibly return in future release with better implementation)
    - Deep Red Hurt Tint (See New "Damage Tint Style")

These were reasons as to why these were removed, in which being some were very much disliked and some can be done easily
with a resource pack.

## Fixes

- Fixed "Long Un-sneak" setting affecting vanilla sneaking when disabled
- Fixed disabled widget text colors when using "Old Widget Hover Text Colors"
- Fixed jittering when sneaking on slime blocks when "Bring back bobbing tilt" is enabled
- Fixed mod not "requiring" YACL on launch
- Fixed potential crash when using the 1.7 sneak animation
- Fixed Damage tint not affecting blocks/items equipped in the head slot when they should be
- Fixed onboarding showing if the screen was opened via commands and the game closed
- Fixed "Hide Recipe Book" setting missing from config screen
- Fixed "Old Block Mining Progress" doing nothing at all