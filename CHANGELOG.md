# Release 4.2

## Changes

- Fixed damage tint color values
    - Removed v1_8 as it actually matches VANILLA already
    - Added new "Custom" option
- Added new "Glint Affects Armor Tint" setting for damage armor overlay.
    - In 1.8-1.14.4, the overlay tint would also affect the glint rendering on armor, producing
    - this nice pink tint visual.
- Legacy Clouds no longer go transparent into the fog, matching <=1.21.5
    - Planar sky fog also now affects clouds
- Added "Smooth Particles" fix setting to restore smooth-rendering particles like in <=1.13.
- Modified "Legacy Block Outline Rendering" code to better support external block overlay mods
- Rework a bit of the config code.

## Fixes

- Fixed Legacy Cloud Rendering Crash
- Fixed duplicate particles when usage mining if the server feature is also enabled
