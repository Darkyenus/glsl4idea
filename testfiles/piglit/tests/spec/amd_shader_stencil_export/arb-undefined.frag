// [config]
// expect_result: fail
// glsl_version: 1.20
// [end config]
//

#version 120

#extension GL_AMD_shader_stencil_export: require
void main() { gl_FragStencilRefARB = 0; }
