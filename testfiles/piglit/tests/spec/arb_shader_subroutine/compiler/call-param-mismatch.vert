// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_shader_subroutine
// [end config]

#version 150
#extension GL_ARB_shader_subroutine: require

subroutine void func_type();

subroutine (func_type) void impl() {}

subroutine uniform func_type f;

/* Call f() with a mismatched parameter list */
void foo() {
	f(42);
}
