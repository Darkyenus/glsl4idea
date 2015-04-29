// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_shader_subroutine
// [end config]

#version 150
#extension GL_ARB_shader_subroutine: require

/* The ARB_shader_subroutine spec says nothing to
 * explicitly disallow calling subroutine implementations
 * as normal functions.
 *
 * It seems reasonable that this would still work.
 */

subroutine void func_type();

/* A subroutine matching the above type */
subroutine (func_type) void impl() {}

/* Call the function directly, rather than via
 * a subroutine uniform.
 */
void foo() {
	impl();
}
