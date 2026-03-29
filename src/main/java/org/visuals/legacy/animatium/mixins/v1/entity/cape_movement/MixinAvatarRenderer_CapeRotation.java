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

package org.visuals.legacy.animatium.mixins.v1.entity.cape_movement;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Avatar;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(AvatarRenderer.class)
public abstract class MixinAvatarRenderer_CapeRotation<AvatarLikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarLikeEntity, AvatarRenderState, PlayerModel> {
	public MixinAvatarRenderer_CapeRotation(EntityRendererProvider.Context context, boolean slim) {
		super(context, new PlayerModel(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slim), 0.5F);
	}

	@WrapOperation(method = "extractCapeState", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;rotLerp(FFF)F"))
	private static float animatium$changeLerpMethod(float delta, float start, float end, Operation<Float> original) {
		if (Animatium.isEnabled() && AnimatiumConfig.instance().movement.oldCapeMovement) {
			return Mth.lerp(delta, start, end);
		} else {
			return original.call(delta, start, end);
		}
	}

	@ModifyArg(method = "extractCapeState", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F", ordinal = 1), index = 2)
	private static float animatium$uncapRotation(float original) {
		if (Animatium.isEnabled() && AnimatiumConfig.instance().movement.disableCapeLean) {
			return Float.MAX_VALUE;
		} else {
			return original;
		}
	}

	@WrapWithCondition(method = "extractCapeState", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;capeLean:F", ordinal = 1, opcode = Opcodes.PUTFIELD))
	private static boolean animatium$dontAssignLeanField(AvatarRenderState instance, float value) {
		return !Animatium.isEnabled() || !AnimatiumConfig.instance().movement.oldCapeMovement;
	}

	@WrapWithCondition(method = "extractCapeState", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;capeLean2:F", ordinal = 1, opcode = Opcodes.PUTFIELD))
	private static boolean animatium$dontAssignLean2Field(AvatarRenderState instance, float value) {
		return !Animatium.isEnabled() || !AnimatiumConfig.instance().movement.oldCapeMovement;
	}
}
