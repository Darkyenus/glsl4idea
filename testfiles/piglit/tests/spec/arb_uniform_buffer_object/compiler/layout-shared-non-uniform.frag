// [config]
// expect_result: fail
// glsl_version: 1.20
// require_extensions: GL_ARB_uniform_buffer_object
// [end config]

/* From the GL_ARB_uniform_buffer_object_spec:
 *
 *     "Uniform block layout qualifiers can be declared at global
 *      scope, on a single uniform block, or on a single block member.
 *
 *      At global scope, it is an error to use layout qualifiers to
 *      declare a variable. Instead, at global scope, layout
 *      qualifiers apply just to the keyword uniform and establish
 *      default qualification for subsequent blocks:"
 */

#version 120
#extension GL_ARB_uniform_buffer_object: require

layout(shared) vec4 a;

vec4 foo(void) {
	return a;
}
