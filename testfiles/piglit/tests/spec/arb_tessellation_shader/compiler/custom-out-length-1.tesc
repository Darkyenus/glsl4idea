// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_tessellation_shader
// [end config]

#version 150
#extension GL_ARB_tessellation_shader: require

layout(vertices = 3) out;

out float xs[];

/* xs is sized by the preceding output layout declaration */

int test[(xs.length() == 3) ? 1 : -1];
