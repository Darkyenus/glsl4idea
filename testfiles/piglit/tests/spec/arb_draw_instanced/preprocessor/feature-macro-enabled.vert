/* [config]
 * expect_result: pass
 * glsl_version: 1.10
 * require_extensions: GL_ARB_draw_instanced
 * [end config]
 */
#extension GL_ARB_draw_instanced: require

#if !defined GL_ARB_draw_instanced
#    error GL_ARB_draw_instanced is not defined
#elif GL_ARB_draw_instanced != 1
#    error GL_ARB_draw_instanced != 1
#endif

float foo() { return 0.0; }
