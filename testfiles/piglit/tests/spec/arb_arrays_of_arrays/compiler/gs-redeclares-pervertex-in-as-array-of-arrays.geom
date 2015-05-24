// From section 7.1.1 (Compatibility Profile Built-In Language
// Variables) of the GLSL 4.10 spec:
//
//     However, when a built-in interface block with an instance name
//     is redeclared (e.g., gl_in), the instance name must be included
//     in the redeclaration. It is an error to not include the
//     built-in instance name or to change its name.  For example,
//
//     in gl_PerVertex {
//         vec4  gl_ClipVertex;
//         vec4  gl_FrontColor;
//     } gl_in[];  // must be present and must be "gl_in[]"
//
// Note: although this text appears in a section referring to
// compatibility profile variables, it's clear from context that it's
// meant to apply to any redeclaration of gl_in, whether it is done in
// a compatibility or a core profile.
//
// This appears to be a clarification to the behaviour established for
// gl_PerVertex by GLSL 1.50, therefore we test it using GLSL version
// 1.50.
//
// In this test, we try redeclaraing the gl_PerVertex input as an
// array of arrays.
//
// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_arrays_of_arrays
// check_link: false
// [end config]

#version 150
#extension GL_ARB_arrays_of_arrays: enable

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

in gl_PerVertex {
  vec4 gl_Position;
} gl_in[][6];

void main()
{
}
