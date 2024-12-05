package me.mixces.animatium.hook;

import net.minecraft.client.render.entity.state.BipedEntityRenderState;

public class ArmorFeatureRendererHook {
    public static final ThreadLocal<BipedEntityRenderState> bipedEntityRenderState = ThreadLocal.withInitial(() -> null);
}
