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

import btw.mixces.animatium.mixins.accessor.CameraAccessor;
import btw.mixces.animatium.mixins.accessor.LivingEntityAccessor;
import com.google.common.base.MoreObjects;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class PlayerUtils {
    private PlayerUtils() {
    }

    public static int getHandMultiplier(Player player) {
        InteractionHand hand = MoreObjects.firstNonNull(player.swingingArm, InteractionHand.MAIN_HAND);
        final int direction = getHandMultiplier(player, hand);
        return (Minecraft.getInstance().options.getCameraType().isFirstPerson() ? 1 : -1) * direction;
    }

    public static int getHandMultiplier(Player player, InteractionHand hand) {
        return getArmMultiplier(hand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite());
    }

    public static int getArmMultiplier(HumanoidArm arm) {
        return arm == HumanoidArm.RIGHT ? 1 : -1;
    }

    public static Vec3 getPosWithEyeHeight(Player entity, float tickDelta, double eyeHeight) {
        return entity.getPosition(tickDelta).add(0.0, eyeHeight, 0.0);
    }

    public static boolean isBlockingArm(HumanoidArm arm, ArmedEntityRenderState armedEntityState) {
        return (arm == HumanoidArm.LEFT && armedEntityState.leftArmPose == HumanoidModel.ArmPose.BLOCK) ||
                (arm == HumanoidArm.RIGHT && armedEntityState.rightArmPose == HumanoidModel.ArmPose.BLOCK);
    }

    public static void fakeHandSwing(Player player, InteractionHand hand) {
        // Clientside NOTE: fake swinging, doesn't send a packet
        if (isNotSwinging(player)) {
            player.swingTime = -1;
            player.swinging = true;
            player.swingingArm = hand;
        }
    }

    // Sends necessary swing packets, without playing the player hand swing animation
    public static void sendSwingPacket(LocalPlayer player, InteractionHand hand) {
        if (isNotSwinging(player) && player.level() instanceof ServerLevel serverLevel) {
            int swingHand = ClientboundAnimatePacket.SWING_MAIN_HAND;
            if (hand == InteractionHand.OFF_HAND) {
                swingHand = ClientboundAnimatePacket.SWING_OFF_HAND;
            }

            serverLevel.getChunkSource().sendToTrackingPlayers(player, new ClientboundAnimatePacket(player, swingHand));
        }

        player.connection.send(new ServerboundSwingPacket(hand));
    }

    public static boolean isNotSwinging(Player player) {
        return !player.swinging || player.swingTime >= ((LivingEntityAccessor) player).animatium$getSwingDuration() / 2 || player.swingTime < 0;
    }

    public static float lerpCameraPosition(Camera camera) {
        final CameraAccessor cameraAccessor = (CameraAccessor) camera;
        return Mth.lerp(camera.getPartialTickTime(), cameraAccessor.animatium$getEyeHeightOld(), cameraAccessor.animatium$getEyeHeight());
    }

    public static void applySwingWhilstMining(ClientLevel level, Player player, HitResult hitResult) {
        final InteractionHand activeHand = player.getUsedItemHand();
        final InteractionHand hand = InteractionHand.MAIN_HAND;
        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK && activeHand.equals(hand)) {
            final BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            final BlockPos blockPos = blockHitResult.getBlockPos();
            if (level != null && !level.getBlockState(blockPos).isAir()) {
                level.addBreakingBlockEffect(blockPos, blockHitResult.getDirection());
            }

            PlayerUtils.fakeHandSwing(player, hand);
        }
    }
}
