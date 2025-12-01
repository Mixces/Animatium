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

package org.visuals.legacy.animatium.mixins.v1.general.items.flat;

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

    @Shadow
    @Final
    ItemStackRenderState field_55345; // ItemStackRenderState.this

    @ModifyArg(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitItem(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/ItemDisplayContext;III[ILjava/util/List;Lnet/minecraft/client/renderer/RenderType;Lnet/minecraft/client/renderer/item/ItemStackRenderState$FoilType;)V"), index = 8)
    private ItemStackRenderState.FoilType animatium$disableGlintOn2DItems(ItemStackRenderState.FoilType glint) {
        final boolean glintDropped = !AnimatiumConfig.instance().items.glintOnItemDrops2D;
        final boolean glintFramed = !AnimatiumConfig.instance().items.glintOnItemFramed2D;
        if (Animatium.ENABLED &&
                (glintDropped && field_55345.displayContext == ItemDisplayContext.GROUND) ||
                (glintFramed && field_55345.displayContext == ItemDisplayContext.FIXED)) {
            return ItemStackRenderState.FoilType.NONE;
        } else {
            return glint;
        }
    }

    // TODO/MOVE
    @Inject(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/model/ItemTransform;apply(ZLcom/mojang/blaze3d/vertex/PoseStack$Pose;)V"))
    private void animatium$itemPositions(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, int k, CallbackInfo ci) {
        if (Animatium.ENABLED) {
            final ItemStack stack = ((ItemUtilityRenderState) field_55345).animatium$getItemStack();
            if (!stack.isEmpty()) {
                final ItemDisplayContext itemDisplayContext = this.field_55345.displayContext;
                boolean isGui = itemDisplayContext == ItemDisplayContext.GUI;
                boolean isFirstPerson = itemDisplayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || itemDisplayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
                boolean isThirdPerson = itemDisplayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || itemDisplayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;

                float x = this.transform.translation().x();
                float y = this.transform.translation().y();
                float z = this.transform.translation().z();
                float rotX = this.transform.rotation().x();
                float rotY = this.transform.rotation().y();
                float rotZ = this.transform.rotation().z();
                float scaleX = this.transform.scale().x();
                float scaleY = this.transform.scale().y();
                float scaleZ = this.transform.scale().z();
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
                    poseStack.mulPose(Axis.XP.rotationDegrees(Utils.toRadians(rotZ)));
                    poseStack.mulPose(Axis.YP.rotationDegrees(Utils.toRadians(rotY)));
                    poseStack.mulPose(Axis.ZP.rotationDegrees(Utils.toRadians(rotX)));
                    poseStack.scale(0.9F, 0.9F, 0.9F);
                    poseStack.scale(scaleX, scaleY, scaleZ);
                    animatium$doInverseTransformations(poseStack);
                }
            }
        }
    }

    @Unique
    private void animatium$doInverseTransformations(PoseStack poseStack) {
        poseStack.scale(1 / this.transform.scale().x(), 1 / this.transform.scale().y(), 1 / this.transform.scale().z());
        poseStack.mulPose(Axis.ZP.rotationDegrees(-Utils.toRadians(this.transform.rotation().x())));
        poseStack.mulPose(Axis.YP.rotationDegrees(-Utils.toRadians(this.transform.rotation().y())));
        poseStack.mulPose(Axis.XP.rotationDegrees(-Utils.toRadians(this.transform.rotation().z())));
        poseStack.translate(-this.transform.translation().x(), -this.transform.translation().y(), -this.transform.translation().z());
    }
}
