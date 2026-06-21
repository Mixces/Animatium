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

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.visuals.legacy.animatium.mixins.accessor.ClientLevelDataAccessor;
import org.visuals.legacy.animatium.mixins.accessor.PlayerAccessor;

import java.util.concurrent.atomic.AtomicReference;

public final class Utils {
    private Utils() {
        throw new UnsupportedOperationException("Initialization of utility class is prohibited!");
    }

    public static float toRadians(final float angle) {
        return angle * Mth.DEG_TO_RAD;
    }

    public static VoxelShape expandVoxelShape(final VoxelShape shape, final float value) {
        final AtomicReference<VoxelShape> voxelShape = new AtomicReference<>(Shapes.empty());
        shape.toAabbs().forEach((aabb) -> voxelShape.set(Shapes.join(voxelShape.get(), Shapes.create(aabb.inflate(value)), BooleanOp.OR)));
        return voxelShape.get();
    }

    public static boolean isSingleplayer() {
        final IntegratedServer server = Minecraft.getInstance().getSingleplayerServer();
        return server != null && server.isSingleplayer();
    }

    public static boolean hasFog1_7(final ClientLevel level) {
        final ClientLevelDataAccessor levelDataAccessor = (ClientLevelDataAccessor) level.getLevelData();
        return !levelDataAccessor.animatium$isFlatWorld() && !level.dimensionType().hasCeiling(); // "isDark" method from 1.7/1.8
    }

    public static void reinitializeInventorySlots() {
        final Player player = Minecraft.getInstance().player;
        if (player != null && !GameType.CREATIVE.equals(player.gameMode())) {
            // Re-initialize the inventory, to reset the slot positions modified by "Old Crafting Slots Position"
            ((PlayerAccessor) player).animatium$setInventoryMenu(new InventoryMenu(player.getInventory(), !player.level().isClientSide(), player));
        }
    }
}
