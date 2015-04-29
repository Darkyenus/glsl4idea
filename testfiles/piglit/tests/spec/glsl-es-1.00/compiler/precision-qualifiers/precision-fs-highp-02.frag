// [config]
// expect_result: fail
// glsl_version: 1.00
// [end config]
//
// If high precision is unavailable in the fragment shader, then it should be
// illegal to use it.
//
// From section 4.5.4 of the GLSL 1.00 spec:
//     "The built-in macro GL_FRAGMENT_PRECISION_HIGH is defined to one on
//     systems supporting highp precision in the fragment language
//         #define GL_FRAGMENT_PRECISION_HIGH 1
//     and is not defined on systems not supporting highp precision in the
//     fragment language."

#version 100

#ifndef GL_FRAGMENT_PRECISION_HIGH
highp float x;
#else
#error
#endif

void f() { }
