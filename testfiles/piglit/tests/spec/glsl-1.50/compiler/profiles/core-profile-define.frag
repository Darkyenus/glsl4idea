// [config]
// expect_result: pass
// glsl_version: 1.50
// [end config]

#version 150
#if !defined GL_core_profile
#error GL_core_profile not defined.
#endif
#if GL_core_profile != 1
#error GL_core_profile is not 1.
#endif

void main() { }
