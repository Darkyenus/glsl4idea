// [config]
// expect_result: pass
// glsl_version: 1.20
// check_link: true
// [end config]
//
// Check that an array can be used as a function inout parameter in
// GLSL 1.20.
//
// In this test, the array is declared using GLSL-style array
// declaration syntax (float[2] x as opposed to float x[2]).
//
// From section 5.8 of the GLSL 1.20 spec:
//     Variables that are built-in types, entire structures or arrays,
//     structure fields, l-values with the field selector ( . )
//     applied to select components or swizzles without repeated
//     fields, l-values within parentheses, and l-values dereferenced
//     with the array subscript operator ( [] ) are all l-values.

#version 120

void f(inout float[2] x)
{
}

void main()
{
  float[2] x;
  f(x);
  gl_Position = vec4(0.0);
}
