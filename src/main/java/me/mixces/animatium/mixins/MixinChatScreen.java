package me.mixces.animatium.mixins;

import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public abstract class MixinChatScreen {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.AFTER))
    private void animatium$oldChatPosition(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (AnimatiumConfig.oldChatPosition) {
            context.getMatrices().translate(0F, 12F, 0F);
        }
    }
}
