// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]
//
// Check that assignment to an array is illegal in GLSL 1.10.
//
// From section 5.8 of the GLSL 1.10 spec:
//     Other binary or unary expressions, non-dereferenced arrays,
//     function names, swizzles with repeated fields, and constants
//     cannot be l-values.

#version 110

void f(float x[2])
{
  float y[2];
  y = x;
}
