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

package org.visuals.legacy.animatium.mixins.v1.entity.fishing;

import org.visuals.legacy.animatium.AnimatiumClient;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.states.ItemUtilityRenderState;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelResolver.class)
public abstract class MixinItemModelResolver {
    @Inject(method = "appendItemLayers", at = @At("HEAD"))
    private void animatium$storeItemStack(ItemStackRenderState itemStackRenderState, ItemStack itemStack, ItemDisplayContext itemDisplayContext, Level level, ItemOwner itemOwner, int i, CallbackInfo ci) {
        ((ItemUtilityRenderState) itemStackRenderState).animatium$setItemStack(itemStack);
    }

    @WrapOperation(method = "appendItemLayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"))
    private Object animatium$stickModelWhenCastInThirdperson(ItemStack instance, DataComponentType<?> dataComponentType, Operation<Object> original, @Local(argsOnly = true) ItemDisplayContext displayContext, @Local(argsOnly = true) ItemOwner itemOwner, @Local(argsOnly = true) ItemStack itemStack) {
        final LivingEntity livingEntity = itemOwner == null ? null : itemOwner.asLivingEntity();
        if (AnimatiumClient.ENABLED &&
                AnimatiumConfig.instance().items.stickModelWhenCastInThirdperson &&
                itemStack.getItem() == Items.FISHING_ROD &&
                (livingEntity instanceof Player player && player.fishing != null) &&
                ((displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND && livingEntity.getOffhandItem() == itemStack) ||
                        (displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND && livingEntity.getMainHandItem() == itemStack))) {
            return ResourceLocation.withDefaultNamespace("stick");
        } else {
            return original.call(instance, dataComponentType);
        }
    }
}
