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

import com.mojang.blaze3d.GpuOutOfMemoryException;
import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.AddressMode;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.TextureFormat;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

// Custom Version of Minecraft MainTarget
public class PanoramaTarget extends RenderTarget {
    private final int FLAGS = 15;

    public PanoramaTarget() {
        super("Panorama", true);

        final MainTarget.Dimension dimension = this.allocateAttachments(256, 256);
        if (this.colorTexture != null) {
            this.colorTexture.setTextureFilter(FilterMode.LINEAR, false);
            this.colorTexture.setAddressMode(AddressMode.CLAMP_TO_EDGE);
        } else {
            throw new IllegalStateException("Missing color textures");
        }

        if (this.depthTexture != null) {
            this.depthTexture.setTextureFilter(FilterMode.LINEAR, false);
            this.depthTexture.setAddressMode(AddressMode.CLAMP_TO_EDGE);
        } else {
            throw new IllegalStateException("Missing depth textures");
        }

        this.width = dimension.width;
        this.height = dimension.height;
        RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(this.getColorTexture(), 0, this.getDepthTexture(), 1.0F);
        this.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
    }

    private MainTarget.Dimension allocateAttachments(int width, int height) {
        RenderSystem.assertOnRenderThread();
        for (MainTarget.Dimension dimension : MainTarget.Dimension.listWithFallback(width, height)) {
            if (this.colorTexture != null) {
                this.colorTexture.close();
                this.colorTexture = null;
                if (this.colorTextureView != null) {
                    this.colorTextureView.close();
                    this.colorTextureView = null;
                }
            }

            if (this.depthTexture != null) {
                this.depthTexture.close();
                this.depthTexture = null;
                if (this.depthTextureView != null) {
                    this.depthTextureView.close();
                    this.depthTextureView = null;
                }
            }

            this.colorTexture = this.allocateColorAttachment(dimension);
            this.depthTexture = this.allocateDepthAttachment(dimension);
            if (this.colorTexture != null && this.depthTexture != null) {
                this.colorTextureView = RenderSystem.getDevice().createTextureView(this.colorTexture);
                this.depthTextureView = RenderSystem.getDevice().createTextureView(this.depthTexture);
                return dimension;
            }
        }

        final String hasColor = this.colorTexture == null ? "missing color" : "have color";
        final String hasDepth = this.depthTexture == null ? "missing depth" : "have depth";
        throw new RuntimeException("Unrecoverable GL_OUT_OF_MEMORY (" + hasColor + ", " + hasDepth + ")");
    }

    @Nullable
    private GpuTexture allocateColorAttachment(MainTarget.Dimension dimension) {
        try {
            return RenderSystem.getDevice().createTexture(() -> this.label + " / Color", FLAGS, TextureFormat.RGBA8, dimension.width, dimension.height, 1, 1);
        } catch (GpuOutOfMemoryException exception) {
            return null;
        }
    }

    @Nullable
    private GpuTexture allocateDepthAttachment(MainTarget.Dimension dimension) {
        try {
            return RenderSystem.getDevice().createTexture(() -> this.label + " / Depth", FLAGS, TextureFormat.DEPTH32, dimension.width, dimension.height, 1, 1);
        } catch (GpuOutOfMemoryException exception) {
            return null;
        }
    }
}