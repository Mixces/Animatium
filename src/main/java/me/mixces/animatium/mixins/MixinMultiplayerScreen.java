package me.mixces.animatium.mixins;

import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public abstract class MixinMultiplayerScreen {
    @Shadow
    protected MultiplayerServerListWidget serverListWidget;

    @Inject(method = "init", at = @At("TAIL"))
    private void animatium$updateListWidget(CallbackInfo ci) {
        if (AnimatiumConfig.centerScrollableListWidgets) {
            this.serverListWidget.refreshScroll();
        }
    }
}
