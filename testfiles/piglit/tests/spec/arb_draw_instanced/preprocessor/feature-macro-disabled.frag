/* [config]
 * expect_result: pass
 * glsl_version: 1.10
 * require_extensions: !GL_ARB_draw_instanced
 * [end config]
 */

#if defined GL_ARB_draw_instanced
#    error GL_ARB_draw_instanced is defined, but should not be
#endif

float foo() { return 0.0; }
