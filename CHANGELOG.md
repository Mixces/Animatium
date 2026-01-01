### NOTICE: This update will reset your settings! I'm sorry.

## Added:
#### Server (For Server Developers)
- New Server Features: 
  - "all" - enables everything
  - "mining_item_usage" - Allows mining blocks whilst using an item like in <=1.7
  - "pick_inflation" - inflates the pick radius to 0.1 like in <=1.8
  - "clientside_entities" - makes entity movement client-sided like in <=1.7
  - "old_sneak_height" - Makes the sneaking bounding box/eye height as it was in <=1.8
#### General
- Merged Extras Back Into Animatium as a new config tab (all disabled by default)
- Onboarding Screen
- Old Render Distance Checks
- Old Depth Far
- "Old Sky Horizon Height" is now "Old Y0 Height" and modifies fog too
- Void Fog/Void Particles
- Fast Grass
- Old Minimum Smooth Lighting
- Old Chat Position
- Disconnect to Title Screen (when leaving multiplayer server like in 1.7)
- Old Reloading/Loading Screen
- 2D Mob Head Item Models
- Egg produces snowball particles (like in 1.7)
- Always steve player model (not skin)
- Center Crosshair (like it was in older versions around <=1.12.2)
- Old Cloud Rendering (<=1.21.5)
- Old Water Overlay (the texture when you are submerged) (<=1.12.2)

## Changed:
#### General
- Simplified a ton of settings (Combined stuff like Sneaking Settings/Fishing Rod Version)
### Server
- Current config info is now sent inside the "info" payload (README for documentation)
- Info payload now sends on server join instead of having to be requested, similar to how it originally was but better now
    - During PLAY JOIN
