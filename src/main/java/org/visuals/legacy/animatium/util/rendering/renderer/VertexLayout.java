package org.visuals.legacy.animatium.util.rendering.renderer;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public record VertexLayout(@NonNull VertexFormat vertexFormat, @NonNull PrimitiveTopology primitiveTopology) {
    public static VertexLayout of(final RenderPipeline pipeline) {
        return new VertexLayout(Objects.requireNonNull(pipeline.getVertexFormatBinding(0)), pipeline.getPrimitiveTopology());
    }
}
