package me.mixces.animatium

import me.mixces.animatium.config.AnimatiumConfig
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.MinecraftClient

class AnimatiumClient : ClientModInitializer {
    companion object {
        fun isLegacySupportedVersion(): Boolean {
            val client = MinecraftClient.getInstance()
            val networkHandler = client.networkHandler ?: return false
            val brand = networkHandler.getBrand()?.lowercase() ?: return false
            return if (brand.contains("hypixel") || brand.contains("hygot") || brand.contains("1.8")) {
                true
            } else {
                val serverInfo = networkHandler.getServerInfo()
                serverInfo != null &&
                        (serverInfo.address.contains("loyisa") ||
                                serverInfo.address.contains("bedwarspractice") ||
                                serverInfo.address.contains("bridger.land"))
            }
        }
    }

    override fun onInitializeClient() {
        AnimatiumConfig.load()
    }
}