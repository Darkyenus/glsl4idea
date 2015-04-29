// [config]
// expect_result: fail
// glsl_version: 1.20
// require_extensions: GL_ARB_uniform_buffer_object
// [end config]

/* From the GL_ARB_uniform_buffer_object_spec:
 *
 *     "Layout qualifiers on member declarations cannot use the
 *      shared, packed, or std140 qualifiers. These can only be used
 *      at global scope or on a block declaration."
 */

#version 120
#extension GL_ARB_uniform_buffer_object: require

uniform ubo {
	layout(std140) vec4 a;
};

vec4 foo(void) {
	return a;
}
