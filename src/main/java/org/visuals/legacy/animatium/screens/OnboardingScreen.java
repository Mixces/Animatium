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

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.NonNull;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.config.ConfigUtil;
import org.visuals.legacy.animatium.util.config.Version;
import org.visuals.legacy.animatium.util.rendering.RenderUtils;

public class OnboardingScreen extends Screen {
    private final Screen original;
    private final boolean accessedViaCommands;
    private Version version = Version.MODERN;

    private Button v1_7Button = null;
    private Button v1_8Button = null;
    private Button modernButton = null;

    public OnboardingScreen(final Screen original, final boolean accessedViaCommands) {
        super(Component.literal("Onboarding"));
        this.original = original;
        this.accessedViaCommands = accessedViaCommands;
    }

    @Override
    protected void init() {
        if (!ConfigUtil.getBoolean(ConfigUtil.ONBOARDING_KEY) && !this.accessedViaCommands) {
            this.minecraft.gui.setScreen(this.original);
            return;
        }

        if (this.original != null) {
            this.original.init(this.width, this.height);
        }

        this.version = ConfigUtil.getEnum(ConfigUtil.VERSION_KEY, Version.MODERN);

        final int buttonWidth = 100;
        this.v1_7Button = this.addRenderableWidget(Button.builder(Component.literal("1.7"), button -> this.version = Version.V1_7)
                .bounds(((this.width / 2) - (buttonWidth / 2) - (buttonWidth + Button.DEFAULT_SPACING)), this.height / 2, buttonWidth, Button.DEFAULT_HEIGHT)
                .build());
        this.v1_8Button = this.addRenderableWidget(Button.builder(Component.literal("1.8"), button -> this.version = Version.V1_8)
                .bounds((this.width / 2) - (buttonWidth / 2), this.height / 2, buttonWidth, Button.DEFAULT_HEIGHT)
                .build());
        this.modernButton = this.addRenderableWidget(Button.builder(Component.literal("Modern"), button -> this.version = Version.MODERN)
                .bounds(((this.width / 2) - (buttonWidth / 2) + (buttonWidth + Button.DEFAULT_SPACING)), this.height / 2, buttonWidth, Button.DEFAULT_HEIGHT)
                .build());
        this.updateVersionButtonState();

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> {
            ConfigUtil.put(ConfigUtil.ONBOARDING_KEY, false);
            ConfigUtil.put(ConfigUtil.VERSION_KEY, this.version.name());
            this.version.apply();
            AnimatiumConfig.save();
            this.minecraft.gui.setScreen(this.original);
        }).pos((this.width / 2) - (Button.DEFAULT_WIDTH / 2), (int) (this.height / 1.2F)).tooltip(Tooltip.create(Component.literal("WARNING! THIS WILL RESET ALL YOUR SETTINGS").withStyle(ChatFormatting.RED))).build());
    }

    @Override
    public void extractRenderState(final @NonNull GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float tickDelta) {
        if (this.original != null) {
            this.original.extractRenderState(graphics, -999, -999, tickDelta);
        }

        graphics.fill(0, 0, this.width, this.height, ARGB.color(this.accessedViaCommands ? 0.35F : 0.72F, 0));
        super.extractRenderState(graphics, mouseX, mouseY, tickDelta);

        RenderUtils.drawScaledText(graphics, this.font, "Welcome to Animatium Onboarding!", this.width / 2, this.height / 4, 2.0F);
        graphics.centeredText(this.font, "Hello! Thank you for downloading Animatium!", this.width / 2, (int) (this.height / 2.8), 0xFFD6D6D6);
        graphics.centeredText(this.font, "Please select the version of visuals you would like to use!", this.width / 2, (int) (this.height / 2.4), 0xFFD6D6D6);
        if (!accessedViaCommands) {
            graphics.centeredText(this.font, "NOTE: If you have already went through this,", this.width / 2, (int) (this.height / 1.4F), 0xFFFFA600);
            graphics.centeredText(this.font, "ask for help in the discord before continuing!", this.width / 2, (int) (this.height / 1.3F), 0xFFFFA600);
        } else {
            graphics.centeredText(this.font, "Current saved version: " + ConfigUtil.getEnum(ConfigUtil.VERSION_KEY, this.version), this.width / 2, (int) (this.height / 1.3F), ARGB.white(0.625F));
        }
    }

    @Override
    public boolean mouseClicked(final @NonNull MouseButtonEvent event, final boolean doubleClick) {
        if (this.v1_7Button.mouseClicked(event, doubleClick) || this.v1_8Button.mouseClicked(event, doubleClick) || this.modernButton.mouseClicked(event, doubleClick)) {
            this.updateVersionButtonState();
            return true;
        } else {
            return super.mouseClicked(event, doubleClick);
        }
    }

    @Override
    public void onClose() {
        // NOTE: Only allow escaping if you used the ``/animatium onboarding`` command
        if (this.original == null) {
            ConfigUtil.put(ConfigUtil.ONBOARDING_KEY, false);
            super.onClose();
        }
    }

    private void updateVersionButtonMessage(final Button button, final Version version) {
        button.setMessage(button.getMessage()
                .copy()
                .withColor(button.isActive() ? (version == this.version ? ARGB.color(1.0F, 0x00FF00) : ARGB.white(1.0F)) : 0xFFFFFFA0));
    }

    private void updateVersionButtonState() {
        this.v1_7Button.active = this.version != Version.V1_7;
        this.updateVersionButtonMessage(this.v1_7Button, Version.V1_7);

        this.v1_8Button.active = this.version != Version.V1_8;
        this.updateVersionButtonMessage(this.v1_8Button, Version.V1_8);

        this.modernButton.active = this.version != Version.MODERN;
        this.updateVersionButtonMessage(this.modernButton, Version.MODERN);
    }
}
