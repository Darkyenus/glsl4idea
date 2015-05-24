// [config]
// expect_result: fail
// glsl_version: 1.20
// [end config]
//

#version 120

#extension GL_ARB_shader_stencil_export: require
void main() { gl_FragStencilRefAMD = 0; }
