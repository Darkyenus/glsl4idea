// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]

#ifdef GL_ES
precision mediump float;
#endif

attribute vec4 vertex;
uniform int a;
uniform int b;
uniform int c;

void main() {
	if (a == b == c) {
		gl_Position = vertex;
	} else {
		gl_Position = vec4(1.0) - vertex;
	}
}
