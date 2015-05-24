// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_tessellation_shader
// [end config]

#version 150
#extension GL_ARB_tessellation_shader: require

#if !defined GL_ARB_tessellation_shader
#  error GL_ARB_tessellation_shader is not defined
#elif GL_ARB_tessellation_shader != 1
#  error GL_ARB_tessellation_shader is not equal to 1
#endif

/* Some compilers generate spurious errors if a shader does not contain
 * any code or declarations.
 */
int foo(void) { return 1; }
