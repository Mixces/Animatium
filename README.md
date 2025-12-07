# Animatium

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/C0C31INYGG)

Visual Tweaks/QOL Settings For Modern Minecraft Versions, Aimed At 1.7/1.8 players.

Join our discord: https://discord.gg/C8KKgbA8jy

## License

This project is licensed under the GPL-3.0 license w/ Minecraft Linking Exception.

## Download

You can download the latest releases from Modrinth [here](https://modrinth.com/mod/animatium) or from
CurseForge [here](https://www.curseforge.com/minecraft/mc-mods/animatium).

## Dependencies

This mod uses [YACL](https://modrinth.com/mod/yacl) as it's config library of choice. Make sure you install the correct
version to prevent crashing.

## Notes

If you are using Lunar Client, some features will not work as intended.
If you find anything else not working, please report it on the GitHub and we will try to fix it.
Do note that, Lunar does take priority in some places for enabled/disabled settings.

## Support

Have any issues or need support? Feel free to use
our [issue tracker](https://github.com/Legacy-Visuals-Project/Animatium/issues) to address that. If you are reporting a
crash, make sure you include information about the mods you are using and attach any relevant log files you have. If you
want to suggest features, join our [discord](https://discord.gg/C8KKgbA8jy)!

## Enums

### Feature

| Feature Name                     | Identifier            | Description                                                                                                       |
|----------------------------------|:----------------------|:------------------------------------------------------------------------------------------------------------------|
| All                              | all                   | Turn on all of these features.                                                                                    |
| Miss Penalty                     | miss_penalty          | Turn on/off the swing miss penalty.                                                                               |
| Left Click Item Usage            | left_click_item_usage | Turn on/off the ability to start using a item whilst holding left click.                                          |
| Mining Item Usage                | mining_item_usage     | Turn on/off the ability to start mining/break blocks whilst using a item like in 1.7.                             |
| Hide Attached Fishing Rod Bobber | hide_rod_bobber       | Turn on/off the rendering of the fishing rod bobber when attached to you in first-person.                         |
| Pick Inflation                   | pick_inflation        | Turn on/off the 0.1 pick (hitbox) inflation when attacking an entity.                                             |
| Client-sided Entity Movement     | clientside_entities   | Turn on/off the entity movement calculations from the client. (In 1.7, entity movement was handled by the client) |

## Payloads

Allows the server to enable/disable server-only features that enhance gameplay.

| Identifier             | Direction | Field Name          | Field Type      | Description                                                                                  |
|------------------------|-----------|:--------------------|:----------------|:---------------------------------------------------------------------------------------------|
| animatium:info         | Server    | Version             | Double          | Current release version of the mod.                                                          |
|                        |           | Development Version | String?         | Current dev build commit of the mod. Only provided when mod is a development build.          |
| animatium:request_info | Client    |                     |                 | Sends the animatium:info payload back containing information about the mod version.          |
| animatium:set_features | Client    | Features List       | List\<Feature\> | List of features the client should use currently, maps to Feature above or skips if unknown. |