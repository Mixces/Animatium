package org.visuals.legacy.animatium.util.rendering;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class DynamicTransforms {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private @Nullable Matrix4f modelViewMatrix = null;
        private @Nullable Matrix4f textureMatrix = null;
        private Vector4f shaderColor = new Vector4f(1.0F);
        private Vector3f modelOffset = new Vector3f();

        Builder() {
        }

        public Matrix4f getModelViewMatrix() {
            return this.modelViewMatrix == null ? RenderSystem.getModelViewMatrixCopy() : this.modelViewMatrix;
        }

        public Builder withModelViewMatrix(final Matrix4f matrix4f) {
            this.modelViewMatrix = matrix4f;
            return this;
        }

        public Matrix4f getTextureMatrix() {
            return this.textureMatrix == null ? new Matrix4f() : this.textureMatrix;
        }

        public Builder withTextureMatrix(final Matrix4f matrix4f) {
            this.textureMatrix = matrix4f;
            return this;
        }

        public Builder withShaderColor(final Vector4f vector4f) {
            this.shaderColor = vector4f;
            return this;
        }

        public Builder withShaderColor(final float red, final float green, final float blue, final float alpha) {
            return this.withShaderColor(new Vector4f(red, green, blue, alpha));
        }

        public Builder withShaderColor(final float red, final float green, final float blue) {
            return this.withShaderColor(red, green, blue, 1.0F);
        }

        public Builder withShaderColor(final int color) {
            return this.withShaderColor(ARGB.redFloat(color), ARGB.greenFloat(color), ARGB.blueFloat(color), ARGB.alphaFloat(color));
        }

        public Builder withModelOffset(final Vector3f vector3f) {
            this.modelOffset = vector3f;
            return this;
        }

        public GpuBufferSlice build() {
            return RenderSystem.getDynamicUniforms().writeTransform(this.getModelViewMatrix(), this.shaderColor, this.modelOffset, this.getTextureMatrix());
        }
    }
}