package me.mixces.animatium.mixins;

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
        if (AnimatiumConfig.centerScrollableListWidgets) {
            ((ScrollableWidget) (Object) this).refreshScroll();
        }
    }
}
