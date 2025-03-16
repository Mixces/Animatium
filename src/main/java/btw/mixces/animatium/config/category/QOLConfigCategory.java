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

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class QOLConfigCategory {
    public static ConfigCategory setup(AnimatiumConfig defaults, AnimatiumConfig config) {
        ConfigCategory.Builder category = ConfigCategory.createBuilder();
        category.name(Component.translatable("animatium.category.qol"));
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.minimalViewBobbing"))
                .description(OptionDescription.of(Component.translatable("animatium.minimalViewBobbing.description")))
                .binding(
                        defaults.minimalViewBobbing,
                        () -> config.minimalViewBobbing,
                        (newVal) -> config.minimalViewBobbing = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.nameTagInThirdperson"))
                .description(OptionDescription.of(Component.translatable("animatium.nameTagInThirdperson.description")))
                .binding(
                        defaults.nameTagInThirdperson,
                        () -> config.nameTagInThirdperson,
                        (newVal) -> config.nameTagInThirdperson = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.nameTagBackground"))
                .description(OptionDescription.of(Component.translatable("animatium.nameTagBackground.description")))
                .binding(
                        defaults.nameTagBackground,
                        () -> config.nameTagBackground,
                        (newVal) -> config.nameTagBackground = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.nameTagTextShadow"))
                .description(OptionDescription.of(Component.translatable("animatium.nameTagTextShadow.description")))
                .binding(
                        defaults.nameTagTextShadow,
                        () -> config.nameTagTextShadow,
                        (newVal) -> config.nameTagTextShadow = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.debugHudTextColor"))
                .description(OptionDescription.of(Component.translatable("animatium.debugHudTextColor.description")))
                .binding(
                        defaults.debugHudTextColor,
                        () -> config.debugHudTextColor,
                        (newVal) -> config.debugHudTextColor = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.persistentBlockOutline"))
                .description(OptionDescription.of(Component.translatable("animatium.persistentBlockOutline.description")))
                .binding(
                        defaults.persistentBlockOutline,
                        () -> config.persistentBlockOutline,
                        (newVal) -> config.persistentBlockOutline = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.offhandUsageSwinging"))
                .description(OptionDescription.of(Component.translatable("animatium.offhandUsageSwinging.description")))
                .binding(
                        defaults.offhandUsageSwinging,
                        () -> config.offhandUsageSwinging,
                        (newVal) -> config.offhandUsageSwinging = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.alwaysSharpParticles"))
                .description(OptionDescription.of(Component.translatable("animatium.alwaysSharpParticles.description")))
                .binding(
                        defaults.alwaysSharpParticles,
                        () -> config.alwaysSharpParticles,
                        (newVal) -> config.alwaysSharpParticles = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.recipeAndTutorialToasts"))
                .description(OptionDescription.of(Component.translatable("animatium.recipeAndTutorialToasts.description")))
                .binding(
                        defaults.recipeAndTutorialToasts,
                        () -> config.recipeAndTutorialToasts,
                        (newVal) -> config.recipeAndTutorialToasts = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.visibleArmWhileInvisible"))
                .description(OptionDescription.of(Component.translatable("animatium.visibleArmWhileInvisible.description")))
                .binding(
                        defaults.visibleArmWhileInvisible,
                        () -> config.visibleArmWhileInvisible,
                        (newVal) -> config.visibleArmWhileInvisible = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.fakeMissPenaltySwing"))
                .description(OptionDescription.of(Component.translatable("animatium.fakeMissPenaltySwing.description")))
                .binding(
                        defaults.fakeMissPenaltySwing,
                        () -> config.fakeMissPenaltySwing,
                        (newVal) -> config.fakeMissPenaltySwing = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.dontMoveBlueVoid"))
                .description(OptionDescription.of(Component.translatable("animatium.dontMoveBlueVoid.description")))
                .binding(
                        defaults.dontMoveBlueVoid,
                        () -> config.dontMoveBlueVoid,
                        (newVal) -> config.dontMoveBlueVoid = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.usageSwingingParticles"))
                .description(OptionDescription.of(Component.translatable("animatium.usageSwingingParticles.description")))
                .binding(
                        defaults.usageSwingingParticles,
                        () -> config.usageSwingingParticles,
                        (newVal) -> config.usageSwingingParticles = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.entityDeathTopple"))
                .description(OptionDescription.of(Component.translatable("animatium.entityDeathTopple.description")))
                .binding(
                        defaults.entityDeathTopple,
                        () -> config.entityDeathTopple,
                        (newVal) -> config.entityDeathTopple = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.deepRedHurtTint"))
                .description(OptionDescription.of(Component.translatable("animatium.deepRedHurtTint.description")))
                .binding(
                        defaults.deepRedHurtTint,
                        () -> config.deepRedHurtTint,
                        (newVal) -> {
                            config.deepRedHurtTint = newVal;
                            AnimatiumClient.SHOULD_RELOAD_OVERLAY_TEXTURE = true;
                        })
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.particlePhysics"))
                .description(OptionDescription.of(Component.translatable("animatium.particlePhysics.description")))
                .binding(
                        defaults.particlePhysics,
                        () -> config.particlePhysics,
                        (newVal) -> config.particlePhysics = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.firstPersonParticles"))
                .description(OptionDescription.of(Component.translatable("animatium.firstPersonParticles.description")))
                .binding(
                        defaults.firstPersonParticles,
                        () -> config.firstPersonParticles,
                        (newVal) -> config.firstPersonParticles = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.dontClearChat"))
                .description(OptionDescription.of(Component.translatable("animatium.dontClearChat.description")))
                .binding(
                        defaults.dontClearChat,
                        () -> config.dontClearChat,
                        (newVal) -> config.dontClearChat = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.dontCloseChat"))
                .description(OptionDescription.of(Component.translatable("animatium.dontCloseChat.description")))
                .binding(
                        defaults.dontCloseChat,
                        () -> config.dontCloseChat,
                        (newVal) -> config.dontCloseChat = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());

        // Fixes
        {
            OptionGroup.Builder qolFixesGroup = OptionGroup.createBuilder();
            qolFixesGroup.name(Component.translatable("animatium.category.qol.group.qol_fixes"));
            qolFixesGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.fixMirrorArmSwing"))
                    .description(OptionDescription.of(Component.translatable("animatium.fixMirrorArmSwing.description")))
                    .binding(
                            defaults.fixMirrorArmSwing,
                            () -> config.fixMirrorArmSwing,
                            (newVal) -> config.fixMirrorArmSwing = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            qolFixesGroup.option(Option.<Boolean>createBuilder()
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
            qolFixesGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.fixOffHandUsingPose"))
                    .description(OptionDescription.of(Component.translatable("animatium.fixOffHandUsingPose.description")))
                    .binding(
                            defaults.fixOffHandUsingPose,
                            () -> config.fixOffHandUsingPose,
                            (newVal) -> config.fixOffHandUsingPose = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            category.group(qolFixesGroup.build());
        }

        return category.build();
    }
}
