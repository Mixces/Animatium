package org.visuals.legacy.animatium.util

import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.util.Mth
import org.visuals.legacy.animatium.mixins.accessor.CameraAccessor

fun lerpPosition(camera: Camera): Float {
    val cameraAccessor = camera as CameraAccessor
    return Mth.lerp(
        camera.getCameraEntityPartialTicks(Minecraft.getInstance().deltaTracker),
        cameraAccessor.`animatium$getOldEyeHeight`(),
        cameraAccessor.`animatium$getEyeHeight`()
    )
}

fun lerpPosition(cameraRenderState: CameraRenderState): Float {
    return Mth.lerp(
        cameraRenderState.`animatium$getPartialTickTime`(),
        cameraRenderState.`animatium$getOldEyeHeight`(),
        cameraRenderState.`animatium$getEyeHeight`()
    )
}