// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_shader_subroutine
// check_link: true
// [end config]

#version 150
#extension GL_ARB_shader_subroutine: require

/* Two mutually-recursive subroutines */

subroutine void func_type_a(int x);
subroutine void func_type_b(int x);

subroutine uniform func_type_a func_a;
subroutine uniform func_type_b func_b;

subroutine (func_type_a) void impl_a(int x) {
	if (x > 0) func_b(x - 1);
}

subroutine (func_type_b) void impl_b(int x) {
	if (x > 0) func_a(x - 1);
}

void main() {
	func_a(42);
}
