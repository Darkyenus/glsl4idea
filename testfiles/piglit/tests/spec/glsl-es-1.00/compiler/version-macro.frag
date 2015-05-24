// [config]
// expect_result: pass
// glsl_version: 1.00
// [end config]

#version 100
#if !defined __VERSION__
#error __VERSION__ not defined.
#endif
#if __VERSION__ != 100
#error __VERSION__ is not 100
#endif

void main() { }
