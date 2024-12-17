package me.mixces.animatium.mixins.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import me.mixces.animatium.config.AnimatiumConfig;
import me.mixces.animatium.mixins.accessor.ClientWorldPropertiesAccessor;
import me.mixces.animatium.mixins.accessor.SkyRenderingAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.SkyRendering;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldRenderer.class, priority = Integer.MAX_VALUE)
public abstract class MixinWorldRenderer {
    // TODO: Iris functionality/support?
    // TODO: Make blue void work in FabricSkyBoxes/Nuit

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Nullable
    private ClientWorld world;

    @Shadow
    @Final
    private SkyRendering skyRendering;

    @Inject(method = "method_62215", at = @At(value = "TAIL"))
    private void animatium$oldBlueVoidSky(Fog fog, DimensionEffects.SkyType skyType, float tickDelta, DimensionEffects dimensionEffects, CallbackInfo ci, @Local MatrixStack matrices) {
        if (AnimatiumConfig.oldBlueVoidSky && skyType != DimensionEffects.SkyType.END) {
            assert this.client.player != null;
            assert this.world != null;
            // can't get it via local, so have to re-get it this way
            int skyColor = this.world.getSkyColor(this.client.gameRenderer.getCamera().getPos(), tickDelta);
            this.animatium$renderSkyBlueVoid(matrices, skyColor, this.client.player.getCameraPosVec(tickDelta).y - animatium$getHorizonHeight(this.world));
        }
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/DimensionEffects;getCloudsHeight()F"))
    private float animatium$oldCloudHeight(DimensionEffects instance, Operation<Float> original) {
        if (AnimatiumConfig.oldCloudHeight) {
            return instance.getSkyType() == DimensionEffects.SkyType.END ? 8.0F : 128.0F;
        } else {
            return original.call(instance);
        }
    }

    @Unique
    private void animatium$renderSkyBlueVoid(MatrixStack matrices, int skyColor, double depth) {
        // TODO/NOTE: If Statement is a fix for it showing below y 0/-64, supposedly/(not entirely) accurate functionality
        if (depth >= 0.0) {
            ShaderProgram shaderProgram = RenderSystem.setShader(ShaderProgramKeys.POSITION);

            assert this.world != null;
            Vector3f skyColorVec = ColorHelper.toVector(skyColor);
            if (this.world.getDimensionEffects().isAlternateSkyColor()) {
                RenderSystem.setShaderColor(skyColorVec.x * 0.2F + 0.04F, skyColorVec.y * 0.2F + 0.04F, skyColorVec.z * 0.6F + 0.1F, 1.0F);
            } else {
                RenderSystem.setShaderColor(skyColorVec.x, skyColorVec.y, skyColorVec.z, 1.0F);
            }

            matrices.push();
            matrices.multiplyPositionMatrix(RenderSystem.getModelViewMatrix());
            matrices.translate(0.0F, -((float) (depth - 16.0)), 0.0F);
            SkyRenderingAccessor skyRenderingAccessor = (SkyRenderingAccessor) this.skyRendering;
            skyRenderingAccessor.getDarkSkyBuffer().bind();
            skyRenderingAccessor.getDarkSkyBuffer().draw(matrices.peek().getPositionMatrix(), RenderSystem.getProjectionMatrix(), shaderProgram);
            VertexBuffer.unbind();
            matrices.pop();

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Unique
    public double animatium$getHorizonHeight(ClientWorld world) {
        if (((ClientWorldPropertiesAccessor) world.getLevelProperties()).isFlatWorld()) {
            return AnimatiumConfig.oldSkyHorizonHeight ? 0.0D : world.getBottomY();
        } else {
            return 63.0D;
        }
    }
}