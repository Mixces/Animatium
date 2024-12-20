package me.mixces.animatium.util

import me.mixces.animatium.config.AnimatiumConfig
import net.minecraft.client.render.entity.state.EntityRenderState
import net.minecraft.client.render.item.ItemRenderState
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
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
import kotlin.math.roundToInt

abstract class ItemUtils {
    companion object {
        val STACK: ThreadLocal<ItemStack> = ThreadLocal.withInitial { null }
        val TRANSFORMATION_MODE: ThreadLocal<ModelTransformationMode> = ThreadLocal.withInitial { null }

        @JvmStatic
        fun set(stack: ItemStack, transformationMode: ModelTransformationMode) {
            STACK.remove()
            TRANSFORMATION_MODE.remove()
            STACK.set(stack)
            TRANSFORMATION_MODE.set(transformationMode)
        }

        @JvmStatic
        fun getStack(): ItemStack? {
            return STACK.get()
        }

        @JvmStatic
        fun getTransformMode(): ModelTransformationMode? {
            return TRANSFORMATION_MODE.get()
        }

        @JvmStatic
        fun isFishingRodItem(stack: ItemStack): Boolean {
            return if (!stack.isEmpty) {
                stack.item is FishingRodItem || stack.item is OnAStickItem<*>
            } else {
                false
            }
        }

        @JvmStatic
        fun isRangedWeaponItem(stack: ItemStack): Boolean {
            return if (!stack.isEmpty) {
                stack.item is RangedWeaponItem
            } else {
                false
            }
        }

        @JvmStatic
        fun isHandheldItem(stack: ItemStack): Boolean {
            return if (!stack.isEmpty) {
                // TODO: is this the best way? probably not
                stack.item is MiningToolItem || stack.item is SwordItem
                        || stack.item is MaceItem || stack.item is TridentItem
                        || isFishingRodItem(stack)
                        || setOf(Items.STICK, Items.BREEZE_ROD, Items.BLAZE_ROD).contains(stack.item)
            } else {
                false
            }
        }

        @JvmStatic
        fun isItemBlacklisted(stack: ItemStack): Boolean {
            return if (!stack.isEmpty) {
                stack.item is ShieldItem || stack.item is CrossbowItem
            } else {
                false
            }
        }

        @JvmStatic
        fun isBlock3d(stack: ItemStack, itemRenderState: ItemRenderState): Boolean {
            return if (!stack.isEmpty) {
                stack.item is BlockItem && itemRenderState.hasDepth()
            } else {
                false
            }
        }

        @JvmStatic
        fun applyLegacyFirstpersonTransforms(matrices: MatrixStack, direction: Int, runnable: Runnable) {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * 45.0F))
            matrices.scale(0.4F, 0.4F, 0.4F)
            runnable.run()
            matrices.scale(1 / 0.4F, 1 / 0.4F, 1 / 0.4F)
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction * -45.0F))
        }

        @JvmStatic
        fun applyLegacyThirdpersonTransforms(matrices: MatrixStack, direction: Int, runnable: Runnable) {
            // TODO
            runnable.run()
            // TODO
        }

        @JvmStatic
        // TODO: jesus this name
        fun shouldTiltItemPositionsInThirdperson(entityState: EntityRenderState): Boolean {
            return if (AnimatiumConfig.getInstance().tiltItemPositionsInThirdperson) {
                true
            } else {
                val optionalEntity = EntityUtils.getEntityByState(entityState)
                if (optionalEntity.isEmpty) {
                    false
                } else {
                    val entity = optionalEntity.get()
                    if (entity is LivingEntity) {
                        AnimatiumConfig.getInstance().legacyThirdpersonSwordBlockingPosition && entity.isBlocking
                    } else {
                        false
                    }
                }
            }
        }

        @JvmStatic
        fun getLegacyDurabilityColorValue(stack: ItemStack): Int {
            return (255.0 - stack.damage.toDouble() * 255.0 / stack.maxDamage.toDouble()).roundToInt()
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
    }
}