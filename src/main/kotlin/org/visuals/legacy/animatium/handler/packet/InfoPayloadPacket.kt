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

package org.visuals.legacy.animatium.handler.packet

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import org.visuals.legacy.animatium.Animatium.location
import java.util.*

data class InfoPayloadPacket(val version: Double, val developmentVersion: String?) : CustomPacketPayload {
    companion object {
        val ID = CustomPacketPayload.Type<InfoPayloadPacket>(location("info"))

        val CODEC: StreamCodec<FriendlyByteBuf, InfoPayloadPacket> =
            CustomPacketPayload.codec(InfoPayloadPacket::write, InfoPayloadPacket::read)

        private fun read(buffer: FriendlyByteBuf): Nothing = throw UnsupportedOperationException()
    }

    private fun write(buffer: FriendlyByteBuf) {
        buffer.writeDouble(this.version)
        buffer.writeOptional(Optional.ofNullable(this.developmentVersion), FriendlyByteBuf::writeUtf)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = ID
}