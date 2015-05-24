// [config]
// expect_result: pass
// glsl_version: 1.40
// require_extensions: GL_ARB_shader_atomic_counters
// [end config]

#version 140
#extension GL_ARB_shader_atomic_counters: require

#if !defined GL_ARB_shader_atomic_counters
#  error GL_ARB_shader_atomic_counters is not defined
#elif GL_ARB_shader_atomic_counters != 1
#  error GL_ARB_shader_atomic_counters is not equal to 1
#endif

/* Some compilers generate spurious errors if a shader does not contain
 * any code or declarations.
 */
int foo(void) { return 1; }
