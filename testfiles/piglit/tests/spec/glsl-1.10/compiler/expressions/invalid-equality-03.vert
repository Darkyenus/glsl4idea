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

vec4 A(bool flag) {
	if (flag)
		return vertex;
	else
		return vec4(1.0) - vertex;
}

void main() {
	gl_Position = A(a == b == c);
}
