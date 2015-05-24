/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 *
 * From the GL_ARB_shading_language_420pack spec:
 *
 * "    Add the following parameter qualifier
 *
 *          Qualifier          Meaning
 *          ---------          -------
 *          const              for function parameters that cannot be written to"
 *
 * Test that const-qualifying a function parameter works.
 */
#version 130
#extension GL_ARB_shading_language_420pack: enable
void f(const float x) {}
