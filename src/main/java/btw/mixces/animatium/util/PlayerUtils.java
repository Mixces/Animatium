package btw.mixces.animatium.util;

import btw.mixces.animatium.mixins.accessor.CameraAccessor;
import btw.mixces.animatium.mixins.accessor.LivingEntityAccessor;
import com.google.common.base.MoreObjects;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.phys.Vec3;

public final class PlayerUtils {
    public static int getHandMultiplier(Player player) {
        InteractionHand hand = MoreObjects.firstNonNull(player.swingingArm, InteractionHand.MAIN_HAND);
        final int direction = getHandMultiplier(player, hand);
        Minecraft client = Minecraft.getInstance();
        return (client.options.getCameraType().isFirstPerson() ? 1 : -1) * direction;
    }

    public static int getHandMultiplier(Player player, InteractionHand hand) {
        HumanoidArm arm = hand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
        return getArmMultiplier(arm);
    }

    public static int getArmMultiplier(HumanoidArm arm) {
        return arm == HumanoidArm.RIGHT ? 1 : -1;
    }

    public static Vec3 getPosWithEyeHeight(Player entity, float tickDelta, double eyeHeight) {
        return entity.getPosition(tickDelta).add(0.0, eyeHeight, 0.0);
    }

    public static boolean isBlocking(LivingEntity livingEntity, ItemStack stack) {
        return (livingEntity instanceof Player player && player.getUseItemRemainingTicks() > 0 && stack.getUseAnimation() == ItemUseAnimation.BLOCK);
    }

    public static boolean isBlockingArm(HumanoidArm arm, ArmedEntityRenderState armedEntityState) {
        if (arm == HumanoidArm.LEFT && armedEntityState.leftArmPose == HumanoidModel.ArmPose.BLOCK) {
            return true;
        } else if (arm == HumanoidArm.RIGHT && armedEntityState.rightArmPose == HumanoidModel.ArmPose.BLOCK) {
            return true;
        } else {
            return false;
        }
    }

    public static void fakeHandSwing(Player player, InteractionHand hand) {
        // Clientside NOTE fake swinging, doesn't send a packet
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

            serverLevel.getChunkSource().broadcast(player, new ClientboundAnimatePacket(player, swingHand));
        }

        player.connection.send(new ServerboundSwingPacket(hand));
    }

    public static boolean isNotSwinging(Player player) {
        return !player.swinging || player.swingTime >= ((LivingEntityAccessor) player).getSwingDuration() / 2 || player.swingTime < 0;
    }

    public static float lerpCameraPosition(Camera camera) {
        CameraAccessor cameraAccessor = (CameraAccessor) camera;
        return Mth.lerp(camera.getPartialTickTime(), cameraAccessor.getEyeHeightOld(), cameraAccessor.getEyeHeight());
    }
}
