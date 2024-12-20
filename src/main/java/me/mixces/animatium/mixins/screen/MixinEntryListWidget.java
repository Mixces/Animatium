package me.mixces.animatium.mixins.screen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.ScrollableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntryListWidget.class)
public abstract class MixinEntryListWidget {
    @Inject(method = "renderWidget", at = @At("HEAD"))
    private void animatium$updateScroll(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (AnimatiumConfig.getInstance().getCenterScrollableListWidgets()) {
            ((ScrollableWidget) (Object) this).refreshScroll();
        }
    }

    @WrapOperation(method = "renderEntry", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/EntryListWidget;isFocused()Z"))
    private boolean animatium$oldListWidgetSelectedBorderColor(EntryListWidget<?> instance, Operation<Boolean> original) {
        if (AnimatiumConfig.getInstance().getOldListWidgetSelectedBorderColor()) {
            return false;
        } else {
            return original.call(instance);
        }
    }
}
