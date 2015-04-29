// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_shader_subroutine
// [end config]

#version 150
#extension GL_ARB_shader_subroutine: require

subroutine void func_type();

subroutine (func_type) void impl() {}

subroutine uniform func_type f[4];

uniform int n;

/* Arrays of subroutine uniforms may be indexed with
 * dynamically uniform expressions. GLSL 4.00-4.30
 * leave this underspecified as 'dynamically indexed'
 * but GLSL 4.40 clarifies that the intent all along
 * was to require the indexing expression to be
 * dynamically uniform.
 */
void foo() {
	f[n]();
}
