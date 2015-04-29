// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_vertex_attrib_64bit
// [end config]

#version 150
#extension GL_ARB_vertex_attrib_64bit: require

#if !defined GL_ARB_vertex_attrib_64bit
#  error GL_ARB_vertex_attrib_64bit is not defined
#elif GL_ARB_vertex_attrib_64bit != 1
#  error GL_ARB_vertex_attrib_64bit is not equal to 1
#endif

/* Some compilers generate spurious errors if a shader does not contain
 * any code or declarations.
 */
int foo(void) { return 1; }
