// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_tessellation_shader
// [end config]

#version 150
#extension GL_ARB_tessellation_shader: require

/* Must not use gl_out.length() before it is established by the layout declaration */

int test[gl_out.length()];

layout (vertices = 3) out;
