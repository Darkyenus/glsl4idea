// [config]
// expect_result: fail
// glsl_version: 1.20
// require_extensions: GL_ARB_uniform_buffer_object
// [end config]

#version 120
#extension GL_ARB_uniform_buffer_object: require

/* From the GL_ARB_uniform_buffer_object spec:
 *
 *     "While uniforms in the default uniform block are updated with
 *      glUniform* entry points and can have static initializers,
 *      uniforms in named uniform blocks are not. Instead, uniform
 *      block data is updated using the routines that update buffer
 *      objects and can not use static initializers.
 *
 *      ...
 *
 *      (15) How are the values from static initializer values
 *           propagated to a buffer object once it is attached?
 *
 *          Resolved: Static initialization values declared in a
 *          shader mapped to a uniform buffer are disallowed by the
 *          grammar and must be established through writes to the
 *          uniform buffer."
 */

uniform a {
	vec4 b = vec4(1.0);
};

vec4 foo(void) {
	return b;
}
