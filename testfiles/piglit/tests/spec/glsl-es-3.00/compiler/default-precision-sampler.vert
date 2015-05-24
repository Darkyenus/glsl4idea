// [config]
// expect_result: pass
// glsl_version: 3.00 es
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
// This test verifies that a default precision qualifier can be used
// on all of GLSL ES 3.00's sampler types.

#version 300 es

precision mediump sampler2D;
precision mediump sampler3D;
precision mediump samplerCube;
precision mediump samplerCubeShadow;
precision mediump sampler2DShadow;
precision mediump sampler2DArray;
precision mediump sampler2DArrayShadow;
precision mediump isampler2D;
precision mediump isampler3D;
precision mediump isamplerCube;
precision mediump isampler2DArray;
precision mediump usampler2D;
precision mediump usampler3D;
precision mediump usamplerCube;
precision mediump usampler2DArray;

void main()
{
	gl_Position = vec4(0.0);
}
