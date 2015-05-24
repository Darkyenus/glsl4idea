/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * [end config]
 */
#version 120

float x[];

void foo() { x[3] = 2.; }

// The array must be at least 4 elements because of the previous
// access to x[3].
float x[] = float[2](1., 2.);

void main()
{
	foo();
	gl_Position = vec4(x[0]);
}
