/**
 * Animatium
 * The all-you-could-want legacy animations mod for modern minecraft versions.
 * Brings back animations from the 1.7/1.8 era and more.
 * <p>
 * Copyright (C) 2024-2025 lowercasebtw
 * Copyright (C) 2024-2025 mixces
 * Copyright (C) 2024-2025 Contributors to the project retain their copyright
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package btw.mixces.animatium.config.category;

import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.CameraVersion;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;

public final class ScreenConfigCategory {
    public static ConfigCategory setup(AnimatiumConfig defaults, AnimatiumConfig config) {
        ConfigCategory.Builder category = ConfigCategory.createBuilder();
        category.name(Component.translatable("animatium.category.screen"));
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.showCrosshairInThirdperson"))
                .description(OptionDescription.of(Component.translatable("animatium.showCrosshairInThirdperson.description")))
                .binding(
                        defaults.showCrosshairInThirdperson,
                        () -> config.showCrosshairInThirdperson,
                        (newVal) -> config.showCrosshairInThirdperson = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.fixHighAttackSpeedIndicator"))
                .description(OptionDescription.of(Component.translatable("animatium.fixHighAttackSpeedIndicator.description")))
                .binding(
                        defaults.fixHighAttackSpeedIndicator,
                        () -> config.fixHighAttackSpeedIndicator,
                        (newVal) -> config.fixHighAttackSpeedIndicator = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.removeHeartFlash"))
                .description(OptionDescription.of(Component.translatable("animatium.removeHeartFlash.description")))
                .binding(
                        defaults.removeHeartFlash,
                        () -> config.removeHeartFlash,
                        (newVal) -> config.removeHeartFlash = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.fixTextStrikethroughStyle"))
                .description(OptionDescription.of(Component.translatable("animatium.fixTextStrikethroughStyle.description")))
                .binding(
                        defaults.fixTextStrikethroughStyle,
                        () -> config.fixTextStrikethroughStyle,
                        (newVal) -> config.fixTextStrikethroughStyle = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.centerScrollableListWidgets"))
                .description(OptionDescription.of(Component.translatable("animatium.centerScrollableListWidgets.description")))
                .binding(
                        defaults.centerScrollableListWidgets,
                        () -> config.centerScrollableListWidgets,
                        (newVal) -> config.centerScrollableListWidgets = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.oldListWidgetSelectedBorderColor"))
                .description(OptionDescription.of(Component.translatable("animatium.oldListWidgetSelectedBorderColor.description")))
                .binding(
                        defaults.oldListWidgetSelectedBorderColor,
                        () -> config.oldListWidgetSelectedBorderColor,
                        (newVal) -> config.oldListWidgetSelectedBorderColor = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.oldButtonTextColors"))
                .description(OptionDescription.of(Component.translatable("animatium.oldButtonTextColors.description")))
                .binding(
                        defaults.oldButtonTextColors,
                        () -> config.oldButtonTextColors,
                        (newVal) -> config.oldButtonTextColors = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.removeDebugHudBackground"))
                .description(OptionDescription.of(Component.translatable("animatium.removeDebugHudBackground.description")))
                .binding(
                        defaults.removeDebugHudBackground,
                        () -> config.removeDebugHudBackground,
                        (newVal) -> config.removeDebugHudBackground = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.debugHudTextShadow"))
                .description(OptionDescription.of(Component.translatable("animatium.debugHudTextShadow.description")))
                .binding(
                        defaults.debugHudTextShadow,
                        () -> config.debugHudTextShadow,
                        (newVal) -> config.debugHudTextShadow = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.disableCameraTransparentPassthrough"))
                .description(OptionDescription.of(Component.translatable("animatium.disableCameraTransparentPassthrough.description")))
                .binding(
                        defaults.disableCameraTransparentPassthrough,
                        () -> config.disableCameraTransparentPassthrough,
                        (newVal) -> config.disableCameraTransparentPassthrough = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.oldTooltipStyleRendering"))
                .description(OptionDescription.of(Component.translatable("animatium.oldTooltipStyleRendering.description")))
                .binding(
                        defaults.oldTooltipStyleRendering,
                        () -> config.oldTooltipStyleRendering,
                        (newVal) -> config.oldTooltipStyleRendering = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.oldSlotHoverStyleRendering"))
                .description(OptionDescription.of(Component.translatable("animatium.oldSlotHoverStyleRendering.description")))
                .binding(
                        defaults.oldSlotHoverStyleRendering,
                        () -> config.oldSlotHoverStyleRendering,
                        (newVal) -> config.oldSlotHoverStyleRendering = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.oldEffectsInventoryPosition"))
                .description(OptionDescription.of(Component.translatable("animatium.oldEffectsInventoryPosition.description")))
                .binding(
                        defaults.oldEffectsInventoryPosition,
                        () -> config.oldEffectsInventoryPosition,
                        (newVal) -> config.oldEffectsInventoryPosition = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.hideRecipeBook"))
                .description(OptionDescription.of(Component.translatable("animatium.hideRecipeBook.description")))
                .binding(
                        defaults.hideRecipeBook,
                        () -> config.hideRecipeBook,
                        (newVal) -> config.hideRecipeBook = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<CameraVersion>createBuilder()
                .name(Component.translatable("animatium.cameraVersion"))
                .description(OptionDescription.of(Component.translatable("animatium.cameraVersion.description")))
                .binding(
                        defaults.cameraVersion,
                        () -> config.cameraVersion,
                        (newVal) -> config.cameraVersion = newVal)
                .controller((opt) ->
                        EnumControllerBuilder.create(opt)
                                .enumClass(CameraVersion.class)
                                .formatValue((it) -> Component.translatable("animatium.enum.CameraVersion." + it.name())))
                .build());
        return category.build();
    }
}
