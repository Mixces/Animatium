package me.mixces.animatium.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mixces.animatium.Animatium;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @ModifyExpressionValue(
            method = "doItemUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isBreakingBlock()Z"
            )
    )
    private boolean animatium$interruptBlockBreaking(boolean original) {
        return !Animatium.CONFIG.PUNCH_DURING_USAGE && original;
    }

    @ModifyExpressionValue(
            method = "handleBlockBreaking",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"
            )
    )
    private boolean animatium$allowWhileUsingItem(boolean original) {
        return !Animatium.CONFIG.PUNCH_DURING_USAGE && original;
    }
}
