// [config]
// expect_result: pass
// glsl_version: 1.10
// [end config]

void main() {
	vec4 f = vec4(1e5, -1e5, 1e-5, -1e-5);
	gl_Position = vec4(1.0);
}
