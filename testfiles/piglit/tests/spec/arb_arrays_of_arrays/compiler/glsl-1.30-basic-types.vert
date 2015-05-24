/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 *
 * Test the basic types added in glsl 1.30
 */
#version 130
#extension GL_ARB_arrays_of_arrays: enable

uniform uint array01[1][1];
uniform uvec2 array02[1][1];
uniform uvec3 array03[1][1];
uniform uvec4 array04[1][1];
uniform samplerCubeShadow array05[1][1];
uniform sampler1DArray array06[1][1];
uniform sampler2DArray array07[1][1];
uniform isampler1D array08[1][1];
uniform isampler2D array09[1][1];
uniform isampler3D array10[1][1];
uniform isamplerCube array11[1][1];
uniform isampler1DArray array12[1][1];
uniform isampler2DArray array13[1][1];
uniform usampler1D array14[1][1];
uniform usampler2D array15[1][1];
uniform usampler3D array16[1][1];
uniform usamplerCube array17[1][1];
uniform usampler1DArray array18[1][1];
uniform usampler2DArray array19[1][1];

void main() { gl_Position = vec4(0.0); }
