#version 330

layout(std140) uniform LightmapInfo {
    float SkyDarken;
    float SkyFlicker;
    float BlockFlicker;
    float SkyDarkness;
    int UseBrightLightmap;
    float NightVisionScale;
    float Gamma;
};

in vec2 texCoord;

out vec4 fragColor;

float getBrightness(int index) {
    float value = 1.0F - float(index) / 15.0F;
    return (1.0F - value) / (value * 3.0F + 1.0F) + 0.0F;
}

float notGamma(float value) {
    float inverted = 1.0F - value;
    return 1.0F - inverted * inverted * inverted * inverted;
}

vec3 clampColor(vec3 color) {
    return vec3(clamp(color.x, 0.0F, 1.0F), clamp(color.y, 0.0F, 1.0F), clamp(color.z, 0.0F, 1.0F));
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
    if (SkyDarkness > 0.0F) {
        color = mix(color, color * vec3(0.7F, 0.6F, 0.6F), SkyDarkness);
    }

    // TODO: Double check if correct, should be
    if (UseBrightLightmap == 1) {
        color = vec3(0.22F + blockLight * 0.75F, 0.28F + blockVA * 0.75F, 0.25F + blockVB * 0.75F);
    }

    // TODO: Figure out why lightmap goes full white (this code is correct and when night-vision starts fading. it shows the right lightmap behind the white while flashing)
    if (NightVisionScale > 0.0) {
        float scale = 1.0F / max(color.x, max(color.y, color.z));
        color = mix(color, color * scale, NightVisionScale);
    }

    color = min(color, vec3(1.0F));
    color = mix(color, vec3(notGamma(color.x), notGamma(color.y), notGamma(color.z)), Gamma);
    color = mix(color, vec3(0.75F), 0.04F);
    color = clampColor(color);
    fragColor = vec4(color.rgb, 1.0);
}
