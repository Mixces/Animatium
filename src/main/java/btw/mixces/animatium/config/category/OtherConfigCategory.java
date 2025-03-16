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
                    .name(Component.translatable("animatium.oldBlueVoidSky"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldBlueVoidSky.description")))
                    .binding(
                            defaults.oldBlueVoidSky,
                            () -> config.oldBlueVoidSky,
                            (newVal) -> config.oldBlueVoidSky = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            skyGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.oldSkyHorizonHeight"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldSkyHorizonHeight.description")))
                    .binding(
                            defaults.oldSkyHorizonHeight,
                            () -> config.oldSkyHorizonHeight,
                            (newVal) -> config.oldSkyHorizonHeight = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            skyGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.oldVoidSkyFogHeight"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldVoidSkyFogHeight.description")))
                    .binding(
                            defaults.oldVoidSkyFogHeight,
                            () -> config.oldVoidSkyFogHeight,
                            (newVal) -> config.oldVoidSkyFogHeight = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            skyGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.oldCloudHeight"))
                    .description(OptionDescription.of(Component.translatable("animatium.oldCloudHeight.description")))
                    .binding(
                            defaults.oldCloudHeight,
                            () -> config.oldCloudHeight,
                            (newVal) -> config.oldCloudHeight = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            category.group(skyGroup.build());
        }

        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.oldThirdpersonSwordBlockingPosition"))
                .description(OptionDescription.of(Component.translatable("animatium.oldThirdpersonSwordBlockingPosition.description")))
                .binding(
                        defaults.oldThirdpersonSwordBlockingPosition,
                        () -> config.oldThirdpersonSwordBlockingPosition,
                        (newVal) -> config.oldThirdpersonSwordBlockingPosition = newVal)
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
                .name(Component.translatable("animatium.disableProjectileAgeCheck"))
                .description(OptionDescription.of(Component.translatable("animatium.disableProjectileAgeCheck.description")))
                .binding(
                        defaults.disableProjectileAgeCheck,
                        () -> config.disableProjectileAgeCheck,
                        (newVal) -> config.disableProjectileAgeCheck = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.oldBlockMiningProgress"))
                .description(OptionDescription.of(Component.translatable("animatium.oldBlockMiningProgress.description")))
                .binding(
                        defaults.oldBlockMiningProgress,
                        () -> config.oldBlockMiningProgress,
                        (newVal) -> config.oldBlockMiningProgress = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.disableInventoryEntityScissor"))
                .description(OptionDescription.of(Component.translatable("animatium.disableInventoryEntityScissor.description")))
                .binding(
                        defaults.disableInventoryEntityScissor,
                        () -> config.disableInventoryEntityScissor,
                        (newVal) -> config.disableInventoryEntityScissor = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.oldBlockOutlineRendering"))
                .description(OptionDescription.of(Component.translatable("animatium.oldBlockOutlineRendering.description")))
                .binding(
                        defaults.oldBlockOutlineRendering,
                        () -> config.oldBlockOutlineRendering,
                        (newVal) -> config.oldBlockOutlineRendering = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.hideModelWhilstSleeping"))
                .description(OptionDescription.of(Component.translatable("animatium.hideModelWhilstSleeping.description")))
                .binding(
                        defaults.hideModelWhilstSleeping,
                        () -> config.hideModelWhilstSleeping,
                        (newVal) -> config.hideModelWhilstSleeping = newVal)
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
                .name(Component.translatable("animatium.forceItemGlintOnEntity"))
                .description(OptionDescription.of(Component.translatable("animatium.forceItemGlintOnEntity.description")))
                .binding(
                        defaults.forceItemGlintOnEntity,
                        () -> config.forceItemGlintOnEntity,
                        (newVal) -> config.forceItemGlintOnEntity = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.forceMaxGlintProperties"))
                .description(OptionDescription.of(Component.translatable("animatium.forceMaxGlintProperties.description")))
                .binding(
                        defaults.forceMaxGlintProperties,
                        () -> config.forceMaxGlintProperties,
                        (newVal) -> config.forceMaxGlintProperties = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.oldArmorHurtRendering"))
                .description(OptionDescription.of(Component.translatable("animatium.oldArmorHurtRendering.description")))
                .binding(
                        defaults.oldArmorHurtRendering,
                        () -> config.oldArmorHurtRendering,
                        (newVal) -> config.oldArmorHurtRendering = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.oldGlintRendering"))
                .description(OptionDescription.of(Component.translatable("animatium.oldGlintRendering.description")))
                .binding(
                        defaults.oldGlintRendering,
                        () -> config.oldGlintRendering,
                        (newVal) -> config.oldGlintRendering = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.forceHighAttackSpeedVisual"))
                .description(OptionDescription.of(Component.translatable("animatium.forceHighAttackSpeedVisual.description")))
                .binding(
                        defaults.forceHighAttackSpeedVisual,
                        () -> config.forceHighAttackSpeedVisual,
                        (newVal) -> config.forceHighAttackSpeedVisual = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.disableEntityGlowOutline"))
                .description(OptionDescription.of(Component.translatable("animatium.disableEntityGlowOutline.description")))
                .binding(
                        defaults.disableEntityGlowOutline,
                        () -> config.disableEntityGlowOutline,
                        (newVal) -> config.disableEntityGlowOutline = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.disableModernCombatSounds"))
                .description(OptionDescription.of(Component.translatable("animatium.disableModernCombatSounds.description")))
                .binding(
                        defaults.disableModernCombatSounds,
                        () -> config.disableModernCombatSounds,
                        (newVal) -> config.disableModernCombatSounds = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.disableModernCombatParticles"))
                .description(OptionDescription.of(Component.translatable("animatium.disableModernCombatParticles.description")))
                .binding(
                        defaults.disableModernCombatParticles,
                        () -> config.disableModernCombatParticles,
                        (newVal) -> config.disableModernCombatParticles = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.oldHeldItemArmLogic"))
                .description(OptionDescription.of(Component.translatable("animatium.oldHeldItemArmLogic.description")))
                .binding(
                        defaults.oldHeldItemArmLogic,
                        () -> config.oldHeldItemArmLogic,
                        (newVal) -> config.oldHeldItemArmLogic = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.oldFlameDimensions"))
                .description(OptionDescription.of(Component.translatable("animatium.oldFlameDimensions.description")))
                .binding(
                        defaults.oldFlameDimensions,
                        () -> config.oldFlameDimensions,
                        (newVal) -> config.oldFlameDimensions = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.oldFlameOffset"))
                .description(OptionDescription.of(Component.translatable("animatium.oldFlameOffset.description")))
                .binding(
                        defaults.oldFlameOffset,
                        () -> config.oldFlameOffset,
                        (newVal) -> config.oldFlameOffset = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());

        return category.build();
    }
}
