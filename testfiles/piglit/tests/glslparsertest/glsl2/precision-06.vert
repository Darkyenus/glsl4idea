// [config]
// expect_result: pass
// glsl_version: 1.30
//
// [end config]

#version 130
/* PASS */

/* This code comes directly from the GLSL 1.30 spec.
 */
lowp float color;
out mediump vec2 P;
lowp ivec2 foo(lowp mat3);
highp mat4 m;
