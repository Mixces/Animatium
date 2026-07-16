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

import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.TerrainParticle
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.GameType
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RedStoneWireBlock
import net.minecraft.world.phys.EntityHitResult
import org.visuals.legacy.animatium.config.AnimatiumConfig
import org.visuals.legacy.animatium.handler.AnimatiumParticles

// Credit to Orange Marshalls 1.8 Mod "Vanilla Enhancements"
class BloodParticle(
    level: ClientLevel,
    x: Double, y: Double, z: Double,
    velocityX: Double, velocityY: Double, velocityZ: Double,
    random: RandomSource
) : TerrainParticle(
    level,
    x, y, z,
    velocityX, velocityY, velocityZ,
    Blocks.REDSTONE_WIRE.defaultBlockState().setValue(RedStoneWireBlock.POWER, 15)
) {
    init {
        this.rCol = random.nextFloat() * 0.25F + 0.3F
        this.gCol = 0.0F
        this.bCol = 0.0F
        this.quadSize *= 0.8F
    }

    class Provider : ParticleProvider<SimpleParticleType> {
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
        ): Particle = BloodParticle(level, x, y, z, velocityX, velocityY, velocityZ, random)
    }

    companion object {
        @JvmStatic
        fun canSpawn(): Boolean {
            val minecraft = Minecraft.getInstance()
            val hitResult = minecraft.hitResult
            if (hitResult !is EntityHitResult) {
                return false
            } else if (!hitResult.entity.isAlive) {
                return false
            } else {
                val netPlayerHandler = minecraft.gameMode ?: return false
                return (netPlayerHandler.playerMode == GameType.SURVIVAL) || (netPlayerHandler.playerMode == GameType.CREATIVE)
            }
        }

        @JvmStatic
        fun spawn(target: Entity) {
            val eyePos = BlockPos.containing(target.x, target.y + 0.5, target.z)
            val count = 5 * AnimatiumConfig.instance().extras.bloodParticleMultiplier
            for (i in 0..<count) {
                val x = eyePos.x.toDouble() + Math.random()
                val y = eyePos.y.toDouble() + 0.3 + Math.random() * 1.3
                val z = eyePos.z.toDouble() + Math.random()
                val velocityX = Math.random() * 2.0 - 1.3
                val velocityY = Math.random() * 0.8
                val velocityZ = Math.random() * 2.0 - 1.3
                target.level()
                    .addParticle(AnimatiumParticles.BLOOD_PARTICLE_TYPE, x, y, z, velocityX, velocityY, velocityZ)
            }
        }
    }
}