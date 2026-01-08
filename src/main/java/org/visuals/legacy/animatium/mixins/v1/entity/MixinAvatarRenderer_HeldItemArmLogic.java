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

package org.visuals.legacy.animatium.mixins.v1.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(AvatarRenderer.class)
public abstract class MixinAvatarRenderer_HeldItemArmLogic<AvatarLikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarLikeEntity, AvatarRenderState, PlayerModel> {
	@Unique
	private final ThreadLocal<@Nullable AvatarRenderState> animatium$renderState = ThreadLocal.withInitial(() -> null);

	public MixinAvatarRenderer_HeldItemArmLogic(final EntityRendererProvider.Context context, final PlayerModel model, final float shadowRadius) {
		super(context, model, shadowRadius);
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
	private void animatium$storeAvatarState(final AvatarLikeEntity avatar, final AvatarRenderState avatarRenderState, final float tickDelta, final CallbackInfo ci) {
		animatium$renderState.set(avatarRenderState);
	}

	@Inject(method = "renderHand", at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/geom/ModelPart;visible:Z", ordinal = 2, opcode = Opcodes.PUTFIELD))
	private void animatium$heldItemArmLogic(final PoseStack poseStack, final SubmitNodeCollector nodeCollector, final int packedLight, final ResourceLocation skinTexture, final ModelPart modelPart, final boolean renderSleeve, final CallbackInfo ci) {
		if (Animatium.isEnabled() && AnimatiumConfig.instance().other.heldItemArmLogic) {
			final HumanoidArm arm = modelPart == model.rightArm ? HumanoidArm.RIGHT : HumanoidArm.LEFT;
			final AvatarRenderState avatarRenderState = animatium$renderState.get();
			if (avatarRenderState != null && (avatarRenderState.mainArm == arm ? avatarRenderState.rightArmPose : avatarRenderState.leftArmPose) == HumanoidModel.ArmPose.ITEM) {
				// Adapted from the ITEM arm pose rotations in HumanoidModel#poseRightArm/poseLeftArm
				modelPart.xRot = modelPart.xRot * 0.5F - (float) (Math.PI / 10);
				modelPart.yRot = 0.0F;
			}
		}
	}
}
