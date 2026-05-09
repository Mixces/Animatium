package org.visuals.legacy.animatium.util.rendering.renderer;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;

public class VertexLayouts {
    public static final VertexLayout POSITIONED_QUAD = new VertexLayout(DefaultVertexFormat.POSITION, PrimitiveTopology.QUADS);
    public static final VertexLayout TEXTURED_QUAD = new VertexLayout(DefaultVertexFormat.POSITION_TEX, PrimitiveTopology.QUADS);
}
