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
import btw.mixces.animatium.util.enums.CameraVersion;
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
                .name(Component.translatable("animatium.crosshairInThirdPerson"))
                .description(OptionDescription.of(Component.translatable("animatium.crosshairInThirdPerson.description")))
                .binding(
                        defaults.crosshairInThirdPerson,
                        () -> config.crosshairInThirdPerson,
                        (newVal) -> config.crosshairInThirdPerson = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.heartFlash"))
                .description(OptionDescription.of(Component.translatable("animatium.heartFlash.description")))
                .binding(
                        defaults.heartFlash,
                        () -> config.heartFlash,
                        (newVal) -> config.heartFlash = newVal)
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
                .name(Component.translatable("animatium.listWidgetSelectedBorderColor"))
                .description(OptionDescription.of(Component.translatable("animatium.listWidgetSelectedBorderColor.description")))
                .binding(
                        defaults.listWidgetSelectedBorderColor,
                        () -> config.listWidgetSelectedBorderColor,
                        (newVal) -> config.listWidgetSelectedBorderColor = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.buttonTextColors"))
                .description(OptionDescription.of(Component.translatable("animatium.buttonTextColors.description")))
                .binding(
                        defaults.buttonTextColors,
                        () -> config.buttonTextColors,
                        (newVal) -> config.buttonTextColors = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.debugHudBackground"))
                .description(OptionDescription.of(Component.translatable("animatium.debugHudBackground.description")))
                .binding(
                        defaults.debugHudBackground,
                        () -> config.debugHudBackground,
                        (newVal) -> config.debugHudBackground = newVal)
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
                .name(Component.translatable("animatium.cameraTransparentPassthrough"))
                .description(OptionDescription.of(Component.translatable("animatium.cameraTransparentPassthrough.description")))
                .binding(
                        defaults.cameraTransparentPassthrough,
                        () -> config.cameraTransparentPassthrough,
                        (newVal) -> config.cameraTransparentPassthrough = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.tooltipStyleRendering"))
                .description(OptionDescription.of(Component.translatable("animatium.tooltipStyleRendering.description")))
                .binding(
                        defaults.tooltipStyleRendering,
                        () -> config.tooltipStyleRendering,
                        (newVal) -> config.tooltipStyleRendering = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.slotHoverStyleRendering"))
                .description(OptionDescription.of(Component.translatable("animatium.slotHoverStyleRendering.description")))
                .binding(
                        defaults.slotHoverStyleRendering,
                        () -> config.slotHoverStyleRendering,
                        (newVal) -> config.slotHoverStyleRendering = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.effectsInventoryPosition"))
                .description(OptionDescription.of(Component.translatable("animatium.effectsInventoryPosition.description")))
                .binding(
                        defaults.effectsInventoryPosition,
                        () -> config.effectsInventoryPosition,
                        (newVal) -> config.effectsInventoryPosition = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.recipeBook"))
                .description(OptionDescription.of(Component.translatable("animatium.recipeBook.description")))
                .binding(
                        defaults.recipeBook,
                        () -> config.recipeBook,
                        (newVal) -> config.recipeBook = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.panoramaRendering"))
                .description(OptionDescription.of(Component.translatable("animatium.panoramaRendering.description")))
                .binding(
                        defaults.panoramaRendering,
                        () -> config.panoramaRendering,
                        (newVal) -> config.panoramaRendering = newVal)
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
