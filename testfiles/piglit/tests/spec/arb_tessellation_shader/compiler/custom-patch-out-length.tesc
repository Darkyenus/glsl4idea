// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_tessellation_shader
// [end config]

#version 150
#extension GL_ARB_tessellation_shader: require

layout(vertices = 3) out;

patch out float xs[];

/* xs is not a per-vertex output, so is not sized by the layout declaration! */

int test[xs.length()];
