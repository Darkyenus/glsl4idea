/* [config]
 * expect_result: pass
 * glsl_version: 1.20
 * [end config]
 */
#version 120

float x[];

void foo() { x[1] = 2.; }

// The array must be at least 2 elements because of the previous
// access to x[1].
float x[] = float[2](1., 2.);

void main()
{
	foo();
	gl_Position = vec4(x[0]);
}
