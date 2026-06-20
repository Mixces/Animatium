package org.visuals.legacy.animatium

import com.mojang.logging.LogUtils
import net.minecraft.SharedConstants
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.debug.DebugScreenEntries
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.Identifier
import org.visuals.legacy.animatium.config.AnimatiumConfig
import org.visuals.legacy.animatium.config.ConfigBundles
import org.visuals.legacy.animatium.mixins.accessor.GameRendererAccessor
import org.visuals.legacy.animatium.util.AnimatiumDebugEntry
import org.visuals.legacy.animatium.util.Utils
import org.visuals.legacy.animatium.util.config.ConfigUtil
import org.visuals.legacy.animatium.util.enums.ServerFeature
import org.visuals.legacy.animatium.util.rendering.lighting.LegacyDiffuseLighting
import java.util.*

object Animatium {
    private val LOGGER = LogUtils.getLogger()

    @JvmField
    val ENABLED_SERVER_FEATURES: EnumSet<ServerFeature> = EnumSet.noneOf(ServerFeature::class.java)

    @JvmStatic
    var enabled = true
        set(value) {
            field = value
            ConfigUtil.put(ConfigUtil.ENABLED_KEY, value)
        }

    @JvmStatic
    fun isEnabled(): Boolean {
        return enabled
    }

    @JvmStatic
    fun hasServerFeature(feature: ServerFeature): Boolean {
        if (Utils.isSingleplayer()) {
            for (entry in ConfigBundles.EXTRAS.entries()) {
                if (entry.name.equals(feature.getName())) {
                    return entry.value() as Boolean
                }
            }

            return false
        } else {
            return ENABLED_SERVER_FEATURES.contains(ServerFeature.ALL) || ENABLED_SERVER_FEATURES.contains(feature)
        }
    }

    @JvmStatic
    fun reload() {
        val minecraft = Minecraft.getInstance()
        minecraft.levelExtractor.allChanged()
        LegacyDiffuseLighting.refresh()
        (minecraft.gameRenderer as GameRendererAccessor).`animatium$setOverlayTexture`(OverlayTexture())
        Utils.reinitializeInventorySlots()
    }

    @JvmStatic
    fun location(path: String): Identifier {
        return Identifier.fromNamespaceAndPath(AnimatiumConstants.MOD_ID, path)
    }

    @JvmStatic
    fun initialize() {
        if (AnimatiumConstants.IS_DEVELOPMENT) {
            SharedConstants.IS_RUNNING_IN_IDE = true
        }

        AnimatiumConfig.load()
        try {
            ConfigUtil.load()
            LOGGER.info("Successfully loaded the animatium utility config!")
        } catch (_: Exception) {
            enabled = ConfigUtil.getBoolean(ConfigUtil.ENABLED_KEY)
            LOGGER.error("Failed to load animatium utility config, defaulting...")
        }

        DebugScreenEntries.register(AnimatiumDebugEntry.GROUP, AnimatiumDebugEntry())
    }
}