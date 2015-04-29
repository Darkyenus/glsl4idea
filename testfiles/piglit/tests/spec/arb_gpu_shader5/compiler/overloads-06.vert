// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// Test overload resolution where all candidates require implicit
// conversions. Under unextended GLSL 1.50, resolution is ambiguous,
// since both functions require implicit conversions. With ARB_gpu_shader5,
// this case is still ambiguous, since neither function is better than the other.

#version 150
#extension GL_ARB_gpu_shader5 : enable

void foo(float x, int y, float z) {}	/* better for `y` */
void foo(float x, float y, int z) {}	/* better for `z` */

void bar()
{
	int a = 0;
	int b = 1;
	int c = 2;

	foo(a, b, c);
}
