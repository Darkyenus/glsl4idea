// [config]
// expect_result: fail
// glsl_version: 1.00
// [end config]
//
// Check that assignment to an array is illegal in GLSL ES 1.00.
//
// From section 5.8 of the GLSL ES 1.00 spec:
//     Array variables are l-values and may be passed to parameters
//     declared as out or inout. However, they may not be used as the
//     target of an assignment.

#version 100

void f(float x[2])
{
  float y[2];
  y = x;
}
