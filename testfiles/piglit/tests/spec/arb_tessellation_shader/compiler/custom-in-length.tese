// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_tessellation_shader
// [end config]

#version 150
#extension GL_ARB_tessellation_shader: require

in float xs[];

/* Since xs is unsized, xs.length() is gl_MaxPatchVertices */

int test[(xs.length() == gl_MaxPatchVertices) ? 1 : -1];
