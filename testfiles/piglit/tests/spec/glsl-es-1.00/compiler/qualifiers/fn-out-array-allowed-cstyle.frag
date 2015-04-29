// [config]
// expect_result: pass
// glsl_version: 1.00
// check_link: true
// [end config]
//
// Check that an array can be used as a function out parameter in
// GLSL ES 1.00.
//
// In this test, the array is declared using C-style array
// declaration syntax (float x[2] as opposed to float[2] x).
//
// From section 5.8 of the GLSL ES 1.00 spec:
//     Array variables are l-values and may be passed to parameters
//     declared as out or inout. However, they may not be used as the
//     target of an assignment.

#version 100
precision mediump float;

void f(out float x[2])
{
}

void main()
{
  float[2] x;
  f(x);
}
