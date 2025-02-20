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

package btw.mixces.animatium.mixins.screen.inventory;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.mixins.accessor.AbstractRecipeBookScreenAccessor;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EffectsInInventory.class)
public abstract class MixinEffectsInInventory {
    @Shadow
    @Final
    private AbstractContainerScreen<?> screen;

    @WrapOperation(method = "renderEffects", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;imageWidth:I"))
    private int animatium$oldEffectsInventoryPosition(AbstractContainerScreen<?> instance, Operation<Integer> original) {
        final int imageWidth = original.call(instance);
        if (AnimatiumClient.getEnabled() && AnimatiumConfig.instance().getOldEffectsInventoryPosition() && !(this.screen instanceof AbstractRecipeBookScreen<?> recipeBookScreen && ((AbstractRecipeBookScreenAccessor) recipeBookScreen).getRecipeBookComponent().isVisible())) {
            return 0;
        } else {
            return imageWidth;
        }
    }

    @ModifyExpressionValue(method = "renderEffects", at = @At(value = "CONSTANT", args = "intValue=2"))
    private int animatium$oldEffectsInventoryPosition(int original) {
        if (AnimatiumClient.getEnabled() && AnimatiumConfig.instance().getOldEffectsInventoryPosition() && !(this.screen instanceof AbstractRecipeBookScreen<?> recipeBookScreen && ((AbstractRecipeBookScreenAccessor) recipeBookScreen).getRecipeBookComponent().isVisible())) {
            return -124;
        } else {
            return original;
        }
    }
}
