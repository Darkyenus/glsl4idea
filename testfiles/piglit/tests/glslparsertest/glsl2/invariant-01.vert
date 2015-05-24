// [config]
// expect_result: pass
// glsl_version: 1.20
//
// [end config]

/* PASS - based on examples in the GLSL 1.20 spec */
#version 120

invariant gl_Position;

varying vec3 Color1;
invariant Color1;

invariant varying vec3 Color2;

void main() { }
