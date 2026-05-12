#version 330

layout(std140) uniform LightmapInfo {
    float SkyDarken;
    float SkyFlicker;
    float BlockFlicker;
    float SkyDarkness;
    float NightVisionScale;
    float Gamma;
    int UseBrightLightmap;
};

in vec2 texCoord;

out vec4 fragColor;

float getBrightness(int index) {
    float value = 1.0 - float(index) / 15.0;
    return (1.0 - value) / (value * 3.0 + 1.0) + 0.0;
}

float notGamma(float value) {
    float inverted = 1.0 - value;
    return 1.0 - inverted * inverted * inverted * inverted;
}

void main() {
    float skyLight = getBrightness(min(int(texCoord.y * 16), 15)) * SkyFlicker;
    float blockLight = getBrightness(min(int(texCoord.x * 16), 15)) * BlockFlicker;

    float skyV = skyLight * (SkyDarken * 0.65F + 0.35F);
    float blockVA = blockLight * ((blockLight * 0.6F + 0.4F) * 0.6F + 0.4F);
    float blockVB = blockLight * (blockLight * blockLight * 0.6F + 0.4F);

    vec3 color = vec3(skyV + blockLight, skyV + blockVA, skyLight + blockVB);
    color *= vec3(0.96F);
    color += vec3(0.03F, 0.03F, 0.03F);
    if (SkyDarkness > 0.0) {
        color = mix(color, color * vec3(0.7F, 0.6F, 0.6F), SkyDarkness);
    }

    // TODO: Double check if correct, should be
    if (UseBrightLightmap == 1) {
        color = vec3(0.22F + blockLight * 0.75F, 0.28F + blockVA * 0.75F, 0.25F + blockVB * 0.75F);
    }

    if (NightVisionScale > 0.0) {
        color *= mix(1.0, 1.0 / min(min(color.r, color.g), color.b), NightVisionScale);
    }

    color = min(color, vec3(1.0));
    color = mix(color, vec3(notGamma(color.r), notGamma(color.g), notGamma(color.b)), Gamma);
    color = color * 0.96F + 0.03F;
    color = clamp(color, 0.0, 1.0);
    fragColor = vec4(color, 1.0);
}
