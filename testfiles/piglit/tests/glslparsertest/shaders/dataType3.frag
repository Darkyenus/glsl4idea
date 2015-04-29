// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

#version 110

/*
 * In GLSL 1.10 uniform initializers are illegal
 * In GLSL 1.20 or later, uniform initializers are allowed
 */

uniform int i = 1; // uniforms are read only in GLSL 1.10

void main()
{
}
