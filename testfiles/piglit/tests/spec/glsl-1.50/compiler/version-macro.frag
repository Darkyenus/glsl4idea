// [config]
// expect_result: pass
// glsl_version: 1.50
// [end config]

#version 150
#if !defined __VERSION__
#error __VERSION__ not defined.
#endif
#if __VERSION__ != 150
#error __VERSION__ is not 150
#endif

void main() { }
