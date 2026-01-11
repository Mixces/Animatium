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

package org.visuals.legacy.animatium.mixins.v1.rendering.items.flat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.ItemUtils;
import org.visuals.legacy.animatium.util.Utils;
import org.visuals.legacy.animatium.util.enums.FishingRodVersion;
import org.visuals.legacy.animatium.util.states.ItemUtilityRenderState;

@Mixin(ItemStackRenderState.LayerRenderState.class)
public abstract class MixinItemStackRenderLayerState {
	@Shadow
	ItemTransform transform;

	@Shadow(aliases = "field_55345") // ItemStackRenderState.this
	@Final
	ItemStackRenderState itemStackRenderState;

	@ModifyArg(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitItem(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/ItemDisplayContext;III[ILjava/util/List;Lnet/minecraft/client/renderer/RenderType;Lnet/minecraft/client/renderer/item/ItemStackRenderState$FoilType;)V"), index = 8)
	private ItemStackRenderState.FoilType animatium$disableGlintOn2DItems(final ItemStackRenderState.FoilType foilType) {
		final boolean glintDropped = !AnimatiumConfig.instance().items.glintOnItemDrops2D;
		final boolean glintFramed = !AnimatiumConfig.instance().items.glintOnItemFramed2D;
		if (Animatium.isEnabled() &&
				(glintDropped && itemStackRenderState.displayContext == ItemDisplayContext.GROUND) ||
				(glintFramed && itemStackRenderState.displayContext == ItemDisplayContext.FIXED)) {
			return ItemStackRenderState.FoilType.NONE;
		} else {
			return foilType;
		}
	}

	// TODO/MOVE
	@Inject(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/model/ItemTransform;apply(ZLcom/mojang/blaze3d/vertex/PoseStack$Pose;)V"))
	private void animatium$itemPositions(final PoseStack poseStack, final SubmitNodeCollector nodeCollector, final int packedLight, final int packedOverlay, final int outlineColor, final CallbackInfo ci) {
		if (Animatium.isEnabled()) {
			final ItemStack stack = ((ItemUtilityRenderState) itemStackRenderState).animatium$getItemStack();
			if (!stack.isEmpty()) {
				final ItemDisplayContext itemDisplayContext = this.itemStackRenderState.displayContext;
				final boolean isGui = itemDisplayContext == ItemDisplayContext.GUI;
				final boolean isFirstPerson = itemDisplayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || itemDisplayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
				final boolean isThirdPerson = itemDisplayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || itemDisplayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
				float x = this.transform.translation().x();
				float y = this.transform.translation().y();
				float z = this.transform.translation().z();
				if (AnimatiumConfig.instance().items.fishingRodVersion != FishingRodVersion.VANILLA && ItemUtils.isFishingRodItem(stack) && isFirstPerson) {
					final int ordinal = AnimatiumConfig.instance().items.fishingRodVersion.ordinal();
					if (ordinal <= FishingRodVersion.V1_8.ordinal()) {
						poseStack.translate(0.070625, 0.1, 0.020625);
					}

					poseStack.translate(x, y, z);
					if (ordinal == FishingRodVersion.V1_7.ordinal()) {
						poseStack.mulPose(Axis.YP.rotationDegrees(180));
					}

					poseStack.translate(-x, -y, -z);
				}

				if (AnimatiumConfig.instance().items.thinBlockPositions && ItemUtils.isThinBlockItem(stack)) {
					if (isFirstPerson) {
						poseStack.translate(0, -4.2 * 0.0625, 0);
					} else if (isThirdPerson) {
						poseStack.translate(0, 0, -2 * 0.0625);
					}
				}

				// TODO/NEED TO FIX
				if (AnimatiumConfig.instance().items.skullPosition && ItemUtils.isSkullBlock(stack) && isGui && !AnimatiumConfig.instance().items.mobHeadIcons) {
					poseStack.translate(x, y, z);
					poseStack.mulPose(Axis.XP.rotationDegrees(Utils.toRadians(this.transform.rotation().x())));
					poseStack.mulPose(Axis.YP.rotationDegrees(Utils.toRadians(this.transform.rotation().y())));
					poseStack.mulPose(Axis.ZP.rotationDegrees(Utils.toRadians(this.transform.rotation().x())));
					poseStack.scale(0.9F, 0.9F, 0.9F);
					poseStack.scale(this.transform.scale().x(), this.transform.scale().y(), this.transform.scale().z());
					animatium$doInverseTransformations(poseStack);
				}
			}
		}
	}

	@Unique
	private void animatium$doInverseTransformations(final PoseStack poseStack) {
		poseStack.scale(1 / this.transform.scale().x(), 1 / this.transform.scale().y(), 1 / this.transform.scale().z());
		poseStack.mulPose(Axis.ZP.rotationDegrees(-Utils.toRadians(this.transform.rotation().x())));
		poseStack.mulPose(Axis.YP.rotationDegrees(-Utils.toRadians(this.transform.rotation().y())));
		poseStack.mulPose(Axis.XP.rotationDegrees(-Utils.toRadians(this.transform.rotation().z())));
		poseStack.translate(-this.transform.translation().x(), -this.transform.translation().y(), -this.transform.translation().z());
	}
}
