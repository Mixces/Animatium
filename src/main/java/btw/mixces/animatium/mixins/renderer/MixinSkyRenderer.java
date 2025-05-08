package btw.mixces.animatium.mixins.renderer;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import btw.mixces.animatium.util.RenderUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.SkyRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRenderer.class)
public abstract class MixinSkyRenderer {
    @Unique
    private static GpuBuffer animatium$topSkyBuffer = null;

    @Unique
    private static RenderSystem.AutoStorageIndexBuffer animatium$skyIndexBuffer;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        animatium$topSkyBuffer = RenderUtils.initializeSky((builder) -> RenderUtils.buildSkyHalf(builder, 16.0F, false));
        animatium$skyIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
    }

    @WrapOperation(method = "renderSkyDisc", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setPipeline(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V"))
    private void animatium$planarFogPipeline$skyDisc(RenderPass instance, RenderPipeline renderPipeline, Operation<Void> original) {
        RenderPipeline pipeline = renderPipeline;
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().planarSkyFog) {
            pipeline = AnimatiumClient.LEGACY_SKY_PLANAR_FOG_PIPELINE;
        }

        original.call(instance, pipeline);
    }

    @WrapOperation(method = "renderSkyDisc", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setVertexBuffer(ILcom/mojang/blaze3d/buffers/GpuBuffer;)V", ordinal = 0))
    private void animatium$planarFogPipeline$skyDisc$vertexBuffer(RenderPass instance, int index, GpuBuffer gpuBuffer, Operation<Void> original) {
        GpuBuffer buffer = gpuBuffer;
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().planarSkyFog) {
            buffer = animatium$topSkyBuffer;
            instance.setIndexBuffer(animatium$skyIndexBuffer.getBuffer(6), animatium$skyIndexBuffer.type());
        }

        original.call(instance, index, buffer);
    }

    @WrapOperation(method = "renderDarkDisc", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setPipeline(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V"))
    private void animatium$planarFogPipeline$darkSkyDisc(RenderPass instance, RenderPipeline renderPipeline, Operation<Void> original) {
        RenderPipeline pipeline = renderPipeline;
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().planarSkyFog) {
            pipeline = AnimatiumClient.LEGACY_SKY_PLANAR_FOG_PIPELINE;
        }

        original.call(instance, pipeline);
    }

    @WrapOperation(method = "renderSkyDisc", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;draw(II)V", ordinal = 0))
    private void animatium$planarFogPipeline$skyDisc$draw(RenderPass instance, int i, int j, Operation<Void> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().planarSkyFog) {
            instance.drawIndexed(i, 1014);
        } else {
            original.call(instance, i, j);
        }
    }

    @WrapOperation(method = "renderDarkDisc", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setVertexBuffer(ILcom/mojang/blaze3d/buffers/GpuBuffer;)V", ordinal = 0))
    private void animatium$planarFogPipeline$darkSkyDisc$vertexBuffer(RenderPass instance, int index, GpuBuffer gpuBuffer, Operation<Void> original) {
        GpuBuffer buffer = gpuBuffer;
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().planarSkyFog) {
            buffer = RenderUtils.getBlueVoidBuffer();
            instance.setIndexBuffer(animatium$skyIndexBuffer.getBuffer(6), animatium$skyIndexBuffer.type());
        }

        original.call(instance, index, buffer);
    }

    @WrapOperation(method = "renderDarkDisc", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;draw(II)V", ordinal = 0))
    private void animatium$planarFogPipeline$darkSkyDisc$draw(RenderPass instance, int i, int j, Operation<Void> original) {
        if (AnimatiumClient.isEnabled() && AnimatiumConfig.instance().planarSkyFog) {
            instance.drawIndexed(i, 1014);
        } else {
            original.call(instance, i, j);
        }
    }
}
