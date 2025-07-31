package btw.mixces.animatium.mixins.renderer.entity;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.EntityUtils;
import btw.mixces.animatium.util.PlayerUtils;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Objects;

@Mixin(FlameFeatureRenderer.class)
public abstract class MixinFlameFeatureRenderer {
    @ModifyExpressionValue(method = "renderFlame", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/renderer/entity/state/EntityRenderState;boundingBoxWidth:F"))
    private float animatium$flameWidth(float original, @Local(argsOnly = true) EntityRenderState entityRenderState) {
        Entity entity = EntityUtils.getEntityByState(entityRenderState);
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().flameDimensions && entity instanceof Player) {
            return 0.6F;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(method = "renderFlame", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/renderer/entity/state/EntityRenderState;boundingBoxHeight:F"))
    private float animatium$flameHeight(float original, @Local(argsOnly = true) EntityRenderState entityRenderState) {
        Entity entity = EntityUtils.getEntityByState(entityRenderState);
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().flameDimensions && entity instanceof Player) {
            return 1.8F;
        } else {
            return original;
        }
    }

    @ModifyArg(method = "renderFlame", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack$Pose;translate(FFF)Lorg/joml/Matrix4f;", ordinal = 0), index = 1)
    private float animatium$flameOffset(float original, @Local(argsOnly = true) EntityRenderState entityRenderState) {
        Entity entity = EntityUtils.getEntityByState(entityRenderState);
        if (AnimatiumClient.isEnabled() && entity instanceof Player player) {
            final float scale = player.getScale();
            boolean shouldSyncPlayerModelWithEyeHeight = AnimatiumConfig.instance().syncPlayerModelWithEyeHeight;
            if (shouldSyncPlayerModelWithEyeHeight) {
                original = (Player.STANDING_DIMENSIONS.eyeHeight() * scale) - PlayerUtils.lerpCameraPosition(Objects.requireNonNull(Minecraft.getInstance().getEntityRenderDispatcher().camera));
            }

            if (AnimatiumConfig.instance().flameOffset) {
                original += ((shouldSyncPlayerModelWithEyeHeight && player.isCrouching() ? 0.140625F : 0.296875F) * scale);
            }
        }

        return original;
    }
}
