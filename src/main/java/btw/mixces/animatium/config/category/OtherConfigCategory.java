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
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;

public final class OtherConfigCategory {
    public static ConfigCategory setup(AnimatiumConfig defaults, AnimatiumConfig config) {
        ConfigCategory.Builder category = ConfigCategory.createBuilder();
        category.name(Component.translatable("animatium.category.other"));

        // Sky
        {
            OptionGroup.Builder skyGroup = OptionGroup.createBuilder();
            skyGroup.name(Component.translatable("animatium.category.other.group.sky"));
            skyGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.blueVoidSky"))
                    .description(OptionDescription.of(Component.translatable("animatium.blueVoidSky.description")))
                    .binding(
                            defaults.blueVoidSky,
                            () -> config.blueVoidSky,
                            (newVal) -> config.blueVoidSky = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            skyGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.skyHorizonHeight"))
                    .description(OptionDescription.of(Component.translatable("animatium.skyHorizonHeight.description")))
                    .binding(
                            defaults.skyHorizonHeight,
                            () -> config.skyHorizonHeight,
                            (newVal) -> config.skyHorizonHeight = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            skyGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.voidSkyFogHeight"))
                    .description(OptionDescription.of(Component.translatable("animatium.voidSkyFogHeight.description")))
                    .binding(
                            defaults.voidSkyFogHeight,
                            () -> config.voidSkyFogHeight,
                            (newVal) -> config.voidSkyFogHeight = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            skyGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.cloudHeight"))
                    .description(OptionDescription.of(Component.translatable("animatium.cloudHeight.description")))
                    .binding(
                            defaults.cloudHeight,
                            () -> config.cloudHeight,
                            (newVal) -> config.cloudHeight = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            category.group(skyGroup.build());
        }

        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.thirdPersonSwordBlockingPosition"))
                .description(OptionDescription.of(Component.translatable("animatium.thirdPersonSwordBlockingPosition.description")))
                .binding(
                        defaults.thirdPersonSwordBlockingPosition,
                        () -> config.thirdPersonSwordBlockingPosition,
                        (newVal) -> config.thirdPersonSwordBlockingPosition = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.lockBlockingArmRotation"))
                .description(OptionDescription.of(Component.translatable("animatium.lockBlockingArmRotation.description")))
                .binding(
                        defaults.lockBlockingArmRotation,
                        () -> config.lockBlockingArmRotation,
                        (newVal) -> config.lockBlockingArmRotation = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.projectileAgeCheck"))
                .description(OptionDescription.of(Component.translatable("animatium.projectileAgeCheck.description")))
                .binding(
                        defaults.projectileAgeCheck,
                        () -> config.projectileAgeCheck,
                        (newVal) -> config.projectileAgeCheck = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.blockMiningProgress"))
                .description(OptionDescription.of(Component.translatable("animatium.blockMiningProgress.description")))
                .binding(
                        defaults.blockMiningProgress,
                        () -> config.blockMiningProgress,
                        (newVal) -> config.blockMiningProgress = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.inventoryEntityScissor"))
                .description(OptionDescription.of(Component.translatable("animatium.inventoryEntityScissor.description")))
                .binding(
                        defaults.inventoryEntityScissor,
                        () -> config.inventoryEntityScissor,
                        (newVal) -> config.inventoryEntityScissor = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.blockOutlineRendering"))
                .description(OptionDescription.of(Component.translatable("animatium.blockOutlineRendering.description")))
                .binding(
                        defaults.blockOutlineRendering,
                        () -> config.blockOutlineRendering,
                        (newVal) -> config.blockOutlineRendering = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.modelWhilstSleeping"))
                .description(OptionDescription.of(Component.translatable("animatium.modelWhilstSleeping.description")))
                .binding(
                        defaults.modelWhilstSleeping,
                        () -> config.modelWhilstSleeping,
                        (newVal) -> config.modelWhilstSleeping = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.entityArmorHurtTint"))
                .description(OptionDescription.of(Component.translatable("animatium.entityArmorHurtTint.description")))
                .binding(
                        defaults.entityArmorHurtTint,
                        () -> config.entityArmorHurtTint,
                        (newVal) -> config.entityArmorHurtTint = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.itemGlintOnEntity"))
                .description(OptionDescription.of(Component.translatable("animatium.itemGlintOnEntity.description")))
                .binding(
                        defaults.itemGlintOnEntity,
                        () -> config.itemGlintOnEntity,
                        (newVal) -> config.itemGlintOnEntity = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.maxGlintProperties"))
                .description(OptionDescription.of(Component.translatable("animatium.maxGlintProperties.description")))
                .binding(
                        defaults.maxGlintProperties,
                        () -> config.maxGlintProperties,
                        (newVal) -> config.maxGlintProperties = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.armorHurtRendering"))
                .description(OptionDescription.of(Component.translatable("animatium.armorHurtRendering.description")))
                .binding(
                        defaults.armorHurtRendering,
                        () -> config.armorHurtRendering,
                        (newVal) -> config.armorHurtRendering = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.glintRendering"))
                .description(OptionDescription.of(Component.translatable("animatium.glintRendering.description")))
                .binding(
                        defaults.glintRendering,
                        () -> config.glintRendering,
                        (newVal) -> config.glintRendering = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.highAttackSpeedVisual"))
                .description(OptionDescription.of(Component.translatable("animatium.highAttackSpeedVisual.description")))
                .binding(
                        defaults.highAttackSpeedVisual,
                        () -> config.highAttackSpeedVisual,
                        (newVal) -> config.highAttackSpeedVisual = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.entityGlowOutline"))
                .description(OptionDescription.of(Component.translatable("animatium.entityGlowOutline.description")))
                .binding(
                        defaults.entityGlowOutline,
                        () -> config.entityGlowOutline,
                        (newVal) -> config.entityGlowOutline = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.modernCombatSounds"))
                .description(OptionDescription.of(Component.translatable("animatium.modernCombatSounds.description")))
                .binding(
                        defaults.modernCombatSounds,
                        () -> config.modernCombatSounds,
                        (newVal) -> config.modernCombatSounds = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.modernCombatParticles"))
                .description(OptionDescription.of(Component.translatable("animatium.modernCombatParticles.description")))
                .binding(
                        defaults.modernCombatParticles,
                        () -> config.modernCombatParticles,
                        (newVal) -> config.modernCombatParticles = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.heldItemArmLogic"))
                .description(OptionDescription.of(Component.translatable("animatium.heldItemArmLogic.description")))
                .binding(
                        defaults.heldItemArmLogic,
                        () -> config.heldItemArmLogic,
                        (newVal) -> config.heldItemArmLogic = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.flameDimensions"))
                .description(OptionDescription.of(Component.translatable("animatium.flameDimensions.description")))
                .binding(
                        defaults.flameDimensions,
                        () -> config.flameDimensions,
                        (newVal) -> config.flameDimensions = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.flameOffset"))
                .description(OptionDescription.of(Component.translatable("animatium.flameOffset.description")))
                .binding(
                        defaults.flameOffset,
                        () -> config.flameOffset,
                        (newVal) -> config.flameOffset = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());

        return category.build();
    }
}
