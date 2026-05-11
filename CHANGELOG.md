# Release 4.0

## General

- Reworked a lot of code
- Config setting names are more generalized

## New

- New Configuration Screen
    - Does not replace the old config screen
    - Is accessed via '\' by default, rebindable in settings
    - Is a simple interface with simple settings accommodated to those who expect settings to be named like in "Animatium Legacy"
- New Settings
    - Screen
        - Legacy Panorama Rendering
        - Full Width Inventory Status-Effects Background
    - Other
        - Legacy Diffuse Lighting
    - Extras:
        - Damage Tint Items (From Animatium Legacy)
        - Damage Tint Cape (From Animatium Legacy)

## Removed

- Removed Settings
    - Force High Attack Speed Visual
    - Disable Modern Combat Sounds
    - Disable Modern Combat Particles
    - Disable Entity Glow Outline
    - Item Colors 2D

## Fixes

- Fixed "Long Unsneak" setting affecting vanilla sneaking when disabled
- Fixed disabled widget text colors when using "Old Widget Hover Text Colors"
- Fixed jittering when sneaking on slime blocks when "Bring back bobbing tilt" is enabled
- Damage tint now affects blocks/items equipped in the head slot
- Fixed mod not "requiring" YACL on launch