package org.visuals.legacy.animatium.mixins.v1.gui;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.visuals.legacy.animatium.screens.OnboardingScreen;
import org.visuals.legacy.animatium.util.ConfigUtil;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Definition(id = "screen", field = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;")
    @Definition(id = "guiScreen", local = @Local(type = Screen.class, argsOnly = true))
    @Expression("@(this).screen = guiScreen")
    @ModifyVariable(method = "setScreen", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.BEFORE), argsOnly = true)
    private Screen animatium$showOnboarding(Screen value) {
        if (value instanceof TitleScreen titleScreen && ConfigUtil.bool("onboarding")) {
            return new OnboardingScreen(titleScreen);
        } else {
            return value;
        }
    }
}
