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
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.entity.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(RenderStateShard.TextureStateShard.class)
public class MixinTextureStateShard {
    // TODO/NOTE: Do we need this when we have MixinTextureManager
    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Optional;of(Ljava/lang/Object;)Ljava/util/Optional;"))
    private Optional<Object> animatium$useItemGlint(Object value, Operation<Optional<Object>> original) {
        if (AnimatiumClient.getEnabled() && AnimatiumConfig.instance().getForceItemGlintOnEntity() && value == ItemRenderer.ENCHANTED_GLINT_ENTITY) {
            return Optional.of(ItemRenderer.ENCHANTED_GLINT_ITEM);
        } else {
            return original.call(value);
        }
    }
}
