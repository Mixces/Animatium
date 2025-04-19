#version 150

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:dynamictransforms.glsl>

uniform sampler2D Sampler0;

in float vertexDistance;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * ColorModulator;
    if (color.a < 0.1) {
        discard;
    }

    vec3 GlintColor = vec3(0.5019607843137255F, 0.25098039215686274F, 0.8F);

    float fade = linear_fog_fade(vertexDistance, FogStart, FogEnd);
    fragColor = vec4((color.rgb * GlintColor.rgb) * fade, color.a);
}
