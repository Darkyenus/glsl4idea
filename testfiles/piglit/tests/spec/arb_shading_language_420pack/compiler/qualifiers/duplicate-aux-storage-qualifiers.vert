/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 *
 * From the GL_ARB_shading_language_420pack spec:
 *
 * "Some input and output qualified variables can be qualified with at most
 *  one additional auxiliary storage qualifier."
 *
 * Test that the same auxiliary storage qualifier twice results in a compile
 * error.
 */
#version 130
#extension GL_ARB_shading_language_420pack: enable
centroid centroid out float x;
