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

package org.visuals.legacy.animatium.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import lombok.experimental.UtilityClass;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.Nullable;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

import java.util.List;

@UtilityClass
public class ItemUtils {
	public boolean isSwordItem(final ItemStack stack) {
		return stack.is(ItemTags.SWORDS);
	}

	public boolean isAxeItem(final ItemStack stack) {
		return stack.getItem() instanceof AxeItem || stack.is(ItemTags.AXES);
	}

	public boolean isPickaxeItem(final ItemStack stack) {
		return stack.is(ItemTags.PICKAXES);
	}

	public boolean isShovelItem(final ItemStack stack) {
		return stack.getItem() instanceof ShovelItem || stack.is(ItemTags.SHOVELS);
	}

	public boolean isHoeItem(final ItemStack stack) {
		return stack.getItem() instanceof HoeItem || stack.is(ItemTags.HOES);
	}

	public boolean isDiggerItem(final ItemStack stack) {
		return isAxeItem(stack) || isPickaxeItem(stack) || isShovelItem(stack) || isHoeItem(stack);
	}

	public boolean isShieldItem(final ItemStack stack) {
		return stack.getItem() instanceof ShieldItem || stack.is(Items.SHIELD);
	}

	public boolean isFishingRodItem(final ItemStack stack) {
		final Item item = stack.getItem();
		return item instanceof FishingRodItem || item instanceof FoodOnAStickItem<?>;
	}

	public boolean isRangedWeaponItem(final ItemStack stack) {
		return stack.getItem() instanceof ProjectileWeaponItem;
	}

	public boolean isHandheldItem(final ItemStack stack) {
		return isDiggerItem(stack) ||
				isSwordItem(stack) ||
				isFishingRodItem(stack) ||
				List.of(Items.MACE, Items.TRIDENT, Items.STICK, Items.BREEZE_ROD, Items.BLAZE_ROD).contains(stack.getItem());
	}

	public boolean isThinBlockItem(final ItemStack stack) {
		final Block block = Block.byItem(stack.getItem());
		return block instanceof CarpetBlock ||
				block instanceof TrapDoorBlock ||
				block instanceof PressurePlateBlock ||
				block instanceof SnowLayerBlock ||
				block instanceof DaylightDetectorBlock;
	}

	public boolean isSkullBlock(final ItemStack stack) {
		return Block.byItem(stack.getItem()) instanceof SkullBlock;
	}

	public boolean isBlockItemBlacklisted(final ItemStack stack) {
		final Block block = Block.byItem(stack.getItem());
		return block instanceof BannerBlock ||
				block instanceof RodBlock ||
				block instanceof BedBlock ||
				(isSkullBlock(stack) && !AnimatiumConfig.instance().items.mobHeadIcons);
	}

	public boolean isItemBlacklisted(final ItemStack stack) {
		return isShieldItem(stack) ||
				isBlockItemBlacklisted(stack) ||
				stack.is(Items.CROSSBOW);
	}

	public boolean isSwingItemBlacklisted(final ItemStack stack) {
		final Item item = stack.getItem();
		return item instanceof ProjectileItem ||
				item instanceof BucketItem ||
				item instanceof ShearsItem ||
				item instanceof EnderpearlItem;
	}

	public boolean isBlock3d(final ItemStack stack, final boolean usesBlockLight) {
		return stack.getItem() instanceof BlockItem && usesBlockLight;
	}

	public void applyLegacyFirstPersonTransforms(final PoseStack poseStack, final int direction, final Runnable runnable) {
		poseStack.mulPose(Axis.YP.rotationDegrees(direction * 45.0F));
		poseStack.scale(0.4F, 0.4F, 0.4F);
		runnable.run();
		poseStack.scale(1 / 0.4F, 1 / 0.4F, 1 / 0.4F);
		poseStack.mulPose(Axis.YP.rotationDegrees(direction * -45.0F));
	}

	public boolean shouldApplyItemPositionsInThirdPerson(final ArmedEntityRenderState armedEntityRenderState) {
		if (AnimatiumConfig.instance().items.itemPositionsInThirdPerson) {
			return true;
		} else {
			return AnimatiumConfig.instance().other.thirdPersonSwordBlockingPosition && Utils.isBlockingArm(armedEntityRenderState.mainArm, armedEntityRenderState);
		}
	}

	public int getLegacyDurabilityColorValue(final ItemStack stack) {
		final double value = (255.0 - (double) stack.getDamageValue() * 255.0 / (double) stack.getMaxDamage());
		if (!Double.isNaN(value)) {
			return (int) Math.round(value);
		} else {
			return 0;
		}
	}

	public Rarity getLegacyItemRarity(final ItemStack stack) {
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

	public static @Nullable ResourceLocation getMobHeadLocation(final Item item) {
		final Block block = Block.byItem(item);
		if (block == Blocks.AIR || !(block instanceof SkullBlock skullBlock && skullBlock.getType() instanceof SkullBlock.Types types)) {
			return null;
		} else {
			return switch (types) {
				case CREEPER -> Animatium.location("creeper_skull");
				case DRAGON -> Animatium.location("dragon_skull");
				case PIGLIN -> Animatium.location("piglin_skull");
				case PLAYER -> Animatium.location("player_skull");
				case SKELETON -> Animatium.location("skeleton_skull");
				case WITHER_SKELETON -> Animatium.location("wither_skeleton_skull");
				case ZOMBIE -> Animatium.location("zombie_skull");
			};
		}
	}

	// TODO/NOTE: Might need rework? as vanilla now has the fix as of 1.21.11+ but doesn't seem fully the same/accurate
	public boolean shouldInstantlyReplaceVisibleItem1_8(final ItemStack prevStack, final ItemStack currentStack) {
		// TODO/NOTE: Apparently 1.7 doesn't do any special checks inside the inventory
		final boolean itemsMatch = ItemStack.isSameItem(prevStack, currentStack);
		final boolean durabilityMatch = prevStack.getDamageValue() == currentStack.getDamageValue();
		final boolean countMatch = prevStack.getCount() == currentStack.getCount();
		return (itemsMatch && (!durabilityMatch || !countMatch));
	}
}