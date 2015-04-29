// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]

#ifdef GL_ES
precision mediump float;
#endif

uniform int a;

void main() {
	// a is not boolean, so the logical-not operator cannot be applied
	// to it.  Further, the result of the logical-not operator is boolean,
	// so it cannot be assigned to a variable of type int.
	int x = !a;

	gl_Position = vec4(x);
}
