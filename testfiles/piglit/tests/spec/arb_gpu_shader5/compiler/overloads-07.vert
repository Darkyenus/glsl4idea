// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// Test overload resolution where all candidates require implicit
// conversions. Under unextended GLSL 1.50, resolution is ambiguous,
// since both functions require implicit conversions. With ARB_gpu_shader5,
// this case is still ambiguous, since int->float conversion is not
// considered better or worse than int->uint conversion.

#version 150
#extension GL_ARB_gpu_shader5 : enable

void foo(float x) {}
void foo(uint x) {}

void bar()
{
	int x = 0;
	foo(x);
}

