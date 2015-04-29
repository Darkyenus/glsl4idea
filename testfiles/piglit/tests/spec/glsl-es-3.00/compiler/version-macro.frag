// [config]
// expect_result: pass
// glsl_version: 3.00
// [end config]

#version 300 es
#if !defined __VERSION__
#error __VERSION__ not defined.
#endif
#if __VERSION__ != 300
#error __VERSION__ is not 300
#endif

void main() { }
