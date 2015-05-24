// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]

void main() {
	// Float-suffixes are not in GLSL 1.10
	float f = 1.0F;
	gl_Position = vec4(1.0);
}
