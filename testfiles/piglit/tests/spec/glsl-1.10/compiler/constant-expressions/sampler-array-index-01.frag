// [config]
// expect_result: pass
// glsl_version: 1.10
// [end config]
//
// Check that sampler arrays can be indexed with constant expressions.
//
// This should work in GLSL 1.10, GLSL 1.20, and GLSL ES 1.00.

#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D a[4];

void main() {
	gl_FragColor = texture2D(a[0], vec2(0.0, 0.0));
}
