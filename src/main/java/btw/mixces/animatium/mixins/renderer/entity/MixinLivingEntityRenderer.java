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

package btw.mixces.animatium.mixins.renderer.entity;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.EntityUtils;
import btw.mixces.animatium.util.PlayerUtils;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<S extends LivingEntityRenderState> {
    @Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 1))
    private void animatium$syncPlayerModelWithEyeHeight(S livingEntityRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (AnimatiumClient.getEnabled() && AnimatiumConfig.instance().getSyncPlayerModelWithEyeHeight()) {
            Minecraft client = Minecraft.getInstance();
            LocalPlayer player = client.player;
            if (livingEntityRenderState instanceof PlayerRenderState state && player != null && state.id == player.getId()) {
                float cameraLerpValue = PlayerUtils.lerpCameraPosition(client.gameRenderer.getMainCamera());
                poseStack.translate(0.0F, (Player.STANDING_DIMENSIONS.eyeHeight() * player.getScale()) - cameraLerpValue, 0.0F);
            }
        }
    }

    @ModifyExpressionValue(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isAlive()Z"))
    private boolean animatium$oldDeathLimbs(boolean original) {
        if (AnimatiumClient.getEnabled() && AnimatiumConfig.instance().getOldDeathLimbs()) {
            return true;
        } else {
            return original;
        }
    }

    @WrapOperation(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;D)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getCameraEntity()Lnet/minecraft/world/entity/Entity;"))
    private Entity animatium$showNametagInThirdperson(Minecraft instance, Operation<Entity> original) {
        if (AnimatiumClient.getEnabled() && AnimatiumConfig.instance().getShowNametagInThirdperson()) {
            return null;
        } else {
            return original.call(instance);
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"), cancellable = true)
    private void animatium$hideModelWhilstSleeping(S livingEntityRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        Entity entity = EntityUtils.getEntityByState(livingEntityRenderState);
        if (AnimatiumClient.getEnabled() && AnimatiumConfig.instance().getHideModelWhilstSleeping() &&
                entity instanceof LivingEntity livingEntity &&
                livingEntity == Objects.requireNonNull(Minecraft.getInstance().player) &&
                livingEntityRenderState.hasPose(Pose.SLEEPING) &&
                livingEntity.isSleeping()) {
            ci.cancel();
        }
    }

    @WrapOperation(method = "setupRotations", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;deathTime:F"))
    private float animatium$disableDeathTopple(LivingEntityRenderState instance, Operation<Float> original, @Local(argsOnly = true) S state) {
        Entity entity = EntityUtils.getEntityByState(state);
        if (AnimatiumClient.getEnabled() && AnimatiumConfig.instance().getDisableEntityDeathTopple() && entity instanceof Player) {
            return 0;
        } else {
            return original.call(instance);
        }
    }
}
