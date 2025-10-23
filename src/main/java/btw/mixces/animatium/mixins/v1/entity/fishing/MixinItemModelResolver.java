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

package btw.mixces.animatium.mixins.v1.entity.fishing;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemModelResolver.class)
public abstract class MixinItemModelResolver {
    @WrapOperation(method = "appendItemLayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"))
    private Object animatium$stickModelWhenCastInThirdperson(ItemStack instance, DataComponentType<ResourceLocation> dataComponentType, Operation<ResourceLocation> original, @Local(argsOnly = true) ItemDisplayContext displayContext, @Local(argsOnly = true) ItemOwner itemOwner, @Local(argsOnly = true) ItemStack itemStack) {
        final LivingEntity livingEntity = itemOwner == null ? null : itemOwner.asLivingEntity();
        if (AnimatiumClient.isEnabled() &&
                AnimatiumConfig.instance().stickModelWhenCastInThirdperson &&
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
