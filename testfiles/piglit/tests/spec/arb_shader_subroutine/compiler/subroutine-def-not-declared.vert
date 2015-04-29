// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_shader_subroutine
// [end config]

#version 150
#extension GL_ARB_shader_subroutine: require

/* There is no subroutine type 'bogus_type' */
subroutine (bogus_type) void f() {}
