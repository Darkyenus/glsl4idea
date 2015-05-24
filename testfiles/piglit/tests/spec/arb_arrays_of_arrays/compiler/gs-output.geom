// Verify that geometry shader output arrays of arrays can be declared.
//
// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_arrays_of_arrays
// check_link: false
// [end config]

#version 150
#extension GL_ARB_arrays_of_arrays: enable

layout(triangles) in;

in vec4 v1[3];

out vec4 v2[2][4];
out blk1 {
  out vec4 v3[2][1];
};
out blk2 {
  out vec4 v[2][5];
} ifc1;
out blk3 {
  out vec4 v;
} ifc2[2][3];
out vec4[3] v4[4];
out vec4[2][3] v5;
