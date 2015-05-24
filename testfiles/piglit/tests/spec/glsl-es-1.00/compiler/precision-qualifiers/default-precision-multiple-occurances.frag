// [config]
// expect_result: pass
// glsl_version: 1.00
// [end config]
//
// Section 4.5.3 (Default Precision Qualifiers) of the GLSL ES 1.00 spec says:
//
//     "Multiple precision statements for the same basic type can appear
//     inside the same scope, with later statements overriding earlier
//     statements within that scope."

#version 100

precision mediump float;

float f()
{
	float x;
	return 0.;
}

precision lowp float;
float y;
