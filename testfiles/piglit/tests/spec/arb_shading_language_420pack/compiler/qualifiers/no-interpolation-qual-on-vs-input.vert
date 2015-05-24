/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 *
 * From the GL_ARB_shading_language_420pack spec:
 *
 *     "It is an error to use any auxiliary or interpolation qualifiers on a
 *      vertex shader input."
 *
 * Test that an interpolation qualifier on a vertex shader input results in a
 * compile error.
 */
#version 130
#extension GL_ARB_shading_language_420pack: enable
flat in float x;
