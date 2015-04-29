// [config]
// expect_result: pass
// glsl_version: 1.10
// [end config]

#version 110
#if !defined __VERSION__
#error __VERSION__ not defined.
#endif
#if __VERSION__ != 110
#error __VERSION__ is not 110
#endif

void main() { }
