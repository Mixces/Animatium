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
import org.visuals.legacy.animatium.config.ConfigBundles;
import org.visuals.legacy.animatium.util.config.EntryBundle;

// TODO: Rewrite/ass
public record ConfigDataPayloadPacket() implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, ConfigDataPayloadPacket> CODEC = CustomPacketPayload.codec(ConfigDataPayloadPacket::write, null);
	public static final CustomPacketPayload.Type<ConfigDataPayloadPacket> PAYLOAD_ID = new CustomPacketPayload.Type<>(Animatium.location("config_data"));

	private void write(FriendlyByteBuf buffer) {
		buffer.writeByte(ConfigBundles.ALL.length);
		for (final EntryBundle bundle : ConfigBundles.ALL) {
			writeBundle(buffer, bundle);
		}
	}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return PAYLOAD_ID;
	}

	private void writeBundle(final FriendlyByteBuf buffer, final EntryBundle bundle) {
		buffer.writeVarInt(bundle.entries().size());
		for (final var entry : bundle.entries()) {
			buffer.writeUtf(entry.name);
			buffer.writeEnum(entry.type);
			final Object value = entry.value();
			switch (entry.type) {
				case BOOLEAN -> buffer.writeBoolean((boolean) value);
				case FLOAT -> buffer.writeFloat((float) value);
				case ENUM -> buffer.writeEnum((Enum<?>) value);
				default -> throw new RuntimeException("Missing entry for type " + entry.type);
			}
		}
	}
}