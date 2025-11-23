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
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import org.visuals.legacy.animatium.mixins.accessor.GameRendererAccessor;
import org.visuals.legacy.animatium.util.compatibility.Mods;

public class ExtrasConfigCategory {
    public boolean minimalViewBobbing = false;
    public boolean showNameTagInThirdPerson = false;
    public boolean hideNameTagBackground = false;
    public boolean nameTagTextShadow = false;
    public boolean debugHudTextColor = false;
    public boolean persistentBlockOutline = false;
    public boolean offhandUsageSwinging = false;
    public boolean alwaysUsageSwing = false;
    public boolean alwaysSharpParticles = false;
    public boolean disableRecipeAndTutorialToasts = false;
    public boolean showArmWhileInvisible = false;
    public boolean fakeMissPenaltySwing = false;
    public boolean dontMoveBlueVoid = false;
    public boolean disableEntityDeathTopple = false;
    public boolean deepRedHurtTint = false;
    public boolean disableParticlePhysics = false;
    public boolean disableFirstPersonParticles = false;
    public boolean dontClearChat = false;
    public boolean dontCloseChat = false;

    public static ConfigCategory setup(final ExtrasConfigCategory defaults, final ExtrasConfigCategory config) {
        final ConfigCategory.Builder category = ConfigCategory.createBuilder();
        category.name(Component.translatable("animatium.category.extras"));
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
                .name(Component.translatable("animatium.showNameTagInThirdPerson"))
                .description(OptionDescription.of(Component.translatable("animatium.showNameTagInThirdPerson.description")))
                .binding(
                        defaults.showNameTagInThirdPerson,
                        () -> config.showNameTagInThirdPerson,
                        (newVal) -> config.showNameTagInThirdPerson = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.hideNameTagBackground"))
                .description(OptionDescription.of(Component.translatable("animatium.hideNameTagBackground.description")))
                .binding(
                        defaults.hideNameTagBackground,
                        () -> config.hideNameTagBackground,
                        (newVal) -> config.hideNameTagBackground = newVal)
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
                .name(Component.translatable("animatium.alwaysUsageSwing"))
                .description(OptionDescription.of(Component.translatable("animatium.alwaysUsageSwing.description")))
                .binding(
                        defaults.alwaysUsageSwing,
                        () -> config.alwaysUsageSwing,
                        (newVal) -> config.alwaysUsageSwing = newVal)
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
        if (!Mods.HAS_SODIUM_EXTRAS) {
            category.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.disableRecipeAndTutorialToasts"))
                    .description(OptionDescription.of(Component.translatable("animatium.disableRecipeAndTutorialToasts.description")))
                    .binding(
                            defaults.disableRecipeAndTutorialToasts,
                            () -> config.disableRecipeAndTutorialToasts,
                            (newVal) -> config.disableRecipeAndTutorialToasts = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
        }
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.showArmWhileInvisible"))
                .description(OptionDescription.of(Component.translatable("animatium.showArmWhileInvisible.description")))
                .binding(
                        defaults.showArmWhileInvisible,
                        () -> config.showArmWhileInvisible,
                        (newVal) -> config.showArmWhileInvisible = newVal)
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
                .name(Component.translatable("animatium.disableEntityDeathTopple"))
                .description(OptionDescription.of(Component.translatable("animatium.disableEntityDeathTopple.description")))
                .binding(
                        defaults.disableEntityDeathTopple,
                        () -> config.disableEntityDeathTopple,
                        (newVal) -> config.disableEntityDeathTopple = newVal)
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
                            ((GameRendererAccessor) Minecraft.getInstance().gameRenderer).animatium$setOverlayTexture(new OverlayTexture());
                        })
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.disableParticlePhysics"))
                .description(OptionDescription.of(Component.translatable("animatium.disableParticlePhysics.description")))
                .binding(
                        defaults.disableParticlePhysics,
                        () -> config.disableParticlePhysics,
                        (newVal) -> config.disableParticlePhysics = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.disableFirstPersonParticles"))
                .description(OptionDescription.of(Component.translatable("animatium.disableFirstPersonParticles.description")))
                .binding(
                        defaults.disableFirstPersonParticles,
                        () -> config.disableFirstPersonParticles,
                        (newVal) -> config.disableFirstPersonParticles = newVal)
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
        return category.build();
    }
}
