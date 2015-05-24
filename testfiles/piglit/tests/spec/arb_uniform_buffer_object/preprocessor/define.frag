// [config]
// expect_result: pass
// glsl_version: 1.10
// require_extensions: GL_ARB_uniform_buffer_object
// [end config]

#version 110
#extension GL_ARB_uniform_buffer_object: require

#if !defined GL_ARB_uniform_buffer_object
#  error GL_ARB_uniform_buffer_object is not defined
#elif GL_ARB_uniform_buffer_object != 1
#  error GL_ARB_uniform_buffer_object is not equal to 1
#endif

/* Some compilers generate spurious errors if a shader does not contain
 * any code or declarations.
 */
int foo(void) { return 1; }
