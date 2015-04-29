/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 */
#version 120
#extension GL_ARB_arrays_of_arrays: enable

float x[][2];

void foo() { x[3][1] = 2.; }

// The left most array must be at least 4 elements because of
// the previous access to x[3][1].
float x[][2] = float[][2](float[2](1., 2.),
                          float[2](1., 2.));

void main()
{
	foo();
	gl_Position = vec4(x[0][0]);
}
