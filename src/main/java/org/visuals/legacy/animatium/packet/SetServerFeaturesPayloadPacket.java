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

package org.visuals.legacy.animatium.packet;

import org.visuals.legacy.animatium.AnimatiumClient;
import org.visuals.legacy.animatium.util.enums.ServerFeature;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record SetServerFeaturesPayloadPacket(List<ServerFeature> features) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, SetServerFeaturesPayloadPacket> CODEC = CustomPacketPayload.codec(null, SetServerFeaturesPayloadPacket::read);
    public static final Type<SetServerFeaturesPayloadPacket> PAYLOAD_ID = new Type<>(AnimatiumClient.id("set_features"));

    private static SetServerFeaturesPayloadPacket read(FriendlyByteBuf buffer) {
        final int size = buffer.readVarInt();

        List<ServerFeature> features = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            ServerFeature.byId(buffer.readUtf()).ifPresent(features::add);
        }

        return new SetServerFeaturesPayloadPacket(features);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_ID;
    }
}