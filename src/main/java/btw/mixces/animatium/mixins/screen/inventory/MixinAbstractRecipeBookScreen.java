package btw.mixces.animatium.mixins.screen.inventory;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractRecipeBookScreen.class)
public abstract class MixinAbstractRecipeBookScreen {
    @WrapWithCondition(method = "initButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractRecipeBookScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"))
    private boolean animatium$hideRecipeBook$button(AbstractRecipeBookScreen<?> instance, GuiEventListener guiEventListener) {
        return !AnimatiumClient.getEnabled() || !AnimatiumConfig.instance().getHideRecipeBook();
    }

    @WrapWithCondition(method = "initButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractRecipeBookScreen;addWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"))
    private boolean animatium$hideRecipeBook$widget(AbstractRecipeBookScreen<?> instance, GuiEventListener guiEventListener) {
        return !AnimatiumClient.getEnabled() || !AnimatiumConfig.instance().getHideRecipeBook();
    }
}
