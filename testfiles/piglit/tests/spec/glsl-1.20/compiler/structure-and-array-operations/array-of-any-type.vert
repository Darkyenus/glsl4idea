/* [config]
 * expect_result: pass
 * glsl_version: 1.20
 * [end config]
 *
 * From page 19 (page 25 of the PDF) of the GLSL 1.20 spec:
 *
 *     "All basic types and structures can be formed into arrays."
 */
#version 120

uniform float array01[1];
uniform int array02[1];
uniform bool array03[1];
uniform vec2 array04[1];
uniform vec3 array05[1];
uniform vec4 array06[1];
uniform ivec2 array07[1];
uniform ivec3 array08[1];
uniform ivec4 array09[1];
uniform bvec2 array10[1];
uniform bvec3 array11[1];
uniform bvec4 array12[1];
uniform mat2 array13[1];
uniform mat2x2 array14[1];
uniform mat2x3 array15[1];
uniform mat2x4 array16[1];
uniform mat3 array17[1];
uniform mat3x2 array18[1];
uniform mat3x3 array19[1];
uniform mat3x4 array20[1];
uniform mat4 array21[1];
uniform mat4x2 array22[1];
uniform mat4x3 array23[1];
uniform mat4x4 array24[1];
uniform sampler1D array25[1];
uniform sampler2D array26[1];
uniform sampler3D array27[1];
uniform samplerCube array28[1];
uniform sampler1DShadow array29[1];
uniform sampler2DShadow array30[1];

void main() { gl_Position = vec4(0.0); }
