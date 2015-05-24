// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: false
// [end config]
//
// From section 7.1.1 (Compatibility Profile Built-In Language
// Variables) of the GLSL 4.10 spec:
//
//     However, when a built-in interface block with an instance name
//     is redeclared (e.g., gl_in), the instance name must be included
//     in the redeclaration. It is an error to not include the
//     built-in instance name or to change its name.
//
// Note: although this text appears in a section referring to
// compatibility profile variables, it's clear from context that it's
// meant to apply to any redeclaration of gl_in, whether it is done in
// a compatibility or a core profile.
//
// Although not explicitly stated, it seems logical to apply the
// converse rule to redeclaring the gl_PerVertex output; in other
// words, the gl_PerVertex output must be redeclared *without* and
// instance name (and hence, as a non-array).
//
// This appears to be a clarification to the behaviour established for
// gl_PerVertex by GLSL 1.50, therefore we test it using GLSL version
// 1.50.
//
// In this test, we try redeclaraing the gl_PerVertex input as an array.

#version 150

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

out gl_PerVertex {
  vec4 gl_Position;
} foo[3];

void main()
{
}
