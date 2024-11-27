package me.mixces.animatium.mixin;

import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EntryListWidget.class)
public abstract class EntryListWidgetMixin {

    @Shadow
    private double scrollAmount;

    @Shadow
    protected boolean centerListVertically;

    @Shadow
    public abstract int getMaxScroll();

    /**
     * @author Mixces
     * @reason Old GUI
     */
    @Overwrite
    public void setScrollAmountOnly(double amount) {
        int var1 = getMaxScroll();
        if (var1 < 0) {
            var1 /= 2;
        }
        if (!centerListVertically && var1 < 0) {
            var1 = 0;
        }
        if (scrollAmount < 0.0F) {
            scrollAmount = 0.0F;
        }
        if (scrollAmount > (float) var1) {
            scrollAmount = (float) var1;
        }
    }

    @ModifyArgs(
            method = "getMaxScroll",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;max(II)I"
            )
    )
    private void animatium$removeNonNegativeRestriction(Args args) {
        args.set(0, args.get(1));
    }
}
