package me.mixces.animatium.mixins.accessor;

import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.SkyRendering;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SkyRendering.class)
public interface SkyRenderingAccessor {
    @Final
    @Accessor("darkSkyBuffer")
    VertexBuffer getDarkSkyBuffer();
}
