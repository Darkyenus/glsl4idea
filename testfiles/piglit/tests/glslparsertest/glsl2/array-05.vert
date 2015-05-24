// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - array size type must be scalar */
uniform vec4 a[ivec4(3)];
