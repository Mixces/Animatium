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

package btw.mixces.animatium.mixins.renderer.item;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.mixins.accessor.ItemRendererAccessor;
import btw.mixces.animatium.util.FishingRodVersion;
import btw.mixces.animatium.util.ItemUtils;
import btw.mixces.animatium.util.MathUtils;
import btw.mixces.animatium.util.PlayerUtils;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemInHandRenderer.class, priority = 500)
public abstract class MixinItemInHandRenderer {
    @Shadow
    protected abstract void applyItemArmAttackTransform(PoseStack matrices, HumanoidArm arm, float swingProgress);

    @Shadow
    protected abstract boolean shouldInstantlyReplaceVisibleItem(ItemStack itemStack, ItemStack itemStack2);

    @Shadow
    private float mainHandHeight;

    @Shadow
    private ItemStack mainHandItem;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Final
    private ItemRenderer itemRenderer;

    @Unique
    private int animatium$currentSlot = -1;

    @Unique
    private ItemStack animatium$mainHandItem = ItemStack.EMPTY;

    @WrapOperation(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;isUsingItem()Z", ordinal = 1))
    private boolean animatium$itemUsageVisualInGUI(AbstractClientPlayer instance, Operation<Boolean> original) {
        if (!AnimatiumConfig.instance().itemUsageVisualInGUI && this.minecraft.screen != null) {
            return false;
        } else {
            return original.call(instance);
        }
    }

    @WrapOperation(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V", ordinal = 1))
    private void animatium$postBowTransform(PoseStack poseStack, float x, float y, float z, Operation<Void> original, @Local(argsOnly = true) AbstractClientPlayer player, @Local(argsOnly = true) InteractionHand hand) {
        int direction = PlayerUtils.getHandMultiplier(player, hand);
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().itemPositions) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(direction * -335));
            poseStack.mulPose(Axis.YP.rotationDegrees(direction * -50.0F));
        }

        original.call(poseStack, x, y, z);
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().itemPositions) {
            poseStack.mulPose(Axis.YP.rotationDegrees(direction * 50.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(direction * 335));
        }
    }

    @WrapOperation(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"))
    private Item animatium$oldFirstPersonSwordBlock(ItemStack instance, Operation<Item> original, @Local(argsOnly = true) AbstractClientPlayer player, @Local(argsOnly = true) InteractionHand hand, @Local(argsOnly = true) PoseStack poseStack) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().itemPositions && !(instance.getItem() instanceof ShieldItem)) {
            int direction = PlayerUtils.getHandMultiplier(player, hand);
            // We do this to fix a rounding error in Mojangs code.
            ItemUtils.applyLegacyFirstpersonTransforms(poseStack, direction, () -> {
                poseStack.translate(direction * -0.5F, 0.2F, 0.0F);
                poseStack.mulPose(Axis.YP.rotationDegrees(direction * 30.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(-80.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(direction * 60.0F));
            });
            return Items.SHIELD; // Cancels the vanilla blocking code
        } else {
            return original.call(instance);
        }
    }

    @Inject(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", ordinal = 1))
    private void animatium$itemPositions(AbstractClientPlayer player, float tickDelta, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equipProgress, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, CallbackInfo ci) {
        int direction = PlayerUtils.getHandMultiplier(player, hand);
        if (AnimatiumClient.isEnabled()) {
            if (AnimatiumConfig.instance().fishingRodVersion == FishingRodVersion.V1_7 && ItemUtils.isFishingRodItem(stack)) {
                poseStack.mulPose(Axis.YP.rotationDegrees(direction * 180.0F));
            }

            if (AnimatiumConfig.instance().itemPositions && !ItemUtils.isBlock3d(stack, ((ItemRendererAccessor) itemRenderer).getScratchItemStackRenderState()) && !ItemUtils.isItemBlacklisted(stack)) {
                float angle = MathUtils.toRadians(25);

                poseStack.scale(0.6F, 0.6F, 0.6F);
                poseStack.mulPose(Axis.YP.rotationDegrees(direction * 275.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(direction * 25.0F));
                poseStack.translate(direction * (-0.2F * Math.sin(angle) + 0.4375F), -0.2F * Math.cos(angle) + 0.4375F, 0.03125F);

                poseStack.scale(1 / 0.68F, 1 / 0.68F, 1 / 0.68F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(direction * -25.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(direction * 90.0F));
                poseStack.translate(direction * -1.13 * 0.0625F, -3.2 * 0.0625F, -1.13 * 0.0625F);
            }

            if (AnimatiumConfig.instance().skullPosition && ItemUtils.isSkullBlock(stack)) {
                poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
                poseStack.scale(0.4F, 0.4F, 0.4F);

                // TODO: This is not quite right...
                poseStack.mulPose(Axis.YP.rotationDegrees(-180.0F));
                poseStack.translate(0.0F, 0.25F, 0.0F);
                poseStack.scale(1.125F, 1.125F, 1.125F);
            }
        }
    }

    @Inject(method = "renderArmWithItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V", shift = At.Shift.AFTER),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/ItemUseAnimation;"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V", ordinal = 6)
            ))
    private void animatium$itemUsageSwinging(AbstractClientPlayer abstractClientPlayer, float tickDelta, float pitch, InteractionHand interactionHand, float swingProgress, ItemStack itemStack, float equipProgress, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, CallbackInfo ci, @Local HumanoidArm arm) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().itemUsageSwinging) {
            applyItemArmAttackTransform(poseStack, arm, swingProgress);
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F"))
    private float animatium$highAttackSpeedVisual(LocalPlayer instance, float defaultAttackScale, Operation<Float> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().highAttackSpeedVisual) {
            return defaultAttackScale;
        } else {
            return original.call(instance, defaultAttackScale);
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isHandsBusy()Z"))
    private boolean animatium$heldItemVisibilityInBoat(LocalPlayer instance, Operation<Boolean> original) {
        return (!AnimatiumClient.isEnabled() || !AnimatiumConfig.instance().heldItemVisibilityInBoat) && original.call(instance);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getOffhandItem()Lnet/minecraft/world/item/ItemStack;"))
    private void animatium$createCopyStack(CallbackInfo ci, @Local LocalPlayer localPlayer, @Local ItemStack itemStack, @Share("copyStack") LocalRef<ItemStack> copyStack) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().fixEquipAnimation) {
            // Initialize our copied stack
            copyStack.set(itemStack.copy());
            boolean slotsMatch = this.animatium$currentSlot == localPlayer.getInventory().getSelectedSlot();
            // Equip logic fix
            boolean shouldSwap1_8 = ItemUtils.shouldInstantlyReplaceVisibleItem1_8(this.animatium$mainHandItem, copyStack.get());
            // Original equip logic
            boolean shouldSwap = this.shouldInstantlyReplaceVisibleItem(this.animatium$mainHandItem, copyStack.get());
            if ((slotsMatch && shouldSwap1_8) || shouldSwap) {
                this.animatium$mainHandItem = copyStack.get();
            }
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;shouldInstantlyReplaceVisibleItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z", ordinal = 0))
    private boolean animatium$fixEquipAnimationItemCheck2(ItemInHandRenderer instance, ItemStack itemStack, ItemStack itemStack2, Operation<Boolean> original, @Local LocalPlayer localPlayer) {
        boolean value = original.call(instance, itemStack, itemStack2);
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().fixEquipAnimation) {
            // Apply our equip logic fix to offhand items
            boolean slotsMatch = this.animatium$currentSlot == localPlayer.getInventory().getSelectedSlot();
            return (slotsMatch && ItemUtils.shouldInstantlyReplaceVisibleItem1_8(itemStack, itemStack2)) || value;
        } else {
            return value;
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;shouldInstantlyReplaceVisibleItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z", ordinal = 1))
    private boolean animatium$fixEquipAnimationItemCheck(ItemInHandRenderer instance, ItemStack itemStack, ItemStack itemStack2, Operation<Boolean> original) {
        boolean value = original.call(instance, itemStack, itemStack2);
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().fixEquipAnimation) {
            // Apply our equip logic fix to offhand items
            return ItemUtils.shouldInstantlyReplaceVisibleItem1_8(itemStack, itemStack2) || value;
        } else {
            return value;
        }
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;mainHandItem:Lnet/minecraft/world/item/ItemStack;", ordinal = 1))
    private ItemStack animatium$useCopyStackField(ItemStack original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().fixEquipAnimation) {
            // Use the copy stack field for the stack comparison
            return this.animatium$mainHandItem;
        } else {
            return original;
        }
    }

    @ModifyVariable(method = "tick", at = @At(value = "LOAD", ordinal = 2), index = 2, name = "itemStack")
    private ItemStack animatium$useLocalCopyStack(ItemStack original, @Share("copyStack") LocalRef<ItemStack> copyStack) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().fixEquipAnimation) {
            // Use the local copied stack for the stack comparison
            return copyStack.get();
        } else {
            return original;
        }
    }

    @Inject(method = "tick", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;mainHandHeight:F", ordinal = 4))
    private void animatium$setCurrentSlotAndCopyStack(CallbackInfo ci, @Share("copyStack") LocalRef<ItemStack> copyStack) {
        LocalPlayer localPlayer = this.minecraft.player;
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().fixEquipAnimation && localPlayer != null && this.mainHandHeight < 0.1F) {
            // Update our copied stack
            this.animatium$mainHandItem = copyStack.get();
            // Cache the previous slot item to use in our comparison above
            this.animatium$currentSlot = localPlayer.getInventory().getSelectedSlot();
        }
    }

    @ModifyArg(method = "renderHandsWithItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", ordinal = 0), index = 5)
    private ItemStack animatium$useCopyStackFieldForRender(ItemStack original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().fixEquipAnimation) {
            // Use our copied stack field for hand animations
            return animatium$mainHandItem;
        } else {
            return original;
        }
    }

    @ModifyArg(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"), index = 1)
    private ItemStack animatium$useActualStackForRender(ItemStack original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().fixEquipAnimation) {
            // Use the original stack for rendering to avoid rendering issues
            return original == animatium$mainHandItem && !mainHandItem.isEmpty() ? mainHandItem : original;
        } else {
            return original;
        }
    }
}
