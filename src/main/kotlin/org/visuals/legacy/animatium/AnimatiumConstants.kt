package org.visuals.legacy.animatium

import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey
import net.minecraft.client.renderer.block.dispatch.BlockStateModel
import org.visuals.legacy.animatium.packet.InfoPayloadPacket
import java.lang.Boolean.parseBoolean
import java.lang.Double.parseDouble

object AnimatiumConstants {
    const val MOD_ID = "@MODID@";
    const val DEVELOPMENT_VERSION = "@COMMIT@";

    @JvmField
    val VERSION = parseDouble("@VERSION@");

    @JvmField
    val IS_DEVELOPMENT = parseBoolean("@DEVELOPMENT@");

    @JvmField
    val FAST_GRASS_MODEL_LOCATION = Animatium.location("block/fast_grass_block");

    @JvmField
    val FAST_GRASS_MODEL_KEY: ExtraModelKey<BlockStateModel> =
        ExtraModelKey.create(FAST_GRASS_MODEL_LOCATION::toString);

    @JvmStatic
    fun getInfoPayload(): InfoPayloadPacket {
        return InfoPayloadPacket(VERSION, if (IS_DEVELOPMENT) DEVELOPMENT_VERSION else null);
    }
}