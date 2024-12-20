package me.mixces.animatium

import me.mixces.animatium.config.AnimatiumConfig
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.MinecraftClient

class AnimatiumClient : ClientModInitializer {
    companion object {
        fun isLegacySupportedVersion(): Boolean {
            val client = MinecraftClient.getInstance()
            val networkHandler = client.getNetworkHandler() ?: return false
            val brand = networkHandler.getBrand()?.lowercase() ?: return false
            if (brand.contains("hypixel") || brand.contains("hygot") || brand.contains("1.8")) {
                return true
            } else {
                val serverInfo = networkHandler.getServerInfo()
                return serverInfo != null &&
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