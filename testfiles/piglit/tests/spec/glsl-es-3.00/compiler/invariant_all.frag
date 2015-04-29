// [config]
// expect_result: fail
// glsl_version: 3.00
// [end config]
//
// Check that 'invariant(all)' cannot be used in fragment shader.
//
// From the GLSL ES 3.00 specification, section 4.6.1 ("The
// Invariant Qualifier"):
//
//     "To force all output variables to be invariant, use the pragma
//
//         #pragma STDGL invariant(all)
//
//     before all declarations in a shader. If this pragma is used
//     after the declaration of any variables or functions, then the
//     set of outputs that behave as invariant is undefined. It is an
//     error to use this pragma in a fragment shader."
//
#version 300 es
#pragma STDGL invariant(all)
void main()
{
}
