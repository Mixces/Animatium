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
 */

package btw.mixces.animatium.util

import btw.mixces.animatium.config.AnimatiumConfig
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.renderer.entity.state.EntityRenderState
import net.minecraft.client.renderer.item.ItemStackRenderState
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.*
import net.minecraft.world.level.block.*
import kotlin.math.roundToInt

object ItemUtils {
    private val RENDER_STATE: ThreadLocal<ItemStackRenderState?> = ThreadLocal.withInitial { null }
    private val STACK: ThreadLocal<ItemStack?> = ThreadLocal.withInitial { null }
    private val DISPLAY_CONTEXT: ThreadLocal<ItemDisplayContext?> = ThreadLocal.withInitial { null }

    @JvmStatic
    fun set(renderState: ItemStackRenderState, stack: ItemStack, displayContext: ItemDisplayContext) {
        RENDER_STATE.remove()
        STACK.remove()
        DISPLAY_CONTEXT.remove()
        RENDER_STATE.set(renderState)
        STACK.set(stack)
        DISPLAY_CONTEXT.set(displayContext)
    }

    @JvmStatic
    fun getRenderState(): ItemStackRenderState? {
        return RENDER_STATE.get()
    }

    @JvmStatic
    fun getStack(): ItemStack? {
        return STACK.get()
    }

    @JvmStatic
    fun getDisplayContext(): ItemDisplayContext? {
        return DISPLAY_CONTEXT.get()
    }

    @JvmStatic
    fun isFishingRodItem(stack: ItemStack): Boolean {
        return if (!stack.isEmpty) {
            stack.item is FishingRodItem ||
                    stack.item is FoodOnAStickItem<*>
        } else {
            false
        }
    }

    @JvmStatic
    fun isRangedWeaponItem(stack: ItemStack): Boolean {
        return if (!stack.isEmpty) {
            stack.item is ProjectileWeaponItem
        } else {
            false
        }
    }

    @JvmStatic
    fun isHandheldItem(stack: ItemStack): Boolean {
        return if (!stack.isEmpty) {
            ItemClassUtils.isDiggerItem(stack) ||
                    ItemClassUtils.isSwordItem(stack) ||
                    isFishingRodItem(stack) ||
                    setOf(
                        Items.MACE,
                        Items.TRIDENT,
                        Items.STICK,
                        Items.BREEZE_ROD,
                        Items.BLAZE_ROD
                    ).contains(stack.item)
        } else {
            false
        }
    }

    @JvmStatic
    fun isThinBlockItem(stack: ItemStack): Boolean {
        return if (!stack.isEmpty) {
            val item = Block.byItem(stack.item)
            item is CarpetBlock ||
                    item is TrapDoorBlock || item is PressurePlateBlock ||
                    item is SnowLayerBlock || item is DaylightDetectorBlock
        } else {
            false
        }
    }

    @JvmStatic
    fun isSkullBlock(stack: ItemStack): Boolean {
        return if (!stack.isEmpty) {
            Block.byItem(stack.item) is SkullBlock
        } else {
            false
        }
    }

    @JvmStatic
    fun isBlockItemBlacklisted(stack: ItemStack): Boolean {
        return if (!stack.isEmpty) {
            val item = Block.byItem(stack.item)
            item is BannerBlock ||
                    item is RodBlock ||
                    isSkullBlock(stack) ||
                    item is BedBlock
        } else {
            false
        }
    }

    @JvmStatic
    fun isItemBlacklisted(stack: ItemStack): Boolean {
        return if (!stack.isEmpty) {
            val item = stack.item
            ItemClassUtils.isShieldItem(item) ||
                    isBlockItemBlacklisted(stack) ||
                    item is CrossbowItem
        } else {
            false
        }
    }

    @JvmStatic
    fun isSwingItemBlacklisted(stack: ItemStack): Boolean {
        return if (!stack.isEmpty) {
            val item = stack.item
            item is ProjectileItem ||
                    item is BucketItem ||
                    item is ShearsItem ||
                    item is EnderpearlItem
        } else {
            false
        }
    }

    @JvmStatic
    fun isBlock3d(stack: ItemStack, itemStackRenderState: ItemStackRenderState): Boolean {
        return if (!stack.isEmpty) {
            stack.item is BlockItem && itemStackRenderState.isGui3d
        } else {
            false
        }
    }

    @JvmStatic
    fun applyLegacyFirstpersonTransforms(poseStack: PoseStack, direction: Int, runnable: Runnable) {
        poseStack.mulPose(Axis.YP.rotationDegrees(direction * 45.0F))
        poseStack.scale(0.4F, 0.4F, 0.4F)
        runnable.run()
        poseStack.scale(1 / 0.4F, 1 / 0.4F, 1 / 0.4F)
        poseStack.mulPose(Axis.YP.rotationDegrees(direction * -45.0F))
    }

    @JvmStatic
    fun shouldTiltItemPositionsInThirdperson(entityState: EntityRenderState): Boolean {
        return if (AnimatiumConfig.instance().tiltItemPositionsInThirdperson) {
            true
        } else {
            val entity = EntityUtils.getEntityByState(entityState) ?: return false
            if (entity is LivingEntity) {
                AnimatiumConfig.instance().oldThirdpersonSwordBlockingPosition && entity.isBlocking
            } else {
                false
            }
        }
    }

    @JvmStatic
    fun getLegacyDurabilityColorValue(stack: ItemStack): Int {
        val value = (255.0 - stack.damageValue.toDouble() * 255.0 / stack.maxDamage.toDouble())
        return if (!value.isNaN()) {
            value.roundToInt()
        } else {
            0
        }
    }

    @JvmStatic
    fun getOldItemRarity(stack: ItemStack): Rarity {
        return if (listOf(Items.GOLDEN_APPLE, Items.END_CRYSTAL).contains(stack.item)) {
            Rarity.RARE
        } else if (listOf(Items.NETHER_STAR, Items.ELYTRA, Items.DRAGON_HEAD).contains(stack.item)) {
            Rarity.UNCOMMON
        } else if (stack.item == Items.ENCHANTED_GOLDEN_APPLE) {
            Rarity.EPIC
        } else if (stack.item == Items.TRIDENT) {
            Rarity.COMMON
        } else {
            // TODO?: Trims? eh, if someone requests it ig
            stack.rarity
        }
    }

    @JvmStatic
    fun shouldInstantlyReplaceVisibleItem1_8(prevStack: ItemStack, currentStack: ItemStack): Boolean {
        // TODO/NOTE: Apparently 1.7 doesn't do any special checks inside the inventory
        val itemsMatch = ItemStack.isSameItem(prevStack, currentStack)
        val durabilitiesMatch = prevStack.damageValue == currentStack.damageValue
        val countMatch = prevStack.count == currentStack.count
        return (itemsMatch && (!durabilitiesMatch || !countMatch))
    }
}