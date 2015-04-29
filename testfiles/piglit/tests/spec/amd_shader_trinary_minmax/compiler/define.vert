// [config]
// expect_result: pass
// glsl_version: 1.10
// require_extensions: GL_AMD_shader_trinary_minmax
// [end config]

#version 110
#extension GL_AMD_shader_trinary_minmax: require

#if !defined GL_AMD_shader_trinary_minmax
#  error GL_AMD_shader_trinary_minmax is not defined
#elif GL_AMD_shader_trinary_minmax != 1
#  error GL_AMD_shader_trinary_minmax is not equal to 1
#endif

/* Some compilers generate spurious errors if a shader does not contain
 * any code or declarations.
 */
int foo(void) { return 1; }
