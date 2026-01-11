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

package org.visuals.legacy.animatium.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.mixins.accessor.GameRendererAccessor;
import org.visuals.legacy.animatium.screens.OnboardingScreen;
import org.visuals.legacy.animatium.util.config.ConfigUtil;

import java.util.Random;

public class AnimatiumCommand implements Command<FabricClientCommandSource> {
	public static LiteralArgumentBuilder<FabricClientCommandSource> create() {
		final LiteralArgumentBuilder<FabricClientCommandSource> command = ClientCommandManager.literal("animatium").executes(new AnimatiumCommand());

		command.then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("on").executes((context) -> {
			final FabricClientCommandSource source = context.getSource();
			if (Animatium.isEnabled()) {
				source.sendFeedback(Component.literal("Mod is already enabled!").withStyle(ChatFormatting.YELLOW));
			} else {
				source.sendFeedback(Component.literal("Mod enabled.").withStyle(ChatFormatting.GREEN));
				Animatium.setEnabled(true);

				final Minecraft minecraft = source.getClient();
				((GameRendererAccessor) minecraft.gameRenderer).animatium$setOverlayTexture(new OverlayTexture());
				minecraft.reloadResourcePacks();

				if (!ConfigUtil.save()) {
					System.err.println("Failed to save animatium utility config...");
				}
			}

			return Command.SINGLE_SUCCESS;
		}));

		command.then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("off").executes((context) -> {
			final FabricClientCommandSource source = context.getSource();
			if (!Animatium.isEnabled()) {
				source.sendFeedback(Component.literal("Mod is already disabled!").withStyle(ChatFormatting.YELLOW));
			} else {
				source.sendFeedback(Component.literal("Mod disabled.").withStyle(ChatFormatting.RED));
				Animatium.setEnabled(false);

				final Minecraft minecraft = source.getClient();
				((GameRendererAccessor) minecraft.gameRenderer).animatium$setOverlayTexture(new OverlayTexture());
				minecraft.reloadResourcePacks();

				ConfigUtil.save();
			}

			return Command.SINGLE_SUCCESS;
		}));

		command.then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("onboarding").executes((context) -> {
			ConfigUtil.put("onboarding", true);

			final Minecraft minecraft = context.getSource().getClient();
			minecraft.schedule(() -> minecraft.setScreen(new OnboardingScreen(minecraft.screen)));

			return Command.SINGLE_SUCCESS;
		}));

		return command;
	}

	@Override
	public int run(CommandContext<FabricClientCommandSource> context) {
		final FabricClientCommandSource source = context.getSource();
		source.sendFeedback(Component.literal("Opening config menu...").withColor(new Random().nextInt(0xFFFFFF)));

		final Minecraft minecraft = source.getClient();
		minecraft.schedule(() -> minecraft.setScreen(AnimatiumConfig.getConfigScreen(minecraft.screen)));

		return Command.SINGLE_SUCCESS;
	}
}