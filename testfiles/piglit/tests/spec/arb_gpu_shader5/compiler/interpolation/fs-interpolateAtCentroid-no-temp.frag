// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

#version 150
#extension GL_ARB_gpu_shader5: require

in vec4 v4;

void main()
{
	vec4 res = vec4(0);

	// <interpolant> must be a shader input.
	vec4 t4 = v4;

	res += interpolateAtCentroid(t4);

	gl_FragColor = res;
}
