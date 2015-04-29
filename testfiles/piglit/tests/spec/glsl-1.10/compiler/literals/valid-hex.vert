// [config]
// expect_result: pass
// glsl_version: 1.10
// [end config]

void main() {
	ivec4 i = ivec4(0xf, 0Xa, 0xB, 0XC);
	gl_Position = vec4(1.0);
}
