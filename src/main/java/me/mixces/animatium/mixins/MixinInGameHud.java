package me.mixces.animatium.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud {
    @Inject(method = "renderChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;render(Lnet/minecraft/client/gui/DrawContext;IIIZ)V", shift = At.Shift.BEFORE))
    private void animatium$oldChatPosition(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci, @Local Window window) {
        if (AnimatiumConfig.oldChatPosition) {
            context.getMatrices().translate(0F, 12F, 0F);
        }
    }

    @WrapOperation(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/Perspective;isFirstPerson()Z"))
    private boolean animatium$showCrosshairInThirdperson(Perspective instance, Operation<Boolean> original) {
        if (AnimatiumConfig.showCrosshairInThirdperson) {
            return true;
        } else {
            return original.call(instance);
        }
    }

    @WrapWithCondition(method = "renderHealthBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawHeart(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/gui/hud/InGameHud$HeartType;IIZZZ)V"))
    private boolean animatium$removeHeartFlash(InGameHud instance, DrawContext context, InGameHud.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half) {
        return !AnimatiumConfig.removeHeartFlash || !blinking || type == InGameHud.HeartType.CONTAINER;
    }
}
