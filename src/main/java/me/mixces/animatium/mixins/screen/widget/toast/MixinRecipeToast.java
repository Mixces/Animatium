package me.mixces.animatium.mixins.screen.widget.toast;

import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.toast.RecipeToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.recipe.display.RecipeDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeToast.class)
public abstract class MixinRecipeToast {
    @Inject(method = "show", at = @At("HEAD"), cancellable = true)
    private static void animatium$disableRecipeAndTutorialToasts(ToastManager toastManager, RecipeDisplay display, CallbackInfo ci) {
        if (AnimatiumConfig.getInstance().disableRecipeAndTutorialToasts) {
            ci.cancel();
        }
    }
}
