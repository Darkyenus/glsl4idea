/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 *
 * From the GL_ARB_shading_language_420pack spec:
 *
 *    "It is an error to use auxiliary storage qualifiers or interpolation 
 *     qualifiers on an output in a fragment shader."
 *
 * Test that an auxiliary qualifier on a fragment shader output results in
 * a compile error.
 */
#version 130
#extension GL_ARB_shading_language_420pack: enable
centroid out float x;
