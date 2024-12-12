package me.mixces.animatium.mixins;

import me.mixces.animatium.config.AnimatiumConfig;
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
    private void applySwingWhilstMining(CallbackInfo ci) {
        if (!AnimatiumConfig.applyItemSwingUsage) {
            return;
        }

        ClientPlayerEntity player = this.player;
        if (player == null || player.getStackInHand(player.getActiveHand()) == null || !this.options.attackKey.isPressed()) {
            return;
        }

        // TODO: Possible setting to allow swinging without having to look at a block?
        if (this.crosshairTarget != null && this.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            // NOTE: Clientside fake swinging, doesn't send a packet
            if (!player.handSwinging || player.handSwingTicks >= player.getHandSwingDuration() / 2 || player.handSwingTicks < 0) {
                player.handSwingTicks = -1;
                player.handSwinging = true;
                player.preferredHand = player.getActiveHand();
            }
        }
    }
}
