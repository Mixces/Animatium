package me.mixces.animatium.mixins;

import me.mixces.animatium.config.AnimatiumConfig;
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
    private void animatium$allowNegativeScrolling(double scrollY, CallbackInfo ci) {
        if (AnimatiumConfig.centerScrollableListWidgets && (ScrollableWidget) (Object) this instanceof EntryListWidget<?> entryListWidget) {
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
    public void animatium$modifyMaxScroll(CallbackInfoReturnable<Integer> cir) {
        if (AnimatiumConfig.centerScrollableListWidgets && (ScrollableWidget) (Object) this instanceof EntryListWidget<?> entryListWidget) {
            cir.setReturnValue(this.getContentsHeightWithPadding() - entryListWidget.getHeight());
        }
    }
}
