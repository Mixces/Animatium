package me.mixces.animatium.mixins.screen.widget.toast;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.toast.RecipeToast;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.toast.TutorialToast;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ToastManager.Entry.class)
public abstract class MixinToastManagerEntry<T extends Toast> {
    @Shadow
    @Final
    private T instance;

    @WrapWithCondition(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/toast/Toast$Visibility;playSound(Lnet/minecraft/client/sound/SoundManager;)V"))
    private boolean animatium$disableRecipeAndTutorialToasts(Toast.Visibility instance, SoundManager soundManager) {
        return !AnimatiumConfig.getInstance().disableRecipeAndTutorialToasts || (!(this.instance instanceof RecipeToast) && !(this.instance instanceof TutorialToast));
    }
}
