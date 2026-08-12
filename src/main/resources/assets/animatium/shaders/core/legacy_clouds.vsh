#version 150

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

in vec3 Position;
in vec4 Color;

out float cylindricalVertexDistance;
out float sphericalVertexDistance;
out vec4 vertexColor;

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
