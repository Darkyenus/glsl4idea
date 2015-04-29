/* [config]
 * expect_result: pass
 * glsl_version: 1.40
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 *
 * Test the basic types added in glsl 1.40
 */
#version 140
#extension GL_ARB_arrays_of_arrays: enable

uniform sampler2DRect array01[1][1];
uniform isampler2DRect array02[1][1];
uniform usampler2DRect array03[1][1];
uniform sampler2DRectShadow array04[1][1];
uniform samplerBuffer array05[1][1];
uniform isamplerBuffer array06[1][1];
uniform usamplerBuffer array07[1][1];

void main() { gl_Position = vec4(0.0); }
