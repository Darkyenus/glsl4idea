// [config]
// expect_result: fail
// glsl_version: 1.20
// [end config]
//
// "inout" is only allowed in parameter list in GLSL 1.20
#version 120

inout vec4 foo;
