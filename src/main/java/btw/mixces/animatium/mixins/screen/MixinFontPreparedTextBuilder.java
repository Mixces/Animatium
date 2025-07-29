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

package btw.mixces.animatium.mixins.screen;

import btw.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.gui.Font;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Font.PreparedTextBuilder.class)
public abstract class MixinFontPreparedTextBuilder {
    @Unique
    private static final float animatium$strikethroughOffset = 0.5F;

    @ModifyArg(method = "accept(ILnet/minecraft/network/chat/Style;Lnet/minecraft/client/gui/font/glyphs/BakeableGlyph;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/font/glyphs/BakedGlyph$Effect;<init>(FFFFFIIF)V", ordinal = 0), index = 1)
    private float animatium$fixTextStrikethroughStyle$minY(float minY) {
        if (AnimatiumConfig.instance().fixTextStrikethroughStyle) {
            return minY - animatium$strikethroughOffset;
        } else {
            return minY;
        }
    }

    @ModifyArg(method = "accept(ILnet/minecraft/network/chat/Style;Lnet/minecraft/client/gui/font/glyphs/BakeableGlyph;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/font/glyphs/BakedGlyph$Effect;<init>(FFFFFIIF)V", ordinal = 0), index = 3)
    private float animatium$fixTextStrikethroughStyle$maxY(float maxY) {
        if (AnimatiumConfig.instance().fixTextStrikethroughStyle) {
            return maxY - animatium$strikethroughOffset;
        } else {
            return maxY;
        }
    }
}
