// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// From section 7.1 (Built-In Language Variables) of the GLSL 4.10
// spec:
//
//     If a built-in interface block is redeclared, it must appear in
//     the shader before any use of any member included in the
//     built-in declaration, or a compilation error will result.
//
// This appears to be a clarification to the behaviour established for
// gl_PerVertex by GLSL 1.50, therefore we test it using GLSL version
// 1.50.
//
// In this test the variable that we attempt to use before redeclaring
// gl_PerVertex is included in the redeclaration of gl_PerVertex.

#version 150

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

void foo()
{
  gl_PointSize = 1.0;
}

out gl_PerVertex {
    vec4 gl_Position;
    float gl_PointSize;
};

void main()
{
}
