package me.mixces.animatium.mixin.access;

import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface ICameraMixin {

    @Accessor
    float getCameraY();

    @Accessor
    float getLastCameraY();
}
