// [config]
// expect_result: pass
// glsl_version: 1.20
// [end config]

void main() {
	vec4 f = vec4(1e5f, -1e5f, 1e-5f, -1e-5f);
	vec4 g = vec4(1e5F, -1e5F, 1e-5F, -1e-5F);
	vec4 h = vec4(1E5f, -1E5f, 1E-5f, -1E-5f);
	vec4 i = vec4(1E5F, -1E5F, 1E-5F, -1E-5F);
	gl_Position = vec4(1.0);
}
