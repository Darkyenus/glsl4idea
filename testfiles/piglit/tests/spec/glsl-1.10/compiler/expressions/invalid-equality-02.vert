// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]

#ifdef GL_ES
precision mediump float;
#endif

attribute vec4 vertex;

void main() {
	if (0 == 1 == 2) {
		gl_Position = vertex;
	} else {
		gl_Position = vec4(1.0) - vertex;
	}
}
