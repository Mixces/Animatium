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
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
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
import org.visuals.legacy.animatium.util.ItemUtilKt;
import org.visuals.legacy.animatium.util.UtilsKt;
import org.visuals.legacy.animatium.util.enums.FishingRodVersionSetting;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(ItemStackRenderState.LayerRenderState.class)
public abstract class MixinItemStackRenderLayerState {
    @Shadow
    ItemTransform transform;

    @Shadow(aliases = "field_55345")
    @Final
    ItemStackRenderState itemStackRenderState;

    @Shadow
    boolean usesBlockLight;

    @ModifyArg(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitItem(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/ItemDisplayContext;III[ILjava/util/List;Lnet/minecraft/client/renderer/rendertype/RenderType;Lnet/minecraft/client/renderer/item/ItemStackRenderState$FoilType;)V"), index = 6)
    private List<BakedQuad> animatium$itemDrops2D(final List<BakedQuad> quads) {
        if (Animatium.isEnabled() && animatium$isTransformationModeValid() && !this.usesBlockLight) {
            return quads.stream().filter(baked -> baked.direction() == Direction.SOUTH).collect(Collectors.toList());
        } else {
            return quads;
        }
    }

    @ModifyArg(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitItem(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/ItemDisplayContext;III[ILjava/util/List;Lnet/minecraft/client/renderer/rendertype/RenderType;Lnet/minecraft/client/renderer/item/ItemStackRenderState$FoilType;)V"), index = 8)
    private ItemStackRenderState.FoilType animatium$disableGlintOn2DItems(final ItemStackRenderState.FoilType foilType) {
        final boolean glintDropped = !AnimatiumConfig.instance().items.glintOnItemDrops2D;
        final boolean glintFramed = !AnimatiumConfig.instance().items.glintOnItemFramed2D;
        if (Animatium.isEnabled() &&
                (glintDropped && this.itemStackRenderState.displayContext == ItemDisplayContext.GROUND) ||
                (glintFramed && this.itemStackRenderState.displayContext == ItemDisplayContext.FIXED)) {
            return ItemStackRenderState.FoilType.NONE;
        } else {
            return foilType;
        }
    }

    // TODO/MOVE
    @Inject(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/model/ItemTransform;apply(ZLcom/mojang/blaze3d/vertex/PoseStack$Pose;)V"))
    private void animatium$itemPositions(final PoseStack poseStack, final SubmitNodeCollector nodeCollector, final int packedLight, final int packedOverlay, final int outlineColor, final CallbackInfo ci) {
        if (Animatium.isEnabled()) {
            final ItemStack stack = this.itemStackRenderState.animatium$getItemStack();
            if (!stack.isEmpty()) {
                final PoseStack.Pose localPose = poseStack.last();
                final ItemDisplayContext itemDisplayContext = this.itemStackRenderState.displayContext;
                final boolean isGui = itemDisplayContext == ItemDisplayContext.GUI;
                final boolean isFirstPerson = itemDisplayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || itemDisplayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
                final boolean isThirdPerson = itemDisplayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || itemDisplayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                float x = this.transform.translation().x();
                float y = this.transform.translation().y();
                float z = this.transform.translation().z();
                if (AnimatiumConfig.instance().items.fishingRodVersion != FishingRodVersionSetting.VANILLA && ItemUtilKt.isFishingRodItem(stack) && isFirstPerson) {
                    final int ordinal = AnimatiumConfig.instance().items.fishingRodVersion.ordinal();
                    if (ordinal <= FishingRodVersionSetting.V1_8.ordinal()) {
                        localPose.translate(0.070625F, 0.1F, 0.020625F);
                    }

                    localPose.translate(x, y, z);
                    if (ordinal == FishingRodVersionSetting.V1_7.ordinal()) {
                        localPose.mulPose(Axis.YP.rotationDegrees(180).get(new Matrix4f()));
                    }

                    localPose.translate(-x, -y, -z);
                }

                if (AnimatiumConfig.instance().items.thinBlockPositions && ItemUtilKt.isThinBlockItem(stack)) {
                    if (isFirstPerson) {
                        localPose.translate(0.0F, -4.2F * 0.0625F, 0.0F);
                    } else if (isThirdPerson) {
                        localPose.translate(0.0F, 0.0F, -2.0F * 0.0625F);
                    }
                }

                // TODO/NEED TO FIX
                if (AnimatiumConfig.instance().items.skullPosition && ItemUtilKt.isSkullBlock(stack) && isGui && !AnimatiumConfig.instance().items.mobHeadIcons) {
                    localPose.translate(x, y, z);
                    localPose.mulPose(Axis.XP.rotationDegrees(UtilsKt.toRadians(this.transform.rotation().x())).get(new Matrix4f()));
                    localPose.mulPose(Axis.YP.rotationDegrees(UtilsKt.toRadians(this.transform.rotation().y())).get(new Matrix4f()));
                    localPose.mulPose(Axis.ZP.rotationDegrees(UtilsKt.toRadians(this.transform.rotation().x())).get(new Matrix4f()));
                    localPose.scale(0.9F, 0.9F, 0.9F);
                    localPose.scale(this.transform.scale().x(), this.transform.scale().y(), this.transform.scale().z());
                    animatium$doInverseTransformations(localPose);
                }
            }
        }
    }

    @Unique
    private void animatium$doInverseTransformations(final PoseStack.Pose localPose) {
        localPose.scale(1 / this.transform.scale().x(), 1 / this.transform.scale().y(), 1 / this.transform.scale().z());
        localPose.mulPose(Axis.ZP.rotationDegrees(-UtilsKt.toRadians(this.transform.rotation().x())).get(new Matrix4f()));
        localPose.mulPose(Axis.YP.rotationDegrees(-UtilsKt.toRadians(this.transform.rotation().y())).get(new Matrix4f()));
        localPose.mulPose(Axis.XP.rotationDegrees(-UtilsKt.toRadians(this.transform.rotation().z())).get(new Matrix4f()));
        localPose.translate(-this.transform.translation().x(), -this.transform.translation().y(), -this.transform.translation().z());
    }

    @Unique
    private boolean animatium$isTransformationModeValid() {
        final boolean itemDrops2D = AnimatiumConfig.instance().items.itemDrops2D;
        final boolean itemFramed2D = AnimatiumConfig.instance().items.itemFramed2D;
        return (itemDrops2D && this.itemStackRenderState.displayContext == ItemDisplayContext.GROUND) || (itemFramed2D && this.itemStackRenderState.displayContext == ItemDisplayContext.FIXED);
    }
}
