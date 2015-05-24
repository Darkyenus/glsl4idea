// [config]
// expect_result: pass
// glsl_version: 1.20
// [end config]

#version 120
#if !defined __VERSION__
#error __VERSION__ not defined.
#endif
#if __VERSION__ != 120
#error __VERSION__ is not 120
#endif

void main() { }
