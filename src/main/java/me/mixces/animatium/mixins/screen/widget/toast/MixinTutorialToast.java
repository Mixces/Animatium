package me.mixces.animatium.mixins.screen.widget.toast;

import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.toast.TutorialToast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TutorialToast.class)
public abstract class MixinTutorialToast {
    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void animatium$disableRecipeAndTutorialToasts(ToastManager manager, long time, CallbackInfo ci) {
        if (AnimatiumConfig.getInstance().disableRecipeAndTutorialToasts) {
            ci.cancel();
        }
    }

    @Inject(method = "draw", at = @At("HEAD"), cancellable = true)
    private void animatium$disableRecipeAndTutorialToasts(DrawContext context, TextRenderer textRenderer, long startTime, CallbackInfo ci) {
        if (AnimatiumConfig.getInstance().disableRecipeAndTutorialToasts) {
            ci.cancel();
        }
    }
}
