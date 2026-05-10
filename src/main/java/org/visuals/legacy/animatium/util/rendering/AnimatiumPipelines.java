package org.visuals.legacy.animatium.util.rendering;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.BlendFactor;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import org.visuals.legacy.animatium.Animatium;

import java.util.Optional;

public class AnimatiumPipelines {
    // Panorama
    public static final BlendFunction PANORAMA_BLEND = new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA, BlendFactor.ONE, BlendFactor.ZERO);

    public static final RenderPipeline.Snippet TEXTURED_QUAD = RenderPipeline.builder()
            .withBindGroupLayout(BindGroupLayouts.GLOBALS)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .buildSnippet();

    public static final RenderPipeline.Snippet LEGACY_PANORAMA_SNIPPET = RenderPipeline.builder(TEXTURED_QUAD)
            .withVertexShader(Animatium.location("core/legacy_panorama"))
            .withFragmentShader(Animatium.location("core/legacy_panorama"))
            .withDepthStencilState(RenderUtils.NO_DEPTH_WRITE)
            .withCull(false)
            .withVertexBinding(0, DefaultVertexFormat.POSITION)
            .buildSnippet();

    public static final RenderPipeline LEGACY_PANORAMA_1 = RenderPipeline.builder(LEGACY_PANORAMA_SNIPPET)
            .withLocation(Animatium.location("pipeline/legacy_panorama_1"))
            .withColorTargetState(new ColorTargetState(Optional.of(PANORAMA_BLEND), GpuFormat.RGBA8_UNORM, ColorTargetState.WRITE_ALL))
            .build();

    public static final RenderPipeline LEGACY_PANORAMA_2 = RenderPipeline.builder(LEGACY_PANORAMA_SNIPPET)
            .withLocation(Animatium.location("pipeline/legacy_panorama_2"))
            .withColorTargetState(new ColorTargetState(Optional.of(PANORAMA_BLEND), GpuFormat.RGBA8_UNORM, ColorTargetState.WRITE_COLOR))
            .build();

    public static final RenderPipeline LEGACY_PANORAMA_BLUR = RenderPipeline.builder(TEXTURED_QUAD)
            .withLocation(Animatium.location("pipeline/legacy_panorama_blur"))
            .withVertexShader(Animatium.location("core/legacy_panorama_blur"))
            .withFragmentShader(Animatium.location("core/legacy_panorama_blur"))
            .withColorTargetState(new ColorTargetState(Optional.of(PANORAMA_BLEND), GpuFormat.RGBA8_UNORM, ColorTargetState.WRITE_COLOR))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX)
            .build();

    // Items
    public static final RenderPipeline LEGACY_ITEM_CUTOUT = RenderPipelineOverrider.of(RenderPipelines.ITEM_CUTOUT)
            .withLocation(Animatium.location("pipeline/legacy_item_cutout"))
            .withVertexShader(Animatium.location("core/legacy_item"))
            .withShaderDefine("NO_CARDINAL_LIGHTING")
            .build();

    public static final RenderPipeline LEGACY_ITEM_TRANSLUCENT = RenderPipelineOverrider.of(RenderPipelines.ITEM_TRANSLUCENT)
            .withLocation(Animatium.location("pipeline/legacy_item_translucent"))
            .withVertexShader(Animatium.location("core/legacy_item"))
            .build();
}
