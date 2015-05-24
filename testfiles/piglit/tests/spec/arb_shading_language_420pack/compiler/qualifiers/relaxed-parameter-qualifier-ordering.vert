/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 *
 * From the GL_ARB_shading_language_420pack spec:
 *
 *       "parameter-qualifiers :
 *           empty
 *           list of parameter-qualifier
 *
 *        parameter-qualifier :
 *           empty [sic]
 *           const
 *           in
 *           out
 *           inout
 *           memory qualifier
 *           precision qualifier"
 *
 * Test that parameter qualifiers may be ordered arbitrarily.
 */
#version 130
#extension GL_ARB_shading_language_420pack: enable
void a(in const float x) {}
void d(const in float x) {}
