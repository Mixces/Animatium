#version 150

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>

uniform sampler2D Sampler0;

in float cylindricalVertexDistance;
in float sphericalVertexDistance;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * ColorModulator;
    if (color.a < 0.1) {
        discard;
    }

    vec3 GlintColor = vec3(0.5019607843137255F, 0.25098039215686274F, 0.8F);
    float fade = (1.0F - total_fog_value(sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd));
    fragColor = vec4((color.rgb * GlintColor.rgb) * fade, color.a);
}
