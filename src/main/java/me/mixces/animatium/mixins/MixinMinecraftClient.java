package me.mixces.animatium.mixins;

import me.mixces.animatium.config.AnimatiumConfig;
import me.mixces.animatium.util.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.hit.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Shadow
    @Final
    public GameOptions options;

    @Shadow
    @Nullable
    public HitResult crosshairTarget;

    @Inject(method = "tick", at = @At("TAIL"))
    private void animatium$applySwingWhilstMining(CallbackInfo ci) {
        if (AnimatiumConfig.getInstance().applyItemSwingUsage) {
            ClientPlayerEntity player = this.player;
            if (player == null || player.getStackInHand(player.getActiveHand()) == null || !player.isUsingItem() || !this.options.attackKey.isPressed()) {
                return;
            }

            // TODO: Possible setting to allow swinging without having to look at a block?
            if (AnimatiumConfig.getInstance().alwaysAllowUsageSwinging || (this.crosshairTarget != null && this.crosshairTarget.getType() == HitResult.Type.BLOCK)) {
                PlayerUtils.fakeHandSwing(player, player.getActiveHand());
            }
        }
    }
}
