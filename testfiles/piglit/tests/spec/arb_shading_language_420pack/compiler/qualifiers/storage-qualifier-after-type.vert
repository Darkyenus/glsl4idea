/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 *
 * From the GL_ARB_shading_language_420pack spec:
 *
 * "Variable declarations may have at most one storage qualifier specified in
 *  front of the type."
 *
 * Test that a storage qualifier after the type results in a compile error.
 */
#version 130
#extension GL_ARB_shading_language_420pack: enable
float in x;
