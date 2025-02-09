package btw.mixces.animatium.mixins.screen.recipebook;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RecipeBookComponent.class)
public abstract class MixinRecipeBookComponent {
    @WrapOperation(method = "isVisible", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent;visible:Z"))
    private boolean animatium$hideRecipeBook(RecipeBookComponent<?> instance, Operation<Boolean> original) {
        if (AnimatiumClient.getEnabled() && AnimatiumConfig.instance().getHideRecipeBook()) {
            return false;
        } else {
            return original.call(instance);
        }
    }
}
