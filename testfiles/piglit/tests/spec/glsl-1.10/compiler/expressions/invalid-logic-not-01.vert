// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]

#ifdef GL_ES
precision mediump float;
#endif

uniform int a;

void main() {
	// a is not boolean, so the logical not operator cannot be applied
	// to it.
	if (!a)
		gl_Position = vec4(0.0);
	else
		gl_Position = vec4(1.0);
}
