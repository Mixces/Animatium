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

package org.visuals.legacy.animatium.mixins.v1.general.camera.view_bobbing;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.states.ViewBobbingStorage;

@Mixin(Entity.class)
public abstract class MixinEntity_HorizontalViewBobbing implements ViewBobbingStorage {
    @Unique
    private float animatium$horizontalSpeed = 0.0F;

    @Unique
    private float animatium$previousHorizontalSpeed = 0.0F;

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;handlePortal()V", shift = At.Shift.AFTER))
    private void animatium$storePreviousHorizontalSpeed(final CallbackInfo ci) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().movement.handViewBobbingMovement) {
            this.animatium$previousHorizontalSpeed = this.animatium$horizontalSpeed;
        }
    }

    @Inject(method = "applyMovementEmissionAndPlaySound", at = @At("HEAD"))
    private void animatium$storeHorizontalSpeed(final Entity.MovementEmission emission, final Vec3 clippedMovement, final BlockPos effectPos, final BlockState effectState, final CallbackInfo ci) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().movement.handViewBobbingMovement) {
            this.animatium$horizontalSpeed = this.animatium$horizontalSpeed + (float) clippedMovement.horizontalDistance() * 0.6F;
        }
    }

    @Override
    public float animatium$getHorizontalSpeed() {
        return this.animatium$horizontalSpeed;
    }

    @Override
    public float animatium$getPreviousHorizontalSpeed() {
        return this.animatium$previousHorizontalSpeed;
    }
}
