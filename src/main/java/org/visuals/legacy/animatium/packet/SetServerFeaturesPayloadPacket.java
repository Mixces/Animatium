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

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.util.enums.ServerFeature;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;

public record SetServerFeaturesPayloadPacket(EnumSet<ServerFeature> features) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, SetServerFeaturesPayloadPacket> CODEC = CustomPacketPayload.codec(null, SetServerFeaturesPayloadPacket::read);
    public static final Type<SetServerFeaturesPayloadPacket> PAYLOAD_ID = new Type<>(Animatium.location("set_features"));

    private static SetServerFeaturesPayloadPacket read(FriendlyByteBuf buffer) {
        buffer.markReaderIndex();
        final int size = buffer.readVarInt();
        buffer.resetReaderIndex();
        // TODO/NOTE: This is very hacky and stupid rah, hope it doesn't cause anyone issues
        if (size > 0 && size <= ServerFeature.VALUES.length) {
            // v0 @Deprecated
            return new SetServerFeaturesPayloadPacket(readV0(buffer));
        } else {
            // v1
            return new SetServerFeaturesPayloadPacket(readV1(buffer));
        }
    }

    @Deprecated
    private static EnumSet<ServerFeature> readV0(final FriendlyByteBuf buffer) {
        System.out.println("Server sent features using v0 api! This is deprecated and will be removed in the future!");
        final EnumSet<ServerFeature> enumSet = EnumSet.noneOf(ServerFeature.class);

        final int size = buffer.readVarInt();
        for (int i = 0; i < size; ++i) {
            final String name = buffer.readUtf();
            final Optional<ServerFeature> optionalServerFeature = Arrays.stream(ServerFeature.VALUES).filter(feature -> feature.getId().equals(name)).findFirst();
            optionalServerFeature.ifPresent(enumSet::add);
        }

        return enumSet;
    }

    // TODO/NOTE: Servers should instead use this as v0 will be removed in the future
    private static EnumSet<ServerFeature> readV1(final FriendlyByteBuf buffer) {
        return buffer.readEnumSet(ServerFeature.class);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_ID;
    }
}