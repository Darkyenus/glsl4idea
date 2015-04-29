// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_tessellation_shader
// [end config]

#version 150
#extension GL_ARB_tessellation_shader: require

layout(vertices = 3) out;

/* gl_out is sized by the preceding output layout declaration */

int test[(gl_out.length() == 3) ? 1 : -1];
