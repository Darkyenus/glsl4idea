// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// From section 7.1 (Built-In Language Variables) of the GLSL 4.10
// spec:
//
//     It is also a compilation error to redeclare the [gl_PerVertex]
//     block more than once...
//
// This appears to be a clarification to the behaviour established for
// gl_PerVertex by GLSL 1.50, therefore we test it using GLSL version
// 1.50.

#version 150

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

in gl_PerVertex {
    vec4 gl_Position;
} gl_in[];

in gl_PerVertex {
    vec4 gl_Position;
} gl_in[];

void main()
{
}
