package me.mixces.animatium;

import me.mixces.animatium.config.AnimatiumConfig;
import me.mixces.animatium.mixins.accessor.PlayerEntityAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Objects;

public class Animatium implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AnimatiumConfig.init("animatium", AnimatiumConfig.class);
    }

    public static EntityDimensions getLegacySneakingDimensions(PlayerEntity player, EntityPose defaultPose) {
        // Changes the sneak height to the one from <=1.13.2 on Hypixel & Loyisa & Bedwars Practice & Bridger Land
        EntityDimensions dimensions = Objects.requireNonNull(PlayerEntityAccessor.getPoseDimensions()).getOrDefault(isLegacySupportedVersion() ? null : defaultPose, PlayerEntity.STANDING_DIMENSIONS);
        if (((PlayerEntityAccessor) player).canChangeIntoPose$(EntityPose.STANDING)) {
            return dimensions.withEyeHeight(1.54F);
        } else {
            return dimensions;
        }
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

            if (brand.toLowerCase().contains("hypixel") || brand.toLowerCase().contains("hygot") || brand.contains("1.8")) {
                return true;
            } else {
                ServerInfo serverInfo = networkHandler.getServerInfo();
                return serverInfo != null &&
                        (serverInfo.address.contains("loyisa") ||
                                serverInfo.address.contains("bedwarspractice") ||
                                serverInfo.address.contains("bridger.land"));
            }
        }

        return false;
    }
}