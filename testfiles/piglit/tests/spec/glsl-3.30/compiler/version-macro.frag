// [config]
// expect_result: pass
// glsl_version: 3.30
// [end config]

#version 330
#if !defined __VERSION__
#error __VERSION__ not defined.
#endif
#if __VERSION__ != 330
#error __VERSION__ is not 330
#endif

void main() { }
