// [config]
// expect_result: pass
// glsl_version: 1.40
// [end config]

#version 140
#if !defined __VERSION__
#error __VERSION__ not defined.
#endif
#if __VERSION__ != 140
#error __VERSION__ is not 140
#endif

void main() { }
