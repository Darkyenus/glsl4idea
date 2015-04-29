// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]

#ifdef GL_ES
precision mediump float;
#endif

void main() {
	// "0" is not boolean, so the logical-not operator cannot be applied
	// to it.  Further, the result of the logical-not operator is boolean,
	// so it cannot be assigned to a variable of type int.
	int x = !0;

	gl_Position = vec4(x);
}
