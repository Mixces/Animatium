package org.visuals.legacy.animatium.util.rendering.renderer;

import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public record TextureAndSampler(GpuTextureView textureView, GpuSampler sampler) {
    public static TextureAndSampler get(final Identifier location) {
        final TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        final AbstractTexture texture = textureManager.getTexture(location);
        return new TextureAndSampler(texture.getTextureView(), texture.getSampler());
    }

    public static @Nullable TextureAndSampler get(final int index, final TextureSetup setup) {
        final GpuTextureView texture0 = setup.texure0();
        if (index == 0 && texture0 != null) {
            return new TextureAndSampler(texture0, setup.sampler0());
        }

        final GpuTextureView texture1 = setup.texure1();
        if (index == 1 && texture1 != null) {
            return new TextureAndSampler(texture1, setup.sampler1());
        }

        final GpuTextureView texture2 = setup.texure2();
        if (index == 2 && texture2 != null) {
            return new TextureAndSampler(texture2, setup.sampler2());
        }

        return null;
    }
}
