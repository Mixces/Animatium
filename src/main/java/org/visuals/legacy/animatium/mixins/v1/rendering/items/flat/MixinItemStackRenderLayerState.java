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
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
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

@Mixin(ItemStackRenderState.LayerRenderState.class)
public abstract class MixinItemStackRenderLayerState {
    @Shadow
    private ItemTransform itemTransform;

    @Shadow(aliases = "this$0")
    @Final
    ItemStackRenderState itemStackRenderState;

    @ModifyArg(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitItem(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/ItemDisplayContext;III[ILjava/util/List;Lnet/minecraft/client/renderer/item/ItemStackRenderState$FoilType;)V"), index = 7)
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
    @Inject(method = "applyTransform", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/cuboid/ItemTransform;apply(ZLcom/mojang/blaze3d/vertex/PoseStack$Pose;)V"))
    private void animatium$itemPositions(final PoseStack.Pose localPose, final CallbackInfo ci) {
        if (Animatium.isEnabled()) {
            final ItemStack stack = itemStackRenderState.animatium$getItemStack();
            if (!stack.isEmpty()) {
                final ItemDisplayContext itemDisplayContext = this.itemStackRenderState.displayContext;
                final boolean isGui = itemDisplayContext == ItemDisplayContext.GUI;
                final boolean isFirstPerson = itemDisplayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || itemDisplayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
                final boolean isThirdPerson = itemDisplayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || itemDisplayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                float x = this.itemTransform.translation().x();
                float y = this.itemTransform.translation().y();
                float z = this.itemTransform.translation().z();
                if (AnimatiumConfig.instance().items.fishingRodVersion != FishingRodVersion.VANILLA && ItemUtils.isFishingRodItem(stack) && isFirstPerson) {
                    final int ordinal = AnimatiumConfig.instance().items.fishingRodVersion.ordinal();
                    if (ordinal <= FishingRodVersion.V1_8.ordinal()) {
                        localPose.translate(0.070625F, 0.1F, 0.020625F);
                    }

                    localPose.translate(x, y, z);
                    if (ordinal == FishingRodVersion.V1_7.ordinal()) {
                        localPose.mulPose(Axis.YP.rotationDegrees(180).get(new Matrix4f()));
                    }

                    localPose.translate(-x, -y, -z);
                }

                if (AnimatiumConfig.instance().items.thinBlockPositions && ItemUtils.isThinBlockItem(stack)) {
                    if (isFirstPerson) {
                        localPose.translate(0.0F, -4.2F * 0.0625F, 0.0F);
                    } else if (isThirdPerson) {
                        localPose.translate(0.0F, 0.0F, -2.0F * 0.0625F);
                    }
                }

                // TODO/NEED TO FIX
                if (AnimatiumConfig.instance().items.skullPosition && ItemUtils.isSkullBlock(stack) && isGui && !AnimatiumConfig.instance().items.mobHeadIcons) {
                    localPose.translate(x, y, z);
                    localPose.mulPose(Axis.XP.rotationDegrees(Utils.toRadians(this.itemTransform.rotation().x())).get(new Matrix4f()));
                    localPose.mulPose(Axis.YP.rotationDegrees(Utils.toRadians(this.itemTransform.rotation().y())).get(new Matrix4f()));
                    localPose.mulPose(Axis.ZP.rotationDegrees(Utils.toRadians(this.itemTransform.rotation().x())).get(new Matrix4f()));
                    localPose.scale(0.9F, 0.9F, 0.9F);
                    localPose.scale(this.itemTransform.scale().x(), this.itemTransform.scale().y(), this.itemTransform.scale().z());
                    animatium$doInverseTransformations(localPose);
                }
            }
        }
    }

    @Unique
    private void animatium$doInverseTransformations(final PoseStack.Pose localPose) {
        localPose.scale(1 / this.itemTransform.scale().x(), 1 / this.itemTransform.scale().y(), 1 / this.itemTransform.scale().z());
        localPose.mulPose(Axis.ZP.rotationDegrees(-Utils.toRadians(this.itemTransform.rotation().x())).get(new Matrix4f()));
        localPose.mulPose(Axis.YP.rotationDegrees(-Utils.toRadians(this.itemTransform.rotation().y())).get(new Matrix4f()));
        localPose.mulPose(Axis.XP.rotationDegrees(-Utils.toRadians(this.itemTransform.rotation().z())).get(new Matrix4f()));
        localPose.translate(-this.itemTransform.translation().x(), -this.itemTransform.translation().y(), -this.itemTransform.translation().z());
    }
}
