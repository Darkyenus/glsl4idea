// [config]
// expect_result: pass
// glsl_version: 1.00
// check_link: true
// [end config]
//
// From the GLSL ES 1.00 specification, section 4.5.3 ("Default
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
// on all of GLSL ES 1.00's sampler types.

#version 100

precision mediump sampler2D;
precision mediump samplerCube;

void main()
{
}
