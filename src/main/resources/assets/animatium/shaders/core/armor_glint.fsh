#version 330
#extension GL_ARB_separate_shader_objects : require

#include <minecraft:fog.glsl>
#include <minecraft:globals.glsl>
#include <minecraft:dynamictransforms.glsl>

uniform sampler2D Sampler0;

layout(location = 0) in float sphericalVertexDistance;
layout(location = 1) in float cylindricalVertexDistance;
layout(location = 2) in vec2 texCoord0;
layout(location = 3) in vec4 overlayColor;

layout(location = 0) out vec4 fragColor;

// Shader source from 26.2 glint.fsh modified
void main() {
    vec4 color = texture(Sampler0, texCoord0) * ColorModulator;
    if (color.a < 0.1) {
        discard;
    } else {
        float fade = (1.0f - total_fog_value(sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd)) * GlintAlpha;
        fragColor = vec4(mix(overlayColor.rgb, color.rgb, overlayColor.a) * fade, color.a);
    }
}