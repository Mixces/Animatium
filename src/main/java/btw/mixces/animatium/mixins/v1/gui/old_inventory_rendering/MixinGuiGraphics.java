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

package btw.mixces.animatium.mixins.v1.gui.old_inventory_rendering;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.ItemUtils;
import btw.mixces.animatium.util.RenderUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public abstract class MixinGuiGraphics {
    @Shadow
    public abstract void fill(int minX, int minY, int maxX, int maxY, int color);

    @WrapOperation(method = "renderTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/TooltipRenderUtil;renderTooltipBackground(Lnet/minecraft/client/gui/GuiGraphics;IIIILnet/minecraft/resources/ResourceLocation;)V"))
    private void animatium$tooltipStyleRendering(GuiGraphics guiGraphics, int x, int y, int width, int height, ResourceLocation sprite, Operation<Void> original) {
        if (AnimatiumClient.ENABLED && AnimatiumConfig.instance().screen.tooltipStyleRendering) {
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
            animatium$drawFrameGradient(guiGraphics, n, o + 1, p, q, 1347420415, 1344798847);
        } else {
            original.call(guiGraphics, x, y, width, height, sprite);
        }
    }

    @Inject(method = "renderItemBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(Lcom/mojang/blaze3d/pipeline/RenderPipeline;IIIII)V", ordinal = 0, shift = At.Shift.AFTER))
    private void animatium$oldDurabilityBar(ItemStack stack, int x, int y, CallbackInfo ci) {
        if (AnimatiumClient.ENABLED && AnimatiumConfig.instance().items.durabilityBarColors && !(stack.getItem() instanceof BundleItem)) {
            int i = x + 2;
            int j = y + 13;
            final int color = ARGB.opaque(ARGB.color((255 - ItemUtils.getLegacyDurabilityColorValue(stack)) / 4, 64, 0));
            this.fill(i, j, i + 12, j + 1, color);
        }
    }

    @Unique
    private static void animatium$drawFrameGradient(GuiGraphics context, int x, int y, int width, int height, int startColor, int endColor) {
        RenderUtils.fillVerticalGradientLine(context, x, y, height - 2, startColor, endColor);
        RenderUtils.fillVerticalGradientLine(context, x + width - 1, y, height - 2, startColor, endColor);
        RenderUtils.fillHorizontalLine(context, x, y - 1, width, startColor);
        RenderUtils.fillHorizontalLine(context, x, y - 1 + height - 1, width, endColor);
    }
}
