// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]
//
// Check that an array can't be used as a function out parameter in
// GLSL 1.10.
//
// In this test, the array is declared using C-style array
// declaration syntax (float x[2] as opposed to float[2] x).
//
// From section 5.8 of the GLSL 1.10 spec:
//     Other binary or unary expressions, non-dereferenced arrays,
//     function names, swizzles with repeated fields, and constants
//     cannot be l-values.

#version 110

void f(out float x[2])
{
}
