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

package org.visuals.legacy.animatium.mixins.v1.general.camera;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.mixins.accessor.PlayerAccessor;
import org.visuals.legacy.animatium.util.enums.CameraVersion;
import org.visuals.legacy.animatium.util.enums.SneakAnimationSetting;

@Mixin(Camera.class)
public abstract class MixinCamera {
    @Shadow
    private float eyeHeightOld;

    @Shadow
    private float eyeHeight;

    @Shadow
    private Entity entity;

    @Shadow
    protected abstract void move(float zoom, float dy, float dx);

    @Inject(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V", shift = At.Shift.AFTER))
    private void animatium$removeSmoothSneaking(CallbackInfo ci) {
        if (Animatium.isEnabled() && !AnimatiumConfig.instance().movement.sneakAnimation.isSmooth()) {
            this.eyeHeightOld = this.eyeHeight;
            this.eyeHeight = this.animatium$getStandingEyeHeight();
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getEyeHeight()F"))
    private float animatium$useOldEyeHeight(Entity instance, Operation<Float> original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().movement.fakeOldSneakEyeHeight) {
            return this.animatium$getStandingEyeHeight();
        } else {
            return original.call(instance);
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/Camera;eyeHeight:F"))
    private void animatium$oldSneakAnimationInterpolation(Camera instance, float value, Operation<Void> original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().movement.sneakAnimation == SneakAnimationSetting.V1_7 && this.entity.getEyeHeight() < eyeHeight) {
            this.eyeHeight = this.animatium$getStandingEyeHeight();
        } else {
            original.call(instance, value);
        }
    }

    @WrapOperation(method = "getMaxZoom", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/ClipContext$Block;VISUAL:Lnet/minecraft/world/level/ClipContext$Block;", opcode = Opcodes.GETSTATIC))
    private ClipContext.Block animatium$cameraTransparentPassthrough(Operation<ClipContext.Block> original) {
        if (Animatium.isEnabled() && !AnimatiumConfig.instance().screen.cameraTransparentPassthrough) {
            return ClipContext.Block.OUTLINE;
        } else {
            return original.call();
        }
    }

    @Inject(method = "setup", at = @At(value = "TAIL"))
    private void animatium$oldCameraVersion(BlockGetter area, Entity entity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        // TODO: Fix bed/sleeping position
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.cameraVersion != CameraVersion.VANILLA && !thirdPerson && !(entity instanceof LivingEntity && ((LivingEntity) entity).isSleeping())) {
            final int ordinal = AnimatiumConfig.instance().screen.cameraVersion.ordinal();
            if (ordinal <= CameraVersion.V1_14_V1_14_3.ordinal()) {
                // <= 1.14.3
                this.move(-0.05000000074505806F, 0.0F, 0.0F);
                // <= 1.13.2
                if (ordinal <= CameraVersion.V1_9_V1_13_2.ordinal()) {
                    this.move(0.1F, 0.0F, 0.0F);
                    // <= 1.8
                    if (ordinal == CameraVersion.V1_8.ordinal()) {
                        this.move(-0.15F, 0, 0); // unfixing parallax
                    }
                }
            }
        }
    }

    @Unique
    private float animatium$getStandingEyeHeight() {
        final float standingEyeHeight = this.entity.getEyeHeight();
        if (Animatium.isEnabled() &&
                AnimatiumConfig.instance().movement.fakeOldSneakEyeHeight &&
                this.entity.hasPose(Pose.CROUCHING) &&
                this.entity instanceof Player player &&
                ((PlayerAccessor) player).animatium$canChangeIntoPose(Pose.STANDING)) {
            return 1.54F * player.getScale();
        } else {
            return standingEyeHeight;
        }
    }
}
