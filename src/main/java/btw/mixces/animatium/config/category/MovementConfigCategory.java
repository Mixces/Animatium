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
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;

public class MovementConfigCategory {
    // (Movement) Sneaking
    public boolean smoothSneaking = true;
    public boolean sneakAnimationInterpolation = false;
    public boolean fakeOldSneakEyeHeight = false;
    public boolean sneakingFeetPosition = true;
    public boolean syncPlayerModelWithEyeHeight = false;
    public boolean sneakAnimationWhileFlying = true;
    // (Movement) Other
    public boolean rotateBackwardsWalking = true;
    public boolean uncapBlockingHeadRotation = true;
    public boolean headRotationInterpolation = false;
    public boolean viewBobbing = true;
    public boolean deathLimbs = true;
    public boolean bowArmMovement = true;
    public boolean damageTilt = false;
    public boolean offsetHurtTime = false;
    // (Movement) Cape
    public boolean capeMovement = true;
    public boolean clampCapeLean = false;
    public boolean capeSwingRotation = true;
    public boolean capeChestplateTranslation = true;
    public boolean capeSneakPosition = false;

    public static ConfigCategory setup(MovementConfigCategory defaults, MovementConfigCategory config) {
        ConfigCategory.Builder category = ConfigCategory.createBuilder();
        category.name(Component.translatable("animatium.category.movement"));

        // Sneaking
        {
            OptionGroup.Builder sneakingGroup = OptionGroup.createBuilder();
            sneakingGroup.name(Component.translatable("animatium.category.movement.group.sneaking"));
            sneakingGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.smoothSneaking"))
                    .description(OptionDescription.of(Component.translatable("animatium.smoothSneaking.description")))
                    .binding(
                            defaults.smoothSneaking,
                            () -> config.smoothSneaking,
                            (newVal) -> config.smoothSneaking = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            sneakingGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.sneakAnimationInterpolation"))
                    .description(OptionDescription.of(Component.translatable("animatium.sneakAnimationInterpolation.description")))
                    .binding(
                            defaults.sneakAnimationInterpolation,
                            () -> config.sneakAnimationInterpolation,
                            (newVal) -> config.sneakAnimationInterpolation = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            sneakingGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.fakeOldSneakEyeHeight"))
                    .description(OptionDescription.of(Component.translatable("animatium.fakeOldSneakEyeHeight.description")))
                    .binding(
                            defaults.fakeOldSneakEyeHeight,
                            () -> config.fakeOldSneakEyeHeight,
                            (newVal) -> config.fakeOldSneakEyeHeight = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            sneakingGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.sneakingFeetPosition"))
                    .description(OptionDescription.of(Component.translatable("animatium.sneakingFeetPosition.description")))
                    .binding(
                            defaults.sneakingFeetPosition,
                            () -> config.sneakingFeetPosition,
                            (newVal) -> config.sneakingFeetPosition = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            sneakingGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.syncPlayerModelWithEyeHeight"))
                    .description(OptionDescription.of(Component.translatable("animatium.syncPlayerModelWithEyeHeight.description")))
                    .binding(
                            defaults.syncPlayerModelWithEyeHeight,
                            () -> config.syncPlayerModelWithEyeHeight,
                            (newVal) -> config.syncPlayerModelWithEyeHeight = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            sneakingGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.sneakAnimationWhileFlying"))
                    .description(OptionDescription.of(Component.translatable("animatium.sneakAnimationWhileFlying.description")))
                    .binding(
                            defaults.sneakAnimationWhileFlying,
                            () -> config.sneakAnimationWhileFlying,
                            (newVal) -> config.sneakAnimationWhileFlying = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            category.group(sneakingGroup.build());
        }

        {
            OptionGroup.Builder capeGroup = OptionGroup.createBuilder();
            capeGroup.name(Component.translatable("animatium.category.movement.group.cape"));
            capeGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.capeMovement"))
                    .description(OptionDescription.of(Component.translatable("animatium.capeMovement.description")))
                    .binding(
                            defaults.capeMovement,
                            () -> config.capeMovement,
                            (newVal) -> config.capeMovement = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            capeGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.clampCapeLean"))
                    .description(OptionDescription.of(Component.translatable("animatium.clampCapeLean.description")))
                    .binding(
                            defaults.clampCapeLean,
                            () -> config.clampCapeLean,
                            (newVal) -> config.clampCapeLean = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            capeGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.capeSwingRotation"))
                    .description(OptionDescription.of(Component.translatable("animatium.capeSwingRotation.description")))
                    .binding(
                            defaults.capeSwingRotation,
                            () -> config.capeSwingRotation,
                            (newVal) -> config.capeSwingRotation = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            capeGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.capeChestplateTranslation"))
                    .description(OptionDescription.of(Component.translatable("animatium.capeChestplateTranslation.description")))
                    .binding(
                            defaults.capeChestplateTranslation,
                            () -> config.capeChestplateTranslation,
                            (newVal) -> config.capeChestplateTranslation = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            capeGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.capeSneakPosition"))
                    .description(OptionDescription.of(Component.translatable("animatium.capeSneakPosition.description")))
                    .binding(
                            defaults.capeSneakPosition,
                            () -> config.capeSneakPosition,
                            (newVal) -> config.capeSneakPosition = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            category.group(capeGroup.build());
        }

        // Other
        {
            OptionGroup.Builder otherGroup = OptionGroup.createBuilder();
            otherGroup.name(Component.translatable("animatium.category.movement.group.other"));
            otherGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.rotateBackwardsWalking"))
                    .description(OptionDescription.of(Component.translatable("animatium.rotateBackwardsWalking.description")))
                    .binding(
                            defaults.rotateBackwardsWalking,
                            () -> config.rotateBackwardsWalking,
                            (newVal) -> config.rotateBackwardsWalking = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            otherGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.uncapBlockingHeadRotation"))
                    .description(OptionDescription.of(Component.translatable("animatium.uncapBlockingHeadRotation.description")))
                    .binding(
                            defaults.uncapBlockingHeadRotation,
                            () -> config.uncapBlockingHeadRotation,
                            (newVal) -> config.uncapBlockingHeadRotation = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            otherGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.headRotationInterpolation"))
                    .description(OptionDescription.of(Component.translatable("animatium.headRotationInterpolation.description")))
                    .binding(
                            defaults.headRotationInterpolation,
                            () -> config.headRotationInterpolation,
                            (newVal) -> config.headRotationInterpolation = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            otherGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.viewBobbing"))
                    .description(OptionDescription.of(Component.translatable("animatium.viewBobbing.description")))
                    .binding(
                            defaults.viewBobbing,
                            () -> config.viewBobbing,
                            (newVal) -> config.viewBobbing = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            otherGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.deathLimbs"))
                    .description(OptionDescription.of(Component.translatable("animatium.deathLimbs.description")))
                    .binding(
                            defaults.deathLimbs,
                            () -> config.deathLimbs,
                            (newVal) -> config.deathLimbs = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            otherGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.bowArmMovement"))
                    .description(OptionDescription.of(Component.translatable("animatium.bowArmMovement.description")))
                    .binding(
                            defaults.bowArmMovement,
                            () -> config.bowArmMovement,
                            (newVal) -> config.bowArmMovement = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            otherGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.damageTilt"))
                    .description(OptionDescription.of(Component.translatable("animatium.damageTilt.description")))
                    .binding(
                            defaults.damageTilt,
                            () -> config.damageTilt,
                            (newVal) -> config.damageTilt = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            otherGroup.option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("animatium.offsetHurtTime"))
                    .description(OptionDescription.of(Component.translatable("animatium.offsetHurtTime.description")))
                    .binding(
                            defaults.offsetHurtTime,
                            () -> config.offsetHurtTime,
                            (newVal) -> config.offsetHurtTime = newVal)
                    .controller(TickBoxControllerBuilder::create)
                    .build());
            category.group(otherGroup.build());
        }

        return category.build();
    }
}
