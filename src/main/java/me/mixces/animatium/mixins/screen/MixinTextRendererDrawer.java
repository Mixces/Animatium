package me.mixces.animatium.mixins.screen;

import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.font.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TextRenderer.Drawer.class)
public abstract class MixinTextRendererDrawer {
    @Unique
    private static final float animatium$strikethroughOffset = 0.5F;

    @ModifyArg(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/BakedGlyph$Rectangle;<init>(FFFFFIIF)V", ordinal = 0), index = 1)
    private float animatium$fixTextStrikethroughStyle$minY(float minY) {
        if (AnimatiumConfig.getInstance().fixTextStrikethroughStyle) {
            return minY - animatium$strikethroughOffset;
        } else {
            return minY;
        }
    }

    @ModifyArg(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/BakedGlyph$Rectangle;<init>(FFFFFIIF)V", ordinal = 0), index = 3)
    private float animatium$fixTextStrikethroughStyle$maxY(float maxY) {
        if (AnimatiumConfig.getInstance().fixTextStrikethroughStyle) {
            return maxY - animatium$strikethroughOffset;
        } else {
            return maxY;
        }
    }
}
