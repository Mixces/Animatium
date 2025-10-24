/**
 * Animatium
 * The all-you-could-want legacy animations mod for modern minecraft versions.
 * Brings back animations from the 1.7/1.8 era and more.
 * <p>
 * Copyright (C) 2024-2025 lowercasebtw
 * Copyright (C) 2024-2025 mixces
 * Copyright (C) 2024-2025 Contributors to the project retain their copyright
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * "MINECRAFT" LINKING EXCEPTION TO THE GPL
 */

package btw.mixces.animatium.util;

import btw.mixces.animatium.config.AnimatiumConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

import java.util.List;

public final class ItemUtils {
    private ItemUtils() {
    }

    public static boolean isSwordItem(ItemStack stack) {
        return stack.is(ItemTags.SWORDS);
    }

    public static boolean isAxeItem(ItemStack stack) {
        return stack.is(ItemTags.AXES);
    }

    public static boolean isPickaxeItem(ItemStack stack) {
        return stack.is(ItemTags.PICKAXES);
    }

    public static boolean isShovelItem(ItemStack stack) {
        return stack.is(ItemTags.SHOVELS);
    }

    public static boolean isHoeItem(ItemStack stack) {
        return stack.is(ItemTags.HOES);
    }

    public static boolean isDiggerItem(ItemStack stack) {
        return isAxeItem(stack) || isPickaxeItem(stack) || isShovelItem(stack) || isHoeItem(stack);
    }

    public static boolean isShieldItem(ItemStack stack) {
        return stack.is(Items.SHIELD);
    }

    public static boolean isFishingRodItem(ItemStack stack) {
        if (!stack.isEmpty()) {
            final Item item = stack.getItem();
            return item instanceof FishingRodItem ||
                    item instanceof FoodOnAStickItem<?>;
        } else {
            return false;
        }
    }

    public static boolean isRangedWeaponItem(ItemStack stack) {
        if (!stack.isEmpty()) {
            return stack.getItem() instanceof ProjectileWeaponItem;
        } else {
            return false;
        }
    }

    public static boolean isHandheldItem(ItemStack stack) {
        if (!stack.isEmpty()) {
            return isDiggerItem(stack) ||
                    isSwordItem(stack) ||
                    isFishingRodItem(stack) ||
                    List.of(Items.MACE, Items.TRIDENT, Items.STICK, Items.BREEZE_ROD, Items.BLAZE_ROD).contains(stack.getItem());
        } else {
            return false;
        }
    }

    public static boolean isThinBlockItem(ItemStack stack) {
        if (!stack.isEmpty()) {
            final Block block = Block.byItem(stack.getItem());
            return block instanceof CarpetBlock ||
                    block instanceof TrapDoorBlock || block instanceof PressurePlateBlock ||
                    block instanceof SnowLayerBlock || block instanceof DaylightDetectorBlock;
        } else {
            return false;
        }
    }

    public static boolean isSkullBlock(ItemStack stack) {
        if (!stack.isEmpty()) {
            return Block.byItem(stack.getItem()) instanceof SkullBlock;
        } else {
            return false;
        }
    }

    public static boolean isBlockItemBlacklisted(ItemStack stack) {
        if (!stack.isEmpty()) {
            final Block block = Block.byItem(stack.getItem());
            return block instanceof BannerBlock ||
                    block instanceof RodBlock ||
                    block instanceof BedBlock ||
                    isSkullBlock(stack);
        } else {
            return false;
        }
    }

    public static boolean isItemBlacklisted(ItemStack stack) {
        if (!stack.isEmpty()) {
            return isShieldItem(stack) ||
                    isBlockItemBlacklisted(stack) ||
                    stack.is(Items.CROSSBOW);
        } else {
            return false;
        }
    }

    public static boolean isSwingItemBlacklisted(ItemStack stack) {
        if (!stack.isEmpty()) {
            final Item item = stack.getItem();
            return item instanceof ProjectileItem ||
                    item instanceof BucketItem ||
                    item instanceof ShearsItem ||
                    item instanceof EnderpearlItem;
        } else {
            return false;
        }
    }

    public static boolean isBlock3d(ItemStack stack, ItemStackRenderState itemStackRenderState) {
        if (!stack.isEmpty()) {
            return stack.getItem() instanceof BlockItem && itemStackRenderState.usesBlockLight();
        } else {
            return false;
        }
    }

    public static void applyLegacyFirstpersonTransforms(PoseStack poseStack, int direction, Runnable runnable) {
        poseStack.mulPose(Axis.YP.rotationDegrees(direction * 45.0F));
        poseStack.scale(0.4F, 0.4F, 0.4F);
        runnable.run();
        poseStack.scale(1 / 0.4F, 1 / 0.4F, 1 / 0.4F);
        poseStack.mulPose(Axis.YP.rotationDegrees(direction * -45.0F));
    }

    public static boolean shouldApplyItemPositionsInThirdperson(ArmedEntityRenderState armedEntityRenderState) {
        if (AnimatiumConfig.instance().items.itemPositionsInThirdPerson) {
            return true;
        } else {
            return AnimatiumConfig.instance().other.thirdPersonSwordBlockingPosition && PlayerUtils.isBlockingArm(armedEntityRenderState.mainArm, armedEntityRenderState);
        }
    }

    public static int getLegacyDurabilityColorValue(ItemStack stack) {
        final double value = (255.0 - (double) stack.getDamageValue() * 255.0 / (double) stack.getMaxDamage());
        if (!Double.isNaN(value)) {
            return (int) Math.round(value);
        } else {
            return 0;
        }
    }

    public static Rarity getLegacyItemRarity(ItemStack stack) {
        final Item item = stack.getItem();
        if (List.of(Items.GOLDEN_APPLE, Items.END_CRYSTAL).contains(item)) {
            return Rarity.RARE;
        } else if (List.of(Items.NETHER_STAR, Items.ELYTRA, Items.DRAGON_HEAD).contains(item)) {
            return Rarity.UNCOMMON;
        } else if (item == Items.ENCHANTED_GOLDEN_APPLE) {
            return Rarity.EPIC;
        } else if (item == Items.TRIDENT) {
            return Rarity.COMMON;
        } else {
            return stack.getRarity();
        }
    }

    // TODO/NOTE: Might need rework? as vanilla now has the fix as of 1.21.11+ but doesn't seem fully the same/accurate
    public static boolean shouldInstantlyReplaceVisibleItem1_8(ItemStack prevStack, ItemStack currentStack) {
        // TODO/NOTE: Apparently 1.7 doesn't do any special checks inside the inventory
        final boolean itemsMatch = ItemStack.isSameItem(prevStack, currentStack);
        final boolean durabilitiesMatch = prevStack.getDamageValue() == currentStack.getDamageValue();
        final boolean countMatch = prevStack.getCount() == currentStack.getCount();
        return (itemsMatch && (!durabilitiesMatch || !countMatch));
    }
}