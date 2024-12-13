package me.mixces.animatium.mixins.accessor;

import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface CameraAccessor {
    @Accessor
    float getCameraY();

    @Accessor
    float getLastCameraY();
}