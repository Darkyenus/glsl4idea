// [config]
// expect_result: fail
// glsl_version: 1.20
// require_extensions: GL_ARB_uniform_buffer_object
// [end config]

#version 120
#extension GL_ARB_uniform_buffer_object: require

/* From the GL_ARB_uniform_buffer_object spec:
 *
 *     "    Properties of Uniforms and uniform blocks:
 *          [...]
 *          k) Uniform block declarations may not be nested"
 */

uniform a {
	vec4 b;
	uniform c {
		vec4 d;
	};
};

vec4 foo(void) {
	return b;
}
