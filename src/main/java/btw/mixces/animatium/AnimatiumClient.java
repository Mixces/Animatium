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

package btw.mixces.animatium;

import btw.mixces.animatium.command.AnimatiumCommand;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.mixins.accessor.RenderPipelinesAccessor;
import btw.mixces.animatium.packet.AnimatiumInfoPayloadPacket;
import btw.mixces.animatium.packet.RequestInfoPayloadPacket;
import btw.mixces.animatium.packet.SetFeaturesPayloadPacket;
import btw.mixces.animatium.util.Feature;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class AnimatiumClient implements ClientModInitializer {
    // Settings
    public static boolean ENABLED = true;
    public static List<Feature> ENABLED_FEATURES = new ArrayList<>();

    // Info
    private static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer("animatium").orElseThrow(() -> new RuntimeException("Mod not found"));
    private static final String[] VERSION_PARTS = MOD_CONTAINER.getMetadata().getVersion().getFriendlyString().split("-");
    public static Double VERSION = Double.parseDouble(VERSION_PARTS[0]);
    public static @Nullable String DEVELOPMENT_VERSION = VERSION_PARTS[1];

    public static AnimatiumInfoPayloadPacket getInfoPayload() {
        return new AnimatiumInfoPayloadPacket(VERSION, DEVELOPMENT_VERSION);
    }

    public static ResourceLocation getPath(String path) {
        return ResourceLocation.fromNamespaceAndPath("animatium", path);
    }

    // Shaders
    public static final RenderPipeline LEGACY_SKY_PIPELINE = RenderPipelinesAccessor.registerPipeline(
            RenderPipeline.builder(RenderPipelines.MATRICES_COLOR_FOG_SNIPPET)
                    .withLocation("pipeline/legacy_sky")
                    .withVertexShader("core/position")
                    .withFragmentShader("core/position")
                    .withDepthWrite(false)
                    .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
                    .build());

    public static final RenderPipeline LEGACY_GLINT_PIPELINE = RenderPipelinesAccessor.registerPipeline(
            RenderPipeline.builder(RenderPipelines.MATRICES_COLOR_SNIPPET, RenderPipelines.FOG_NO_COLOR_SNIPPET)
                    .withLocation(getPath("pipeline/legacy_glint"))
                    .withVertexShader(getPath("core/legacy_glint"))
                    .withFragmentShader(getPath("core/legacy_glint"))
                    .withColorWrite(true, false)
                    .withCull(true)
                    .withBlend(BlendFunction.GLINT)
                    .withDepthTestFunction(DepthTestFunction.EQUAL_DEPTH_TEST)
                    .withSampler("Sampler0")
                    .withUniform("GlintColor", UniformType.VEC3)
                    .withUniform("TextureMat", UniformType.MATRIX4X4)
                    .withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS)
                    .build());

    // Config State
    private static final File STATE_FILE = new File(FabricLoader.getInstance().getGameDir().toFile(), "animatium_state.txt");

    public static void loadEnabledState() throws IOException {
        if (STATE_FILE.exists()) {
            ENABLED = Files.readString(STATE_FILE.toPath()).equals("true");
        } else {
            if (!saveEnabledState()) {
                System.err.println("Failed to save enabled state...");
            }
        }
    }

    public static boolean saveEnabledState() {
        boolean success = true;
        try {
            if (!STATE_FILE.exists()) {
                success = STATE_FILE.createNewFile();
            }

            if (success) {
                Files.writeString(STATE_FILE.toPath(), String.valueOf(ENABLED));
            }
        } catch (Exception exception) {
            success = false;
        }

        return success;
    }

    // Other
    public static boolean isEnabled() {
        return ENABLED;
    }

    @Override
    public void onInitializeClient() {
        AnimatiumConfig.load();
        try {
            loadEnabledState();
        } catch (IOException e) {
            ENABLED = true;
            System.err.println("Failed to load enabled state, defaulting to true...");
        }

        // Packs
        ResourceManagerHelper.registerBuiltinResourcePack(ResourceLocation.parse("animatium:classic_textures"), MOD_CONTAINER, ResourcePackActivationType.DEFAULT_ENABLED);

        // Commands
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            if (environment.includeIntegrated) {
                dispatcher.register(AnimatiumCommand.create());
            }
        });

        // Packets
        ClientLoginConnectionEvents.DISCONNECT.register((packet, client) -> ENABLED_FEATURES.clear());
        ClientConfigurationConnectionEvents.DISCONNECT.register((packet, client) -> ENABLED_FEATURES.clear());
        ClientPlayConnectionEvents.DISCONNECT.register((packet, client) -> ENABLED_FEATURES.clear());

        PayloadTypeRegistry.playC2S().register(AnimatiumInfoPayloadPacket.PAYLOAD_ID, AnimatiumInfoPayloadPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(SetFeaturesPayloadPacket.PAYLOAD_ID, SetFeaturesPayloadPacket.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(SetFeaturesPayloadPacket.PAYLOAD_ID, (payload, context) -> context.client().schedule(() -> {
            ENABLED_FEATURES.clear();
            ENABLED_FEATURES.addAll(payload.features());
        }));

        PayloadTypeRegistry.playS2C().register(RequestInfoPayloadPacket.PAYLOAD_ID, RequestInfoPayloadPacket.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(RequestInfoPayloadPacket.PAYLOAD_ID, (payload, context) -> context.client().schedule(() -> ClientPlayNetworking.send(AnimatiumClient.getInfoPayload())));
    }
}
