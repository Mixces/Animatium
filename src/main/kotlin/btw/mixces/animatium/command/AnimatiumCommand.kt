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
 */

package btw.mixces.animatium.command

import btw.mixces.animatium.AnimatiumClient
import btw.mixces.animatium.config.AnimatiumConfig
import btw.mixces.animatium.util.ColorUtils
import btw.mixces.animatium.util.Feature
import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

class AnimatiumCommand : Command<FabricClientCommandSource> {
    companion object {
        fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
            val command = ClientCommandManager.literal("animatium").executes(AnimatiumCommand())

            run {
                command.then(
                    LiteralArgumentBuilder.literal<FabricClientCommandSource>("debug").executes { context ->
                        context.source.sendFeedback(
                            Component.literal(
                                "Disabled miss swing penalty? " + AnimatiumClient.enabledFeatures.contains(
                                    Feature.MISS_PENALTY
                                )
                            ).withColor(ColorUtils.randomColor())
                        )
                        context.source.sendFeedback(
                            Component.literal(
                                "Enabled left-click item usage on ground? " + AnimatiumClient.enabledFeatures.contains(
                                    Feature.LEFT_CLICK_ITEM_USAGE
                                )
                            ).withColor(ColorUtils.randomColor())
                        )
                        return@executes Command.SINGLE_SUCCESS
                    }.requires { ctx ->
                        val player = Minecraft.getInstance().player ?: return@requires false
                        return@requires listOf(
                            "41ee11aa-bde8-40e2-8283-f51c23a9c817",
                            "b0f27308-0a70-43bf-b025-45c12979b7ad",
                            "0e3ee1e0-f4d2-4550-8fe9-4f7a0d2cd08a",
                            "718c0beb-a8c2-4887-a85c-87d118c3d31a"
                        ).contains(player.stringUUID)
                    }
                )
            }

            run {
                command.then(
                    LiteralArgumentBuilder.literal<FabricClientCommandSource>("on").executes { context ->
                        if (AnimatiumClient.enabled) {
                            context.source.sendFeedback(
                                Component.literal("Mod is already enabled!").withColor(0x00FF00)
                            )
                            return@executes Command.SINGLE_SUCCESS
                        } else {
                            context.source.sendFeedback(Component.literal("Mod enabled.").withColor(0x00FF00))
                            AnimatiumClient.enabled = true
                            AnimatiumClient.shouldReloadOverlayTexture = true
                            Minecraft.getInstance().reloadResourcePacks()
                            AnimatiumClient.saveEnabledState()
                            return@executes Command.SINGLE_SUCCESS
                        }
                    }
                )
            }

            run {
                command.then(
                    LiteralArgumentBuilder.literal<FabricClientCommandSource>("off").executes { context ->
                        if (!AnimatiumClient.enabled) {
                            context.source.sendFeedback(
                                Component.literal("Mod is already disabled!").withColor(0x00FF00)
                            )
                            return@executes Command.SINGLE_SUCCESS
                        } else {
                            context.source.sendFeedback(Component.literal("Mod disabled.").withColor(0xFF0000))
                            AnimatiumClient.enabled = false
                            AnimatiumClient.shouldReloadOverlayTexture = true
                            Minecraft.getInstance().reloadResourcePacks()
                            AnimatiumClient.saveEnabledState()
                            return@executes Command.SINGLE_SUCCESS
                        }
                    }
                )
            }

            run {
                command.then(
                    LiteralArgumentBuilder.literal<FabricClientCommandSource>("configure").executes { context ->
                        context.source.sendFeedback(Component.literal("Opening config menu...").withColor(0x00FF00))
                        context.source.client.schedule {
                            context.source.client.setScreen(AnimatiumConfig.getConfigScreen(null))
                        }
                        return@executes Command.SINGLE_SUCCESS
                    }
                )
            }

            dispatcher.register(command)
        }
    }

    override fun run(context: CommandContext<FabricClientCommandSource>): Int {
        context.source.sendFeedback(Component.literal("Hello there!").withColor(ColorUtils.randomColor()))
        return Command.SINGLE_SUCCESS
    }
}