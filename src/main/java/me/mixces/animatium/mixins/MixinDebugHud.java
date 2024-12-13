package me.mixces.animatium.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DebugHud.class)
public abstract class MixinDebugHud {
    @WrapOperation(method = "drawText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
    private void removeDebugBackground(DrawContext instance, int x1, int y1, int x2, int y2, int color, Operation<Void> original) {
        if (!AnimatiumConfig.removeDebugHudBackground) {
            original.call(instance, x1, y1, x2, y2, color);
        }
    }

    @ModifyArg(method = "drawText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I"), index = 5)
    private boolean addDebugShadow(boolean shadow) {
        if (AnimatiumConfig.debugHudTextShadow) {
            return true;
        } else {
            return shadow;
        }
    }
}
