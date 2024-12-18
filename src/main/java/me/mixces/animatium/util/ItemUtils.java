package me.mixces.animatium.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;

import java.util.Optional;

public abstract class ItemUtils {
    private static final ThreadLocal<ItemStack> STACK = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<ModelTransformationMode> TRANSFORMATION_MODE = ThreadLocal.withInitial(() -> null);

    public static void set(ItemStack stack, ModelTransformationMode transformationMode) {
        ItemUtils.STACK.remove();
        ItemUtils.TRANSFORMATION_MODE.remove();
        ItemUtils.STACK.set(stack);
        ItemUtils.TRANSFORMATION_MODE.set(transformationMode);
    }

    public static Optional<ItemStack> getStack() {
        return Optional.ofNullable(STACK.get());
    }

    public static Optional<ModelTransformationMode> getTransformMode() {
        return Optional.ofNullable(TRANSFORMATION_MODE.get());
    }

    public static int getLegacyDurabilityColorValue(ItemStack stack) {
        return (int) Math.round(255.0 - (double) stack.getDamage() * 255.0 / (double) stack.getMaxDamage());
    }
}
