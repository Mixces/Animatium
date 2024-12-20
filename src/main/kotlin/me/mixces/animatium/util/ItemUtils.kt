package me.mixces.animatium.util

import net.minecraft.client.render.item.ItemRenderState
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.BlockItem
import net.minecraft.item.CrossbowItem
import net.minecraft.item.FishingRodItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.MaceItem
import net.minecraft.item.MiningToolItem
import net.minecraft.item.ModelTransformationMode
import net.minecraft.item.OnAStickItem
import net.minecraft.item.RangedWeaponItem
import net.minecraft.item.ShieldItem
import net.minecraft.item.SwordItem
import net.minecraft.item.TridentItem
import net.minecraft.util.Rarity
import net.minecraft.util.math.RotationAxis
import java.util.Optional
import kotlin.math.roundToInt

abstract class ItemUtils {
    companion object {
        val STACK: ThreadLocal<ItemStack> = ThreadLocal.withInitial({ null })
        val TRANSFORMATION_MODE: ThreadLocal<ModelTransformationMode> = ThreadLocal.withInitial({ null })

        fun set(stack: ItemStack, transformationMode: ModelTransformationMode) {
            STACK.remove()
            TRANSFORMATION_MODE.remove()
            STACK.set(stack)
            TRANSFORMATION_MODE.set(transformationMode)
        }

        fun getStack(): Optional<ItemStack> {
            return Optional.ofNullable(STACK.get())
        }

        fun getTransformMode(): Optional<ModelTransformationMode> {
            return Optional.ofNullable(TRANSFORMATION_MODE.get())
        }

        fun isFishingRodItem(stack: ItemStack): Boolean {
            return if (!stack.isEmpty) {
                stack.item is FishingRodItem || stack.item is OnAStickItem<*>
            } else {
                false
            }
        }

        fun isRangedWeaponItem(stack: ItemStack): Boolean {
            return if (!stack.isEmpty) {
                stack.item is RangedWeaponItem
            } else {
                false
            }
        }

        fun isHandheldItem(stack: ItemStack): Boolean {
            return if (!stack.isEmpty) {
                // TODO: is this the best way? probably not
                (stack.item is MiningToolItem
                        || stack.item is SwordItem
                        || stack.item is MaceItem
                        || stack.item is TridentItem
                        || isFishingRodItem(stack)
                        || setOf(Items.STICK, Items.BREEZE_ROD, Items.BLAZE_ROD).contains(stack.item))
            } else {
                false
            }
        }

        fun isItemBlacklisted(stack: ItemStack): Boolean {
            return if (!stack.isEmpty) {
                stack.item is ShieldItem || stack.item is CrossbowItem
            } else {
                false
            }
        }

        fun isBlock3d(stack: ItemStack, itemRenderState: ItemRenderState): Boolean {
            return if (!stack.isEmpty) {
                stack.item is BlockItem && itemRenderState.hasDepth()
            } else {
                false
            }
        }

        fun applyLegacyFirstpersonTransforms(matrices: MatrixStack, direction: Int, runnable: Runnable) {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * 45.0F))
            matrices.scale(0.4F, 0.4F, 0.4F)
            runnable.run()
            matrices.scale(1 / 0.4F, 1 / 0.4F, 1 / 0.4F)
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * -45.0F))
        }

        fun applyLegacyThirdpersonTransforms(matrices: MatrixStack, direction: Int, runnable: Runnable) {
            // TODO
            runnable.run()
            // TODO
        }

        fun getLegacyDurabilityColorValue(stack: ItemStack): Int {
            return (255.0 - stack.damage.toDouble() * 255.0 / stack.maxDamage.toDouble()).roundToInt()
        }

        fun getOldItemRarity(stack: ItemStack): Rarity {
            var original = stack.rarity
            if (!stack.isEmpty) {
                // TODO?: Trims? eh, if someone requests it ig
                if (listOf(Items.GOLDEN_APPLE, Items.END_CRYSTAL).contains(stack.item)) {
                    return Rarity.RARE
                } else if (listOf(Items.NETHER_STAR, Items.ELYTRA, Items.DRAGON_HEAD).contains(stack.item)) {
                    return Rarity.UNCOMMON
                } else if (stack.item == Items.ENCHANTED_GOLDEN_APPLE) {
                    return Rarity.EPIC
                } else if (stack.item == Items.TRIDENT) {
                    return Rarity.COMMON
                }
            }

            return original
        }
    }
}