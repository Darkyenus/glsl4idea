// [config]
// expect_result: pass
// glsl_version: 1.00
// [end config]
//
// High precision is always available in the vertex shader.
//
// From section 4.5.2 of the GLSL 1.00 spec:
//     "The vertex language requires any uses of lowp, mediump and highp to
//     compile and link without error."

#version 100

highp float x;

void f() { }
