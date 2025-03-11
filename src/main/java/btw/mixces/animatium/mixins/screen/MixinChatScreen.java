/**
 * Animatium
 * The all-you-could-want legacy animations mod for modern minecraft versions.
 * Brings back animations from the 1.7/1.8 era and more.
 * <p>
 * Copyright (C) 2024-2025 lowercasebtw
 * Copyright (C) 2024-2025 mixces
 * Copyright (C) 2024-2025 Contributors to the project retain their copyright
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package btw.mixces.animatium.mixins.screen;


import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ChatScreen.class)
public abstract class MixinChatScreen {
    @Unique
    private static final float animatium$chatOffset = 12.0F;

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent;render(Lnet/minecraft/client/gui/GuiGraphics;IIIZ)V"))
    private void animatium$oldChatPosition(ChatComponent instance, GuiGraphics context, int currentTick, int mouseX, int mouseY, boolean focused, Operation<Void> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().getOldChatVisual()) {
            context.pose().translate(0F, animatium$chatOffset, 0F);
        }

        original.call(instance, context, currentTick, mouseX, mouseY, focused);
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().getOldChatVisual()) {
            context.pose().translate(0F, -animatium$chatOffset, 0F);
        }
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/ChatScreen;getComponentStyleAt(DD)Lnet/minecraft/network/chat/Style;"), index = 1)
    private double animatium$oldChatPosition$fixY(double d) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().getOldChatVisual()) {
            return d - animatium$chatOffset;
        } else {
            return d;
        }
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent;getMessageTagAt(DD)Lnet/minecraft/client/GuiMessageTag;"))
    private GuiMessageTag animatium$oldChatVisual$removeChatIndicatorTooltip(ChatComponent instance, double mouseX, double mouseY, Operation<GuiMessageTag> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().getOldChatVisual()) {
            return null;
        } else {
            return original.call(instance, mouseX, mouseY);
        }
    }

    @ModifyArg(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent;handleChatQueueClicked(DD)Z"), index = 1)
    private double animatium$oldChatPosition$clicked$0(double d) {
        return d - ((AnimatiumClient.isEnabled() && AnimatiumConfig.instance().getOldChatVisual()) ? animatium$chatOffset : 0);
    }

    @ModifyArg(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/ChatScreen;getComponentStyleAt(DD)Lnet/minecraft/network/chat/Style;"), index = 1)
    private double animatium$oldChatPosition$clicked$1(double d) {
        return d - ((AnimatiumClient.isEnabled() && AnimatiumConfig.instance().getOldChatVisual()) ? animatium$chatOffset : 0);
    }

    @ModifyArg(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseClicked(DDI)Z"), index = 1)
    private double animatium$oldChatPosition$clicked$2(double d) {
        return d - ((AnimatiumClient.isEnabled() && AnimatiumConfig.instance().getOldChatVisual()) ? animatium$chatOffset : 0);
    }
}
