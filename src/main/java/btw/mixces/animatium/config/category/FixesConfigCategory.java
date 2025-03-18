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
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class FixesConfigCategory {
    public static ConfigCategory setup(AnimatiumConfig defaults, AnimatiumConfig config) {
        ConfigCategory.Builder category = ConfigCategory.createBuilder();
        category.name(Component.translatable("animatium.category.fixes"));
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
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.fixOffHandUsingPose"))
                .description(OptionDescription.of(Component.translatable("animatium.fixOffHandUsingPose.description")))
                .binding(
                        defaults.fixOffHandUsingPose,
                        () -> config.fixOffHandUsingPose,
                        (newVal) -> config.fixOffHandUsingPose = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        return category.build();
    }
}
