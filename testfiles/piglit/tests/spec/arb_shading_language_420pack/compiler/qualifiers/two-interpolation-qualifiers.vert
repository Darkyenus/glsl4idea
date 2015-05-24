/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 *
 * From the GL_ARB_shading_language_420pack spec:
 *
 * "Inputs and outputs that could be interpolated can be further qualified by
 *  at most one of the following interpolation qualifiers:"
 *
 * Test that multiple interpolation qualifiers result in a compile error.
 */
#version 130
#extension GL_ARB_shading_language_420pack: enable
smooth flat out float x;
