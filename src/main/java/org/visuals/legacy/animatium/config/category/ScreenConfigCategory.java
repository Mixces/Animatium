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
 * <p>
 * "MINECRAFT" LINKING EXCEPTION TO THE GPL
 */

package org.visuals.legacy.animatium.config.category;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.visuals.legacy.animatium.util.enums.CameraVersion;

public class ScreenConfigCategory {
    public boolean crosshairInThirdPerson = true;
    public boolean heartFlash = true;
    public boolean centerScrollableListWidgets = true;
    public boolean listWidgetSelectedBorderColor = true;
    public boolean legacyButtonHoverTextColor = true;
    public boolean disableDebugHudBackground = true;
    public boolean debugHudTextShadow = true;
    public boolean cameraTransparentPassthrough = true;
    public boolean tooltipStyleRendering = true;
    public boolean slotHoverStyleRendering = true;
    public boolean listBackgroundGradient = true;
    public boolean effectsInventoryPosition = true;
    public boolean snappySliderMovement = true;
    public boolean hideRecipeBook = true;
    public boolean panoramaRendering = true;
    public CameraVersion cameraVersion = CameraVersion.V1_8;

    public static ConfigCategory setup(final ScreenConfigCategory defaults, final ScreenConfigCategory config) {
        final ConfigCategory.Builder category = ConfigCategory.createBuilder();
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
                .name(Component.translatable("animatium.legacyButtonHoverTextColor"))
                .description(OptionDescription.of(Component.translatable("animatium.legacyButtonHoverTextColor.description")))
                .binding(
                        defaults.legacyButtonHoverTextColor,
                        () -> config.legacyButtonHoverTextColor,
                        (newVal) -> config.legacyButtonHoverTextColor = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.disableDebugHudBackground"))
                .description(OptionDescription.of(Component.translatable("animatium.disableDebugHudBackground.description")))
                .binding(
                        defaults.disableDebugHudBackground,
                        () -> config.disableDebugHudBackground,
                        (newVal) -> config.disableDebugHudBackground = newVal)
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
                .name(Component.translatable("animatium.tooltipStyleRendering").withStyle(ChatFormatting.GOLD))
                .description(OptionDescription.of(Component.translatable("animatium.tooltipStyleRendering.description")))
                .binding(
                        defaults.tooltipStyleRendering,
                        () -> config.tooltipStyleRendering,
                        (newVal) -> config.tooltipStyleRendering = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.slotHoverStyleRendering").withStyle(ChatFormatting.GOLD))
                .description(OptionDescription.of(Component.translatable("animatium.slotHoverStyleRendering.description")))
                .binding(
                        defaults.slotHoverStyleRendering,
                        () -> config.slotHoverStyleRendering,
                        (newVal) -> config.slotHoverStyleRendering = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.listBackgroundGradient"))
                .description(OptionDescription.of(Component.translatable("animatium.listBackgroundGradient.description")))
                .binding(
                        defaults.listBackgroundGradient,
                        () -> config.listBackgroundGradient,
                        (newVal) -> config.listBackgroundGradient = newVal)
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
        // TODO
//        category.option(Option.<Boolean>createBuilder()
//                .name(Component.translatable("animatium.snappySliderMovement"))
//                .description(OptionDescription.of(Component.translatable("animatium.snappySliderMovement.description")))
//                .binding(
//                        defaults.snappySliderMovement,
//                        () -> config.snappySliderMovement,
//                        (newVal) -> config.snappySliderMovement = newVal)
//                .controller(TickBoxControllerBuilder::create)
//                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.recipeBook"))
                .description(OptionDescription.of(Component.translatable("animatium.recipeBook.description")))
                .binding(
                        defaults.hideRecipeBook,
                        () -> config.hideRecipeBook,
                        (newVal) -> config.hideRecipeBook = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.panoramaRendering").withStyle(ChatFormatting.GOLD))
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
