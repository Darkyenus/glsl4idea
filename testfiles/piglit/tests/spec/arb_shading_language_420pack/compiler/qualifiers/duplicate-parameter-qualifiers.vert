/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 *
 * From the GL_ARB_shading_language_420pack spec:
 *
 *     "The layout qualifier is the only qualifier that can appear more than
 *      once."
 */
#version 130
#extension GL_ARB_shading_language_420pack: enable
void a(in in float x) {}
