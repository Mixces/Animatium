package org.visuals.legacy.animatium.mixins.v1.rendering;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(ScreenEffectRenderer.class)
public abstract class MixinScreenEffectRenderer_TextureOverlays {
	@ModifyExpressionValue(method = "renderWater", at = @At(value = "CONSTANT", args = "floatValue=0.1"))
	private static float animatium$useOldWaterOverlayOpacity(final float original) {
		if (Animatium.isEnabled() && AnimatiumConfig.instance().other.oldWaterOverlayOpacity) {
			return 0.5F;
		} else {
			return original;
		}
	}

	@ModifyArg(method = "renderTex", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ARGB;colorFromFloat(FFFF)I"), index = 0)
	private static float animatium$useOldBlockOverlayOpacity(final float original) {
		if (Animatium.isEnabled() && AnimatiumConfig.instance().other.oldBlockOverlayOpacity) {
			return 0.5F;
		} else {
			return original;
		}
	}
}
