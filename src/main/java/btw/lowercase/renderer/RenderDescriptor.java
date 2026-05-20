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

package btw.lowercase.renderer;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderPassDescriptor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public record RenderDescriptor(Supplier<String> name, GpuTextureView colorTexture,
                               @Nullable GpuTextureView depthTexture, Area area) {
    public static Builder builder(final Supplier<String> name) {
        return new Builder(name);
    }

    public RenderPassDescriptor vanilla() {
        final RenderPassDescriptor descriptor = RenderPassDescriptor.create(this.name);
        descriptor.withColorAttachment(this.colorTexture);
        if (this.depthTexture != null) {
            descriptor.withDepthAttachment(this.depthTexture);
        }

        descriptor.withRenderArea(this.area.vanilla());
        return descriptor;
    }

    public void render(final Consumer<Renderer> pass) {
        try (final Renderer renderer = Renderer.of(this)) {
            pass.accept(renderer);
        }
    }

    public record Area(int x, int y, int width, int height) {
        public Area(final int width, final int height) {
            this(0, 0, width, height);
        }

        public Area(final RenderTarget renderTarget) {
            this(renderTarget.width, renderTarget.height);
        }

        public Area(final GpuTextureView textureView) {
            this(textureView.getWidth(0), textureView.getHeight(0));
        }

        public RenderPass.RenderArea vanilla() {
            return new RenderPass.RenderArea(this.x, this.y, this.width, this.height);
        }
    }

    public static final class Builder {
        private final Supplier<String> name;
        private GpuTextureView colorTexture;
        private @Nullable GpuTextureView depthTexture;
        private @Nullable Area area;

        Builder(final Supplier<String> name) {
            this.name = name;
        }

        public Builder withRenderTarget(final RenderTarget renderTarget, final boolean ignoreGlobalOverrides) {
            this.colorTexture = renderTarget.getColorTextureView();
            this.depthTexture = renderTarget.useDepth ? renderTarget.getDepthTextureView() : null;
            if (!ignoreGlobalOverrides) {
                if (RenderSystem.outputColorTextureOverride != null) {
                    this.colorTexture = RenderSystem.outputColorTextureOverride;
                }

                if (RenderSystem.outputDepthTextureOverride != null && renderTarget.useDepth) {
                    this.depthTexture = RenderSystem.outputDepthTextureOverride;
                }
            }

            this.area = new Area(0, 0, renderTarget.width, renderTarget.height);
            return this;
        }

        public Builder withRenderTarget(final RenderTarget renderTarget) {
            return this.withRenderTarget(renderTarget, true);
        }

        public Builder withColorTexture(final GpuTextureView colorTexture) {
            this.colorTexture = colorTexture;
            return this;
        }

        public Builder withDepthTexture(final @Nullable GpuTextureView depthTexture) {
            this.depthTexture = depthTexture;
            return this;
        }

        public Builder withArea(final Area area) {
            this.area = area;
            return this;
        }

        public RenderDescriptor build() {
            if (this.colorTexture == null) {
                throw new RuntimeException("Color texture target must not be null!");
            } else {
                return new RenderDescriptor(this.name, this.colorTexture, this.depthTexture, this.area);
            }
        }
    }
}
