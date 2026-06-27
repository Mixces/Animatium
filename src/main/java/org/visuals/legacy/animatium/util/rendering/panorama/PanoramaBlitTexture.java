package org.visuals.legacy.animatium.util.rendering.panorama;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.util.ARGB;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record PanoramaBlitTexture(Matrix3x2f pose, GpuTextureView textureView,
                                  int width, int height) implements GuiElementRenderState {
    @Override
    public void buildVertices(final VertexConsumer vertexConsumer) {
        final int color = ARGB.white(1.0F);
        final float aspect = this.width > this.height ? 120.0F / this.width : 120.0F / this.height;
        final float sw = this.width * aspect / 256.0F;
        final float sh = this.height * aspect / 256.0F;
        vertexConsumer.addVertexWith2DPose(this.pose, 0.0F, this.height).setUv(0.5F - sh, 0.5F + sw).setColor(color);
        vertexConsumer.addVertexWith2DPose(this.pose, this.width, this.height).setUv(0.5F - sh, 0.5F - sw).setColor(color);
        vertexConsumer.addVertexWith2DPose(this.pose, this.width, 0.0F).setUv(0.5F + sh, 0.5F - sw).setColor(color);
        vertexConsumer.addVertexWith2DPose(this.pose, 0.0F, 0.0F).setUv(0.5F + sh, 0.5F + sw).setColor(color);
    }

    @Override
    public @NonNull RenderPipeline pipeline() {
        return RenderPipelines.GUI_TEXTURED;
    }

    @Override
    public @NonNull TextureSetup textureSetup() {
        return TextureSetup.singleTexture(this.textureView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
    }

    @Override
    public @Nullable ScreenRectangle scissorArea() {
        return null;
    }

    @Override
    public @NonNull ScreenRectangle bounds() {
        return new ScreenRectangle(0, 0, this.width, this.height);
    }
}
