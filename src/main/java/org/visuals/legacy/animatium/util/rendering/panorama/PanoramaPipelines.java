package org.visuals.legacy.animatium.util.rendering.panorama;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.experimental.UtilityClass;
import net.minecraft.client.renderer.RenderPipelines;
import org.visuals.legacy.animatium.Animatium;

@UtilityClass
public class PanoramaPipelines {
	public BlendFunction PANORAMA_BLEND = new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);

	public RenderPipeline LEGACY_PANORAMA =
			RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
					.withLocation(Animatium.location("pipeline/legacy_panorama"))
					.withVertexShader(Animatium.location("core/legacy_panorama"))
					.withFragmentShader(Animatium.location("core/legacy_panorama"))
					.withCull(false)
					.withDepthWrite(false)
					.withBlend(PANORAMA_BLEND)
					// .withColorWrite(true, false) // TODO/NOTE: Causes it to not render (alpha becomes 0.0?!??!?!)
					.withSampler("Sampler0")
					.withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
					.build();

	public RenderPipeline LEGACY_PANORAMA_BLUR =
			RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET)
					.withLocation(Animatium.location("pipeline/legacy_panorama_blur"))
					.withVertexShader(Animatium.location("core/legacy_panorama_blur"))
					.withFragmentShader(Animatium.location("core/legacy_panorama_blur"))
					.withBlend(PANORAMA_BLEND)
					.withColorWrite(true, false)
					.build();
}
