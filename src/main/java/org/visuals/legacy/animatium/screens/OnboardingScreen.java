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

package org.visuals.legacy.animatium.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.visuals.legacy.animatium.util.RenderUtils;
import org.visuals.legacy.animatium.util.config.ConfigUtil;
import org.visuals.legacy.animatium.util.config.Version;
import org.visuals.legacy.animatium.util.config.VersionUtil;

public class OnboardingScreen extends Screen {
    private final TitleScreen original;
    private Version version = Version.MODERN;

    private Button v1_7Button = null;
    private Button v1_8Button = null;
    private Button modernButton = null;

    public OnboardingScreen(TitleScreen original) {
        super(Component.literal("Onboarding"));
        this.original = original;
    }

    @Override
    @SuppressWarnings({"DataFlowIssue"})
    protected void init() {
        if (!ConfigUtil.bool("onboarding")) {
            this.minecraft.setScreen(this.original);
            return;
        }

        this.original.init(this.minecraft, this.width, this.height);

        final int buttonWidth = 120;
        this.v1_7Button = this.addRenderableWidget(Button.builder(Component.literal("1.7"), button -> this.version = Version.V1_7)
                .bounds(((this.width / 2) - (buttonWidth / 2) - (buttonWidth + Button.DEFAULT_SPACING)), this.height / 2, buttonWidth, Button.DEFAULT_HEIGHT)
                .build());
        this.v1_8Button = this.addRenderableWidget(Button.builder(Component.literal("1.8"), button -> this.version = Version.V1_8)
                .bounds((this.width / 2) - (buttonWidth / 2), this.height / 2, buttonWidth, Button.DEFAULT_HEIGHT)
                .build());
        this.modernButton = this.addRenderableWidget(Button.builder(Component.literal("Modern"), button -> this.version = Version.MODERN)
                .bounds(((this.width / 2) - (buttonWidth / 2) + (buttonWidth + Button.DEFAULT_SPACING)), this.height / 2, buttonWidth, Button.DEFAULT_HEIGHT)
                .build());
        updateVersionButtonState();

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> {
            ConfigUtil.put("onboarding", false);
            VersionUtil.setVersion(this.version);
            this.minecraft.setScreen(this.original);
        }).pos((this.width / 2) - (Button.DEFAULT_WIDTH / 2), (int) (this.height / 1.2F)).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.original.render(guiGraphics, 0, 0, partialTick);

        guiGraphics.fill(0, 0, this.width, this.height, ARGB.color(0.72F, 0x000000));
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        RenderUtils.drawScaledText(guiGraphics, this.font, "Welcome to Animatium Onboarding!", this.width / 2, this.height / 4, 2.0F);
        guiGraphics.drawCenteredString(this.font, "Hello! Thank you for downloading Animatium!", this.width / 2, (int) (this.height / 2.8), 0xFFD6D6D6);
        guiGraphics.drawCenteredString(this.font, "Please select the version of visuals you would like to use!", this.width / 2, (int) (this.height / 2.4), 0xFFD6D6D6);

        guiGraphics.drawCenteredString(this.font, "NOTE: If you have already went through this,", this.width / 2, (int) (this.height / 1.4F), 0xFFFFA600);
        guiGraphics.drawCenteredString(this.font, "ask for help in the discord before continuing!", this.width / 2, (int) (this.height / 1.3F), 0xFFFFA600);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
        if (this.v1_7Button.mouseClicked(event, isDoubleClick) || this.v1_8Button.mouseClicked(event, isDoubleClick) || this.modernButton.mouseClicked(event, isDoubleClick)) {
            updateVersionButtonState();
            return true;
        } else {
            return super.mouseClicked(event, isDoubleClick);
        }
    }

    private void updateVersionButtonMessage(Button button) {
        button.setMessage(button.getMessage().copy().withColor(button.isActive() ? ARGB.white(1.0F) : 0xFFFFFFA0));
    }

    private void updateVersionButtonState() {
        this.v1_7Button.active = this.version != Version.V1_7;
        updateVersionButtonMessage(this.v1_7Button);

        this.v1_8Button.active = this.version != Version.V1_8;
        updateVersionButtonMessage(this.v1_8Button);

        this.modernButton.active = this.version != Version.MODERN;
        updateVersionButtonMessage(this.modernButton);
    }
}
