package me.mixces.animatium.util;

import com.google.common.base.MoreObjects;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class PlayerUtils {
    public static int handMultiplier(ClientPlayerEntity player, EntityRenderDispatcher dispatcher) {
        Hand hand = MoreObjects.firstNonNull(player.preferredHand, Hand.MAIN_HAND);
        Arm arm = hand == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
        int i = dispatcher.gameOptions.getPerspective().isFirstPerson() ? 1 : -1;
        return arm == Arm.RIGHT ? i : -i;
    }

    public static Vec3d lerpPlayerWithEyeHeight(PlayerEntity entity, float tickDelta, float eyeHeight) {
        return entity.getLerpedPos(tickDelta).add(0, eyeHeight, 0);
    }
}
