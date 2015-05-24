// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]

#ifdef GL_ES
precision mediump float;
#endif

void A(int x) { }

void main() {
	// "0" is not boolean, so the logical-not operator cannot be applied
	// to it.
	A(!0);
	gl_Position = vec4(0.0);
}
