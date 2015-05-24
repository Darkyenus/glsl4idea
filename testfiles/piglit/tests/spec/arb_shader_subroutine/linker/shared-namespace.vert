// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_shader_subroutine
// check_link: true
// [end config]

#version 150
#extension GL_ARB_shader_subroutine: require

subroutine void func_type();

/* A normal function definition clashing with the subroutine
 * type declaration
 */
void func_type(void);

void main() {}
