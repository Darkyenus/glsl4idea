/* [config]
 * expect_result: pass
 * glsl_version: 1.20
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 *
 * From page 19 (page 25 of the PDF) of the GLSL 1.20 spec:
 *
 *     "It is legal to declare an array without a size and then later
 *     re-declare the same name as an array of the same type and specify a
 *     size."
 */
#version 120
#extension GL_ARB_arrays_of_arrays: enable

float x[][2];

void foo() { x[1][1] = 2.; }

// The left most array must be at least 2 elements because
// of the previous access to x[2][1].
float x[][2] = float[][2](float[2](1., 2.),
                          float[2](1., 2.));

void main()
{
	foo();
	gl_Position = vec4(x[0][0]);
}
