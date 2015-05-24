/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 *
 * From the GL_ARB_shading_language_420pack spec:
 *
 *    "A variable also cannot be declared with both the *in* and the *out*
 *     qualifiers."
 *
 * Test that a variable cannot be declared with both the in and out qualifiers.
 */
#version 130
#extension GL_ARB_shading_language_420pack: enable
in out float x;
