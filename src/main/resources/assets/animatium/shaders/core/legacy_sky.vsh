#version 330
#extension GL_ARB_separate_shader_objects : require

#include <minecraft:fog.glsl>
#include <minecraft:dynamictransforms.glsl>
#include <minecraft:projection.glsl>

layout(location = 0) in vec3 Position;

layout(location = 0) out float cylindricalVertexDistance;
layout(location = 1) out float sphericalVertexDistance;

void main() {
    vec4 eye = ModelViewMat * vec4(Position, 1.0);
#ifdef PLANAR_FOG
    float dist = abs(eye.z);
    cylindricalVertexDistance = dist;
    sphericalVertexDistance = dist;
#else
    cylindricalVertexDistance = fog_cylindrical_distance(Position);
    sphericalVertexDistance = fog_spherical_distance(Position);
#endif
    gl_Position = ProjMat * eye;
}
