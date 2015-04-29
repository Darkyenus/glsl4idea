// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// The ARB_gpu_shader5 spec says:
//    "Component selection operators (e.g., ".xy") may not be used when
//    specifying <interpolant>."

#version 150
#extension GL_ARB_gpu_shader5: require

in vec2 v2;
in vec3 v3;
in vec4 v4;

void main()
{
	vec4 res = vec4(0);

	res += vec4(interpolateAtCentroid(v2.xy), 1, 1);
	res += vec4(interpolateAtCentroid(v3.xyz), 1);
	res += interpolateAtCentroid(v4.xyzw);

	gl_FragColor = res;
}
