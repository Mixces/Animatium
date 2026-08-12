package org.visuals.legacy.animatium.handler.config

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint
import net.minecraft.client.gui.screens.Screen
import org.visuals.legacy.animatium.config.AnimatiumConfig

@Entrypoint("modmenu")
class ModMenuIntegration : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> =
        { screen: Screen -> AnimatiumConfig.getConfigScreen(screen) }
}