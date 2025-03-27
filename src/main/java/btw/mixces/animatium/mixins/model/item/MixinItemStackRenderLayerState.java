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

package btw.mixces.animatium.mixins.model.item;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.FishingRodVersion;
import btw.mixces.animatium.util.ItemUtils;
import btw.mixces.animatium.util.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStackRenderState.LayerRenderState.class)
public abstract class MixinItemStackRenderLayerState {
    @Shadow
    ItemTransform transform;

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderItem(Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II[ILjava/util/List;Lnet/minecraft/client/renderer/RenderType;Lnet/minecraft/client/renderer/item/ItemStackRenderState$FoilType;)V"), index = 8)
    private ItemStackRenderState.FoilType animatium$disableGlintOn2dItems(ItemStackRenderState.FoilType glint) {
        boolean glintDropped = !AnimatiumConfig.instance().glintOnItemDrops2D;
        boolean glintFramed = !AnimatiumConfig.instance().glintOnItemFramed2D;
        if (AnimatiumClient.isEnabled() && ItemUtils.getDisplayContext() != null &&
                (glintDropped && ItemUtils.getDisplayContext() == ItemDisplayContext.GROUND) ||
                (glintFramed && ItemUtils.getDisplayContext() == ItemDisplayContext.FIXED)) {
            return ItemStackRenderState.FoilType.NONE;
        } else {
            return glint;
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/model/ItemTransform;apply(ZLcom/mojang/blaze3d/vertex/PoseStack$Pose;)V"))
    private void animatium$itemPositionsRod(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay, CallbackInfo ci) {
        if (AnimatiumClient.isEnabled()) {
            ItemStack stack = ItemUtils.getStack();
            ItemDisplayContext displayContext = ItemUtils.getDisplayContext();
            if (stack != null && displayContext != null) {
                boolean isGui = displayContext == ItemDisplayContext.GUI;
                boolean isFirstPerson = displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
                boolean isThirdPerson = displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                ItemTransform transform = this.transform;
                float x = transform.translation().x();
                float y = transform.translation().y();
                float z = transform.translation().z();
                float rotX = transform.rotation().x();
                float rotY = transform.rotation().y();
                float rotZ = transform.rotation().z();
                float scaleX = transform.scale().x();
                float scaleY = transform.scale().y();
                float scaleZ = transform.scale().z();
                if (AnimatiumConfig.instance().fishingRodVersion != FishingRodVersion.LATEST && ItemUtils.isFishingRodItem(stack) && isFirstPerson) {
                    int ordinal = AnimatiumConfig.instance().fishingRodVersion.ordinal();
                    if (ordinal <= FishingRodVersion.V1_8.ordinal()) {
                        poseStack.translate(0.070625, 0.1, 0.020625);
                    }

                    poseStack.translate(x, y, z);
                    if (ordinal == FishingRodVersion.V1_7.ordinal()) {
                        poseStack.mulPose(Axis.YP.rotationDegrees(180));
                    }

                    poseStack.translate(-x, -y, -z);
                }

                if (AnimatiumConfig.instance().thinBlockPositions && ItemUtils.isThinBlockItem(stack)) {
                    if (isFirstPerson) {
                        poseStack.translate(0, -4.2 * 0.0625, 0);
                    } else if (isThirdPerson) {
                        poseStack.translate(0, 0, -2 * 0.0625);
                    }
                }

                if (AnimatiumConfig.instance().skullPosition && ItemUtils.isSkullBlock(stack)) {
                    if (isGui) {
                        poseStack.translate(x, y, z);
                        poseStack.mulPose(Axis.XP.rotationDegrees(MathUtils.toRadians(rotZ)));
                        poseStack.mulPose(Axis.YP.rotationDegrees(MathUtils.toRadians(rotY)));
                        poseStack.mulPose(Axis.ZP.rotationDegrees(MathUtils.toRadians(rotX)));
                        poseStack.scale(0.9f, 0.9f, 0.9f);
                        poseStack.scale(scaleX, scaleY, scaleZ);
                        animatium$doInverseTransformations(poseStack);
                    }
                }
            }
        }
    }

    @Unique
    private void animatium$doInverseTransformations(PoseStack poseStack) {
        ItemTransform transform = this.transform;
        poseStack.scale(1 / transform.scale().x(), 1 / transform.scale().y(), 1 / transform.scale().z());
        poseStack.mulPose(Axis.ZP.rotationDegrees(-MathUtils.toRadians(transform.rotation().x())));
        poseStack.mulPose(Axis.YP.rotationDegrees(-MathUtils.toRadians(transform.rotation().y())));
        poseStack.mulPose(Axis.XP.rotationDegrees(-MathUtils.toRadians(transform.rotation().z())));
        poseStack.translate(-transform.translation().x(), -transform.translation().y(), -transform.translation().z());
    }
}
