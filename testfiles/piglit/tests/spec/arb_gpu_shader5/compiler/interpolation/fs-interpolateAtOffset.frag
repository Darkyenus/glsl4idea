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

// ARB_gpu_shader5 requires at least -0.5 .. 0.5 to be supported.
const vec2 offset = vec2(-0.5, 0.5);

void main()
{
	vec4 res = vec4(0);

	res += vec4(interpolateAtOffset(v1, offset), 1, 1, 1);
	res += vec4(interpolateAtOffset(v2, offset), 1, 1);
	res += vec4(interpolateAtOffset(v3, offset), 1);
	res += interpolateAtOffset(v4, offset);

	gl_FragColor = res;
}
