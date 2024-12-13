package me.mixces.animatium.mixins;

import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.ScrollableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScrollableWidget.class)
public abstract class MixinScrollableWidget {
    @Shadow
    private double scrollY;

    @Shadow
    protected abstract int getContentsHeightWithPadding();

    @Shadow
    public abstract int getMaxScrollY();

    @Inject(method = "setScrollY", at = @At("HEAD"), cancellable = true)
    private void allowNegativeScrolling(double scrollY, CallbackInfo ci) {
        if ((ScrollableWidget) (Object) this instanceof EntryListWidget<?> entryListWidget) {
            ci.cancel();
            int maxScrollY = getMaxScrollY();
            if (maxScrollY < 0)
                maxScrollY /= 2;
            if (!entryListWidget.centerListVertically && maxScrollY < 0)
                maxScrollY = 0;
            this.scrollY = Math.min(Math.max(0, scrollY), maxScrollY);
        }
    }

    @Inject(method = "getMaxScrollY", at = @At("HEAD"), cancellable = true)
    public void modifyMaxScroll(CallbackInfoReturnable<Integer> cir) {
        if ((ScrollableWidget) (Object) this instanceof EntryListWidget<?> entryListWidget) {
            cir.setReturnValue(this.getContentsHeightWithPadding() - entryListWidget.getHeight());
        }
    }
}
