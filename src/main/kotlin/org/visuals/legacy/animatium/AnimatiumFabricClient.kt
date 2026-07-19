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

package org.visuals.legacy.animatium

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel
import net.fabricmc.fabric.api.resource.v1.ResourceLoader
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType
import net.fabricmc.loader.api.FabricLoader
import org.visuals.legacy.animatium.command.AnimatiumCommand
import org.visuals.legacy.animatium.handler.AnimatiumKeybinds
import org.visuals.legacy.animatium.handler.AnimatiumNetworking
import org.visuals.legacy.animatium.handler.AnimatiumParticles

@Entrypoint
class AnimatiumFabricClient : ClientModInitializer {
    override fun onInitializeClient() {
        Animatium.initialize()

        val modContainer = FabricLoader.getInstance().getModContainer(AnimatiumConstants.MOD_ID)
            .orElseThrow({ RuntimeException("Mod container data could not be found for Animatium!") })
        for (pack in listOf("classic_textures", "classic_panorama", "classic_water")) {
            ResourceLoader.registerBuiltinPack(Animatium.location(pack), modContainer, PackActivationType.NORMAL)
        }

        ModelLoadingPlugin.register { context ->
            context.addModel(
                AnimatiumConstants.FAST_GRASS_MODEL_KEY,
                SimpleUnbakedExtraModel.blockStateModel(AnimatiumConstants.FAST_GRASS_MODEL_LOCATION)
            )
        }

        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ -> dispatcher.register(AnimatiumCommand.create()) }

        AnimatiumKeybinds.bootstrap()
        AnimatiumParticles.bootstrap()
        AnimatiumNetworking.bootstrap()
    }
}