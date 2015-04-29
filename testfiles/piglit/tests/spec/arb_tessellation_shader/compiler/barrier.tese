// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_tessellation_shader
// [end config]

#version 150
#extension GL_ARB_tessellation_shader: require
layout(triangles) in;

void main() { barrier(); gl_Position = vec4(0.0);}
