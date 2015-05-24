// [config]
// expect_result: fail
// glsl_version: 1.20
// [end config]
//
// "out" is only allowed in parameter list in GLSL 1.20
#version 120

out vec4 foo;
