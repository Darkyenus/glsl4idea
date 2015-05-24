// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_tessellation_shader
// [end config]

#version 150
#extension GL_ARB_tessellation_shader: require

out float xs[];

layout(vertices = 3) out;

/* xs is sized by the preceding output layout declaration */

int test[(xs.length() == 3) ? 1 : -1];
