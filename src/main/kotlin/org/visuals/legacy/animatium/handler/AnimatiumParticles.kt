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

package org.visuals.legacy.animatium.handler

import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.core.Registry
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import org.visuals.legacy.animatium.Animatium.location
import org.visuals.legacy.animatium.handler.particle.BloodParticle
import org.visuals.legacy.animatium.handler.particle.FootprintParticle

object AnimatiumParticles {
    private val REGISTRY =
        hashMapOf<SimpleParticleType, ParticleProvider<SimpleParticleType>>()
    private val DYNAMIC_REGISTRY =
        hashMapOf<SimpleParticleType, ParticleProviderRegistry.PendingParticleProvider<SimpleParticleType>>()

    val BLOOD_PARTICLE_TYPE = register(location("blood"), true, BloodParticle.Provider())
    val FOOTPRINT_PARTICLE_TYPE = register(location("footprint"), false, FootprintParticle::Provider)

    fun bootstrap() {
        val providerRegistry = ParticleProviderRegistry.getInstance()
        for (entry in REGISTRY) {
            providerRegistry.register(entry.key, entry.value)
        }

        for (entry in DYNAMIC_REGISTRY) {
            providerRegistry.register(entry.key, entry.value)
        }
    }

    private fun register(
        id: Identifier,
        alwaysSpawn: Boolean,
        provider: ParticleProvider<SimpleParticleType>
    ): SimpleParticleType {
        val type = FabricParticleTypes.simple(alwaysSpawn)
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, id, type)
        REGISTRY[type] = provider
        return type
    }

    fun register(
        id: Identifier,
        alwaysSpawn: Boolean,
        provider: ParticleProviderRegistry.PendingParticleProvider<SimpleParticleType>
    ): SimpleParticleType {
        val type = FabricParticleTypes.simple(alwaysSpawn)
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, id, type)
        DYNAMIC_REGISTRY[type] = provider
        return type
    }
}