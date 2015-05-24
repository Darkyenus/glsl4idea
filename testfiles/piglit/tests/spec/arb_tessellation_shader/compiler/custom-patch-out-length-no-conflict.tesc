// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_tessellation_shader
// [end config]

#version 150
#extension GL_ARB_tessellation_shader: require

layout(vertices = 3) out;

/* xs is not a per-vertex output, so there is no conflict with the
 * output declaration.
 */

patch out float xs[4];
