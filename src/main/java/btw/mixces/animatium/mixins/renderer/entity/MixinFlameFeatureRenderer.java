package btw.mixces.animatium.mixins.renderer.entity;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.EntityUtils;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

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
}
