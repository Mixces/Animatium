package me.mixces.animatium.util;

import net.minecraft.item.*;

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

    public static boolean isFishingRodItem(ItemStack stack) {
        if (!stack.isEmpty()) {
            Item item = stack.getItem();
            return item instanceof FishingRodItem || item instanceof OnAStickItem<?>;
        } else {
            return false;
        }
    }

    public static boolean isRangedWeaponItem(ItemStack stack) {
        if (!stack.isEmpty()) {
            Item item = stack.getItem();
            return item instanceof RangedWeaponItem;
        } else {
            return false;
        }
    }

    public static boolean isHandheldItem(ItemStack stack) {
        if (!stack.isEmpty()) {
            Item item = stack.getItem();
            // TODO: is this the best way? probably not
            return item instanceof MiningToolItem || item instanceof SwordItem || item instanceof MaceItem || item instanceof TridentItem || isFishingRodItem(stack) || item == Items.STICK;
        } else {
            return false;
        }
    }

    public static boolean isItemBlacklisted(ItemStack stack) {
        if (!stack.isEmpty()) {
            Item item = stack.getItem();
            return item instanceof ShieldItem || item instanceof CrossbowItem;
        } else {
            return false;
        }
    }

    public static int getLegacyDurabilityColorValue(ItemStack stack) {
        return (int) Math.round(255.0 - (double) stack.getDamage() * 255.0 / (double) stack.getMaxDamage());
    }
}
