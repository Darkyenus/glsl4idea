// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader_fp64
// [end config]

#version 150
#extension GL_ARB_gpu_shader_fp64: require

#if !defined GL_ARB_gpu_shader_fp64
#  error GL_ARB_gpu_shader_fp64 is not defined
#elif GL_ARB_gpu_shader_fp64 != 1
#  error GL_ARB_gpu_shader_fp64 is not equal to 1
#endif

/* Some compilers generate spurious errors if a shader does not contain
 * any code or declarations.
 */
int foo(void) { return 1; }
