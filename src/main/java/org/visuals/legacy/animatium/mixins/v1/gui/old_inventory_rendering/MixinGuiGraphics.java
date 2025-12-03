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

package org.visuals.legacy.animatium.mixins.v1.gui.old_inventory_rendering;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.ItemUtils;
import org.visuals.legacy.animatium.util.rendering.RenderUtils;

@Mixin(GuiGraphics.class)
public abstract class MixinGuiGraphics {
    @WrapOperation(method = "renderTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/TooltipRenderUtil;renderTooltipBackground(Lnet/minecraft/client/gui/GuiGraphics;IIIILnet/minecraft/resources/ResourceLocation;)V"))
    private void animatium$tooltipStyleRendering(GuiGraphics guiGraphics, int x, int y, int width, int height, ResourceLocation sprite, Operation<Void> original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.tooltipStyleRendering) {
            int n = x - 3;
            int o = y - 3;
            int p = width + 6;
            int q = height + 6;
            // TODO/NOTE: Figure out good names for these variables LOL
            final int lineColor = -267386864;
            RenderUtils.fillHorizontalLine(guiGraphics, n, o - 1, p, lineColor);
            RenderUtils.fillHorizontalLine(guiGraphics, n, o + q, p, lineColor);
            RenderUtils.fillRectangle(guiGraphics, n, o, p, q, lineColor);
            RenderUtils.fillVerticalLine(guiGraphics, n - 1, o, q, lineColor);
            RenderUtils.fillVerticalLine(guiGraphics, n + p, o, q, lineColor);
            RenderUtils.fillFrameGradient(guiGraphics, n, o + 1, p, q, 0x505000FF, 0x5028007F);
        } else {
            original.call(guiGraphics, x, y, width, height, sprite);
        }
    }

    @Inject(method = "renderItemBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(Lcom/mojang/blaze3d/pipeline/RenderPipeline;IIIII)V", ordinal = 0, shift = At.Shift.AFTER))
    private void animatium$oldDurabilityBar(ItemStack stack, int x, int y, CallbackInfo ci, @Local(ordinal = 2) int i, @Local(ordinal = 3) int j) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().items.durabilityBarColors && !(stack.getItem() instanceof BundleItem)) {
            final int color = ARGB.opaque(ARGB.color((255 - ItemUtils.getLegacyDurabilityColorValue(stack)) / 4, 64, 0));
            RenderUtils.fillRectangle((GuiGraphics) (Object) this, i, j, 12, 1, color);
        }
    }
}
