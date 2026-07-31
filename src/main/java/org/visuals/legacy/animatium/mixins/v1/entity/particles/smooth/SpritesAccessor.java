package org.visuals.legacy.animatium.mixins.v1.entity.particles.smooth;

import net.minecraft.client.particle.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({
        AttackSweepParticle.class,
        BaseAshSmokeParticle.class,
        BubblePopParticle.class,
        DragonBreathParticle.class,
        DustParticleBase.class,
        ExplodeParticle.class,
        FallingDustParticle.class,
        GeyserPlumeParticle.class,
        GlowParticle.class,
        GustParticle.class,
        HugeExplosionParticle.class,
        PlayerCloudParticle.class,
        SculkChargeParticle.class,
        SculkChargePopParticle.class,
        SimpleAnimatedParticle.class,
        SnowflakeParticle.class,
        SoulParticle.class,
        SpellParticle.class,
        TrialSpawnerDetectionParticle.class,
        WakeParticle.class
})
public interface SpritesAccessor {
    @Accessor("sprites")
    SpriteSet animatium$sprites();
}
