// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_tessellation_shader
// [end config]

#version 150
#extension GL_ARB_tessellation_shader: require

/* gl_in.length() is gl_MaxPatchVertices */

int test[(gl_in.length() == gl_MaxPatchVertices) ? 1 : -1];
