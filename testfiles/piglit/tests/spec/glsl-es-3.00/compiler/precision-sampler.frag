// [config]
// expect_result: pass
// glsl_version: 3.00 es
// check_link: true
// [end config]
//
// From the GLSL ES 3.00 specification, section 4.5.4 ("Default
// Precision Qualifiers"):
//
// "The precision statement
//
// precision precision-qualifier type;
//
// can be used to establish a default precision qualifier. The
// type field can be int or float or any of the sampler types, and
// the precision-qualifier can be lowp, mediump, or highp."
//
// Section 8 (Built-In Functions) of the GLSL ES 1.00 spec says:
//
// "uniform lowp sampler2D sampler;
//  highp vec2 coord;
//  ...
//  lowp vec4 col = texture2D (sampler, coord);
//                                         // texture2D returns lowp"
//
// This test verifies that a precision qualifier can be used on all of
// GLSL ES 3.00's sampler types.

#version 300 es

uniform mediump sampler2D s1;
uniform mediump sampler3D s2;
uniform mediump samplerCube s3;
uniform mediump samplerCubeShadow s4;
uniform mediump sampler2DShadow s5;
uniform mediump sampler2DArray s6;
uniform mediump sampler2DArrayShadow s7;
uniform mediump isampler2D s8;
uniform mediump isampler3D s9;
uniform mediump isamplerCube s10;
uniform mediump isampler2DArray s11;
uniform mediump usampler2D s12;
uniform mediump usampler3D s13;
uniform mediump usamplerCube s14;
uniform mediump usampler2DArray s15;

void main()
{
}
