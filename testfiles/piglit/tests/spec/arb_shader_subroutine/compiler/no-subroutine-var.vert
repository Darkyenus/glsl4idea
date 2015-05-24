// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_shader_subroutine
// [end config]

#version 150
#extension GL_ARB_shader_subroutine: require

/* This is not any of the three allowed uses of
 * `subroutine`:
 */

subroutine int x;
