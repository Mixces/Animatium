#version 330
#extension GL_ARB_separate_shader_objects : require

#include <minecraft:fog.glsl>
#include <minecraft:dynamictransforms.glsl>
#include <minecraft:projection.glsl>

layout(location = 0) in vec3 Position;
layout(location = 1) in vec4 Color;

layout(location = 0) out float cylindricalVertexDistance;
layout(location = 1) out float sphericalVertexDistance;
layout(location = 2) out vec4 vertexColor;

void main() {
    vec3 pos = Position + ModelOffset;
    vec4 eye = ModelViewMat * vec4(pos, 1.0);
#ifdef PLANAR_FOG
    float dist = abs(eye.z);
    cylindricalVertexDistance = dist;
    sphericalVertexDistance = dist;
#else
    cylindricalVertexDistance = fog_cylindrical_distance(pos);
    sphericalVertexDistance = fog_spherical_distance(pos);
#endif
    gl_Position = ProjMat * eye;
    vertexColor = Color * ColorModulator;
}
