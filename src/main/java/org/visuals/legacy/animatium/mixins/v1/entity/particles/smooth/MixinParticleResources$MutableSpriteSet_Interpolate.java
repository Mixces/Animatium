package org.visuals.legacy.animatium.mixins.v1.entity.particles.smooth;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

import java.util.List;

@Mixin(ParticleResources.MutableSpriteSet.class)
public abstract class MixinParticleResources$MutableSpriteSet_Interpolate {
    @Shadow
    private List<TextureAtlasSprite> sprites;

    @WrapMethod(method = "get(II)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;")
    private TextureAtlasSprite animatium$interpolateSpriteIndex(final int age, final int lifetime, final Operation<TextureAtlasSprite> original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().fixes.smoothParticles) {
            final int frames = this.sprites.size() - 1;
            final float tickDelta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false); // TODO/NOTE: Find better way to do this?
            return this.sprites.get(Mth.clamp((int) ((age + tickDelta) * ((float) frames) / lifetime), 0, frames)); // TODO/NOTE: Why do I need to clamp? Figure it out.
        } else {
            return original.call(age, lifetime);
        }
    }
}
