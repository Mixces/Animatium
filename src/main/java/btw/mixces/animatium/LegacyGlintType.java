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

package btw.mixces.animatium;

import btw.mixces.animatium.util.MathUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.TriState;
import org.joml.Matrix4f;

public final class LegacyGlintType {
    public static final RenderType ITEM_GLINT_LAYER = makeItemGlintLayer(new RenderStateShard.TexturingStateShard(
            "legacy_glint_texturing",
            () -> setupItemGlintTexturing(-50.0F, false, 3000L),
            RenderSystem::resetTextureMatrix
    ), false);

    public static final RenderType ITEM_GLINT_2ND_LAYER = makeItemGlintLayer(new RenderStateShard.TexturingStateShard(
            "legacy_glint_texturing",
            () -> setupItemGlintTexturing(10.0F, true, 4873L),
            RenderSystem::resetTextureMatrix
    ), false);

    public static final RenderType ITEM_GLINT_TRANSLUCENT_LAYER = makeItemGlintLayer(new RenderStateShard.TexturingStateShard(
            "legacy_glint_texturing",
            () -> setupItemGlintTexturing(-50.0F, false, 3000L),
            RenderSystem::resetTextureMatrix
    ), true);

    public static final RenderType ITEM_GLINT_TRANSLUCENT_2ND_LAYER = makeItemGlintLayer(new RenderStateShard.TexturingStateShard(
            "legacy_glint_texturing",
            () -> setupItemGlintTexturing(10.0F, true, 4873L),
            RenderSystem::resetTextureMatrix
    ), true);

    public static final RenderType ENTITY_GLINT_LAYER = makeEntityGlintLayer(new RenderStateShard.TexturingStateShard(
            "legacy_glint_texturing",
            LegacyGlintType::setupEntityGlintTexturing,
            RenderSystem::resetTextureMatrix
    ), false);

    public static final RenderType ENTITY_ARMOR_GLINT_LAYER = makeEntityGlintLayer(new RenderStateShard.TexturingStateShard(
            "legacy_glint_texturing",
            LegacyGlintType::setupEntityGlintTexturing,
            RenderSystem::resetTextureMatrix
    ), true);

    private static RenderType makeItemGlintLayer(RenderStateShard.TexturingStateShard texturingStateShard, boolean translucent) {
        return RenderType.create(
                "legacy_glint" + (translucent ? "_translucent" : ""),
                DefaultVertexFormat.POSITION_TEX,
                VertexFormat.Mode.QUADS,
                1536,
                RenderType.CompositeState.builder()
                        .setShaderState(new RenderStateShard.ShaderStateShard(AnimatiumClient.legacyGlintProgram))
                        .setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ITEM, TriState.DEFAULT, false))
                        .setWriteMaskState(RenderType.COLOR_WRITE)
                        .setCullState(RenderType.CULL)
                        .setDepthTestState(RenderType.EQUAL_DEPTH_TEST)
                        .setTransparencyState(RenderType.GLINT_TRANSPARENCY)
                        .setTexturingState(texturingStateShard)
                        .setOutputState(translucent ? RenderType.ITEM_ENTITY_TARGET : RenderType.MAIN_TARGET)
                        .createCompositeState(false)
        );
    }

    private static RenderType makeEntityGlintLayer(
            RenderStateShard.TexturingStateShard texturingStateShard, boolean armor) {
        return RenderType.create("legacy_" + (armor ? "armor_" : "") + "entity_glint",
                DefaultVertexFormat.POSITION_TEX,
                VertexFormat.Mode.QUADS,
                1536,
                RenderType.CompositeState.builder()
                        .setShaderState(new RenderStateShard.ShaderStateShard(AnimatiumClient.legacyGlintProgram))
                        .setTextureState(new RenderStateShard.TextureStateShard(ItemRenderer.ENCHANTED_GLINT_ITEM, TriState.DEFAULT, false)) // <=1.19.3 uses item glint texture, we will to
                        .setWriteMaskState(RenderType.COLOR_WRITE)
                        .setCullState(RenderType.CULL)
                        .setDepthTestState(RenderType.EQUAL_DEPTH_TEST)
                        .setTransparencyState(RenderType.GLINT_TRANSPARENCY)
                        .setTexturingState(texturingStateShard)
                        .setLayeringState(armor ? RenderType.VIEW_OFFSET_Z_LAYERING : RenderType.NO_LAYERING)
                        .createCompositeState(false)
        );
    }

    private static void setupItemGlintTexturing(float angle, boolean negative, long clampedTime) {
        float g = (Util.getMillis() % clampedTime) / (float) clampedTime / 8.0F;
        RenderSystem.setTextureMatrix(new Matrix4f().scale(8.0F).translate(negative ? -g : g, 0.0F, 0.0F).rotateZ(MathUtils.toRadians(angle)));
    }

    private static void setupEntityGlintTexturing() {
        // TODO: Replace with proper <=1.14 translation/stuff
        // TEMPORARY
        // CODE FROM RenderStateShader#setupGlintTexturing(float)
        long l = (long) ((double) Util.getMillis() * 8.0);
        float g = (float) (l % 110000L) / 110000.0F;
        float h = (float) (l % 30000L) / 30000.0F;
        RenderSystem.setTextureMatrix(new Matrix4f().translation(-g, h, 0.0F).rotateZ((float) (Math.PI / 18)).scale(0.16F));
    }
}
