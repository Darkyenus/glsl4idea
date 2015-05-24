// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

#version 150
#extension GL_ARB_gpu_shader5: require

out vec4 out_color;

void main()
{
	/* x is neither an input nor output, so 'sample' is not
	 * legal here.
	 */
	sample vec4 x = vec4(1);
	out_color = x;
}

