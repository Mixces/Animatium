package org.visuals.legacy.animatium.mixins.v1.gui.screen_tweaks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.CubeMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.rendering.panorama.LegacyPanoramaRenderer;
import org.visuals.legacy.animatium.util.states.GuiUtility;

@Mixin(GuiRenderer.class)
public abstract class MixinGuiRenderer_LegacyPanoramaRendering {
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/CubeMap;render(FF)V", ordinal = 0))
    private void animatium$panoramaRendering(final CubeMap instance, final float rotXInDegrees, final float spin, final Operation<Void> original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.panoramaRendering) {
            LegacyPanoramaRenderer.render(((GuiUtility) Minecraft.getInstance().gui).animatium$getGraphics());
        } else {
            original.call(instance, rotXInDegrees, spin);
        }
    }
}
