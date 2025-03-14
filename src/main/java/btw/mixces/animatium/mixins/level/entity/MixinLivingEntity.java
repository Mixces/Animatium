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

package btw.mixces.animatium.mixins.level.entity;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.ViewBobbingStorage;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements ViewBobbingStorage {
    @Unique
    private float animatium$bobbingTilt = 0.0F;

    @Unique
    private float animatium$previousBobbingTilt = 0.0F;

    public MixinLivingEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Shadow
    public float yBodyRot;

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;abs(F)F"))
    private float animatium$rotateBackwardsWalking(float value, Operation<Float> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().getRotateBackwardsWalking()) {
            return 0F;
        } else {
            return original.call(value);
        }
    }

    @WrapOperation(method = "tickHeadTurn", at = @At(value = "INVOKE", target = "Ljava/lang/Math;abs(F)F"))
    private float animatium$removeHeadRotationInterpolation(float g, Operation<Float> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().getRotateBackwardsWalking()) {
            g = Mth.clamp(g, -75.0F, 75.0F);
            this.yBodyRot = this.getYRot() - g;
            if (Math.abs(g) > 50.0F) {
                this.yBodyRot += g * 0.2F;
            }

            return Float.MIN_VALUE;
        } else {
            return original.call(g);
        }
    }

    @WrapOperation(method = "lerpHeadRotationStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;rotLerp(DDD)D"))
    public double animatium$removeHeadRotationInterpolation(double delta, double start, double end, Operation<Double> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().getRemoveHeadRotationInterpolation()) {
            return end;
        } else {
            return original.call(delta, start, end);
        }
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;tickEffects()V", shift = At.Shift.BEFORE))
    private void animatium$updatePreviousBobbingTiltValue(CallbackInfo ci) {
        if (AnimatiumConfig.instance().getFixVerticalBobbingTilt()) {
            this.animatium$previousBobbingTilt = this.animatium$bobbingTilt;
        }
    }

    @WrapOperation(method = "tickEffects", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"))
    private boolean animatium$hideFirstpersonParticles(List<ParticleOptions> particleOptions, Operation<Boolean> original) {
        Minecraft client = Minecraft.getInstance();
        if (AnimatiumClient.isEnabled() && !AnimatiumConfig.instance().getFirstPersonParticles() && (Object) this == client.player && client.options.getCameraType().isFirstPerson()) {
            return true;
        } else {
            return original.call(particleOptions);
        }
    }

    @Override
    public void animatium$setBobbingTilt(float bobbingTilt) {
        this.animatium$bobbingTilt = bobbingTilt;
    }

    @Override
    public float animatium$getBobbingTilt() {
        return this.animatium$bobbingTilt;
    }

    @Override
    public float animatium$getPreviousBobbingTilt() {
        return this.animatium$previousBobbingTilt;
    }
}
