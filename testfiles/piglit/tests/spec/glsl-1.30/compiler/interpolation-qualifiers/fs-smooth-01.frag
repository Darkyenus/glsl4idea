// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// Fragment inputs can be declared 'smooth'.

#version 130

smooth in float x;

float f() {
	return 0.0;
}
