// [config]
// expect_result: pass
// glsl_version: 1.30
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

// "uniform lowp sampler2D sampler;
//  highp vec2 coord;
//  ...
//  lowp vec4 col = texture2D (sampler, coord);
//                                         // texture2D returns lowp"
//
// Section 4.5 (Precision and Precision Qualifiers) of the GLSL 1.30
// spec also says:
//
// "Precision qualifiers are added for code portability with OpenGL
//  ES, not for functionality. They have the same syntax as in OpenGL ES."
//
// From this, we infer that GLSL 1.30 (and later) should allow precision
// qualifiers on sampler types just like float and integer types.
//
// This test verifies that a precision qualifier can be used on all of
// GLSL 1.30's sampler types.

#version 130

uniform mediump sampler1D s1;
uniform mediump sampler2D s2;
uniform mediump sampler3D s3;
uniform mediump samplerCube s4;
uniform mediump samplerCubeShadow s5;
uniform mediump sampler1DShadow s6;
uniform mediump sampler2DShadow s7;
uniform mediump sampler1DArray s8;
uniform mediump sampler2DArray s9;
uniform mediump sampler1DArrayShadow s10;
uniform mediump sampler2DArrayShadow s11;
uniform mediump isampler1D s12;
uniform mediump isampler2D s13;
uniform mediump isampler3D s14;
uniform mediump isamplerCube s15;
uniform mediump isampler1DArray s16;
uniform mediump isampler2DArray s17;
uniform mediump usampler1D s18;
uniform mediump usampler2D s19;
uniform mediump usampler3D s20;
uniform mediump usamplerCube s21;
uniform mediump usampler1DArray s22;
uniform mediump usampler2DArray s23;

void main()
{
	gl_Position = vec4(0.0);
}
