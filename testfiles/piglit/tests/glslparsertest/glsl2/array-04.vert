// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - array size type must be int */
uniform vec4 a[3.2];
