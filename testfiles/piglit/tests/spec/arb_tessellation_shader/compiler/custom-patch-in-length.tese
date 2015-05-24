// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_tessellation_shader
// [end config]

#version 150
#extension GL_ARB_tessellation_shader: require

patch in float xs[];

/* xs is a patch input, so is not sized to gl_MaxPatchVertices */

int test[xs.length()];
