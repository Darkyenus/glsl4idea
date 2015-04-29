// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - precision qualifiers require GLSL 1.30 or later */
highp vec4 light;
