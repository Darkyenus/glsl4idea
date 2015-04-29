// Verify that geometry shader output arrays can be declared, and need
// not follow the size consistency rules that geometry shader input
// arrays need to follow.
//
// [config]
// expect_result: pass
// glsl_version: 1.50
// check_link: false
// [end config]

#version 150

layout(triangles) in;

in vec4 v1[3];

/* The following declarations would be errors if they were inputs */
out vec4 v2[2];
out blk1 {
  out vec4 v3[2];
};
out blk2 {
  out vec4 v[2];
} ifc1;
out blk3 {
  out vec4 v;
} ifc2[2];
