/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 *
 * From the GL_ARB_shading_language_420pack spec:
 *
 * '"    Delete the following sentence in the "Invariant Qualifier" section:
 *
 *       "The invariant qualifier must appear before any interpolation qualifiers
 *        or storage qualifiers when combined with a declaration."'
 *
 * Test that the invariant qualifier can be used after interpolation and
 * storage qualifiers.
 */
#version 130
#extension GL_ARB_shading_language_420pack: enable
out flat invariant float x;
