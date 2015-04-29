// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_shader_subroutine
// [end config]

#version 150
#extension GL_ARB_shader_subroutine: require

#if !defined GL_ARB_shader_subroutine
#  error GL_ARB_shader_subroutine is not defined
#elif GL_ARB_shader_subroutine != 1
#  error GL_ARB_shader_subroutine is not equal to 1
#endif

/* Some compilers generate spurious errors if a shader does not contain
 * any code or declarations.
 */
int foo(void) { return 1; }
