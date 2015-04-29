// [config]
// expect_result: fail
// glsl_version: 1.20
// require_extensions: GL_ARB_uniform_buffer_object
// [end config]

#version 120
#extension GL_ARB_uniform_buffer_object: require

/* From the GL_ARB_uniform_buffer_object spec:
 *
 *     "Sampler types are not allowed inside of uniform blocks. All
 *      other types, arrays, and structures allowed for uniforms are
 *      allowed within a uniform block."
 */

uniform a {
	uniform sampler2D s;
};

vec4 foo(void) {
	return texture2D(s, vec2(0.0));
}
