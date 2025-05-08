#version 150

#moj_import <minecraft:fog.glsl>

in vec3 Position;

uniform mat4 ProjMat;
uniform mat4 ModelViewMat;
uniform int FogShape;

out float vertexDistance;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
#ifdef PLANAR_FOG
    vertexDistance = gl_Position.z;
#else
    vertexDistance = fog_distance(Position, FogShape);
#endif
}
