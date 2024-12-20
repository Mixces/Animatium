package me.mixces.animatium.mixins.network;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Objects;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler {
    @Shadow
    private ClientWorld world;

    @WrapOperation(method = "onEntityTrackerUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/s2c/play/EntityTrackerUpdateS2CPacket;trackedValues()Ljava/util/List;"))
    public List<DataTracker.SerializedEntry<?>> animatium$disablePoseUpdates(EntityTrackerUpdateS2CPacket instance, Operation<List<DataTracker.SerializedEntry<?>>> original) {
        if (AnimatiumConfig.getInstance().disablePoseUpdates) {
            if (Objects.requireNonNull(MinecraftClient.getInstance().player).equals(world.getEntityById(instance.id()))) {
                instance.trackedValues().removeIf(entry -> entry.handler().equals(TrackedDataHandlerRegistry.ENTITY_POSE));
            }
        }

        return original.call(instance);
    }
}
