// [config]
// expect_result: pass
// glsl_version: 1.30
// check_link: true
// [end config]
//
// From the GLSL ES 3.00 specification, section 4.5.4 ("Default
// Precision Qualifiers"):
//
//     "The precision statement
//
//         precision precision-qualifier type;
//
//     can be used to establish a default precision qualifier. The
//     type field can be int or float or any of the sampler types, and
//     the precision-qualifier can be lowp, mediump, or highp."
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
// This test verifies that a default precision qualifier can be used
// on all of GLSL 1.30's sampler types.

#version 130

precision mediump sampler1D;
precision mediump sampler2D;
precision mediump sampler3D;
precision mediump samplerCube;
precision mediump samplerCubeShadow;
precision mediump sampler1DShadow;
precision mediump sampler2DShadow;
precision mediump sampler1DArray;
precision mediump sampler2DArray;
precision mediump sampler1DArrayShadow;
precision mediump sampler2DArrayShadow;
precision mediump isampler1D;
precision mediump isampler2D;
precision mediump isampler3D;
precision mediump isamplerCube;
precision mediump isampler1DArray;
precision mediump isampler2DArray;
precision mediump usampler1D;
precision mediump usampler2D;
precision mediump usampler3D;
precision mediump usamplerCube;
precision mediump usampler1DArray;
precision mediump usampler2DArray;

void main()
{
	gl_Position = vec4(0.0);
}
