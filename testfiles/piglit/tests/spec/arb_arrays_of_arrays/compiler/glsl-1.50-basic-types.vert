/* [config]
 * expect_result: pass
 * glsl_version: 1.50
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 *
 * Test the basic types added in glsl 1.50
 */
#version 150
#extension GL_ARB_arrays_of_arrays: enable

uniform sampler2DMS array01[1][1];
uniform isampler2DMS array02[1][1];
uniform usampler2DMS array03[1][1];
uniform sampler2DMSArray array04[1][1];
uniform isampler2DMSArray array05[1][1];
uniform usampler2DMSArray array06[1][1];

void main() { gl_Position = vec4(0.0); }
