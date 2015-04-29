// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// Redeclare gl_FrontColor with an interpolation qualifier.
// See section 4.3.7 of the GLSL 1.30 spec.

#version 130

smooth out vec4 gl_FrontColor;

float f() {
	return 0.0;
}
