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

import com.google.common.base.MoreObjects;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.mixins.accessor.CameraAccessor;
import org.visuals.legacy.animatium.mixins.accessor.ClientLevelDataAccessor;
import org.visuals.legacy.animatium.mixins.accessor.LivingEntityAccessor;
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

    public static float lerpCameraPosition(final Camera camera) {
        final CameraAccessor cameraAccessor = (CameraAccessor) camera;
        return Mth.lerp(camera.getCameraEntityPartialTicks(Minecraft.getInstance().getDeltaTracker()), cameraAccessor.animatium$getOldEyeHeight(), cameraAccessor.animatium$getEyeHeight());
    }

    public static float lerpCameraPosition(final CameraRenderState cameraRenderState) {
        return Mth.lerp(cameraRenderState.animatium$getPartialTickTime(), cameraRenderState.animatium$getOldEyeHeight(), cameraRenderState.animatium$getEyeHeight());
    }

    public static int getHandMultiplier(final Player player) {
        final InteractionHand hand = MoreObjects.firstNonNull(player.swingingArm, InteractionHand.MAIN_HAND);
        return (Minecraft.getInstance().options.getCameraType().isFirstPerson() ? 1 : -1) * getHandMultiplier(player, hand);
    }

    public static int getHandMultiplier(final Player player, final InteractionHand hand) {
        return getArmMultiplier(hand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite());
    }

    public static int getArmMultiplier(final HumanoidArm arm) {
        return arm == HumanoidArm.RIGHT ? 1 : -1;
    }

    public static Vec3 getPosWithEyeHeight(final Player player, final float tickDelta, final double eyeHeight) {
        return player.getPosition(tickDelta).add(0.0, eyeHeight, 0.0);
    }

    public static boolean isBlockingArm(final HumanoidArm arm, final ArmedEntityRenderState armedEntityState) {
        return (arm == HumanoidArm.LEFT && armedEntityState.leftArmPose == HumanoidModel.ArmPose.BLOCK) ||
                (arm == HumanoidArm.RIGHT && armedEntityState.rightArmPose == HumanoidModel.ArmPose.BLOCK);
    }

    public static void fakeHandSwing(final Player player, final InteractionHand hand) {
        // Fake Swinging, Doesn't Send A Packet
        if (isNotSwinging(player)) {
            player.swingTime = -1;
            player.swinging = true;
            player.swingingArm = hand;
        }
    }

    // Sends necessary swing packets, without playing the player hand swing animation
    public static void sendSwingPacket(final LocalPlayer player, final InteractionHand hand) {
        if (isNotSwinging(player) && player.level() instanceof ServerLevel serverLevel) {
            final int swingHand = hand == InteractionHand.MAIN_HAND ? ClientboundAnimatePacket.SWING_MAIN_HAND : ClientboundAnimatePacket.SWING_OFF_HAND;
            serverLevel.getChunkSource().sendToTrackingPlayers(player, new ClientboundAnimatePacket(player, swingHand));
        }

        player.connection.send(new ServerboundSwingPacket(hand));
    }

    public static boolean isNotSwinging(final Player player) {
        return !player.swinging || player.swingTime >= ((LivingEntityAccessor) player).animatium$getSwingDuration() / 2 || player.swingTime < 0;
    }

    public static void applySwingWhilstMining(final ClientLevel level, final Player player, final HitResult hitResult) {
        final InteractionHand activeHand = player.getUsedItemHand();
        final InteractionHand hand = AnimatiumConfig.instance().extras.offhandUsageSwinging ? activeHand : InteractionHand.MAIN_HAND;
        if (activeHand.equals(hand)) {
            if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
                final BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                final BlockPos blockPos = blockHitResult.getBlockPos();
                if (level != null && !level.getBlockState(blockPos).isAir()) {
                    level.addBreakingBlockEffect(blockPos, blockHitResult.getDirection());
                }
            } else if (!AnimatiumConfig.instance().extras.alwaysUsageSwing) {
                return;
            }

            fakeHandSwing(player, hand);
        }
    }

    public static boolean hasFog1_7(final ClientLevel level) {
        final ClientLevelDataAccessor levelDataAccessor = (ClientLevelDataAccessor) level.getLevelData();
        return !levelDataAccessor.animatium$isFlatWorld() && !level.dimensionType().hasCeiling(); // "isDark" method from 1.7/1.8
    }

    /**
     * Can always safely assume that if this returns true, the provided render-state is AvatarRenderState
     *
     * @return Entity id matches the client player id
     */
    public static boolean isSelf(final LivingEntityRenderState livingEntityRenderState) {
        final Player player = Minecraft.getInstance().player;
        return player != null && livingEntityRenderState instanceof AvatarRenderState avatarRenderState && avatarRenderState.id == player.getId();
    }

    public static boolean isSelf(final Entity entity) {
        final Player player = Minecraft.getInstance().player;
        return player != null && entity != null && entity.getId() == player.getId();
    }

    public static void reinitializeInventorySlots() {
        final Player player = Minecraft.getInstance().player;
        if (player != null && !GameType.CREATIVE.equals(player.gameMode())) {
            // Re-initialize the inventory, to reset the slot positions modified by "Old Crafting Slots Position"
            ((PlayerAccessor) player).animatium$setInventoryMenu(new InventoryMenu(player.getInventory(), !player.level().isClientSide(), player));
        }
    }

    public static boolean isSingleplayer() {
        final IntegratedServer server = Minecraft.getInstance().getSingleplayerServer();
        return server != null && server.isSingleplayer();
    }
}
