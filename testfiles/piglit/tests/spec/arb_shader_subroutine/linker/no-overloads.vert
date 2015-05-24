// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_shader_subroutine
// check_link: true
// [end config]

#version 150
#extension GL_ARB_shader_subroutine: require

subroutine void func_type();

/* A program will fail to link if any shader contains two or more
 * functions with the same name, at least one of which is associated
 * with a subroutine type.
 */

subroutine (func_type) void f() {}
void f(int x) {}

void main() {}
