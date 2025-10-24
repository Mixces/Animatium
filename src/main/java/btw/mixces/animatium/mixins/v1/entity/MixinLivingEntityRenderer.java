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

package btw.mixces.animatium.mixins.v1.entity;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.Utils;
import btw.mixces.animatium.util.states.CameraUtilityRenderState;
import btw.mixces.animatium.util.states.UtilityRenderState;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.Pose;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<S extends LivingEntityRenderState> {
    // TODO/MOVE
    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 1))
    private void animatium$syncPlayerModelWithEyeHeight(S livingEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().movement.syncPlayerModelWithEyeHeight) {
            if (livingEntityRenderState instanceof AvatarRenderState) {
                final float cameraLerpValue = Utils.lerpCameraPosition((CameraUtilityRenderState) cameraRenderState);
                poseStack.translate(0.0F, (Avatar.STANDING_DIMENSIONS.eyeHeight() * livingEntityRenderState.scale) - cameraLerpValue, 0.0F);
            }
        }
    }

    @ModifyExpressionValue(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isAlive()Z"))
    private boolean animatium$deathLimbs(boolean original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().movement.deathLimbs) {
            return true;
        } else {
            return original;
        }
    }

    @WrapOperation(method = "setupRotations", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;deathTime:F", opcode = Opcodes.GETFIELD))
    private float animatium$entityDeathTopple(LivingEntityRenderState instance, Operation<Float> original, @Local(argsOnly = true) S livingRenderState) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().extras.disableEntityDeathTopple && livingRenderState instanceof AvatarRenderState) {
            return 0;
        } else {
            return original.call(instance);
        }
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At("HEAD"), cancellable = true)
    private void animatium$modelWhilstSleeping(S livingEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        if (AnimatiumClient.isEnabled() && !AnimatiumConfig.instance().other.modelWhilstSleeping &&
                livingEntityRenderState instanceof AvatarRenderState avatarRenderState &&
                avatarRenderState.id == Minecraft.getInstance().player.getId() &&
                avatarRenderState.hasPose(Pose.SLEEPING)) {
            final UtilityRenderState utilityRenderState = (UtilityRenderState) avatarRenderState;
            if (utilityRenderState.animatium$isSleeping()) {
                ci.cancel();
            }
        }
    }
}
