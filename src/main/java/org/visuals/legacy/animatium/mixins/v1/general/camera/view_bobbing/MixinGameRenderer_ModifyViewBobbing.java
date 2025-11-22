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

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientAvatarState;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.states.ViewBobbingStorage;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer_ModifyViewBobbing {
    @Shadow
    @Final
    private Minecraft minecraft;

    @WrapOperation(method = "bobHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getHurtDir()F"))
    private float animatium$damageTilt(LivingEntity instance, Operation<Float> original) {
        if (Animatium.ENABLED && AnimatiumConfig.instance().movement.legacyDamageTilt) {
            return 0.0F;
        } else {
            return original.call(instance);
        }
    }

    @WrapWithCondition(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;bobView(Lcom/mojang/blaze3d/vertex/PoseStack;F)V"))
    private boolean animatium$minimalViewBobbing(GameRenderer instance, PoseStack poseStack, float partialTicks) {
        return !Animatium.ENABLED || !AnimatiumConfig.instance().extras.minimalViewBobbing;
    }

    @WrapOperation(method = "bobHurt", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;hurtTime:I", opcode = Opcodes.GETFIELD))
    private int animatium$offsetHurtTime(LivingEntity instance, Operation<Integer> original) {
        final int hurtTime = original.call(instance);
        if (Animatium.ENABLED && AnimatiumConfig.instance().movement.offsetHurtTime) {
            return Math.max(hurtTime - 1, 0);
        } else {
            return hurtTime;
        }
    }

    @Inject(method = "bobView", at = @At("TAIL"))
    private void animatium$fixVerticalBobbingTilt(PoseStack poseStack, float partialTicks, CallbackInfo ci) {
        if (AnimatiumConfig.instance().fixes.fixVerticalBobbingTilt && this.minecraft.getCameraEntity() instanceof AbstractClientPlayer player) {
            ViewBobbingStorage bobbingAccessor = (ViewBobbingStorage) player;
            float fallDist = Mth.lerp(partialTicks, bobbingAccessor.animatium$getPreviousBobbingTilt(), bobbingAccessor.animatium$getBobbingTilt());
            poseStack.mulPose(Axis.XP.rotationDegrees(fallDist));
        }
    }

    @WrapOperation(method = "bobView", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/ClientAvatarState;getBackwardsInterpolatedWalkDistance(F)F"))
    private float animatium$viewBobbing$changeDistance(ClientAvatarState instance, float partialTicks, Operation<Float> original) {
        if (Animatium.ENABLED && AnimatiumConfig.instance().movement.viewBobbing && this.minecraft.getCameraEntity() instanceof AbstractClientPlayer abstractClientPlayer) {
            final float walkDist = ((ViewBobbingStorage) abstractClientPlayer).animatium$getHorizontalSpeed();
            final float walkDistO = ((ViewBobbingStorage) abstractClientPlayer).animatium$getPreviousHorizontalSpeed();
            return -(walkDist + (walkDist - walkDistO) * partialTicks);
        } else {
            return original.call(instance, partialTicks);
        }
    }

    // TODO/MOVE
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlobalSettingsUniform;update(IIDJLnet/minecraft/client/DeltaTracker;I)V"), index = 2)
    private double animatium$forceMaxGlintStrength(double original) {
        if (Animatium.ENABLED && AnimatiumConfig.instance().other.maxGlintProperties) {
            // 100% glint strength
            return 1.0F;
        } else {
            return original;
        }
    }
}
