// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - array size must be > 0 */
uniform vec4 a[0];
