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
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.mixins.accessor.GameRendererAccessor;
import org.visuals.legacy.animatium.screens.OnboardingScreen;
import org.visuals.legacy.animatium.util.Utils;
import org.visuals.legacy.animatium.util.config.ConfigUtil;

import java.util.Calendar;
import java.util.Random;

public class AnimatiumCommand implements Command<FabricClientCommandSource> {
    public static LiteralArgumentBuilder<FabricClientCommandSource> create() {
        final LiteralArgumentBuilder<FabricClientCommandSource> command = ClientCommands.literal("animatium").executes(new AnimatiumCommand());
        command.then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("on").executes(On.UNIT));
        command.then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("off").executes(Off.UNIT));
        command.then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("reload").executes(Reload.UNIT));
        command.then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("onboarding").executes(Onboarding.UNIT));

        final Calendar calendar = Calendar.getInstance();
//        if (calendar.get(Calendar.MONTH) == Calendar.SEPTEMBER && calendar.get(Calendar.DAY_OF_MONTH) == 6) {
        command.then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("birthday").executes(Birthday.UNIT));
//        }

        return command;
    }

    @Override
    public int run(final CommandContext<FabricClientCommandSource> context) {
        final FabricClientCommandSource source = context.getSource();
        source.sendFeedback(Component.literal("Opening config menu...").withColor(new Random().nextInt(0xFFFFFF)));

        final Minecraft minecraft = source.getClient();
        minecraft.schedule(() -> minecraft.gui.setScreen(AnimatiumConfig.getConfigScreen(minecraft.gui.screen())));

        return Command.SINGLE_SUCCESS;
    }

    private static void reload() {
        final Minecraft minecraft = Minecraft.getInstance();
        minecraft.levelExtractor.allChanged();
        ((GameRendererAccessor) minecraft.gameRenderer).animatium$setOverlayTexture(new OverlayTexture());
        Utils.reinitializeInventorySlots();
    }

    private static class On implements Command<FabricClientCommandSource> {
        public static final On UNIT = new On();

        @Override
        public int run(final CommandContext<FabricClientCommandSource> context) {
            final FabricClientCommandSource source = context.getSource();
            if (Animatium.isEnabled()) {
                source.sendFeedback(Component.literal("Mod is already enabled!").withStyle(ChatFormatting.YELLOW));
            } else {
                source.sendFeedback(Component.literal("Mod enabled.").withStyle(ChatFormatting.GREEN));
                Animatium.setEnabled(true);
                reload();
            }

            return Command.SINGLE_SUCCESS;
        }
    }

    private static class Off implements Command<FabricClientCommandSource> {
        public static final Off UNIT = new Off();

        @Override
        public int run(final CommandContext<FabricClientCommandSource> context) {
            final FabricClientCommandSource source = context.getSource();
            if (!Animatium.isEnabled()) {
                source.sendFeedback(Component.literal("Mod is already disabled!").withStyle(ChatFormatting.YELLOW));
            } else {
                source.sendFeedback(Component.literal("Mod disabled.").withStyle(ChatFormatting.RED));
                Animatium.setEnabled(false);
                reload();
            }

            return Command.SINGLE_SUCCESS;
        }
    }

    private static class Reload implements Command<FabricClientCommandSource> {
        public static final Reload UNIT = new Reload();

        @Override
        public int run(final CommandContext<FabricClientCommandSource> context) {
            final FabricClientCommandSource source = context.getSource();
            source.sendFeedback(Component.literal("Mod reloaded.").withStyle(ChatFormatting.GREEN));
            reload();
            return Command.SINGLE_SUCCESS;
        }
    }

    private static class Onboarding implements Command<FabricClientCommandSource> {
        public static final Onboarding UNIT = new Onboarding();

        @Override
        public int run(final CommandContext<FabricClientCommandSource> context) {
            final Minecraft minecraft = context.getSource().getClient();
            minecraft.schedule(() -> minecraft.gui.setScreen(new OnboardingScreen(minecraft.gui.screen(), true)));
            return Command.SINGLE_SUCCESS;
        }
    }

    private static class Birthday implements Command<FabricClientCommandSource> {
        public static final Birthday UNIT = new Birthday();

        private final RandomSource random = RandomSource.createThreadLocalInstance();

        @Override
        public int run(final CommandContext<FabricClientCommandSource> context) {
            final Entity entity = context.getSource().getEntity();
            final ClientLevel level = context.getSource().getLevel();
            if (!(entity instanceof Player)) {
                // Do nothing for non-players
                return Command.SINGLE_SUCCESS;
            }

            final double x = entity.getBlockX() + this.random.nextDouble();
            final double y = entity.getBlockY() + this.random.nextDouble();
            final double z = entity.getBlockZ() + this.random.nextDouble();
            for (int i = 0; i < 180; ++i) {
                SoundEvent sound;
                final int random = this.random.nextIntBetweenInclusive(0, 6);
                if (random > 3) {
                    sound = SoundEvents.AMETHYST_BLOCK_CHIME;
                } else {
                    sound = SoundEvents.FIREWORK_ROCKET_BLAST;
                }

                level.playLocalSound(x, y, z, sound, SoundSource.AMBIENT, 20.0F, 0.95F + this.random.nextFloat() * 0.15F, true);
            }

            context.getSource().sendFeedback(colorful("It's the creators birthday today!!! Wish them a happy birthday!"));
            return Command.SINGLE_SUCCESS;
        }

        private Component colorful(final String literal) {
            final String[] parts = literal.split("");
            final MutableComponent component = Component.empty();
            for (final String part : parts) {
                component.append(Component.literal(part).withColor(ARGB.opaque((int) (Math.random() * 16777215))));
            }

            return component;
        }
    }
}