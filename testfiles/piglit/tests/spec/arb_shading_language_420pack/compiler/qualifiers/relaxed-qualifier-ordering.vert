/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 *
 * From the GL_ARB_shading_language_420pack spec:
 *
 *     "When multiple qualifiers are present in a declaration, they may appear in
 *      any order, but they must all appear before the type. The layout qualifier
 *      is the only qualifier that can appear more than once. Further, a
 *      declaration can have at most one storage qualifier, at most one auxiliary
 *      storage qualifier, and at most one interpolation qualifier. Multiple
 *      memory qualifiers can be used. Any violation of these rules will cause a
 *      compile-time error."
 *
 * Test that interpolation, auxiliary, and storage qualifiers may be ordered
 * arbitrarily.
 */
#version 130
#extension GL_ARB_shading_language_420pack: enable

// storage interpolation
out flat float s_i;

// interpolation storage
flat out float i_s;

// storage auxiliary
out centroid float s_a;

// auxiliary storage
centroid out float a_s;

// Cannot test auxiliary interpolation without a storage qualifier.

// auxiliary interpolation storage
centroid flat out float a_i_s;

// auxiliary storage interpolation
centroid out flat float a_s_i;

// interpolation auxiliary storage
flat centroid out float i_a_s;

// interpolation storage auxiliary
flat out centroid float i_s_a;

// storage interpolation auxiliary
out flat centroid float s_i_a;

// storage auxiliary interpolation
out centroid flat float s_a_i;
