#version 150

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

in vec3 Position;

out float cylindricalVertexDistance;
out float sphericalVertexDistance;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
#ifdef PLANAR_FOG
    cylindricalVertexDistance = gl_Position.z;
    sphericalVertexDistance = gl_Position.z;
#else
    cylindricalVertexDistance = fog_cylindrical_distance(Position);
    sphericalVertexDistance = fog_spherical_distance(Position);
#endif
}
