package me.mixces.animatium.hook;

public class EntityRenderDispatcherHook {
    public static final ThreadLocal<Float> tickDelta = ThreadLocal.withInitial(() -> 0.0F);
}
