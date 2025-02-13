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
 */

package btw.mixces.animatium

import btw.mixces.animatium.command.AnimatiumCommand
import btw.mixces.animatium.config.AnimatiumConfig
import btw.mixces.animatium.packet.AnimatiumInfoPayloadPacket
import btw.mixces.animatium.packet.SetFeaturesPayloadPacket
import btw.mixces.animatium.util.Feature
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.ResourcePackActivationType
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.CoreShaders
import net.minecraft.client.renderer.ShaderDefines
import net.minecraft.client.renderer.ShaderProgram
import net.minecraft.resources.ResourceLocation
import java.io.File
import java.util.Optional

class AnimatiumClient : ClientModInitializer {
    companion object {
        // Settings
        @JvmStatic
        var enabled = true

        @JvmStatic
        val enabledFeatures = arrayListOf<Feature>()

        @JvmStatic
        var shouldReloadOverlayTexture = false

        @JvmStatic
        fun reloadOverlayTexture() {
            shouldReloadOverlayTexture = true
        }

        // Info
        // TODO/NOTE: Find a better way/cleanup
        val modContainer: ModContainer =
            FabricLoader.getInstance().getModContainer("animatium").orElseThrow { RuntimeException("Mod not found") }
        val VERSION_FULL = modContainer.metadata.version.friendlyString.split("-")

        val VERSION = VERSION_FULL[0].toDouble()
        val DEVELOPMENT_VERSION: Optional<String> = Optional.ofNullable(VERSION_FULL[1])

        @JvmStatic
        fun getInfoPayload(): AnimatiumInfoPayloadPacket {
            return AnimatiumInfoPayloadPacket(VERSION, DEVELOPMENT_VERSION)
        }

        // Other
        val stateFile = File(FabricLoader.getInstance().gameDir.toFile(), "animatium_state.txt")
        fun saveEnabledState() {
            if (!stateFile.exists()) {
                stateFile.createNewFile()
            }

            stateFile.writeText(enabled.toString())
        }

        fun loadEnabledState() {
            if (stateFile.exists()) {
                enabled = stateFile.readText() == "true"
            } else {
                saveEnabledState()
            }
        }

        // Shaders
        val renderTypeLegacyGlint = ShaderProgram(
            ResourceLocation.fromNamespaceAndPath("animatium", "core/rendertype_legacy_glint"),
            DefaultVertexFormat.POSITION_TEX,
            ShaderDefines.EMPTY
        )

        @JvmStatic
        fun setGlintColor(red: Float, green: Float, blue: Float) {
            val minecraft = Minecraft.getInstance()
            minecraft.execute {
                val shaderManager = Minecraft.getInstance().shaderManager ?: return@execute
                val compiledShader = shaderManager.getProgram(renderTypeLegacyGlint) ?: return@execute
                val glintColorUniform = compiledShader.getUniform("GlintColor") ?: return@execute
                glintColorUniform.set(red, green, blue)
            }
        }

        init {
            CoreShaders.getProgramsToPreload().add(renderTypeLegacyGlint)
        }
    }

    override fun onInitializeClient() {
        AnimatiumConfig.load()
        loadEnabledState()

        // Packs
        ResourceManagerHelper.registerBuiltinResourcePack(
            ResourceLocation.parse("animatium:classic_textures"),
            modContainer,
            ResourcePackActivationType.NORMAL
        )

        // Commands
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, registryAccess ->
            AnimatiumCommand.register(dispatcher)
        }

        // Packets
        ClientLoginConnectionEvents.DISCONNECT.register { packet, client -> enabledFeatures.clear() }
        ClientConfigurationConnectionEvents.DISCONNECT.register { packet, client -> enabledFeatures.clear() }
        ClientPlayConnectionEvents.DISCONNECT.register { packet, client -> enabledFeatures.clear() }

        PayloadTypeRegistry.playC2S().register(AnimatiumInfoPayloadPacket.PAYLOAD_ID, AnimatiumInfoPayloadPacket.CODEC)
        PayloadTypeRegistry.playS2C().register(SetFeaturesPayloadPacket.PAYLOAD_ID, SetFeaturesPayloadPacket.CODEC)
        ClientPlayNetworking.registerGlobalReceiver(SetFeaturesPayloadPacket.PAYLOAD_ID) { payload, context ->
            context.client().execute {
                enabledFeatures.clear()
                for (feature in payload.features) {
                    enabledFeatures.add(feature)
                }
            }
        }
    }
}