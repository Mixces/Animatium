package org.visuals.legacy.animatium.mixins.v1.entity.particles.smooth;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(SingleQuadParticle.class)
public abstract class MixinSingleQuadParticle_UpdateSprite extends Particle {
    @Shadow
    public abstract void setSpriteFromAge(final SpriteSet sprites);

    protected MixinSingleQuadParticle_UpdateSprite(final ClientLevel level, final double x, final double y, final double z) {
        super(level, x, y, z);
    }

    @Inject(method = "extract", at = @At("HEAD"))
    public void animatium$extractSprite(final QuadParticleRenderState quadParticleRenderState, final Camera camera, final float tickDelta, final CallbackInfo ci) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().fixes.smoothParticles && this instanceof SpritesAccessor spritesAccessor) {
            this.setSpriteFromAge(spritesAccessor.animatium$sprites());
        }
    }
}