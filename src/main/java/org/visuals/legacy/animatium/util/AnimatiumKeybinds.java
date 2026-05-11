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

package org.visuals.legacy.animatium.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.screens.ModScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AnimatiumKeybinds {
    private static final List<Binding> BINDINGS = new ArrayList<>();
    public static final KeyMapping.Category ANIMATIUM_CATEGORY = new KeyMapping.Category(Animatium.location("common"));
    public static final KeyMapping MOD_SCREEN = create("Open Mod Configuration", GLFW.GLFW_KEY_BACKSLASH, client -> client.gui.setScreen(new ModScreen(client.gui.screen())));

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(AnimatiumKeybinds::tick);
        for (final Binding binding : BINDINGS) {
            KeyMappingHelper.registerKeyMapping(binding.mapping);
        }
    }

    public static void tick(final Minecraft minecraft) {
        for (final Binding binding : BINDINGS) {
            if (binding.mapping.consumeClick()) {
                binding.onClick.accept(minecraft);
            }
        }
    }

    private static KeyMapping create(final String name, final int keybind, final Consumer<Minecraft> onClick) {
        final KeyMapping mapping = new KeyMapping(name, keybind, ANIMATIUM_CATEGORY);
        BINDINGS.add(new Binding(mapping, onClick));
        return mapping;
    }

    private record Binding(KeyMapping mapping, Consumer<Minecraft> onClick) {
    }
}
