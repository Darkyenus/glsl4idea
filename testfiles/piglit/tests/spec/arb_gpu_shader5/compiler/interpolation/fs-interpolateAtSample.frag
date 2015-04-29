// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

#version 150
#extension GL_ARB_gpu_shader5: require

in float v1;
in vec2 v2;
in vec3 v3;
in vec4 v4;

void main()
{
	vec4 res = vec4(0);

	res += vec4(interpolateAtSample(v1, 0), 1, 1, 1);
	res += vec4(interpolateAtSample(v2, 1), 1, 1);
	res += vec4(interpolateAtSample(v3, 2), 1);
	res += interpolateAtSample(v4, 3);

	gl_FragColor = res;
}
