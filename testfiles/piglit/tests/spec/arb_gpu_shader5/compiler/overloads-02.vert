// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// If a function name is declared twice with the same parameter types,
// then the return types _and all qualifiers_ must match.

#version 150
#extension GL_ARB_gpu_shader5 : enable

void foo(int x) {}
void foo(out int x) {}
