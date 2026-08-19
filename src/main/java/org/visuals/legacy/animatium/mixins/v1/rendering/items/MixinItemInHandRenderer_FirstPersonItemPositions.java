/**
 * Animatium
 * The all-you-could-want legacy animations mod for modern minecraft versions.
 * Brings back animations from the 1.7/1.8 era and more.
 * <p>
 * Copyright (C) 2024-2027 lowercasebtw
 * Copyright (C) 2024-2027 mixces
 * Copyright (C) 2024-2027 Contributors to the project retain their copyright
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

package org.visuals.legacy.animatium.mixins.v1.rendering.items;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.config.category.ExtrasConfigCategory;
import org.visuals.legacy.animatium.util.ItemUtilKt;
import org.visuals.legacy.animatium.util.SwingUtilKt;
import org.visuals.legacy.animatium.util.enums.EquipAnimationVersionSetting;
import org.visuals.legacy.animatium.util.enums.FishingRodVersionSetting;

// TODO/NOTE: Why 500?
@Mixin(value = ItemInHandRenderer.class, priority = 500)
public abstract class MixinItemInHandRenderer_FirstPersonItemPositions {
    @Shadow
    private float mainHandHeight;

    @Shadow
    private ItemStack mainHandItem;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Final
    private ItemModelResolver itemModelResolver;

    @Shadow
    protected abstract void applyItemArmAttackTransform(final PoseStack poseStack, final HumanoidArm arm, final float attackValue);

    @Shadow
    protected abstract boolean shouldInstantlyReplaceVisibleItem(final ItemStack currentlyVisibleItem, final ItemStack expectedItem);

    @Unique
    private int animatium$currentSlot = -1;

    @Unique
    private ItemStack animatium$mainHandItem = ItemStack.EMPTY;

    @SuppressWarnings({"MixinAnnotationTarget"})
    @ModifyExpressionValue(method = {"renderOneHandedMap", "renderTwoHandedMap", "submitArmWithItem"}, at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;isInvisible()Z"),
            @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isInvisible()Z")
    })
    private boolean animatium$showArmWhileInvisible(final boolean original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().extras.showArmWhileInvisible) {
            return false;
        } else {
            return original;
        }
    }

    @WrapWithCondition(method = "swingArm", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    private boolean animatium$disableSwingTranslate(final PoseStack instance, final float x, final float y, final float z) {
        if (Animatium.isEnabled()) {
            return !AnimatiumConfig.instance().extras.disableSwingTranslate;
        } else {
            return true;
        }
    }

    @WrapOperation(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;isUsingItem()Z"))
    private boolean animatium$fixDoubleBlockingVisual$itemUsageVisualInGUI(final AbstractClientPlayer instance, final Operation<Boolean> original) {
        final boolean value = original.call(instance);
        if (Animatium.isEnabled()) {
            if (AnimatiumConfig.instance().fixes.fixItemUsageVisualInGUI && this.minecraft.gui.screen() != null) {
                return false;
            } else if (AnimatiumConfig.instance().fixes.fixDoubleUsageVisual) {
                return value && this.minecraft.options.keyUse.isDown();
            }
        }

        return value;
    }

    @WrapOperation(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V", ordinal = 1))
    private void animatium$postBowTransform(final PoseStack instance, final float xScale, final float yScale, final float zScale, final Operation<Void> original, @Local(argsOnly = true, name = "player") final AbstractClientPlayer player, @Local(argsOnly = true, name = "hand") final InteractionHand hand) {
        final int direction = SwingUtilKt.getHandMultiplier(player, hand);
        if (Animatium.isEnabled() && AnimatiumConfig.instance().items.itemPositions) {
            instance.mulPose(Axis.ZP.rotationDegrees(direction * -335));
            instance.mulPose(Axis.YP.rotationDegrees(direction * -50.0F));
        }

        original.call(instance, xScale, yScale, zScale);
        if (Animatium.isEnabled() && AnimatiumConfig.instance().items.itemPositions) {
            instance.mulPose(Axis.YP.rotationDegrees(direction * 50.0F));
            instance.mulPose(Axis.ZP.rotationDegrees(direction * 335));
        }
    }

    @Definition(id = "item", local = @Local(type = ItemStack.class, argsOnly = true))
    @Definition(id = "getItem", method = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;")
    @Definition(id = "ShieldItem", type = ShieldItem.class)
    @Expression("item.getItem() instanceof ShieldItem")
    @ModifyExpressionValue(method = "submitArmWithItem", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean animatium$oldFirstPersonSwordBlock(final boolean original, @Local(argsOnly = true, name = "player") final AbstractClientPlayer player, @Local(argsOnly = true, name = "hand") final InteractionHand hand, @Local(argsOnly = true, name = "itemStack") final ItemStack itemStack, @Local(argsOnly = true, name = "poseStack") final PoseStack poseStack) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().items.itemPositions && !(itemStack.getItem() instanceof ShieldItem)) {
            final int direction = SwingUtilKt.getHandMultiplier(player, hand);
            // We do this to fix a rounding error in Mojangs code.
            ItemUtilKt.applyLegacyFirstPersonTransforms(poseStack, direction, () -> {
                poseStack.translate(direction * -0.5F, 0.2F, 0.0F);
                poseStack.mulPose(Axis.YP.rotationDegrees(direction * 30.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(-80.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(direction * 60.0F));
            });
            return true; // Cancels the vanilla blocking code
        } else {
            return original;
        }
    }

    @Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V"))
    private void animatium$itemPositions(final AbstractClientPlayer player, final float tickDelta, final float pitch, final InteractionHand hand, final float swingProgress, final ItemStack itemStack, final float equippedProgress, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final int lightCoords, final CallbackInfo ci) {
        final int direction = SwingUtilKt.getHandMultiplier(player, hand);
        if (Animatium.isEnabled()) {
            if (AnimatiumConfig.instance().items.fishingRodVersion == FishingRodVersionSetting.V1_7 && ItemUtilKt.isFishingRodItem(itemStack)) {
                poseStack.mulPose(Axis.YP.rotationDegrees(direction * 180.0F));
            }

            final ItemDisplayContext displayContext = hand == InteractionHand.MAIN_HAND ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
            final ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
            itemModelResolver.updateForTopItem(
                    itemStackRenderState,
                    itemStack,
                    displayContext,
                    player.level(),
                    player,
                    lightCoords
            ); // TODO/NOTE: Might be wrong

            final boolean isNotBlock3d = !ItemUtilKt.isBlock3d(itemStack, itemStackRenderState.usesBlockLight());
            if (AnimatiumConfig.instance().items.itemPositions && isNotBlock3d && !ItemUtilKt.isItemBlacklisted(itemStack)) {
                final float radians = 0.4363323129985824F;

                poseStack.scale(0.6F, 0.6F, 0.6F);
                poseStack.mulPose(Axis.YP.rotationDegrees(direction * 275.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(direction * 25.0F));
                poseStack.translate(direction * (-0.2F * Math.sin(radians) + 0.4375F), -0.2F * Math.cos(radians) + 0.4375F, 0.03125F);

                poseStack.scale(1 / 0.68F, 1 / 0.68F, 1 / 0.68F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(direction * -25.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(direction * 90.0F));
                poseStack.translate(direction * -1.13 * 0.0625F, -3.2 * 0.0625F, -1.13 * 0.0625F);
            }

            if (AnimatiumConfig.instance().items.skullPosition && ItemUtilKt.isSkullBlock(itemStack) && !AnimatiumConfig.instance().items.mobHeadIcons) {
                poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
                poseStack.scale(0.4F, 0.4F, 0.4F);

                // TODO: This is not quite right... (@Mixces)
                poseStack.mulPose(Axis.YP.rotationDegrees(-180.0F));
                poseStack.translate(0.0F, 0.25F, 0.0F);
                poseStack.scale(1.125F, 1.125F, 1.125F);
            }

            final ExtrasConfigCategory extras = AnimatiumConfig.instance().extras;
            if (isNotBlock3d) {
                poseStack.translate(extras.itemOffsetX * 0.05F, extras.itemOffsetY * 0.05F, extras.itemOffsetZ * 0.05F);
                poseStack.scale(extras.itemScaleX, extras.itemScaleY, extras.itemScaleZ);
            }
        }
    }

    @Inject(method = "submitArmWithItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V", shift = At.Shift.AFTER),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/ItemUseAnimation;"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V", ordinal = 4)
            ))
    private void animatium$itemUsageSwinging(final AbstractClientPlayer player, final float tickDelta, final float pitch, final InteractionHand hand, final float swingProgress, final ItemStack itemStack, final float equippedProgress, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final int lightCoords, final CallbackInfo ci, @Local(name = "arm") final HumanoidArm arm) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().items.itemUsageSwinging) {
            this.applyItemArmAttackTransform(poseStack, arm, swingProgress);
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isHandsBusy()Z"))
    private boolean animatium$heldItemVisibilityInBoat(final LocalPlayer instance, final Operation<Boolean> original) {
        return (!Animatium.isEnabled() || !AnimatiumConfig.instance().items.heldItemVisibilityInBoat) && original.call(instance);
    }

    // Equip Animation Stuff
    @ModifyArg(method = "submitHandsWithItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;submitArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V", ordinal = 0), index = 5)
    private ItemStack animatium$useCopyStackFieldForRender(final ItemStack original) {
        // TODO/NOTE: 26.2 makes the item persist in hand even when empty (temp check added)
        if (Animatium.isEnabled() && AnimatiumConfig.instance().items.equipAnimationVersion != EquipAnimationVersionSetting.VANILLA && !original.isEmpty()) {
            // Use our copied stack field for hand animations
            return this.animatium$mainHandItem;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;shouldInstantlyReplaceVisibleItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z", ordinal = 0))
    private boolean animatium$disableEquipConstraint(final boolean original) {
        return (!Animatium.isEnabled() || AnimatiumConfig.instance().items.equipAnimationVersion == EquipAnimationVersionSetting.VANILLA) && original;
    }

    // Fixes MC-262560
    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F", ordinal = 2), index = 0)
    private float animatium$handleEquipLogic(final float original) {
        final LocalPlayer player = this.minecraft.player;
        final EquipAnimationVersionSetting setting = AnimatiumConfig.instance().items.equipAnimationVersion;
        if (Animatium.isEnabled() && setting != EquipAnimationVersionSetting.VANILLA && player != null) {
            final float attackAnim = player.getItemSwapScale(1.0F);
            final float scale = (float) Math.pow(attackAnim, 3);
            final ItemStack stackCopy = player.getInventory().getSelectedItem().copy();

            float mainHandTargetHeight = stackCopy == this.animatium$mainHandItem ? scale : 0;
            if (this.animatium$mainHandItem.isEmpty() && stackCopy.isEmpty()) {
                mainHandTargetHeight = scale;
            }

            if (!stackCopy.isEmpty() && !this.animatium$mainHandItem.isEmpty() &&
                    stackCopy != this.animatium$mainHandItem && stackCopy.getItem() == this.animatium$mainHandItem.getItem() &&
                    stackCopy.getDamageValue() == this.animatium$mainHandItem.getDamageValue()) {
                this.animatium$mainHandItem = stackCopy;
                mainHandTargetHeight = scale;
            }

            if (setting == EquipAnimationVersionSetting.V1_7 && this.animatium$currentSlot != player.getInventory().getSelectedSlot()) {
                mainHandTargetHeight = 0;
            }

            return mainHandTargetHeight - this.mainHandHeight;
        } else {
            return original;
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void animatium$updateFakeItem(final CallbackInfo ci) {
        final LocalPlayer player = this.minecraft.player;
        if (Animatium.isEnabled() && AnimatiumConfig.instance().items.equipAnimationVersion != EquipAnimationVersionSetting.VANILLA && player != null && this.mainHandHeight < 0.1F) {
            this.animatium$mainHandItem = this.mainHandItem.copy();
            this.animatium$currentSlot = this.minecraft.player.getInventory().getSelectedSlot();
        }
    }
}
