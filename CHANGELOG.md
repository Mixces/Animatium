# Release 4.2

## Changes

- Fixed damage tint color values
    - Removed v1_8 as it actually matches VANILLA already
- Added new "Glint Affects Armor Tint" setting for damage armor overlay.
    - In 1.8-1.14.4, the overlay tint would also affect the glint rendering on armor, producing
    - this nice pink tint visual.
- Legacy Clouds no longer go transparent into the fog, matching <=1.21.5
    - Planar sky fog also now affects clouds
- Added "Smooth Particles" fix setting to restore smooth-rendering particles.

## Fixes

- Fixed Legacy Cloud Rendering Crash
- Fixed duplicate particles when usage mining if the server feature is also enabled
