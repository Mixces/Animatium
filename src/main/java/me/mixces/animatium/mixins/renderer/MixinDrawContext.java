package me.mixces.animatium.mixins.renderer;

import me.mixces.animatium.config.AnimatiumConfig;
import me.mixces.animatium.util.ItemUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public abstract class MixinDrawContext {
    @Shadow
    public abstract void fill(RenderLayer layer, int x1, int y1, int x2, int y2, int z, int color);

    @Inject(method = "drawItemBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIIII)V", ordinal = 0, shift = At.Shift.AFTER))
    private void animatium$oldDurabilityBar(ItemStack stack, int x, int y, CallbackInfo ci) {
        if (AnimatiumConfig.getInstance().getOldDurabilityBarColors()) {
            int i = x + 2;
            int j = y + 13;
            int color = ColorHelper.getArgb((255 - ItemUtils.getLegacyDurabilityColorValue(stack)) / 4, 64, 0);
            this.fill(RenderLayer.getGui(), i, j, i + 12, j + 1, 200, ColorHelper.fullAlpha(color));
        }
    }
}
