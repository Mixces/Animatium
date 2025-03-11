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

package btw.mixces.animatium.mixins.renderer;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.ItemUtils;
import btw.mixces.animatium.util.RenderUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
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
public abstract class MixinDrawContext {
    @Shadow
    public abstract void fill(RenderType renderType, int i, int j, int k, int l, int m, int n);

    @WrapOperation(method = "renderTooltipInternal", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/TooltipRenderUtil;renderTooltipBackground(Lnet/minecraft/client/gui/GuiGraphics;IIIIILnet/minecraft/resources/ResourceLocation;)V"))
    private void animatium$oldTooltipStyleRendering(GuiGraphics context, int i, int j, int k, int l, int padding, ResourceLocation resourceLocation, Operation<Void> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().getOldTooltipStyleRendering()) {
            int n = i - 3;
            int o = j - 3;
            int p = k + 3 + 3;
            int q = l + 3 + 3;
            // TODO/NOTE: Figure out good names for these variables LOL
            RenderUtils.fillHorizontalLine(context, n, o - 1, p, padding, -267386864);
            RenderUtils.fillHorizontalLine(context, n, o + q, p, padding, -267386864);
            RenderUtils.fillRectangle(context, n, o, p, q, padding, -267386864);
            RenderUtils.fillVerticalLine(context, n - 1, o, q, padding, -267386864);
            RenderUtils.fillVerticalLine(context, n + p, o, q, padding, -267386864);
            animatium$drawFrameGradient(context, n, o + 1, p, q, padding, 1347420415, 1344798847);
        } else {
            original.call(context, i, j, k, l, padding, resourceLocation);
        }
    }

    @Inject(method = "renderItemBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(Lnet/minecraft/client/renderer/RenderType;IIIIII)V", ordinal = 0, shift = At.Shift.AFTER))
    private void animatium$oldDurabilityBar(ItemStack stack, int x, int y, CallbackInfo ci) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().getOldDurabilityBarColors() && !(stack.getItem() instanceof BundleItem)) {
            int i = x + 2;
            int j = y + 13;
            int color = ARGB.color((255 - ItemUtils.getLegacyDurabilityColorValue(stack)) / 4, 64, 0);
            this.fill(RenderType.gui(), i, j, i + 12, j + 1, 200, ARGB.opaque(color));
        }
    }

    @Unique
    private static void animatium$drawFrameGradient(GuiGraphics context, int i, int j, int k, int l, int padding, int startColor, int endColor) {
        RenderUtils.fillVerticalGradientLine(context, i, j, l - 2, padding, startColor, endColor);
        RenderUtils.fillVerticalGradientLine(context, i + k - 1, j, l - 2, padding, startColor, endColor);
        RenderUtils.fillHorizontalLine(context, i, j - 1, k, padding, startColor);
        RenderUtils.fillHorizontalLine(context, i, j - 1 + l - 1, k, padding, endColor);
    }
}
