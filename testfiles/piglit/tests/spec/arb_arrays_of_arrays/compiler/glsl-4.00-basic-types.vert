/* [config]
 * expect_result: pass
 * glsl_version: 4.00
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 *
 * Test the basic types added in glsl 4.00
 */
#version 400
#extension GL_ARB_arrays_of_arrays: enable

uniform double array01[1][1];
uniform dvec2 array02[1][1];
uniform dvec3 array03[1][1];
uniform dvec4 array04[1][1];
uniform dmat2 array05[1][1];
uniform dmat2x2 array06[1][1];
uniform dmat2x3 array07[1][1];
uniform dmat2x4 array08[1][1];
uniform dmat3 array09[1][1];
uniform dmat3x2 array10[1][1];
uniform dmat3x3 array11[1][1];
uniform dmat3x4 array12[1][1];
uniform dmat4 array13[1][1];
uniform dmat4x2 array14[1][1];
uniform dmat4x3 array15[1][1];
uniform dmat4x4 array16[1][1];
uniform samplerCubeArray array17[1][1];
uniform isamplerCubeArray array18[1][1];
uniform usamplerCubeArray array19[1][1];
uniform samplerCubeArrayShadow array20[1][1];

void main() { gl_Position = vec4(0.0); }
