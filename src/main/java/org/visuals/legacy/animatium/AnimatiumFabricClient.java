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

package org.visuals.legacy.animatium;

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.visuals.legacy.animatium.command.AnimatiumCommand;
import org.visuals.legacy.animatium.packet.ConfigDataPayloadPacket;
import org.visuals.legacy.animatium.packet.HelloPayloadPacket;
import org.visuals.legacy.animatium.packet.SetServerFeaturesPayloadPacket;

@Entrypoint
public final class AnimatiumFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Animatium.initialize();
		final ModContainer modContainer = FabricLoader.getInstance().getModContainer(AnimatiumConstants.MOD_ID).orElseThrow(() -> new RuntimeException("Mod container data could not be found for Animatium!"));
		ResourceManagerHelper.registerBuiltinResourcePack(Animatium.location("classic_textures"), modContainer, ResourcePackActivationType.NORMAL);
		ResourceManagerHelper.registerBuiltinResourcePack(Animatium.location("classic_water"), modContainer, ResourcePackActivationType.NORMAL);
		ModelLoadingPlugin.register(context -> context.addModel(AnimatiumConstants.FAST_GRASS_MODEL_KEY, SimpleUnbakedExtraModel.blockStateModel(AnimatiumConstants.FAST_GRASS_MODEL_LOCATION)));
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, context) -> dispatcher.register(AnimatiumCommand.create()));
		this.registerPayloads();
	}

	private void registerPayloads() {
		ClientLoginConnectionEvents.DISCONNECT.register((listener, client) -> Animatium.ENABLED_SERVER_FEATURES.clear());
		ClientConfigurationConnectionEvents.DISCONNECT.register((listener, client) -> Animatium.ENABLED_SERVER_FEATURES.clear());
		ClientPlayConnectionEvents.DISCONNECT.register((listener, client) -> Animatium.ENABLED_SERVER_FEATURES.clear());

		ClientPlayConnectionEvents.JOIN.register((listener, sender, client) -> {
			if (!client.isLocalServer()) {
				sender.sendPacket(AnimatiumConstants.getHelloPayload());
				sender.sendPacket(new ConfigDataPayloadPacket());
			}
		});

		PayloadTypeRegistry.configurationS2C().register(SetServerFeaturesPayloadPacket.PAYLOAD_ID, SetServerFeaturesPayloadPacket.CODEC);
		ClientConfigurationNetworking.registerGlobalReceiver(SetServerFeaturesPayloadPacket.PAYLOAD_ID, (payload, context) -> context.client().schedule(() -> {
			Animatium.ENABLED_SERVER_FEATURES.clear();
			Animatium.ENABLED_SERVER_FEATURES.addAll(payload.features());
		}));

		PayloadTypeRegistry.playS2C().register(SetServerFeaturesPayloadPacket.PAYLOAD_ID, SetServerFeaturesPayloadPacket.CODEC);
		ClientPlayNetworking.registerGlobalReceiver(SetServerFeaturesPayloadPacket.PAYLOAD_ID, (payload, context) -> context.client().schedule(() -> {
			Animatium.ENABLED_SERVER_FEATURES.clear();
			Animatium.ENABLED_SERVER_FEATURES.addAll(payload.features());
		}));

		PayloadTypeRegistry.playC2S().register(HelloPayloadPacket.PAYLOAD_ID, HelloPayloadPacket.CODEC);
		PayloadTypeRegistry.playC2S().register(ConfigDataPayloadPacket.PAYLOAD_ID, ConfigDataPayloadPacket.CODEC);
	}
}
