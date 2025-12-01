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

package org.visuals.legacy.animatium;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SkullBlock;
import org.jetbrains.annotations.Nullable;
import org.visuals.legacy.animatium.packet.AnimatiumInfoPayloadPacket;

public class AnimatiumConstants {
    public static final String MOD_ID = "@MODID@";
    public static final Double VERSION = Double.parseDouble("@VERSION@");
    public static final String DEVELOPMENT_VERSION = "@COMMIT@";
    public static final boolean IS_DEVELOPMENT = Boolean.parseBoolean("@DEVELOPMENT@");

    public static @Nullable ResourceLocation getMobHeadLocation(Item item) {
        final Block block = Block.byItem(item);
        if (block == Blocks.AIR || !(block instanceof SkullBlock skullBlock)) {
            return null;
        } else {
            return switch (skullBlock.getType()) {
                case SkullBlock.Types.CREEPER -> Animatium.location("creeper_skull");
                case SkullBlock.Types.DRAGON -> Animatium.location("dragon_skull");
                case SkullBlock.Types.PIGLIN -> Animatium.location("piglin_skull");
                case SkullBlock.Types.PLAYER -> Animatium.location("player_skull");
                case SkullBlock.Types.SKELETON -> Animatium.location("skeleton_skull");
                case SkullBlock.Types.WITHER_SKELETON -> Animatium.location("wither_skeleton_skull");
                case SkullBlock.Types.ZOMBIE -> Animatium.location("zombie_skull");
                default -> throw new IllegalStateException("Unexpected value: " + skullBlock.getType());
            };
        }
    }

    public static AnimatiumInfoPayloadPacket getInfoPayload() {
        return new AnimatiumInfoPayloadPacket(VERSION, IS_DEVELOPMENT ? DEVELOPMENT_VERSION : null);
    }
}
