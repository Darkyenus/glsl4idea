/*
 * [config]
 * glsl_version: 1.20
 * expect_result: pass
 * [end config]
 *
 * Test that implicit type conversion of out parameters is properly
 * used to match function calls to callees.
 *
 * From the GLSL 1.30 spec (which clarifies, but does not change, the
 * rules for implicit type conversion in GLSL 1.20), section 6.1
 * (Function Definitions):
 *
 *   Mismatched types on output parameters (out or inout) must have a
 *   conversion from the formal parameter type to the calling argument
 *   type.
 */

#version 120

void f(out int x)
{
  x = 0;
}

void
main() {
    float x;
    f(x);
    gl_Position = gl_Vertex;
}
