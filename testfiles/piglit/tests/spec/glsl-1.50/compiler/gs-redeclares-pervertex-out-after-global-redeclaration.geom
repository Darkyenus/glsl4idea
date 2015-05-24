// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// From section 7.1 (Built-In Language Variables) of the GLSL 4.10
// spec:
//
//     Also, if a built-in interface block is redeclared, no member of
//     the built-in declaration can be redeclared outside the block
//     redeclaration.
//
// This appears to be a clarification to the behaviour established for
// gl_PerVertex by GLSL 1.50, therefore we test it using GLSL version
// 1.50.
//
// In this test we attempt to redeclare gl_PerVertex after having
// redeclared gl_ClipDistance globally, and gl_ClipDistance is present
// in the redeclaration of gl_PerVertex.

#version 150

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

out float gl_ClipDistance[];

out gl_PerVertex {
    vec4 gl_Position;
    float gl_ClipDistance[4];
};

void main()
{
}
