// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - uniform initializers not allowed in GLSL 1.10 */
uniform vec2 a = vec2(1.0, 2.0);
