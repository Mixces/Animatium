#version 150

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

in vec3 Position;

out float cylindricalVertexDistance;
out float sphericalVertexDistance;

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
