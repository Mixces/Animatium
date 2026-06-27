package org.visuals.legacy.animatium.util.rendering.panorama;

import org.joml.Matrix3x2f;

public record LegacyPanoramaRenderState(Matrix3x2f pose, int width, int height, float spin) {
}
