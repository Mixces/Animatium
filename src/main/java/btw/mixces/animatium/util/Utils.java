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

package btw.mixces.animatium.util;

import btw.mixces.animatium.mixins.accessor.CameraAccessor;
import btw.mixces.animatium.util.states.CameraUtilityRenderState;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Camera;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.concurrent.atomic.AtomicReference;

public final class Utils {
    public static final boolean HAS_VIAFABRICPLUS = FabricLoader.getInstance().isModLoaded("viafabricplus");
    public static final boolean HAS_SODIUM_EXTRA = FabricLoader.getInstance().isModLoaded("sodium-extra");

    private Utils() {
    }

    public static float toRadians(float angle) {
        return angle * (float) Math.PI / 180F;
    }

    public static VoxelShape expandVoxelShape(VoxelShape shape, float value) {
        AtomicReference<VoxelShape> voxelShape = new AtomicReference<>(Shapes.empty());
        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
                voxelShape.set(Shapes.join(
                        voxelShape.get(),
                        Shapes.create(new AABB(minX, minY, minZ, maxX, maxY, maxZ).inflate(value)),
                        BooleanOp.OR)));
        return voxelShape.get();
    }

    public static float lerpCameraPosition(Camera camera) {
        final CameraAccessor cameraAccessor = (CameraAccessor) camera;
        return Mth.lerp(camera.getPartialTickTime(), cameraAccessor.animatium$getOldEyeHeight(), cameraAccessor.animatium$getEyeHeight());
    }

    public static float lerpCameraPosition(CameraUtilityRenderState cameraUtilityRenderState) {
        return Mth.lerp(cameraUtilityRenderState.animatium$getPartialTickTime(), cameraUtilityRenderState.animatium$getOldEyeHeight(), cameraUtilityRenderState.animatium$getEyeHeight());
    }

    public static boolean isSwordItem(ItemStack stack) {
        return stack.is(ItemTags.SWORDS);
    }

    public static boolean isAxeItem(ItemStack stack) {
        return stack.is(ItemTags.AXES);
    }

    public static boolean isPickaxeItem(ItemStack stack) {
        return stack.is(ItemTags.PICKAXES);
    }

    public static boolean isShovelItem(ItemStack stack) {
        return stack.is(ItemTags.SHOVELS);
    }

    public static boolean isHoeItem(ItemStack stack) {
        return stack.is(ItemTags.HOES);
    }

    public static boolean isDiggerItem(ItemStack stack) {
        return isAxeItem(stack) || isPickaxeItem(stack) || isShovelItem(stack) || isHoeItem(stack);
    }

    public static boolean isShieldItem(ItemStack stack) {
        return stack.is(Items.SHIELD);
    }
}
