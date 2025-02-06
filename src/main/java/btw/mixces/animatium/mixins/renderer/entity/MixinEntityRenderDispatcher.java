package btw.mixces.animatium.mixins.renderer.entity;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderDispatcher.class)
public abstract class MixinEntityRenderDispatcher {
    @ModifyExpressionValue(method = "renderFlame", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/renderer/entity/state/EntityRenderState;boundingBoxWidth:F"))
    private float animatium$oldFlameWidth(float original) {
        if (AnimatiumClient.getEnabled() && AnimatiumConfig.instance().getOldFlameDimensions()) {
            return 0.6F;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(method = "renderFlame", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/renderer/entity/state/EntityRenderState;boundingBoxHeight:F"))
    private float animatium$oldFlameHeight(float original) {
        if (AnimatiumClient.getEnabled() && AnimatiumConfig.instance().getOldFlameDimensions()) {
            return 1.8F;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(method = "renderFlame", at = @At(value = "CONSTANT", args = "floatValue=0.0", ordinal = 1))
    private float animatium$oldFlameOffset(float original) {
        if (AnimatiumClient.getEnabled() && AnimatiumConfig.instance().getOldFlameOffset()) {
            return -0.2F;
        } else {
            return original;
        }
    }
}
