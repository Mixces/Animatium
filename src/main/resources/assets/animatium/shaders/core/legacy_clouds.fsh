#version 150

#moj_import <minecraft:fog.glsl>

in float cylindricalVertexDistance;
in float sphericalVertexDistance;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    fragColor = apply_fog(vertexColor, sphericalVertexDistance, cylindricalVertexDistance, 0.0, FogCloudsEnd, FogCloudsEnd, FogCloudsEnd, FogColor);
}