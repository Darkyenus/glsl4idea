// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// Vertex outputs can be declared 'smooth'.

#version 130

smooth out float x;

float f() {
	return 0.0;
}
