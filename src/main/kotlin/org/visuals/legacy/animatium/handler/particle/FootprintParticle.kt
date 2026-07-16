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
 * <p>
 * "MINECRAFT" LINKING EXCEPTION TO THE GPL
 */

package org.visuals.legacy.animatium.handler.particle

import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.state.level.QuadParticleRenderState
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import org.joml.Quaternionf

class FootprintParticle(level: ClientLevel, x: Double, y: Double, z: Double, sprite: TextureAtlasSprite) :
    SingleQuadParticle(level, x, y, z, sprite) {
    init {
        this.quadSize = 0.125F
        this.lifetime = 200
    }

    override fun getLayer(): Layer = Layer.TRANSLUCENT

    override fun extractRotatedQuad(
        particleTypeRenderState: QuadParticleRenderState,
        camera: Camera,
        rotation: Quaternionf,
        partialTickTime: Float
    ) {
        var strength = (this.age + partialTickTime) / this.lifetime
        strength *= strength
        this.alpha = ((2.0F - strength * 2.0F).coerceAtMost(1.0F)) * 0.2F
        super.extractRotatedQuad(particleTypeRenderState, camera, Quaternionf().rotateX(-Mth.HALF_PI), partialTickTime)
    }

    class Provider(val provider: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            options: SimpleParticleType,
            level: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double,
            random: RandomSource
        ): Particle = FootprintParticle(level, x, y, z, provider.get(random))
    }
}