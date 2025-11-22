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
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.visuals.legacy.animatium.util.ConfigUtil;
import org.visuals.legacy.animatium.util.RenderUtils;

public class OnboardingScreen extends Screen {
    private final TitleScreen original;

    public OnboardingScreen(TitleScreen original) {
        super(Component.literal("Onboarding"));
        this.original = original;
    }

    @Override
    @SuppressWarnings({"DataFlowIssue"})
    protected void init() {
        this.original.init(this.minecraft, this.width, this.height);
        if (!ConfigUtil.bool("onboarding")) {
            this.minecraft.setScreen(this.original);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.original.render(guiGraphics, 0, 0, partialTick);
        guiGraphics.fill(0, 0, this.width, this.height, ARGB.color(0.67F, 0x000000));
        RenderUtils.drawScaledText(guiGraphics, this.font, "Welcome to Animatium Onboarding!", this.width / 2, this.height / 4, 1.67F);
    }

    @Override
    @SuppressWarnings({"DataFlowIssue"})
    public void onClose() {
        // ConfigUtil.put("onboarding", false);
    }
}
