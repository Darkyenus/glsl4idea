// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]

#version 130
#if !defined __VERSION__
#error __VERSION__ not defined.
#endif
#if __VERSION__ != 130
#error __VERSION__ is not 130
#endif

void main() { }
