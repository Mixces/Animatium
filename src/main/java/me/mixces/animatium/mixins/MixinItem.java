package me.mixces.animatium.mixins;

import me.mixces.animatium.config.AnimatiumConfig;
import me.mixces.animatium.util.ItemUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class MixinItem {
    @Inject(method = "getItemBarColor", at = @At("HEAD"), cancellable = true)
    private void animatium$oldDurabilityBarColors(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (AnimatiumConfig.getInstance().oldDurabilityBarColors) {
            int value = ItemUtils.getLegacyDurabilityColorValue(stack);
            cir.setReturnValue(ColorHelper.getArgb(255 - value, value, 0));
        }
    }
}
