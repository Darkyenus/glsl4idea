// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_shader_subroutine
// [end config]

#version 150
#extension GL_ARB_shader_subroutine: require

subroutine void func_type();

/* A subroutine matching the above type */
subroutine (func_type) void impl() {}

/* An array of subroutine uniforms matching the above */
subroutine uniform func_type f[4];

/* Elements of an array of subroutine uniforms are callable
 * as if they were any other function
 */
void foo() {
	f[0]();
}
