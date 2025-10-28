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

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderSystem;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Optional;

public class DynamicTransformsBuilder {
    private Optional<Matrix4f> modelViewMatrix = Optional.empty();
    private Optional<Vector4f> colorModulator = Optional.empty();
    private Optional<Vector3f> modelOffset = Optional.empty();
    private Optional<Matrix4f> textureMatrix = Optional.empty();

    public static DynamicTransformsBuilder of() {
        return new DynamicTransformsBuilder();
    }

    public DynamicTransformsBuilder withModelViewMatrix(Matrix4f matrix4f) {
        this.modelViewMatrix = Optional.of(matrix4f);
        return this;
    }

    public DynamicTransformsBuilder withShaderColor(Vector4f vector4f) {
        this.colorModulator = Optional.of(vector4f);
        return this;
    }

    public DynamicTransformsBuilder withShaderColor(Vector3f vector3f) {
        return this.withShaderColor(new Vector4f(vector3f, 1.0F));
    }

    public DynamicTransformsBuilder withModelOffset(Vector3f vector3f) {
        this.modelOffset = Optional.of(vector3f);
        return this;
    }

    public DynamicTransformsBuilder withTextureMatrix(Matrix4f matrix4f) {
        this.textureMatrix = Optional.of(matrix4f);
        return this;
    }

    public GpuBufferSlice build() {
        return RenderSystem.getDynamicUniforms().writeTransform(
                this.modelViewMatrix.orElse(RenderSystem.getModelViewMatrix()),
                this.colorModulator.orElse(new Vector4f(1.0F, 1.0F, 1.0F, 1.0F)),
                this.modelOffset.orElse(new Vector3f()),
                this.textureMatrix.orElse(RenderSystem.getTextureMatrix()),
                RenderUtils.getLineWidth(RenderSystem.getShaderLineWidth())
        );
    }
}
