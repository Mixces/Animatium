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

package btw.mixces.animatium.config.category;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class FixesConfigCategory {
    public boolean fixSneakingFeetPosition = true;
    public boolean fixMirrorArmSwing = true;
    public boolean fixOffHandUsingPose = true;
    public boolean fixCastLineCheck = true;
    public boolean fixCastLineSwing = true;
    public boolean fixEquipAnimation = true;
    public boolean fixFireballClientsideVisual = true;
    public boolean fixTextStrikethroughStyle = true;
    public boolean fixHighAttackSpeedIndicator = true;
    public boolean fixVerticalBobbingTilt = true;
    public boolean upMinPixelTransparencyLimit = true;

    public static ConfigCategory setup(FixesConfigCategory defaults, FixesConfigCategory config) {
        ConfigCategory.Builder category = ConfigCategory.createBuilder();
        category.name(Component.translatable("animatium.category.fixes"));
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.fixSneakingFeetPosition"))
                .description(OptionDescription.of(Component.translatable("animatium.fixSneakingFeetPosition.description")))
                .binding(
                        defaults.fixSneakingFeetPosition,
                        () -> config.fixSneakingFeetPosition,
                        (newVal) -> config.fixSneakingFeetPosition = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.fixMirrorArmSwing"))
                .description(OptionDescription.of(Component.translatable("animatium.fixMirrorArmSwing.description")))
                .binding(
                        defaults.fixMirrorArmSwing,
                        () -> config.fixMirrorArmSwing,
                        (newVal) -> config.fixMirrorArmSwing = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.fixOffHandUsingPose"))
                .description(OptionDescription.of(Component.translatable("animatium.fixOffHandUsingPose.description")))
                .binding(
                        defaults.fixOffHandUsingPose,
                        () -> config.fixOffHandUsingPose,
                        (newVal) -> config.fixOffHandUsingPose = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.fixCastLineCheck"))
                .description(OptionDescription.of(Component.translatable("animatium.fixCastLineCheck.description")))
                .binding(
                        defaults.fixCastLineCheck,
                        () -> config.fixCastLineCheck,
                        (newVal) -> config.fixCastLineCheck = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.fixCastLineSwing"))
                .description(OptionDescription.of(Component.translatable("animatium.fixCastLineSwing.description")))
                .binding(
                        defaults.fixCastLineSwing,
                        () -> config.fixCastLineSwing,
                        (newVal) -> config.fixCastLineSwing = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.fixEquipAnimationItemCheck"))
                .description(OptionDescription.of(Component.translatable("animatium.fixEquipAnimationItemCheck.description")))
                .binding(
                        defaults.fixEquipAnimation,
                        () -> config.fixEquipAnimation,
                        (newVal) -> config.fixEquipAnimation = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.fixFireballClientsideVisual"))
                .description(OptionDescription.of(Component.translatable("animatium.fixFireballClientsideVisual.description")))
                .binding(
                        defaults.fixFireballClientsideVisual,
                        () -> config.fixFireballClientsideVisual,
                        (newVal) -> config.fixFireballClientsideVisual = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        if (!FabricLoader.getInstance().isModLoaded("viafabricplus")) {
            category.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.fixTextStrikethroughStyle"))
                    .description(OptionDescription.of(Component.translatable("animatium.fixTextStrikethroughStyle.description")))
                    .binding(
                            defaults.fixTextStrikethroughStyle,
                            () -> config.fixTextStrikethroughStyle,
                            (newVal) -> config.fixTextStrikethroughStyle = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
        }
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
                .name(Component.translatable("animatium.fixVerticalBobbingTilt"))
                .description(OptionDescription.of(Component.translatable("animatium.fixVerticalBobbingTilt.description")))
                .binding(
                        defaults.fixVerticalBobbingTilt,
                        () -> config.fixVerticalBobbingTilt,
                        (newVal) -> config.fixVerticalBobbingTilt = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.upMinPixelTransparencyLimit"))
                .description(OptionDescription.of(Component.translatable("animatium.upMinPixelTransparencyLimit.description")))
                .binding(
                        defaults.upMinPixelTransparencyLimit,
                        () -> config.upMinPixelTransparencyLimit,
                        (newVal) -> {
                            config.upMinPixelTransparencyLimit = newVal;
                            Minecraft.getInstance().reloadResourcePacks();
                        })
                .controller(TickBoxControllerBuilder::create)
                .build());
        return category.build();
    }
}
