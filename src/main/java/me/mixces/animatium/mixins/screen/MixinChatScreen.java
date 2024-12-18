package me.mixces.animatium.mixins.screen;

import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChatScreen.class)
public abstract class MixinChatScreen {
//    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;render(Lnet/minecraft/client/gui/DrawContext;IIIZ)V"))
//    private void animatium$oldChatPosition$undo(ChatHud instance, DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused, Operation<Void> original) {
//        if (AnimatiumConfig.getInstance().oldChatPosition) {
//            context.getMatrices().translate(0F, -12F, 0F);
//        }
//
//        // TODO: fix mouse pos offset
//        original.call(instance, context, currentTick, mouseX, mouseY, focused);
//        if (AnimatiumConfig.getInstance().oldChatPosition) {
//            context.getMatrices().translate(0F, 12F, 0F);
//        }
//    }
}
