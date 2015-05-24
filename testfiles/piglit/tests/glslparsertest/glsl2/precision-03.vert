// [config]
// expect_result: fail
// glsl_version: 1.30
//
// [end config]

#version 130
/* FAIL - precision qualified type must be int or float */
precision highp vec4;
