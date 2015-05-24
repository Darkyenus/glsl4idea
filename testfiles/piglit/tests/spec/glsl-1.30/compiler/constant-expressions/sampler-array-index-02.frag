// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that sampler arrays cannot be indexed with non-constant expressions.
//
// From page 23 (29 of PDF) of GLSL 1.30 spec:
//     Samplers aggregated into arrays within a shader (using square brackets
//     [ ]) can only be indexed with integral constant expressions



#version 130

uniform int x = 0;
uniform sampler2D a[4];

float f() {
	vec4 v = texture(a[x], vec2(0.0, 0.0));
	return 0.0;
}
