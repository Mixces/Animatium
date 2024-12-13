package me.mixces.animatium;

import me.mixces.animatium.config.AnimatiumConfig;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;

public class Animatium implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AnimatiumConfig.init("animatium", AnimatiumConfig.class);
    }

    public static boolean isLegacySupportedVersion() {
        if (AnimatiumConfig.oldSneakEyeHeight) {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();
            if (networkHandler == null) {
                return false;
            }

            String brand = networkHandler.getBrand();
            if (brand == null) {
                return false;
            }

            if ((brand.toLowerCase().contains("hypixel") || brand.toLowerCase().contains("hygot"))) {
                return true;
            } else if (brand.contains("1.8")) {
                return true;
            } else {
                ServerInfo serverInfo = networkHandler.getServerInfo();
                return serverInfo != null && serverInfo.address.contains("loyisa");
            }
        }

        return false;
    }
}