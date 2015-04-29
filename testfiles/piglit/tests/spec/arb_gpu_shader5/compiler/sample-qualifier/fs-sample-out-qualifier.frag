// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// From the ARB_gpu_shader5 spec:
// "It is an error to use centroid out or sample out in a fragment shader"

#version 150
#extension GL_ARB_gpu_shader5: require

sample out vec4 x;			/* not allowed */
out vec4 out_color;

void main()
{
	out_color = x;
}

