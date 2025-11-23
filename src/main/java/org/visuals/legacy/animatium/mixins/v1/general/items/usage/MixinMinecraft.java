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

package org.visuals.legacy.animatium.mixins.v1.general.items.usage;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.ItemUtils;
import org.visuals.legacy.animatium.util.Utils;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    @Final
    public Options options;

    @Shadow
    @Nullable
    public HitResult hitResult;

    @Shadow
    @Nullable
    public ClientLevel level;

    @Shadow
    @Nullable
    public MultiPlayerGameMode gameMode;

    @ModifyVariable(method = "startUseItem", at = @At(value = "STORE", ordinal = 0))
    private ItemStack animatium$fixCopyStackUseItem(ItemStack original) {
        if (Animatium.ENABLED && AnimatiumConfig.instance().fixes.fixEquipAnimation) {
            // Update the stack to match mutations to the stack in other classes
            return original.copy();
        } else {
            return original;
        }
    }

    @WrapOperation(method = "startUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;)V", ordinal = 2))
    private void animatium$swingOnUse(LocalPlayer instance, InteractionHand hand, Operation<Void> original, @Local InteractionHand interactionHand, @Local ItemStack itemStack) {
        if (Animatium.ENABLED && !AnimatiumConfig.instance().items.swingOnUse && ItemUtils.isSwingItemBlacklisted(itemStack)) {
            Utils.sendSwingPacket(instance, hand);
        } else {
            original.call(instance, hand);
        }
    }

    @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;)V"))
    private void animatium$swingOnDrop(LocalPlayer instance, InteractionHand hand, Operation<Void> original) {
        if (Animatium.ENABLED && !AnimatiumConfig.instance().items.swingOnDrop) {
            Utils.sendSwingPacket(instance, hand);
        } else {
            original.call(instance, hand);
        }
    }

    @WrapOperation(method = "startUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;)V", ordinal = 0))
    private void animatium$swingOnEntityInteract(LocalPlayer instance, InteractionHand hand, Operation<Void> original) {
        if (Animatium.ENABLED && !AnimatiumConfig.instance().items.swingOnEntityInteract) {
            Utils.sendSwingPacket(instance, hand);
        } else {
            original.call(instance, hand);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void animatium$applySwingWhilstMining(CallbackInfo ci) {
        if (Animatium.ENABLED && AnimatiumConfig.instance().items.itemUsageSwinging) {
            if (this.player != null && !(this.player.getItemInHand(this.player.getUsedItemHand()).isEmpty() || !this.player.isUsingItem() || !this.options.keyAttack.isDown())) {
                Utils.applySwingWhilstMining(this.level, this.player, this.hitResult);
            }
        }
    }

    @WrapWithCondition(method = "startUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;itemUsed(Lnet/minecraft/world/InteractionHand;)V"))
    private boolean animatium$equipAnimationOnItemUse(ItemInHandRenderer instance, InteractionHand interactionHand) {
        // TODO: This fixes projectile equip, but it isn't going to be 100% accurate in some other areas. This needs to be worked on :)
        if (Animatium.ENABLED && AnimatiumConfig.instance().fixes.fixEquipAnimationOnItemUse) {
            // The equip animation plays when right-clicking blocks in creative mode in <1.8.x
            final boolean isAimedAtBlock = this.hitResult != null && this.hitResult.getType() == HitResult.Type.BLOCK;
            // This might need to be revamped a bit. We are already checking for creative mode in the actual method,
            // however this seems to narrow things down
            final boolean isInCreative = this.gameMode != null && this.gameMode.getPlayerMode().isCreative();
            return isAimedAtBlock && isInCreative;
        } else {
            return true;
        }
    }
}
