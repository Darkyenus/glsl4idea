// [config]
// expect_result: fail
// glsl_version: 1.20
//
// [end config]

/* FAIL -
 *
 * From page 27 (page 33 of the PDF) of the GLSL 1.20 spec:
 *
 *     "Only variables output from a vertex shader can be candidates for
 *     invariance."
 */
#version 120

invariant vec2 x;

void main() { }
