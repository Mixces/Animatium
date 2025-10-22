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

package btw.mixces.animatium.mixins.v0.renderer.item;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.ItemUtils;
import btw.mixces.animatium.util.PlayerUtils;
import btw.mixces.animatium.util.states.UtilityRenderState;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Quaternionfc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ItemInHandLayer.class)
public abstract class MixinItemInHandLayer<S extends ArmedEntityRenderState, M extends EntityModel<S> & ArmedModel> extends RenderLayer<S, M> {
    public MixinItemInHandLayer(RenderLayerParent<S, M> context) {
        super(context);
    }

    @Inject(method = "submitArmWithItem", at = @At("HEAD"))
    private void animatium$setRef(S armedEntityRenderState, ItemStackRenderState itemStackRenderState, HumanoidArm humanoidArm, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, CallbackInfo ci, @Share("stack") LocalRef<ItemStack> stackRef) {
        if (AnimatiumClient.isEnabled() && ItemUtils.shouldApplyItemPositionsInThirdperson(armedEntityRenderState) && !itemStackRenderState.isEmpty()) {
            stackRef.set(((UtilityRenderState) armedEntityRenderState).animatium$getItemHeldByArm(humanoidArm));
        }
    }

    @ModifyArgs(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    private void animatium$oldTransformTranslation(Args args, @Local(argsOnly = true) S entityState, @Share("stack") LocalRef<ItemStack> stackRef) {
        if (AnimatiumClient.isEnabled() && ItemUtils.shouldApplyItemPositionsInThirdperson(entityState) && !ItemUtils.isItemBlacklisted(stackRef.get())) {
            args.setAll((float) args.get(0) * -1.0F, 0.4375F, (float) args.get(2) / 10 * -1.0F);
        }
    }

    @WrapWithCondition(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V"))
    private boolean animatium$removeTransformMultiply(PoseStack instance, Quaternionfc quaternionfc, @Local(argsOnly = true) S entityState, @Share("stack") LocalRef<ItemStack> stackRef) {
        return !AnimatiumClient.isEnabled() || !ItemUtils.shouldApplyItemPositionsInThirdperson(entityState) || ItemUtils.isItemBlacklisted(stackRef.get());
    }

    @Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"))
    private void animatium$itemPositionsThird(S armedEntityRenderState, ItemStackRenderState itemStackRenderState, HumanoidArm humanoidArm, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, CallbackInfo ci) {
        if (AnimatiumClient.isEnabled() && ItemUtils.shouldApplyItemPositionsInThirdperson(armedEntityRenderState)) {
            int direction = PlayerUtils.getArmMultiplier(humanoidArm);
            UtilityRenderState itemArmedEntityRenderState = (UtilityRenderState) armedEntityRenderState;
            ItemStack stack = itemArmedEntityRenderState.animatium$getItemHeldByArm(humanoidArm);
            Item item = stack.getItem();
            if (!stack.isEmpty() && !ItemUtils.isItemBlacklisted(stack)) {
                boolean isStickRod = AnimatiumClient.isEnabled() &&
                        AnimatiumConfig.instance().stickModelWhenCastInThirdperson &&
                        item == Items.FISHING_ROD &&
                        (itemArmedEntityRenderState.animatium$isPlayer() && itemArmedEntityRenderState.animatium$isFishing());
                if (ItemUtils.isBlock3d(stack, itemStackRenderState)) {
                    float scale = 0.375F;
                    poseStack.translate(0.0F, 0.1875F, -0.3125F);
                    poseStack.mulPose(Axis.XP.rotationDegrees(20.0F));
                    poseStack.mulPose(Axis.YP.rotationDegrees(direction * 45.0F));
                    poseStack.scale(-scale, -scale, scale);
                } else if (item instanceof BowItem) {
                    float scale = 0.625F;
                    poseStack.translate(direction * 0.0F, 0.125F, 0.3125F);
                    poseStack.mulPose(Axis.YP.rotationDegrees(direction * -20.0F));
                    poseStack.translate(direction * -0.0625F, 0.0F, 0.0F);
                    poseStack.scale(scale, scale, scale);
                    poseStack.mulPose(Axis.XP.rotationDegrees(180));
                    poseStack.mulPose(Axis.XP.rotationDegrees(100.0F));
                    poseStack.mulPose(Axis.YP.rotationDegrees(direction * -145.0F));
                    poseStack.translate(-0.011765625F, 0.0F, 0.002125F);
                } else if (ItemUtils.isHandheldItem(stack)) {
                    float scale = 0.625F;
                    if (ItemUtils.isFishingRodItem(stack) && !isStickRod) {
                        poseStack.mulPose(Axis.ZP.rotationDegrees(direction * 180.0F));
                        poseStack.translate(0.0F, -0.125F, 0.0F);
                    }

                    if (PlayerUtils.isBlockingArm(humanoidArm, armedEntityRenderState)) {
                        poseStack.translate(direction * 0.05F, 0.0F, -0.1F);
                        poseStack.mulPose(Axis.YP.rotationDegrees(direction * -50.0F));
                        poseStack.mulPose(Axis.XP.rotationDegrees(-10.0F));
                        poseStack.mulPose(Axis.ZP.rotationDegrees(direction * -60.0F));
                    }

                    poseStack.translate(direction * -0.0625F, 0.1875F, 0.0F);
                    poseStack.scale(scale, scale, scale);
                    poseStack.mulPose(Axis.XP.rotationDegrees(180));
                    poseStack.mulPose(Axis.XP.rotationDegrees(100));
                    poseStack.mulPose(Axis.YP.rotationDegrees(direction * -145));
                    poseStack.translate(-0.011765625F, 0.0F, 0.002125F);
                } else {
                    float scale = 0.375F;
                    poseStack.translate(direction * 0.25F, 0.1875F, -0.1875F);
                    poseStack.scale(scale, scale, scale);
                    poseStack.mulPose(Axis.ZP.rotationDegrees(direction * 60.0F));
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                    poseStack.mulPose(Axis.ZP.rotationDegrees(direction * 20.0F));
                }

                if (!ItemUtils.isBlock3d(stack, itemStackRenderState)) {
                    poseStack.translate(0.0F, -0.3F, 0.0F);
                    poseStack.scale(1.5F, 1.5F, 1.5F);
                    poseStack.mulPose(Axis.YP.rotationDegrees(direction * 50.0F));
                    poseStack.mulPose(Axis.ZP.rotationDegrees(direction * 335.0F));
                    poseStack.translate(direction * -0.9375F, -0.0625F, 0.0F);

                    poseStack.mulPose(Axis.YP.rotationDegrees(direction * 180.0F));
                    poseStack.translate(direction * -0.5F, 0.5F, 0.03125F);
                }

                if (ItemUtils.isBlock3d(stack, itemStackRenderState)) {
                    poseStack.scale(1 / 0.375F, 1 / 0.375F, 1 / 0.375F);
                    poseStack.mulPose(Axis.YP.rotationDegrees(direction * -45.0F));
                    poseStack.mulPose(Axis.XP.rotationDegrees(-75.0F));
                    poseStack.translate(0.0F, -2.5F * 0.0625F, 0.0F);
                } else if (item instanceof BowItem) {
                    poseStack.scale(1 / 0.9F, 1 / 0.9F, 1 / 0.9F);
                    poseStack.mulPose(Axis.ZP.rotationDegrees(direction * 40.0F));
                    poseStack.mulPose(Axis.YP.rotationDegrees(direction * -260.0F));
                    poseStack.mulPose(Axis.XP.rotationDegrees(80.0F));
                    poseStack.translate(direction * 0.0625F, 2.0F * 0.0625F, -2.5F * 0.0625F);
                } else if (ItemUtils.isHandheldItem(stack)) {
                    boolean isRod = ItemUtils.isFishingRodItem(stack) && !isStickRod;
                    poseStack.scale(1 / 0.85F, 1 / 0.85F, 1 / 0.85F);
                    poseStack.mulPose(Axis.ZP.rotationDegrees(direction * -55.0F));
                    poseStack.mulPose(Axis.YP.rotationDegrees(direction * 90.0F));
                    if (isRod) {
                        poseStack.mulPose(Axis.YP.rotationDegrees(direction * -180.0F));
                    }

                    poseStack.translate(0.0F, -4.0F * 0.0625F, -0.5F * 0.0625F);
                    if (isRod) {
                        poseStack.translate(0.0F, 0.0F, -2.0F * 0.0625F);
                    }
                } else {
                    poseStack.scale(1 / 0.55F, 1 / 0.55F, 1 / 0.55F);
                    poseStack.translate(0.0F, -3.0F * 0.0625F, -1.0F * 0.0625F);
                }
            }
        }
    }
}
