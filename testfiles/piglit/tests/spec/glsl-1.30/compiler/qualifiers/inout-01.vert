// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// "inout" is only allowed in parameter list in GLSL 1.30
#version 130

inout vec4 foo;
