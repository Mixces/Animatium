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

package btw.mixces.animatium;

import btw.mixces.animatium.command.AnimatiumCommand;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.packet.AnimatiumInfoPayloadPacket;
import btw.mixces.animatium.packet.RequestInfoPayloadPacket;
import btw.mixces.animatium.packet.SetFeaturesPayloadPacket;
import btw.mixces.animatium.util.AnimatiumDebugEntry;
import btw.mixces.animatium.util.ConfigUtil;
import btw.mixces.animatium.util.enums.Feature;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class AnimatiumClient implements ClientModInitializer {
    // Settings
    public static final String MOD_ID = "animatium";
    public static boolean ENABLED = true;
    public static boolean SHOULD_RELOAD_OVERLAY_TEXTURE = true;
    public static List<Feature> ENABLED_FEATURES = new ArrayList<>();

    // Info
    private static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(() -> new RuntimeException("Mod not found"));
    private static final String[] VERSION_PARTS = MOD_CONTAINER.getMetadata().getVersion().getFriendlyString().split("-");
    public static Double VERSION = Double.parseDouble(VERSION_PARTS[0]);
    public static @Nullable String DEVELOPMENT_VERSION = VERSION_PARTS[1];

    public static AnimatiumInfoPayloadPacket getInfoPayload() {
        return new AnimatiumInfoPayloadPacket(VERSION, DEVELOPMENT_VERSION);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitializeClient() {
        AnimatiumConfig.load();
        try {
            ConfigUtil.loadState();
        } catch (IOException e) {
            ENABLED = true;
            System.err.println("Failed to load enabled state, defaulting to true...");
        }

        ResourceManagerHelper.registerBuiltinResourcePack(id("classic_textures"), MOD_CONTAINER, ResourcePackActivationType.DEFAULT_ENABLED);
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, context) -> dispatcher.register(AnimatiumCommand.create()));
        DebugScreenEntries.register(AnimatiumDebugEntry.GROUP, new AnimatiumDebugEntry());
        registerPayloads();
    }

    private void registerPayloads() {
        ClientLoginConnectionEvents.DISCONNECT.register((packet, client) -> ENABLED_FEATURES.clear());
        ClientConfigurationConnectionEvents.DISCONNECT.register((packet, client) -> ENABLED_FEATURES.clear());
        ClientPlayConnectionEvents.DISCONNECT.register((packet, client) -> ENABLED_FEATURES.clear());

        PayloadTypeRegistry.configurationS2C().register(SetFeaturesPayloadPacket.PAYLOAD_ID, SetFeaturesPayloadPacket.CODEC);
        ClientConfigurationNetworking.registerGlobalReceiver(SetFeaturesPayloadPacket.PAYLOAD_ID, (payload, context) -> context.client().schedule(() -> {
            ENABLED_FEATURES.clear();
            ENABLED_FEATURES.addAll(payload.features());
        }));

        PayloadTypeRegistry.playS2C().register(SetFeaturesPayloadPacket.PAYLOAD_ID, SetFeaturesPayloadPacket.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(SetFeaturesPayloadPacket.PAYLOAD_ID, (payload, context) -> context.client().schedule(() -> {
            ENABLED_FEATURES.clear();
            ENABLED_FEATURES.addAll(payload.features());
        }));

        PayloadTypeRegistry.playC2S().register(AnimatiumInfoPayloadPacket.PAYLOAD_ID, AnimatiumInfoPayloadPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(RequestInfoPayloadPacket.PAYLOAD_ID, RequestInfoPayloadPacket.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(RequestInfoPayloadPacket.PAYLOAD_ID, (payload, context) -> context.client().schedule(() -> ClientPlayNetworking.send(AnimatiumClient.getInfoPayload())));
    }
}
