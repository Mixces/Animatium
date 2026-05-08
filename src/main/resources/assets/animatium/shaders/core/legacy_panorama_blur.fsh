#version 150

uniform sampler2D Sampler0;

in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec4 color = vec4(0.0);
    float total = 0.0;
    for (int cycle = 0; cycle < 3; cycle++) {
        float weight = 1.0 / float(cycle + 1);
        float growth = (float(cycle) - 1.5) / 256.0;
        color += texture(Sampler0, texCoord0 + vec2(growth, 0.0)) * weight;
        total += weight;
    }

    fragColor = color / total;
}