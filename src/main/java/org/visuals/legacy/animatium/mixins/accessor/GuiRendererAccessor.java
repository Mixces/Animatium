package org.visuals.legacy.animatium.mixins.accessor;

import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiRenderer.class)
public interface GuiRendererAccessor {
    @Accessor("guiProjectionMatrixBuffer")
    CachedOrthoProjectionMatrixBuffer animatium$orthoMatrixBuffer();
}
