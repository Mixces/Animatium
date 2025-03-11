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

package btw.mixces.animatium.util;

import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public class MethUtils {
    public static float toRadians(float angle) {
        return angle * (float) Math.PI / 180F;
    }

    public static VoxelShape expandVoxelShape(VoxelShape shape, float value) {
        // Code from VoxelShape#simplify
        // TODO: simplify this code? or find alternative?
        List<VoxelShape> voxelShapes = new ArrayList<>();
        voxelShapes.add(Shapes.empty());
        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> voxelShapes.set(0, Shapes.joinUnoptimized(
                voxelShapes.getFirst(),
                Shapes.box(
                        minX - value,
                        minY - value,
                        minZ - value,
                        maxX + value,
                        maxY + value,
                        maxZ + value
                ),
                BooleanOp.OR
        )));
        return voxelShapes.getFirst();
    }
}
