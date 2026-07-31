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

package org.visuals.legacy.animatium.handler.screen

import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.util.ARGB
import org.visuals.legacy.animatium.handler.rendering.drawScaledText
import org.visuals.legacy.animatium.util.config.GeneralConfigUtil
import org.visuals.legacy.animatium.util.config.PresetVersion

class OnboardingScreen(private val original: Screen?, private val accessedViaCommands: Boolean) :
    Screen(Component.literal("Onboarding")) {
    private var v1_7Button: Button? = null
    private var v1_8Button: Button? = null
    private var modernButton: Button? = null

    private var presetVersion = PresetVersion.VANILLA

    override fun init() {
        if (!GeneralConfigUtil.getBoolean(GeneralConfigUtil.ONBOARDING_KEY) && !this.accessedViaCommands) {
            this.minecraft.setScreen(this.original)
            return
        }

        this.original?.init(this.width, this.height)
        this.presetVersion = GeneralConfigUtil.getEnum(GeneralConfigUtil.PRESET_VERSION_KEY, PresetVersion.VANILLA)

        val buttonWidth = 100
        this.v1_7Button = this.addRenderableWidget(
            Button.builder(Component.literal("1.7")) { _: Button -> this.presetVersion = PresetVersion.V1_7 }
                .bounds(
                    ((this.width / 2) - (buttonWidth / 2) - (buttonWidth + Button.DEFAULT_SPACING)),
                    this.height / 2,
                    buttonWidth,
                    Button.DEFAULT_HEIGHT
                )
                .build()
        )
        this.v1_8Button = this.addRenderableWidget(
            Button.builder(Component.literal("1.8")) { _: Button -> this.presetVersion = PresetVersion.V1_8 }
                .bounds((this.width / 2) - (buttonWidth / 2), this.height / 2, buttonWidth, Button.DEFAULT_HEIGHT)
                .build())
        this.modernButton = this.addRenderableWidget(
            Button.builder(Component.literal("Modern")) { _: Button -> this.presetVersion = PresetVersion.VANILLA }
                .bounds(
                    ((this.width / 2) - (buttonWidth / 2) + (buttonWidth + Button.DEFAULT_SPACING)),
                    this.height / 2,
                    buttonWidth,
                    Button.DEFAULT_HEIGHT
                )
                .build()
        )
        this.updateVersionButtonState()

        this.addRenderableWidget(
            Button.builder(CommonComponents.GUI_DONE) { _: Button ->
                GeneralConfigUtil.put(GeneralConfigUtil.ONBOARDING_KEY, false)
                GeneralConfigUtil.put(GeneralConfigUtil.PRESET_VERSION_KEY, this.presetVersion.name)

                this.presetVersion.apply()
                this.minecraft.gui.chat.addMessage(
                    Component.literal("Applied preset " + this@OnboardingScreen.presetVersion.name + "!").withColor(-0xff0100)
                )
                this.minecraft.setScreen(this.original)
            }
                .pos((this.width / 2) - (Button.DEFAULT_WIDTH / 2), (this.height / 1.2f).toInt())
                .tooltip(
                    Tooltip.create(
                        Component.literal("WARNING! THIS WILL RESET ALL YOUR SETTINGS").withStyle(ChatFormatting.RED)
                    )
                ).build()
        )
    }

    override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, tickDelta: Float) {
        this.original?.render(graphics, -999, -999, tickDelta)

        graphics.fill(0, 0, this.width, this.height, ARGB.color(if (this.accessedViaCommands) 0.35F else 0.72F, 0))
        super.render(graphics, mouseX, mouseY, tickDelta)

        graphics.drawScaledText(
            this.font,
            "Welcome to Animatium Onboarding!",
            this.width / 2,
            this.height / 4,
            2.0F
        )
        graphics.drawCenteredString(
            this.font,
            "Hello! Thank you for downloading Animatium!",
            this.width / 2,
            (this.height / 2.8).toInt(),
            ARGB.white(0xD6D6D6)
        )
        graphics.drawCenteredString(
            this.font,
            "Please select the version of visuals you would like to use!",
            this.width / 2,
            (this.height / 2.4).toInt(),
            ARGB.white(0xD6D6D6)
        )

        if (!this.accessedViaCommands) {
            graphics.drawCenteredString(
                this.font,
                "NOTE: If you have already went through this,",
                this.width / 2,
                (this.height / 1.4F).toInt(),
                ARGB.white(0xFFA600)
            )
            graphics.drawCenteredString(
                this.font,
                "ask for help in the discord before continuing!",
                this.width / 2,
                (this.height / 1.3F).toInt(),
                ARGB.white(0xFFA600)
            )
        } else {
            graphics.drawCenteredString(
                this.font,
                "Current saved version: " + GeneralConfigUtil.getEnum(
                    GeneralConfigUtil.PRESET_VERSION_KEY,
                    this.presetVersion
                ),
                this.width / 2,
                (this.height / 1.3F).toInt(),
                ARGB.white(0.625F)
            )
        }
    }

    override fun mouseClicked(event: MouseButtonEvent, doubleClick: Boolean): Boolean =
        if (this.v1_7Button!!.mouseClicked(event, doubleClick) ||
            this.v1_8Button!!.mouseClicked(event, doubleClick) ||
            this.modernButton!!.mouseClicked(event, doubleClick)
        ) {
            this.updateVersionButtonState()
            true
        } else {
            super.mouseClicked(event, doubleClick)
        }

    override fun onClose() {
        // NOTE: Only allow escaping if you used the ``/animatium onboarding`` command
        if (this.original == null) {
            GeneralConfigUtil.put(GeneralConfigUtil.ONBOARDING_KEY, false)
            super.onClose()
        }
    }

    private fun updateVersionButtonState() {
        this.v1_7Button?.active = this.presetVersion != PresetVersion.V1_7
        this.v1_8Button?.active = this.presetVersion != PresetVersion.V1_8
        this.modernButton?.active = this.presetVersion != PresetVersion.VANILLA
    }
}