// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_shader_subroutine
// [end config]

#version 150
#extension GL_ARB_shader_subroutine: require

/* `subroutine` is a reserved keyword; it cannot be used
 * as an identifier.
 */
int subroutine;
