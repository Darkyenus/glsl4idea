// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]

void main() {
	int i = 0xg;
	gl_Position = vec4(1.0);
}
