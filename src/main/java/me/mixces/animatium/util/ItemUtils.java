package me.mixces.animatium.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;

public abstract class ItemUtils {
    private static final ThreadLocal<ItemStack> stack = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<ModelTransformationMode> transformationMode = ThreadLocal.withInitial(() -> null);

    public static void set(ItemStack stack, ModelTransformationMode transformationMode) {
        ItemUtils.stack.set(stack);
        ItemUtils.transformationMode.set(transformationMode);
    }

    public static void clear() {
        ItemUtils.stack.remove();
        ItemUtils.transformationMode.remove();
    }

    public static ItemStack getStack() {
        return stack.get();
    }

    public static ModelTransformationMode getTransformMode() {
        return transformationMode.get();
    }
}
