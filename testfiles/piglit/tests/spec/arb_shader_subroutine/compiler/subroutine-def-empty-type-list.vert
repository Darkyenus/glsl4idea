// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_shader_subroutine
// [end config]

#version 150
#extension GL_ARB_shader_subroutine: require

/* According to the GLSL 4.40 grammar, the list of subroutine types
 * between parentheses is defined as:
 *
 * type_name_list:
 *	TYPE_NAME
 *	type_name_list COMMA TYPE_NAME
 *
 * Therefore, it cannot be empty.
 */
subroutine ( /* nothing */ ) void f() {}
