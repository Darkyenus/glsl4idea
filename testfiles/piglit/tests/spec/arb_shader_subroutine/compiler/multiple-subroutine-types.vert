// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_shader_subroutine
// [end config]

#version 150
#extension GL_ARB_shader_subroutine: require

subroutine void func_type_a();
subroutine void func_type_b();

/* A subroutine matching both types */
subroutine (func_type_a, func_type_b) void f() {}
