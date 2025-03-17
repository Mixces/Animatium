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

package btw.mixces.animatium.mixins.renderer;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.ItemUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

// Priority set to 1500 to fix Vulkan Mod Incompatibility
@Mixin(value = VertexConsumer.class, priority = 1500)
public interface MixinVertexConsumer {
    // TODO: this is only half of the battle + framed item 2d colors are disabled
    @ModifyArgs(method = "putBulkData(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/block/model/BakedQuad;[FFFFF[IIZ)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack$Pose;transformNormal(FFFLorg/joml/Vector3f;)Lorg/joml/Vector3f;"), require = 0)
    default void animatium$itemColors2D(Args args) {
        ItemStackRenderState state = ItemUtils.getRenderState();
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().itemColors2D && state != null && !state.isGui3d()) {
            ItemDisplayContext displayContext = ItemUtils.getDisplayContext();
            if (displayContext == ItemDisplayContext.GROUND) {
                args.set(1, (float) args.get(2));
                args.set(2, (float) args.get(1));
            }
        }
    }
}
