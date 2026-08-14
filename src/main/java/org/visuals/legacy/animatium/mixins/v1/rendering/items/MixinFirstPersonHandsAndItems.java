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

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.player.FirstPersonHandsAndItems;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.ItemUtilKt;

@Mixin(FirstPersonHandsAndItems.class)
public abstract class MixinFirstPersonHandsAndItems {
    @Shadow
    protected abstract boolean shouldInstantlyReplaceVisibleItem(final ItemStack currentlyVisibleItem, final ItemStack expectedItem, final LocalPlayer player);

    @Shadow
    private float mainHandHeight;

    @Unique
    private int animatium$currentSlot = -1;

    @Unique
    private ItemStack animatium$mainHandItem = ItemStack.EMPTY;

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isHandsBusy()Z"))
    private boolean animatium$heldItemVisibilityInBoat(final LocalPlayer instance, final Operation<Boolean> original) {
        return (!Animatium.isEnabled() || !AnimatiumConfig.instance().items.heldItemVisibilityInBoat) && original.call(instance);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getOffhandItem()Lnet/minecraft/world/item/ItemStack;"))
    private void animatium$createCopyStack(final CallbackInfo ci, @Local(argsOnly = true, name = "player") final LocalPlayer player, @Local(name = "nextMainHand") final ItemStack nextMainHand, @Share("copyStack") final LocalRef<ItemStack> copyStack) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().items.equipAnimationItemCheck) {
            // Initialize our copied stack
            copyStack.set(nextMainHand.copy());

            final boolean slotsMatch = this.animatium$currentSlot == player.getInventory().getSelectedSlot();

            // Equip logic fix
            final boolean shouldSwap1_8 = ItemUtilKt.shouldInstantlyReplaceVisibleItem1_8(this.animatium$mainHandItem, copyStack.get());

            // Original equip logic
            final boolean shouldSwap = this.shouldInstantlyReplaceVisibleItem(this.animatium$mainHandItem, copyStack.get(), player);

            if ((slotsMatch && shouldSwap1_8) || shouldSwap) {
                this.animatium$mainHandItem = copyStack.get();
            }
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/FirstPersonHandsAndItems;shouldInstantlyReplaceVisibleItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/player/LocalPlayer;)Z", ordinal = 0))
    private boolean animatium$equipAnimationItemCheck$mainHand(final FirstPersonHandsAndItems instance, final ItemStack currentlyVisibleItem, final ItemStack expectedItem, final LocalPlayer player, final Operation<Boolean> original) {
        final boolean value = original.call(instance, currentlyVisibleItem, expectedItem, player);
        if (Animatium.isEnabled() && AnimatiumConfig.instance().items.equipAnimationItemCheck) {
            // Apply our equip logic fix to offhand items
            final boolean slotsMatch = this.animatium$currentSlot == player.getInventory().getSelectedSlot();
            return (slotsMatch && ItemUtilKt.shouldInstantlyReplaceVisibleItem1_8(currentlyVisibleItem, expectedItem)) || value;
        } else {
            return value;
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/FirstPersonHandsAndItems;shouldInstantlyReplaceVisibleItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/player/LocalPlayer;)Z", ordinal = 1))
    private boolean animatium$equipAnimationItemCheck$offHand(final FirstPersonHandsAndItems instance, final ItemStack currentlyVisibleItem, final ItemStack expectedItem, final LocalPlayer player, final Operation<Boolean> original) {
        final boolean value = original.call(instance, currentlyVisibleItem, expectedItem, player);
        if (Animatium.isEnabled() && AnimatiumConfig.instance().items.equipAnimationItemCheck) {
            // Apply our equip logic fix to offhand items
            return ItemUtilKt.shouldInstantlyReplaceVisibleItem1_8(currentlyVisibleItem, expectedItem) || value;
        } else {
            return value;
        }
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/player/FirstPersonHandsAndItems;mainHandItem:Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack animatium$useCopyStackField(final ItemStack original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().items.equipAnimationItemCheck) {
            // Use the copy stack field for the stack comparison
            return this.animatium$mainHandItem;
        } else {
            return original;
        }
    }

    @ModifyVariable(method = "tick", at = @At(value = "LOAD", ordinal = 2), name = "nextMainHand")
    private ItemStack animatium$useLocalCopyStack(final ItemStack nextMainHand, @Share("copyStack") final LocalRef<ItemStack> copyStack) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().items.equipAnimationItemCheck) {
            // Use the local copied stack for the stack comparison
            return copyStack.get();
        } else {
            return nextMainHand;
        }
    }

    @Inject(method = "tick", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/player/FirstPersonHandsAndItems;mainHandHeight:F", ordinal = 4))
    private void animatium$setCurrentSlotAndCopyStack(final CallbackInfo ci, @Local(argsOnly = true, name = "player") final LocalPlayer player, @Share("copyStack") final LocalRef<ItemStack> copyStack) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().items.equipAnimationItemCheck && this.mainHandHeight < 0.1F) {
            // Update our copied stack
            this.animatium$mainHandItem = copyStack.get();
            // Cache the previous slot item to use in our comparison above
            this.animatium$currentSlot = player.getInventory().getSelectedSlot();
        }
    }

}
