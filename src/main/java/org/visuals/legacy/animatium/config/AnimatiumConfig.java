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

package org.visuals.legacy.animatium.config;

import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.platform.YACLPlatform;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.category.*;
import org.visuals.legacy.animatium.packet.ConfigDataPayloadPacket;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public final class AnimatiumConfig {
	private static final ConfigClassHandler<AnimatiumConfig> CONFIG = ConfigClassHandler.createBuilder(AnimatiumConfig.class)
			.serializer((config) -> GsonConfigSerializerBuilder.create(config)
					.setPath(YACLPlatform.getConfigDir().resolve("animatium3.json"))
					.build()
			).build();

	@SerialEntry
	public MovementConfigCategory movement = new MovementConfigCategory();

	@SerialEntry
	public ItemsConfigCategory items = new ItemsConfigCategory();

	@SerialEntry
	public ScreenConfigCategory screen = new ScreenConfigCategory();

	@SerialEntry
	public FixesConfigCategory fixes = new FixesConfigCategory();

	@SerialEntry
	public OtherConfigCategory other = new OtherConfigCategory();

	@SerialEntry
	public ExtrasConfigCategory extras = new ExtrasConfigCategory();

	public static Screen getConfigScreen(@Nullable Screen parent) {
		return YetAnotherConfigLib.create(CONFIG, (defaults, config, builder) -> {
			builder.title(Component.translatable("animatium.title"));
			builder.category(MovementConfigCategory.create(defaults.movement, config.movement));
			builder.category(ScreenConfigCategory.create(defaults.screen, config.screen));
			builder.category(ItemsConfigCategory.create(defaults.items, config.items));
			builder.category(FixesConfigCategory.create(defaults.fixes, config.fixes));
			builder.category(OtherConfigCategory.create(defaults.other, config.other));
			builder.category(ExtrasConfigCategory.create(defaults.extras, config.extras));
			builder.save(() -> {
				CONFIG.save();

                // TODO:
				// final Minecraft minecraft = Minecraft.getInstance();
				// if (minecraft.getConnection() != null && !minecraft.isLocalServer()) {
				//     ClientPlayNetworking.send(new ConfigDataPayloadPacket());
				// }
			});
			return builder;
		}).generateScreen(parent);
	}

	public static void load() {
		final Path oldFilePath = YACLPlatform.getConfigDir().resolve("animatium.json");
		final File oldFile = oldFilePath.toFile();

		final Path newFilePath = YACLPlatform.getConfigDir().resolve("animatium3.json");
		final File newFile = newFilePath.toFile();

		if (oldFile.exists()) {
			if (newFile.exists()) {
				newFile.delete();
			}

			try {
				Files.move(oldFilePath, newFilePath);
				System.out.println("Detected old animatium 3.0 config file, moved to new location!");
			} catch (Exception exception) {
				System.err.println("Failed to move old animatium config to new location!");
				exception.printStackTrace();
			}
		}

		CONFIG.load();
	}

	public static void save() {
		CONFIG.save();
	}

	public static AnimatiumConfig instance() {
		return CONFIG.instance();
	}
}
