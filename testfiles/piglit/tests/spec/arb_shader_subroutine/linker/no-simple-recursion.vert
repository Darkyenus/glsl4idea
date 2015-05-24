// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_shader_subroutine
// check_link: true
// [end config]

#version 150
#extension GL_ARB_shader_subroutine: require

/* Simple recursion via a subroutine */

subroutine void func_type(int x);

subroutine uniform func_type func;

subroutine (func_type) void impl(int x) {
	if (x > 0) func(x - 1);
}

void main() {
	func(42);
}
