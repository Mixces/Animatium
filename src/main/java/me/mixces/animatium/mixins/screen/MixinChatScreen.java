package me.mixces.animatium.mixins.screen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChatScreen.class)
public abstract class MixinChatScreen {
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;render(Lnet/minecraft/client/gui/DrawContext;IIIZ)V"))
    private void animatium$oldChatPosition$undo(ChatHud instance, DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused, Operation<Void> original) {
        if (AnimatiumConfig.oldChatPosition) {
            context.getMatrices().translate(0F, -12F, 0F);
        }

        // TODO: fix mouse pos offset
        original.call(instance, context, currentTick, mouseX, mouseY, focused);
        if (AnimatiumConfig.oldChatPosition) {
            context.getMatrices().translate(0F, 12F, 0F);
        }
    }
}
